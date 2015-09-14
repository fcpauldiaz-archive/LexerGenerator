/**
* Universidad Del Valle de Guatemala
* 11-sep-2015
* Pablo Díaz 13203
*/

package lexergenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author Pablo
 */
public class LexerAnalyzer {
    
    private String Cocol; //guarda toda la sintaxis de CocoL
    private final HashMap<Integer,String> cadena;
     /* Vocabulario de CocoL */
    private final String letter= "[a-zA-z]";
     //System.out.println(letter);
    private final String digit="\\d";
    private final String number=digit+"("+digit+")*";
    private final String ident= letter + "("+letter+"|"+digit+")*"; //identificador
    private final String string="\""+"("+number+"|"+letter+"|[^\\\"])*"+"\"";
    private final String character="\'"+"("+number+"|"+letter+"|[^\\\'])"+"\'";
    private final String espacio = "(\\s)*";
    private boolean strict =false;
    
    
 
    public LexerAnalyzer(HashMap cadena){
        this.cadena=cadena;
        
        
    }
    
 
    
   
    /**
     * Método para revisar si la línea del archivo está sintácticamente correcta
     * de acuerdo a CocoL
     * @param regex expresión a compararar
     * @param lineaActual línea actual del archivo
     * @param index índice para hacer un substring
     * @return regresa un ArrayList con la cadena encontrada y el último índice
     */
    public ArrayList checkExpression (String regex,int lineaActual,int index){
        String cadena_encontrada="";
        String cadena_revisar = this.cadena.get(lineaActual).substring(index);
        ArrayList res = new ArrayList();
        try{
        Pattern pattern = Pattern.compile(regex);
        
       
        Matcher matcher = pattern.matcher(cadena_revisar);
       
       
        if (matcher.find()) {
            cadena_encontrada=matcher.group();
            
            
            res.add(matcher.end());   //último índice de la cadena encontrada
            res.add(cadena_encontrada);
            return res;
            
        }
        else{//si no lo encuentra es porque hay un error
            if (strict==false)
                System.out.println("Error en la linea " + lineaActual+ ": la cadena " + cadena_revisar + " es inválida");
           // System.out.println("Se buscaba " + regex);
           //System.out.println("Advertencia en la linea " + lineaActual+ ": la cadena " + cadena_revisar + " es inválida"); 
        }
         } catch(Exception e){
           System.out.println("Error en la linea " + lineaActual+ ": la cadena " + cadena_revisar + " es inválida");
        }
        
       
        return res;
    }
    
    public void check(HashMap<Integer,String> cadena){
        int lineaActual = 1;
        int index = 0;
        ArrayList res = checkExpression("Cocol = \"COMPILER\""+this.espacio,lineaActual,index);
        
      
        int index2  = returnArray(res);
       
        ArrayList res2 = checkExpression(this.ident,lineaActual,index2);
        //System.out.println(res2.get(1));
       
        
        int index3 = 0;
        
       
        //ScannerSpecification
        ArrayList scan = scannerSpecification(lineaActual);
        
        
     
        lineaActual = cadena.size();
        ArrayList res3 = checkExpression("\"END\""+this.espacio,lineaActual,index3);
        int index4 = returnArray(res3);
        
        ArrayList res4 = checkExpression(this.ident,lineaActual,index4);
        int index5 = returnArray(res4);
        if (!res4.isEmpty()&&!res2.isEmpty()){
            if (!res4.get(1).equals(res2.get(1))){
                System.out.println("Error Linea " + lineaActual + ": los identificadores no coinciden");
            }
        }
        
        ArrayList res5 = checkExpression("\'.\'"+this.espacio,lineaActual,index5);
       // System.out.println(res5.get(1));
        
        
        
        
    }
    /**
     * Método para revisar el ScannerSpecification
     * @param lineaActual línea actual del archivo leído
     * @return ArrayList [0] = index, [1] = cadena.
     *   ["CHARACTERS" {SetDecl}]
     *   ["KEYWORDS"   {KeyWordDecl}]
     *   ["TOKENS"     {TokenDecl}]
     *   {WhiteSpaceDecl}.
     */
    public ArrayList<String> scannerSpecification(int lineaActual)
    {
        String param ="";
        //characters
        lineaActual++;
        //["Characters" = {SetDecl}
        ArrayList res = checkExpression(this.espacio+"CHARACTERS"+this.espacio,lineaActual,0);
        if (!res.isEmpty()){
            lineaActual++;
            param = (String)res.get(1);
        } 
        while (true&&!param.equals("")){
            ArrayList res2 = setDeclaration(lineaActual);
            if (res2.isEmpty())
                break;
            lineaActual++;
        }
        //keywords
        lineaActual++;
        //whitespaceDecl
        lineaActual++;
                
       return new ArrayList();
    }
    
