/**
* Universidad Del Valle de Guatemala
* 23-sep-2015
* Pablo Díaz 13203
*/

package lexergenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Clase que genera el analizador léxico en el 
 * lenguaje de Java
 * @author Pablo
 */
public class CodeGenerator {
    
    private HashMap<Integer,String> cadena;
    private String nombreArchivo;
    private HashMap<String, String> cadenaCompleta = new HashMap();
    private Stack pilaConcatenacion = new Stack();
    
    public CodeGenerator(HashMap cadena){
        this.cadena=cadena;
    }
    
    public void encontrarNombre(){
        
        for (Map.Entry<Integer, String> entry : cadena.entrySet()) {
            String value = entry.getValue();
            if (value.contains("COMPILER")){
                value = value.trim();
                int index = value.indexOf("R");
                this.nombreArchivo = value.substring(++index,value.length());
                this.nombreArchivo = this.nombreArchivo.trim();

            }

        }
    
    }
   /**
    * Genera la clase analizadora
    */
    public void generarClaseAnalizadora() {

        String scanner_total = (
            "/**"+"\n"+
            " * Nombre del archivo: "+this.nombreArchivo+".java"+"\n"+
            " * Universidad del Valle de Guatemala"+"\n"+
            " * Descripción: Segundo proyecto. Generador de analizador léxico"+"\n"+
            "**/"+"\n"+
            ""+"\n"+
            "import java.io.BufferedReader;"+"\n"+
            "import java.io.File;"+"\n"+
            "import java.io.FileOutputStream;"+"\n"+
            "import java.io.FileWriter;"+"\n"+
            "import java.io.PrintWriter;"+"\n"+
            "import java.io.FileNotFoundException;"+"\n"+
            "import java.util.Scanner;"+"\n"+
            "import javax.swing.JFileChooser;"+"\n"+
            "import java.util.Collections;"+"\n"+
            "import java.util.Comparator;"+"\n"+
            "import java.util.ArrayList;"+"\n"+
            "import java.util.List;"+"\n"+
            "import java.util.HashMap;"+"\n"+
            "import java.util.Iterator;"+"\n"+
            "import java.util.LinkedHashMap;"+"\n"+
            "import java.util.Scanner;"+"\n"+
          
            ""+"\n"+
            "public class "+this.nombreArchivo+" {"+"\n"+
            ""+"\n"+
            "private Simulacion sim = new Simulacion();"+"\n"+
                
            "\t"+"public " + this.nombreArchivo+"(){"+"\n"+"\n"
             
           +"\t" + "}"    
                
        );

        scanner_total += crearAutomatasTexto();
        scanner_total+="\n"+"}";
        
        ReadFile fileCreator = new ReadFile();
        fileCreator.crearArchivo(scanner_total, nombreArchivo);
        
    }
    
    public void generarCharactersYKeywords(){
        
        for (Map.Entry<Integer, String> entry : cadena.entrySet()) {
        String value = entry.getValue();
        if (value.contains("CHARACTERS")){
            int lineaActual = entry.getKey();
            while(true){
                lineaActual = avanzarLinea(lineaActual);
                if (this.cadena.get(lineaActual).contains("KEYWORDS"))
                    break;
                String valor = this.cadena.get(lineaActual);
                valor = valor.trim();
                int index = valor.indexOf("=");
                String ident = valor.substring(0,index);
                String revisar  = valor.substring(++index,valor.length()-1);
                revisar = revisar.trim();
                revisar = crearCadenasOr(revisar);
              
                cadenaCompleta.put(ident.trim(), revisar);
               
                System.out.println(cadenaCompleta);
                
            }
            
        }
        
        }
        
    }
    
