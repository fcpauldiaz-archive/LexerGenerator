/**
* Universidad Del Valle de Guatemala
* 11-sep-2015
* Pablo Díaz 13203
*/

package lexergenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private final String espacio = "(\\s)*";
    
    
 
    public LexerAnalyzer(HashMap cadena){
        this.cadena=cadena;
        
        
    }
    
    

    public void revisarSintaxisCocoL(String inputCocoL){
       
        
       
       
        System.out.println("");
        //System.out.println(Cocol);
        //System.out.println(inputCocoL);
        try{
             Pattern pattern = Pattern.compile(Cocol);
            Matcher matcher = pattern.matcher(inputCocoL);
            while(matcher.find()){
                System.out.println(matcher.group());
                System.out.println(matcher.start());
               
            }
               
           
        }
        catch(PatternSyntaxException  e){
            System.out.println("Error en la linea número: "+ e.getIndex());
            System.out.println("Descripción: "+ e.getDescription());
            String r = this.Cocol.substring(0, 72);
            System.out.println(this.Cocol.length());
            r += "]";
            r += this.Cocol.substring(73);
            System.out.println(r);
            
           
            
            
        }
       
        System.out.println(this.Cocol);
        //System.out.println(inputCocoL);
        //System.out.println(inputCocoL.contains("Cocol = \"COMPILER\""));
        //System.out.println(inputCocoL.contains("END" ));
    }
    
    
    public void revisarSintaxisCocoL(HashMap<Integer,String> inputCocoL){
        String checkIdent = "";
        int dif = 0;
        for (Map.Entry<Integer, String> entry : inputCocoL.entrySet()) {
        
        Integer key = entry.getKey();
        String value = entry.getValue();
        
        if (key==1){
            if (value.contains("Cocol")){
                if (value.contains("=")){
                    if (value.contains("\"COMPILER\"")){
                       
                        checkIdent =value.substring(value.lastIndexOf(" "));
                        checkIdent = checkIdent.trim();
                       
                       
                        if (checkIdent.equals("")){
                        System.out.println("Error línea 1: no contiene identificador");
                    
                        }
                    }else{
                        System.out.println("Error línea 1: no contiene palabra \"COMPILER\"");
                    }
                    
                }else{
                    System.out.println("Error línea 1: no contiene símbolo =");
                }
            }else{
                System.out.println("Error línea 1: no contiene palabra Cocol");
            }
        }
    
        if (key!=inputCocoL.size()&&key!=1){
            
            if (value.equals("CHARACTERS")){
                dif = key;
            }
            if (value.equals("KEYWORDS")){
                dif = key-dif;
                for (int i = 0;i<dif;i++){
                    
                
                }
            }
            
        }
        
        if (key==inputCocoL.size()){
             if (value.contains("\"END\"")){
                 if (value.contains(checkIdent)){
                     if (value.contains("\'.\'")){
                        if (value.contains(".")){
                             
                        }else{
                              System.out.println("Error Línea " + key + " no contiene . ");
                         }
                     }else{
                          System.out.println("Error Línea " + key + " no contiene \'.\' ");
                     }
                     
                 }else{
                     System.out.println("Error Línea " + key + " el identificador " + checkIdent +" no coincide");
                 }
             }else{
                 System.out.println("Error Línea " + key + " no contiene \"END\" ");
             }
         }   
    }
    
    
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
        ArrayList res = new ArrayList();
        Pattern pattern = Pattern.compile(regex);
        String cadena_revisar = this.cadena.get(lineaActual).substring(index);
       
        Matcher matcher = pattern.matcher(cadena_revisar);
       
       
        if (matcher.find()) {
            cadena_encontrada=matcher.group();
            
            
            res.add(matcher.end());   //último índice de la cadena encontrada
            res.add(cadena_encontrada);

            return res;
            
        }
        else{//si no lo encuentra es porque hay un error
            System.out.println("Error en la linea " + lineaActual+ ": la cadena " + cadena_revisar + " es inválida");
           // System.out.println("Se buscaba " + regex);
            
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
        
        lineaActual++;
        
        
     
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
    
    public void setDeclaration(HashMap cadena,int lineaActual,int index){
        
        //ident
        ArrayList res1 = checkExpression(this.ident,lineaActual,index);
        int index1  = returnArray(res1);
        
        //'='
        
        ArrayList res2 = checkExpression(this.espacio+'='+this.espacio,lineaActual,index1);
        int index2 = returnArray(res2);
        
        //
        
        
        
    }
    /**
     * Método para obtener el index guardado en un array
     * que sirve para hacer un substring
     * @param param es un ArrayList
     * @return integer
     */
    public int returnArray(ArrayList param){
        if (!param.isEmpty()){
            //System.out.println(param.get(1));
            return (int)param.get(0);
        }
        //el cero representa que no se corta la cadena
        return 0;
    }
}