    /**
     * Método para revisar SetDeclaration
     * @param lineaActual
     * @return ArrayList con la cadena revisada
     * SetDecl = ident '=' Set '.'.
     * 
     */
    public ArrayList<String> setDeclaration(int lineaActual){
        
        //revisar identificador
        ArrayList res1 = checkExpression(this.ident,lineaActual,0);
        int index1  = returnArray(res1);
        if (res1.isEmpty()){
            return new ArrayList();
        }
        
        //revisar '='
        ArrayList res2 = checkExpression(this.espacio+'='+this.espacio,lineaActual,index1);
        int index2 = returnArray(res2);
        if (res2.isEmpty())
            return new ArrayList();
        
        //revisar Set
        ArrayList res3 = set(lineaActual,index2+index1);
      
        if (res3.isEmpty())
            return new ArrayList();
        int index3 = returnArray(res3);
        
        //revisar '.'
        ArrayList res4 = checkExpression(this.espacio+"."+this.espacio,lineaActual,index3);
        if (res4.isEmpty())
            return new ArrayList();
        
        
        return res3;
    }
    /**
     * Método para revisar el Set
     * @param lineaActual 
     * @return ArrayList
     * Set = BasicSet (('+'|'-') BasicSet)*.
     */
    public ArrayList set(int lineaActual,int lastIndex){
        int index = 0;
        String ret ="";
        //Set = BasicSet
        ArrayList basic = basicSet(lineaActual,lastIndex);
        
        if (basic.isEmpty())
            return new ArrayList();
        lastIndex = (int)basic.get(0);
        ret += (String)basic.get(1);
        while(true){
            ArrayList bl = checkExpression(this.espacio+"(\\+|\\-)"+this.espacio,lineaActual,lastIndex);
            if (bl.isEmpty())
                break;
            lastIndex = (int)bl.get(0);
            ArrayList b = basicSet(lineaActual,lastIndex);
            if (b.isEmpty())
                break;
            lastIndex += (int)b.get(0);
           
            index = index + (int)bl.get(0)+(int)b.get(0);
            ret = ret + (String)bl.get(1)+ (String)b.get(1);
            
            
        }
        
        ArrayList fin = new ArrayList();
        fin.add(index);
        fin.add(ret);
        if (ret.equals(""))
            fin=basic;
        return fin;
    }
    /**
     * 
     * @param lineaActual 
     * @return ArrayList
     * BasicSet = string | ident | Char [ "..." Char].
     */
    public ArrayList<String> basicSet(int lineaActual,int lastIndex){
        ArrayList<String> cadenas = new ArrayList();
        Queue<String> cadenas_sort = new LinkedList();
        //BasicSet = string
         ArrayList res2 = checkExpression(this.ident,lineaActual,lastIndex);
        if (!res2.isEmpty()){
            cadenas.add((String)res2.get(1));
            
        }
        ArrayList res = checkExpression(this.string,lineaActual,lastIndex);
        if (!res.isEmpty()){
            cadenas.add((String)res.get(1));
        }
      
        ArrayList res3 = Char(lineaActual);
        if (!res3.isEmpty())
            cadenas.add((String)res3.get(1));
        
        String preParts = this.cadena.get(lineaActual);
        String[] parts = preParts.substring(0, preParts.length()-1).split(" ");
      
        for (int i =0;i<parts.length;i++){
            for (int j = 0;j<cadenas.size();j++){
                
                if (parts[i].equals(cadenas.get(j))){
                    cadenas_sort.add(cadenas.get(j));

                }
            }
            
        }
      
        
        ArrayList fin = new ArrayList();
        String last = cadenas_sort.poll();
        if (!cadenas.isEmpty()){
            fin.add(last.length());
            fin.add(last);
        }
        
        return fin;
        
    }
    
    public ArrayList<String> Char(int lineaActual){
        this.strict=true;
        ArrayList res = checkExpression(this.character,lineaActual,0);
        if (!res.isEmpty()){
            this.strict=false;
            return res;
        }
        ArrayList res2 = checkExpression("CHR\\("+this.number+"\\)",lineaActual,0);   
        if (!res2.isEmpty()){
            this.strict=false;
            return res2;
        }
        return new ArrayList();
    }
    
    /**
     * Método para obtener el index guardado en un array
     * que sirve para hacer un substring
     * @param param es un ArrayList
     * @return integer
     */
    public int returnArray(ArrayList param){
        if (!param.isEmpty()){
            System.out.println(param.get(1));
            return (int)param.get(0);
        }
        //el cero representa que no se corta la cadena
        return 0;
    }
}


