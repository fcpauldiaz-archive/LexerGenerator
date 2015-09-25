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
    
    /**
     * Método para encontrar el nombre del archivo a generar
     */
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
    * Genera la  estructura de la clase analizadora
    */
    public void generarClaseAnalizadora() {

        String scanner_total = (
            "/**"+"\n"+
            " * Nombre del archivo: "+this.nombreArchivo+".java"+"\n"+
            " * Universidad del Valle de Guatemala"+"\n"+
            " * Pablo Diaz 13203 " + "\n"+
            " * Descripción: Segundo proyecto. Generador de analizador léxico"+"\n"+
            "**/"+"\n"+
            ""+"\n"+
            "import java.io.BufferedReader;"+"\n"+
            "import java.io.File;"+"\n"+
            "import java.io.FileOutputStream;"+"\n"+
            "import java.io.FileWriter;"+"\n"+
            "import java.io.PrintWriter;"+"\n"+
            "import java.util.Map;"+"\n"+
            "import java.util.Scanner;"+"\n"+
            "import javax.swing.JFileChooser;"+"\n"+
            "import java.util.Collections;"+"\n"+
            "import java.util.Comparator;"+"\n"+
            "import java.util.ArrayList;"+"\n"+
            "import java.util.List;"+"\n"+
            "import java.util.HashMap;"+"\n"+
            "import java.util.Iterator;"+"\n"+
           
          
            ""+"\n"+
            "public class "+this.nombreArchivo+" {"+"\n"+
            ""+"\n"+
            "\t"+"private Simulacion sim = new Simulacion();"+"\n"+
            "\t"+"private ArrayList<Automata> automatas = new ArrayList();"+"\n"+
            "\t"+"private HashMap<Integer,String> input;"+"\n"+    
                
            "\t"+"public " + this.nombreArchivo+"(HashMap input){"+"\n"+
             "\t"+"\t"+"this.input=input;"+"\n"+
            "\t"+"\n"+
             
           "\t" + "}"    
                
        );

        scanner_total += crearAutomatasTexto();
        scanner_total += generar();
        scanner_total += metodoRevisar();
        scanner_total+="\n"+"}";
        
        ReadFile fileCreator = new ReadFile();
        fileCreator.crearArchivo(scanner_total, nombreArchivo);
        
    }
    
    /*
    * Método para pasara character y keywords a expresiones regulares
    */
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
    
    /**
     * Crea una cadena con operaciones or en cada caracter
     * En caso de haber concatenaciones calcula si tiene que
     * ser por la izquierda o derecha
     * @param cadena a evaluar
     * @return String con la cadena modificada
     */
    public String crearCadenasOr(String cadena){
        String or = " ";
        if ((cadena.startsWith("\"")||cadena.startsWith("\'"))&&(!cadena.contains("+"))){
             
            cadena=  cadena.replace("\"", "");
            cadena=  cadena.replace("\'", "");
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
                    
                    or = "("+buscarExpr(or) +")|("+ cadenaOr+")";
                }
                else if (lado == 1){
                    
                    or = "("+cadenaOr +")|("+ buscarExpr(or)+")";
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
    /**
     * Método auxiliar que se llama cuando hay más de una concatenación
     * @param anterior String con lo ya concatenado
     * @param actual String con lo que falta concatenar
     * @return String con expresión regular
     */ 
    public String concatenacion(String anterior, String actual){
            String resultado = "";
            String cadenaOr=anterior;
            if (actual.contains("\""))
                cadenaOr = crearCadenasOr(actual);
            //calcular si se concatena a la izquierda o derecha
            int lado = calcularConcatenacion(actual);
            if (lado == -1){

                resultado = "("+buscarExpr(actual) +")|("+ cadenaOr+")";
            }
            else if (lado == 1){

                resultado = "("+cadenaOr +")|("+ buscarExpr(actual)+")";
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
    
    /**
     * Método para calcular la posición a concatenar
     * @param str
     * @return 
     */
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
    /**
     * Método para buscar un identificador en el archivo
     * @param search identificador a buscar
     * @return Devuelve el identificador
     */
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
    
    /**
     * Método para buscar un identificador en el archivo
     * @param search identificador a buscar
     * @return devuelve expresión regular asociada al identificador
     */
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
    
    /**
     * Método para calcular el número de ocurrencias de un character
     * @param s string completo 
     * @param c character a calcular ocurrencias
     * @return 
     */
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
   
    /**
     * Método para exportar los autómatas a texto plano
     * @return Devuelve un string con los autómatass
     */
    public String crearAutomatasTexto(){
       String afn = "";
       afn += "\n";
       afn += "\tpublic void automatas(){";
       afn += "\n";
       int counter = 0;
       for (Map.Entry<String, String> entry : cadenaCompleta.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            if (value.length()>1)
                afn += "\n"+
                     "\t"+"\t" + "RegexConverter convert_"+counter+"= new RegexConverter();"+"\n"+
                     "\t"+"\t" +"String regex_"+counter+" = convert_"+counter+".infixToPostfix("+"\""+value+"\""+");"+"\n"+
                     "\t"+"\t" +"AFNConstruct ThomsonAlgorithim_"+counter+" = new AFNConstruct(regex_"+counter+");"+"\n"+
                     "\t"+"\t" +"ThomsonAlgorithim_"+counter+".construct();"+"\n"+
                     "\t"+"\t" +"Automata temp_"+counter+ " = ThomsonAlgorithim_"+counter+".getAfn();"+"\n"+
                     "\t"+ "\t" +"temp_"+counter+".setTipo("+"\""+key+"\""+");"+"\n"+
                     "\t"+"\t" +"automatas.add(temp_"+counter+ ");"+"\n";
            else{
                 afn += "\n"+
                     
                    
                     "\t"+"\t" +"Automata temp_"+counter+ " ="+"ThomsonAlgorithim_0"+".afnSimple("+"\""+value+"\""+");"+"\n"+
                     "\t"+ "\t" +"temp_"+counter+".setTipo("+"\""+key+"\""+");"+"\n"+
                     "\t"+"\t" +"automatas.add(temp_"+counter+ ");"+"\n";
                
            }
            counter++;
        }
       afn += "\t}";
       
       
       return afn;
   }
    /**
     * Métodos auxiliares para revisar autómatas
     * @return string con los métodos
     */
    public String generar(){
        String metodoVer= "\n"+
     "\t"+" /**" +"\n "+
     "\t"+"* Método para revisar que tipo de sub autómata es aceptado por una "+"\n"+
     "\t"+"* expresión regular"+"\n"+
     "\t"+"* @param regex expresión regular a comparar"+"\n"+
     "\t"+"* @param conjunto arreglo de autómatas"+"\n"+
     "\t"+"*/"+"\n"+
    "\t"+"public void checkIndividualAutomata(String regex, ArrayList<Automata> conjunto,int lineaActual){"+"\n"+
        
        "\t"+"\t"+"ArrayList<Boolean> resultado = new ArrayList();"+"\n"+
       
            
            "\t"+"\t"+"for (int j = 0;j<conjunto.size();j++){"+"\n"+
                "\t"+"\t"+"\t"+"resultado.add(sim.simular(regex, conjunto.get(j)));"+"\n"+
               
           "\t"+ "\t"+"}"+"\n"+
           
            "\t"+"\t"+"ArrayList<Integer> posiciones = checkBoolean(resultado);"+"\n"+
            "\t"+"\t"+"//resultado.clear();"+"\n"+
            
           
            "\t"+"\t"+"for (int k = 0;k<posiciones.size();k++){"+"\n"+
                
                "\t"+"\t"+"\t"+"System.out.println(regex+ \": \" + conjunto.get(posiciones.get(k)).getTipo());"+"\n"+
            "\t"+"\t"+"}"+"\n"+
            "\t"+"\t"+"if (posiciones.isEmpty()){"+"\n"+
               "\t"+"\t"+"\t"+"System.out.println(\"Error línea archivo \" + lineaActual +\" : \"+regex+ \" no fue reconocido\");"+"\n"+
            "\t"+"\t"+"}"+"\n"+
        
    "\t"+"}"+"\n"+
    
    "\t"+"/**"+"\n"+
    "\t"+" * Método que devuelve las posiciones en las que el valor que tiene "+"\n"+
    "\t"+" * en cada posicion es true"+"\n"+
    "\t"+" * @param bool arreglo de booleanos"+"\n"+
    "\t"+" * @return arreglo de enteros"+"\n"+
    "\t"+" */"+"\n"+
    "\t"+"public ArrayList<Integer>  checkBoolean(ArrayList<Boolean> bool){"+"\n"+
        "\t"+"\t"+"ArrayList<Integer> posiciones = new ArrayList();"+"\n"+
       
        "\t"+"\t"+"for (int i = 0;i<bool.size();i++){"+"\n"+
            "\t"+"\t"+"\t"+"if (bool.get(i))"+"\n"+
                "\t"+"\t"+"\t"+"\t"+"posiciones.add(i);"+"\n"+
        "\t"+"\t"+"}"+"\n"+
        "\t"+"\t"+"return posiciones;"+"\n"+
        
    "\t"+"}"+"\n";
        return metodoVer;
    }
    
    public String metodoRevisar(){
        String res = "\n"+
        "\t"+"public void revisar(){"+"\n"+

        "\t"+"\t"+"for (Map.Entry<Integer, String> entry : input.entrySet()) {"+"\n"+
	        "\t"+ "\t"+"\t"+"Integer key = entry.getKey();"+"\n"+
	        "\t"+"\t"+"\t"+"String value = entry.getValue();"+"\n"+
	        "\t"+"\t"+"\t"+"String[] parts = value.split(\" \");"+"\n"+
	        "\t"+"\t"+"\t"+"for (int j = 0;j<value.length();j++){"+"\n"+
		        "\t"+"\t"+"\t"+"\t"+"for (int i= 0;i<parts.length;i++){"+"\n"+
		            "\t"+"\t"+"\t"+"\t"+"\t"+"this.checkIndividualAutomata(value.charAt(j)+\"\", automatas,key);"+"\n"+
		        "\t"+"\t"+"\t"+"\t"+"}"+"\n"+
	    	"\t"+"\t"+"\t"+"}"+"\n"+
	    "\t"+"\t"+"}"+"\n"+
	"\t"+"}"+"\n";
        return res;
    }
    
    public void generarMain(){
        String res = "\n"+
        
        
        "import java.util.HashMap;"+"\n"+

        "/**"+"\n"+
         "*"+"\n"+
         "* @author Pablo"+"\n"+
         "*/"+"\n"+
        "public class "+ "resultadoGenerador" +"Main"+" {"+"\n"+
    
            "public static String EPSILON = \"ε\";"+"\n"+
            "public static char EPSILON_CHAR = EPSILON.charAt(0);"+"\n"+
    

    "/**"+"\n"+
     "* @param args the command line arguments"+"\n"+
    " */"+"\n"+
    "public static void main(String[] args) {"+"\n"+
        "\t"+ "// TODO code application logic here"+"\n"+
        "\t"+"ReadFile read = new ReadFile();"+"\n"+
        "\t"+"HashMap input = read.leerArchivo(\"input\");"+"\n"+
        "\t"+this.nombreArchivo+" resGenerator = new "+this.nombreArchivo+"(input);"+"\n"+
        "\t"+"resGenerator.automatas();"+"\n"+
         "\t"+"resGenerator.revisar();"+
        
     
        
      
        
        "\t"+"}"+"\n"+

    "}"+"\n";
        
        ReadFile fileCreator = new ReadFile();
        fileCreator.crearArchivo(res, "resultadoGeneradorMain");
        
    }
}
