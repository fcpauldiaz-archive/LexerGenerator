/**
* Universidad Del Valle de Guatemala
* 11-sep-2015
* Pablo Díaz 13203
*/

package lexergenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase para revisar la estructura de un archivo de CocoL
 * @author Pablo
 */
public class LexerAnalyzer {
    
    
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
    private boolean output = true;
    
    private Automata letter_;
    private Automata digit_;
    private Automata ident_;
    private Automata string_;
    private Automata character_;
    private Automata number_;
    private Automata basicSet_;
    private Automata igual_;
    private Automata plusOrMinus_;
    private Automata espacio_;
    private Simulacion sim;
    
    
 
    public LexerAnalyzer(HashMap cadena){
        this.sim = new Simulacion();
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
        Pattern p = Pattern.compile("."+"|"+"."+this.espacio);
         Matcher m = p.matcher(cadena_revisar);
       
       if (m.matches())
           return new ArrayList();
        if (matcher.find()) {
            cadena_encontrada=matcher.group();
            
            
            res.add(matcher.end());   //último índice de la cadena encontrada
            res.add(cadena_encontrada);
            return res;
            
        }
        else{//si no lo encuentra es porque hay un error
                if (!cadena_revisar.isEmpty())//si 
                    System.out.println("Error en la linea " + lineaActual + ": la cadena " + cadena_revisar + " es inválida");
                else
                    System.out.println("Error en la línea " + lineaActual + ": falta un identificador");
                
            // System.out.println("Se buscaba " + regex);
           //System.out.println("Advertencia en la linea " + lineaActual+ ": la cadena " + cadena_revisar + " es inválida"); 
        }
        } catch(Exception e){
           System.out.println("Error en la linea " + lineaActual+ ": la cadena " + cadena_revisar + " es inválida");
        }
        
       
        return res;
    }
    /**
     * Revisar formato de la primera línea
     * ScannerSpecification
     * ParserSpecification
     * @param cadena HashMap de la cadena y sus números de línea
     */
    public void check(HashMap<Integer,String> cadena){
        int lineaActual = 1;
        int index = 0;
        ArrayList res = checkExpression("Cocol = \"COMPILER\""+this.espacio,lineaActual,index);
        
        int index2  = returnArray(res);
       
        ArrayList res2 = checkExpression(this.ident,lineaActual,index2);
        //System.out.println(res2.get(1));
        
        //ScannerSpecification
        
        ArrayList scan = scannerSpecification(lineaActual);
        if (scan.isEmpty())
            output=false;
        
        //ParserSpecification
     
        
        
        //END File
        lineaActual = avanzarLinea((int)scan.get(0));
       
        ArrayList res3 = checkExpression("\"END\""+this.espacio,lineaActual,0);
        int index4 = returnArray(res3);
        
        ArrayList res4 = checkExpression(this.ident,lineaActual,index4);
        int index5 = returnArray(res4);
        //revisar identificadores
        if (!res4.isEmpty()&&!res2.isEmpty()){
            if (!res4.get(1).equals(res2.get(1))){
                System.out.println("Error Linea " + lineaActual + ": los identificadores no coinciden");
                output=false;
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
        int returnIndex=0;
        String returnString= "";
        
        //[CHARACTERS]
        lineaActual = avanzarLinea(lineaActual);
       
        if (!this.cadena.get(lineaActual).contains("CHARACTERS")){
            System.out.println("Error línea : "+lineaActual +" No contiene la palabra CHARACTERS");
            return new ArrayList();
        }
        lineaActual = avanzarLinea(lineaActual);
         //["Characters" = {SetDecl}
        while (true){
            ArrayList res2 = setDeclaration(lineaActual);
            if (res2.isEmpty()){
                lineaActual = retrocederLinea(lineaActual);
                break;
                
            }
            returnIndex += (int)res2.get(0);
            returnString += (String)res2.get(1);
            lineaActual = avanzarLinea(lineaActual);
        }
        //[KEYWRORDS]
        lineaActual = avanzarLinea(lineaActual);
         if (!this.cadena.get(lineaActual).contains("KEYWORDS")){
            System.out.println("Error linea :" +lineaActual + " No contiene la palabra KEYWORDS");
            return new ArrayList();
        }
        lineaActual = avanzarLinea(lineaActual);
        //[KEYWORDS = {KeyWordDeclaration}]
        while (true){
            ArrayList keywords = keywordDeclaration(lineaActual);
            if (keywords.isEmpty()){
                lineaActual = retrocederLinea(lineaActual);
                break;
            }
            returnIndex += (int)keywords.get(0);
            returnString += (String)keywords.get(1);
            lineaActual = avanzarLinea(lineaActual);
            
        }
        
        //
        
        //lineaActual = avanzarLinea(lineaActual);
        //whitespaceDecl
      // lineaActual = avanzarLinea(lineaActual);
              
        ArrayList outputScan = new ArrayList();
        outputScan.add(lineaActual);
        outputScan.add(returnString);
       return outputScan;
    }
    
    /**
     * Método que representa el whitespace declaration
     * @param lineaActual
     * @return 
     */
    public ArrayList whiteSpaceDeclaration(int lineaActual){
        if (!this.cadena.get(lineaActual).contains("IGNORE")){
            return new ArrayList();
        }
        ArrayList space = set(lineaActual,0);
        if (space.isEmpty())
            return new ArrayList();
        
        return space;
    }
    
    public ArrayList keywordDeclaration(int lineaActual){
        
        if (this.cadena.get(lineaActual).contains("\"END\"")||this.cadena.get(lineaActual).contains("CHARACTERS"))
            return new ArrayList();
        
        
        ArrayList res1 = checkAutomata(this.ident_,lineaActual,0);
        int index1  = returnArray(res1);
        if (res1.isEmpty()){
            return new ArrayList();
        }
        
        ArrayList res2 = checkAutomata(this.igual_,lineaActual,index1);
        
        int index2 = returnArray(res2);
        if (res2.isEmpty())
            return new ArrayList();
        
         //revisar '='
       
        ArrayList res3 = checkAutomata(this.string_,lineaActual,index1+index2);
        
        int index3 = returnArray(res3);
        if (res3.isEmpty())
            return new ArrayList();
        
        
        
        return res3;
    }
    
    
    /**
     * Método para revisar SetDeclaration
     * @param lineaActual
     * @return ArrayList con la cadena revisada
     * SetDecl = ident '=' Set '.'.
     * 
     */
    public ArrayList<String> setDeclaration(int lineaActual){
        
        if (this.cadena.get(lineaActual).contains("\"END\"")||this.cadena.get(lineaActual).contains("KEYWORDS"))
            return new ArrayList();
        //revisar identificador
        //ArrayList res1 = checkExpression(this.ident,lineaActual,0);
        ArrayList identifier = checkAutomata(this.ident_,lineaActual,0);
        int index1  = returnArray(identifier);
        if (identifier.isEmpty()){
            return new ArrayList();
        }
        
        //revisar '='
        //ArrayList res2 = checkExpression(this.espacio+'='+this.espacio,lineaActual,index1);
        ArrayList equals = checkAutomata(this.igual_,lineaActual,index1);
        
        int index2 = returnArray(equals);
        if (equals.isEmpty())
            return new ArrayList();
        
        //revisar Set
        ArrayList resSet = set(lineaActual,index2+index1);
      
        if (resSet.isEmpty())
            return new ArrayList();
        int index3 = returnArray(resSet);
        
       //revisar '.'
       // ArrayList res4 = checkExpression(this.espacio+"."+this.espacio,lineaActual,index3);
        //if (res4.isEmpty())
          //  return new ArrayList();
        
        
        return resSet;
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
        
        index=(int)basic.get(0);
        ret += (String)basic.get(1);
        lastIndex += index;
        while(true){
            ArrayList bl = checkAutomata(this.plusOrMinus_,lineaActual,lastIndex);
            if (bl.isEmpty()){
                break;
                }
            lastIndex += (int)bl.get(0);
            ArrayList b = basicSet(lineaActual,lastIndex);
            if (b.isEmpty())
                break;
            lastIndex += (int)b.get(0);
           
            index = index + (int)bl.get(0)+(int)b.get(0);
            ret = ret + (String)bl.get(1)+ (String)b.get(1);
            
            
        }
        
        ArrayList fin = new ArrayList();
        fin.add(lastIndex);
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
      
        //BasicSet = {string}
        
       // ArrayList res = checkExpression(this.string+"|"+this.ident,lineaActual,lastIndex);
        ArrayList resBasicSet = checkAutomata(this.basicSet_,lineaActual,lastIndex);
       if (!resBasicSet.isEmpty()){
            cadenas.add((String)resBasicSet.get(1));

        }
        
        if (cadenas.isEmpty()){
        ArrayList resBasicChar = basicChar(lineaActual);
            if (!resBasicChar.isEmpty())
                cadenas.add((String)resBasicChar.get(1));
        }
        
      Collections.sort(cadenas, (String o1, String o2) -> {
          Integer a1 = o1.length();
          Integer a2 = o2.length();
          return a2-a1;
        });
        
        ArrayList fin = new ArrayList();
        
        if (!cadenas.isEmpty()){
           
            fin.add(cadenas.get(0).length());
            fin.add(cadenas.get(0));
        }
       
        return fin;
        
    }
    
    public ArrayList<String> basicChar(int lineaActual){
        ArrayList res = Char(lineaActual);
        if (res.isEmpty())
            return new ArrayList();
        ArrayList res2 = checkExpression("\\.\\.",lineaActual,0);
        if (res2.isEmpty())
            return new ArrayList();
        ArrayList res3 = Char(lineaActual);
        
        if (res3.isEmpty())
            return new ArrayList();
        ArrayList result = new ArrayList();
        result.add((int)res.get(0)+(int)res2.get(0)+(int)res3.get(0));
        result.add((String)res.get(1)+(String)res2.get(1)+(String)res3.get(1));
        return result;
                    
    }
    
    /**
     * Método para revisar si un character
     * @param lineaActual
     * @return 
     */
    public ArrayList<String> Char(int lineaActual){
        
        ArrayList res = checkExpression(this.character,lineaActual,0);
        if (!res.isEmpty()){
           
            return res;
        }
        ArrayList res2 = checkExpression("CHR\\("+this.number+"\\)",lineaActual,0);   
        if (!res2.isEmpty()){
            
            return res2;
        }
        return new ArrayList();
    }
    /**
     * Revisasr si el archivo no tiene errores
     */
    
    public void getOutput(){
        if (output){
            System.out.println("Archivo Aceptado");
        }
        else{
            System.out.println("Archivo no aceptado");
        }
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
    
    /**
     * Revisar si la expresión regular cumple el autómata especificado
     * @param param Automata comparador
     * @param lineaActual lineaActual del archivo
     * @param index índice para recortar el string
     * @return un arreglo: en la posicion cero el último indíce analizado y
     * en la posicion 1 el último string analizado
     */
    public ArrayList checkAutomata(Automata param,int lineaActual, int index){
       
        String cadena_revisar = this.cadena.get(lineaActual).substring(index);
        
        int preIndex = 0;
        try{
          
            while (cadena_revisar.startsWith(" ")){
                preIndex++;
                cadena_revisar = cadena_revisar.substring(preIndex, cadena_revisar.length());
            }
            if (cadena_revisar.contains(" "))
                cadena_revisar = cadena_revisar.substring(0, cadena_revisar.indexOf(" ")+1);
        }catch(Exception e){}
        try{
            cadena_revisar = cadena_revisar.substring(0, cadena_revisar.indexOf("."));
        }catch(Exception e){}
        
        ArrayList resultado = new ArrayList();
        
        boolean returnValue=sim.simular(cadena_revisar.trim(), param);
      
        checkIndividualAutomata(cadena_revisar);
        if (returnValue){
            resultado.add(cadena_revisar.length()+preIndex);
            resultado.add(cadena_revisar);
            return resultado;
        }
        else{
            if (!cadena_revisar.isEmpty()){
                System.out.println("Error en la linea " + lineaActual + ": la cadena " + cadena_revisar + " es inválida");
                this.output=false;
            }
            
            
        }
        
        
        return resultado;
        
    }
    
    /**
     * Método para definir los mini autómatas para comparar
     * las expresiones regulares
     */
    public void vocabulario(){
        RegexConverter convert = new RegexConverter();
        
        String regex = convert.infixToPostfix("[a-z]");
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        letter_ = ThomsonAlgorithim.getAfn();
        
        regex = convert.infixToPostfix("[A-Z]");
        ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        Automata letterMayuscula_ = ThomsonAlgorithim.getAfn();
        letter_ =  ThomsonAlgorithim.union(letter_, letterMayuscula_);
       
       
        //letter_ = ThomsonAlgorithim.concatenacion(letter_, espacio_);
        //letter_ = ThomsonAlgorithim.concatenacion(espacio_, letter_);
        letter_.setTipo("Letra");
        
        regex = convert.infixToPostfix("("+" "+")*");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        espacio_  = ThomsonAlgorithim.getAfn();
        espacio_.setTipo("espacio");
        
        //System.out.println(letter_);
        regex = convert.infixToPostfix("[0-9]");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        digit_ = ThomsonAlgorithim.getAfn();
       // digit_ = ThomsonAlgorithim.concatenacion(digit_, espacio_);
        //digit_ = ThomsonAlgorithim.concatenacion(espacio_, digit_);
        digit_.setTipo("digit");
        
        
       
        Automata digitKleene = ThomsonAlgorithim.cerraduraKleene(digit_);
        //System.out.println(numberKleene);
        number_ = ThomsonAlgorithim.concatenacion(digit_, digitKleene);
        number_.setTipo("número");
        Automata letterOrDigit = ThomsonAlgorithim.union(letter_, digit_);
        //System.out.println(letterOrDigit);
        Automata letterOrDigitKleene = ThomsonAlgorithim.cerraduraKleene(letterOrDigit);
       // System.out.println(letterOrDigitKleene);
        ident_ = ThomsonAlgorithim.concatenacion(letter_, letterOrDigitKleene);
        ident_.setTipo("identificador");
       // System.out.println(ident_);
        Automata ap1 = ThomsonAlgorithim.afnSimple("\"");
        Automata ap2 = ThomsonAlgorithim.afnSimple("\"");
        Automata stringKleene = ThomsonAlgorithim.union(number_, letter_);
        string_ = ThomsonAlgorithim.cerraduraKleene(stringKleene);
        string_ = ThomsonAlgorithim.concatenacion(ap1, string_);
        string_ = ThomsonAlgorithim.concatenacion(string_,ap2);
        
        regex = convert.infixToPostfix("\\|\"|\'");
        ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        Automata specialChars = ThomsonAlgorithim.getAfn();
        string_ = ThomsonAlgorithim.union(string_, specialChars);
        
        
        string_.setTipo("string");
      
         
        
        
        Automata apch1 = ThomsonAlgorithim.afnSimple("\'");
        Automata apch2 = ThomsonAlgorithim.afnSimple("\'");
        Automata chKleene = ThomsonAlgorithim.union(number_, letter_);
        character_ = ThomsonAlgorithim.cerraduraKleene(chKleene);
        character_ = ThomsonAlgorithim.concatenacion(ap1, character_);
        character_ = ThomsonAlgorithim.concatenacion(character_,ap2);
        character_.setTipo("character");
        basicSet_ = ThomsonAlgorithim.union(string_, ident_);
        basicSet_.setTipo("Basic Set");
        
        regex = convert.infixToPostfix(" "+"="+" ");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        igual_  = ThomsonAlgorithim.getAfn();
        igual_.setTipo("=");
        
       
        Automata plus = ThomsonAlgorithim.afnSimple("+");
        Automata minus = ThomsonAlgorithim.afnSimple("-");
        plusOrMinus_ = ThomsonAlgorithim.union(plus, minus);
        plusOrMinus_.setTipo("(+|-)");
        
       
        
        
        
    }
    
    public Integer avanzarLinea(int lineaActual){
       while (true){
           if (this.cadena.containsKey(++lineaActual))
               return lineaActual;
       }
    }
    
    public Integer retrocederLinea(int lineaActual){
       while (true){
           if (this.cadena.containsKey(--lineaActual))
               return lineaActual;
       }
    }
    
    /**
     * Método para revisar que tipo de sub autómata es aceptado por una 
     * expresión regular
     * @param regex expresión regular a comparar
     */
    public void checkIndividualAutomata(String regex){
        ArrayList<Automata> conjunto = conjuntoAutomatas();
        ArrayList<Boolean> resultado = new ArrayList();
        for (int i = 0;i<regex.length();i++){
            Character ch = regex.charAt(i);
            for (int j = 0;j<conjunto.size();j++){
                resultado.add(sim.simular(ch.toString(), conjunto.get(j)));
               
            }
           
            ArrayList<Integer> posiciones = checkBoolean(resultado);
            resultado.clear();
            
           
            for (int k = 0;k<posiciones.size();k++){
                
                System.out.println(ch.toString()+ ": " + conjunto.get(posiciones.get(k)).getTipo());
            }
            if (posiciones.isEmpty()){
               System.out.println(ch.toString()+ " no fue reconocido");
            }
        }
    }
    
    /**
     * Método que devuelve las posiciones en las que el valor que tiene 
     * en cada posicion es true
     * @param bool arreglo de booleanos
     * @return arreglo de enteros
     */
    public ArrayList<Integer>  checkBoolean(ArrayList<Boolean> bool){
        ArrayList<Integer> posiciones = new ArrayList();
       
        for (int i = 0;i<bool.size();i++){
            if (bool.get(i))
                posiciones.add(i);
        }
        return posiciones;
        
    }
    
    /**
     * Método para crear un conjunto de autómatas
     * @return arreglo de autómatas
     */
    public ArrayList<Automata> conjuntoAutomatas(){
        ArrayList<Automata> conjunto = new ArrayList();
        conjunto.add(this.letter_);
        conjunto.add(this.digit_);
        conjunto.add(this.number_);
        conjunto.add(this.ident_);
        conjunto.add(this.string_);
        conjunto.add(this.character_);
        conjunto.add(this.plusOrMinus_);
        conjunto.add(this.igual_);
        conjunto.add(this.basicSet_);
        conjunto.add(this.espacio_);
        
        return conjunto;
        
    }
}