     public String crearCadenasOr(String cadena){
        String or = "";
        if ((cadena.startsWith("\"")||cadena.startsWith("\'"))&&(!cadena.contains("+"))){
             
             cadena=  cadena.replace("\"", "");
            for (int i = 0;i<cadena.length();i++){
                Character c = cadena.charAt(i);
               
                    if (c != ' '){
                   
                        if (i<=cadena.length()-2)
                            or += c +"|";
                        if (i>cadena.length()-2)
                            or +=c;
                        

                }
            }
           
            
           
        }
        else{
            or = cadena;
            if(cadena.contains("+")){
                int cantidadConcatenaciones = count(cadena,'+');
                if (cantidadConcatenaciones>1){
                    System.out.println(cadena.lastIndexOf("+"));
                    System.out.println(cadena.substring(0, cadena.indexOf("+", cadena.indexOf("+") + 1)));
                    pilaConcatenacion.push(cadena.substring(cadena.indexOf("+", cadena.indexOf("+") + 1)));
                    System.out.println(pilaConcatenacion);
                }
               // System.out.println(cantidadConcatenaciones);
                int preIndex=0;
                if (cadena.contains("\""))
                     preIndex = cadena.indexOf(("\""));
                String w = cadena.substring(preIndex+1);
                int postIndex = w.length()-1;
                if (w.contains("\""))
                    postIndex = w.indexOf(("\""));
                String wFinal = cadena.substring(preIndex,preIndex+postIndex+2);
                System.out.println(wFinal);
                String cadenaOr = crearCadenasOr(wFinal);
                //calcular si se concatena a la izquierda o derecha
                int lado = calcularConcatenacion(or);
                if (lado == -1){
                    
                    or = "("+buscarExpr(or) +")("+ cadenaOr+")";
                }
                else if (lado == 1){
                    
                    or = "("+cadenaOr +")("+ buscarExpr(or)+")";
                }
                
                while (!pilaConcatenacion.isEmpty()){
                    String faltante = (String)pilaConcatenacion.pop();
                    cantidadConcatenaciones = count(faltante,'+');
                    if (cantidadConcatenaciones>1){
                        pilaConcatenacion.push(faltante.substring(faltante.indexOf("+", faltante.indexOf("+") + 1)));
                        faltante = (faltante.substring(0, faltante.indexOf("+", faltante.indexOf("+") + 1)));

                    }
                        or = concatenacion(or,faltante);
                }
                
            }
        }
        return or;
     }
     
     public String concatenacion(String anterior, String actual){
            String resultado = "";
            String cadenaOr=anterior;
            if (actual.contains("\""))
                cadenaOr = crearCadenasOr(actual);
            //calcular si se concatena a la izquierda o derecha
            int lado = calcularConcatenacion(actual);
            if (lado == -1){

                resultado = "("+buscarExpr(actual) +")("+ cadenaOr+")";
            }
            else if (lado == 1){

                resultado = "("+cadenaOr +")("+ buscarExpr(actual)+")";
            }
        return resultado;
     }
     /**
     * Método para avanzar de línea, busca la línea que actual
     * @param lineaActual
     * @return lineaActual
     */
    public Integer avanzarLinea(int lineaActual){
       while (true){
           if (this.cadena.containsKey(++lineaActual))
               return lineaActual;
       }
    }
    
    public int calcularConcatenacion(String str){
        int posicion = 0;
        int posConc = str.indexOf("+");
        String ident = buscarIdent(str);
        int indexIdent = str.indexOf(ident) + ident.length();
        if (indexIdent<posConc)
            return -1;
        else if (indexIdent>posConc)
            return 1;
        
        return posicion;
    }
    
    public String buscarIdent(String search){
        String res = "";
        for (Map.Entry<String, String> entry : cadenaCompleta.entrySet()) {
            String value = entry.getKey();
            if (search.contains(value)){
                return value;
                // res = entry.getValue();
                
                
            }

        }
        return res;
        
    }
    
     public String buscarExpr(String search){
        String res = "";
        for (Map.Entry<String, String> entry : cadenaCompleta.entrySet()) {
            String value = entry.getKey();
            if (search.contains(value)){
                return entry.getValue();
                
                
                
            }

        }
        return res;
     }
    
   public  int count( final String s, final char c ) {
        final char[] chars = s.toCharArray();
        int count = 0;
        for(int i=0; i<chars.length; i++) {
          if (chars[i] == c) {
            count++;
          }
        }
        return count;
    }
   
   public String crearAutomatasTexto(){
       String afn = "";
       afn += "\n";
       afn += "\tpublic void automatas(){";
       afn += "\n";
       afn += "\t"+"\t"+"ArrayList<Automata> automatas = new ArrayList();"+"\n";
       int counter = 0;
       for (Map.Entry<String, String> entry : cadenaCompleta.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            
            afn += "\n"+
                 "\t"+"\t" + "RegexConverter convert_"+counter+"= new RegexConverter("+"\""+value+"\""+");"+"\n"+
                 "\t"+"\t" +"String regex_"+counter+" = convert_"+counter+".infixToPostfix();"+"\n"+
                 "\t"+"\t" +"AFNConstruct ThomsonAlgorithim_"+counter+" = new AFNConstruct(regex_"+counter+");"+"\n"+
                 "\t"+"\t" +"ThomsonAlgorithim_"+counter+".construct();"+"\n"+
                 "\t"+"\t" +"Automata temp_"+counter+ " = ThomsonAlgorithim_"+counter+".getAfn();"+"\n"+
                 "\t"+ "\t" +"temp.setTipo("+"\""+key+"\""+")"+"\n"+
                 "\t"+"\t" +"automatas.add(temp_"+counter+ ");"+"\n";
            counter++;
        }
       afn += "\t}";
       
       
       return afn;
   }
}
