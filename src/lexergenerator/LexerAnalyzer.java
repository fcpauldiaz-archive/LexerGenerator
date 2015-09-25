/**
* Universidad Del Valle de Guatemala
* 11-sep-2015
* Pablo Díaz 13203
*/

package lexergenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Clase para revisar la estructura de un archivo de CocoL
 * @author Pablo
 */
public class LexerAnalyzer {
    
    
    private final HashMap<Integer,String> cadena;
    
  
    private final String espacio = "(" +" "+")*";
    private final String ANY = this.espacio+"[ -']"+"|"+"[@-z]"+this.espacio;
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
    private Automata basicChar_;
    
    private Automata compiler_;
    private Automata end_;
    private final Simulacion sim;
    private final Stack compare = new Stack();
    private boolean union = false;
    private int indexAutomata=-1;
    
    private ArrayList<Automata> generador = new ArrayList();
    
    
 
    public LexerAnalyzer(HashMap cadena){
        this.sim = new Simulacion();
        this.cadena=cadena;
    }
    
      /**
     * Método para definir los mini autómatas para comparar
     * las expresiones regulares
     */
    public void vocabulario(){
        RegexConverter convert = new RegexConverter();
        
        
        
        
        String regex = convert.infixToPostfix(ANY);
        AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        letter_ = ThomsonAlgorithim.getAfn();
       
        
       /* regex = convert.infixToPostfix(espacio+"[A-Z]"+espacio);
        ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        Automata letterMayuscula_ = ThomsonAlgorithim.getAfn();
        letter_ =  ThomsonAlgorithim.union(letter_, letterMayuscula_);*/
       
       
        //letter_ = ThomsonAlgorithim.concatenacion(letter_, espacio_);
        //letter_ = ThomsonAlgorithim.concatenacion(espacio_, letter_);
        letter_.setTipo("Letra");
        
        regex = convert.infixToPostfix("("+" "+")*");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        espacio_  = ThomsonAlgorithim.getAfn();
        espacio_.setTipo("espacio");
        
        //System.out.println(letter_);
        regex = convert.infixToPostfix(espacio+"[0-9]"+espacio);
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
        character_ = ThomsonAlgorithim.union(number_, letter_);
        character_ = ThomsonAlgorithim.concatenacion(apch1, character_);
        character_ = ThomsonAlgorithim.concatenacion(character_,apch2);
        regex = convert.infixToPostfix("\"CHR\"(");
        ThomsonAlgorithim = new AFNConstruct(regex);
        ThomsonAlgorithim.construct();
        Automata leftChar = ThomsonAlgorithim.getAfn();
        
       
        Automata rigthChar = ThomsonAlgorithim.afnSimple(")");
        leftChar = ThomsonAlgorithim.concatenacion(number_, leftChar);
       
        Automata innerChar = ThomsonAlgorithim.concatenacion(rigthChar, leftChar);
       
        character_ = ThomsonAlgorithim.union(character_,innerChar);
        character_.setTipo("character");
       
        
        
        Automata pointChar = ThomsonAlgorithim.afnSimple(".");
        Automata pointChar2 = ThomsonAlgorithim.afnSimple(".");
        pointChar = ThomsonAlgorithim.concatenacion(pointChar, pointChar2);
        basicChar_ = ThomsonAlgorithim.concatenacion(character_, pointChar);
        basicChar_ = ThomsonAlgorithim.concatenacion(basicChar_,character_);
        basicChar_.setTipo("Basic Char");
        
        basicSet_ = ThomsonAlgorithim.union(string_, ident_);
        basicSet_ = ThomsonAlgorithim.union(basicSet_, basicChar_);
        basicSet_.setTipo("Basic Set");
        
        regex = convert.infixToPostfix("("+" " +")*"+"="+"("+" " +")*");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        igual_  = ThomsonAlgorithim.getAfn();
        igual_.setTipo("=");
      
        
        Automata plus = ThomsonAlgorithim.afnSimple("+");
        Automata minus = ThomsonAlgorithim.afnSimple("-");
        plusOrMinus_ = ThomsonAlgorithim.union(plus, minus);
        plusOrMinus_.setTipo("(+|-)");
        
       
       
        regex = convert.infixToPostfix("COMPILER");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        compiler_ = ThomsonAlgorithim.getAfn();
        compiler_.setTipo("\"COMPILER\"");
        regex = convert.infixToPostfix("END");
        ThomsonAlgorithim.setRegex(regex);
        ThomsonAlgorithim.construct();
        end_ = ThomsonAlgorithim.getAfn();
        end_.setTipo("\"END\"");
       
        
        
    }
    
   /**
    * Revisar segundo archivo de input
    * puede ser todo en una línea o diferentes líneas
    * @param cadena 
    */
   public  void check(HashMap<Integer,String> cadena){
       
       for (Map.Entry<Integer, String> entry : cadena.entrySet()) {
        Integer key = entry.getKey();
        String value = entry.getValue();
        String[] parts = value.split(" ");
        for (int i= 0;i<parts.length;i++){
            this.checkIndividualAutomata(parts[i], generador,key);
        }
        
    
}
       
   }
   
   
 
    /**
     * Revisar formato de la primera línea
     * ScannerSpecification
     * ParserSpecification
     * @param cadena HashMap de la cadena y sus números de línea
     */
    public void construct(HashMap<Integer,String> cadena){
        int lineaActual = 1;
        int index = 0;
       // ArrayList res = checkExpression("Cocol = \"COMPILER\""+this.espacio,lineaActual,index);
      
        ArrayList res_1 = checkAutomata(this.compiler_,lineaActual,index);
        
        int index2  = returnArray(res_1);
        
        
        ArrayList res2 = checkAutomata(this.ident_,lineaActual,index2);
        //System.out.println(res2.get(1));
        
        //ScannerSpecification
        
        ArrayList scan = scannerSpecification(lineaActual);
        if (scan.isEmpty())
            output=false;
        
        //ParserSpecification
     
        
       
        //END File
        lineaActual = (int)scan.get(0);
       
        ArrayList res3 = checkAutomata(this.end_,lineaActual,0);
        int index4 = returnArray(res3);
        
        ArrayList res4 = checkAutomata(this.ident_,lineaActual,index4);
        
        //revisar identificadores
        if (!res4.isEmpty()&&!res2.isEmpty()){
            if (!res4.get(1).toString().trim().equals(res2.get(1).toString().trim())){
                System.out.println("Error Linea " + lineaActual + ": los identificadores "+ res4.get(1).toString().trim()+ " y "+ res2.get(1).toString().trim() +" no coinciden");
                output=false;
            }
         
        }
        
        
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
            boolean res2 = setDeclaration(lineaActual);
            if (!res2){
                lineaActual = retrocederLinea(lineaActual);
                break;
                
            }
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
        
        //[TOKENS]
        
        lineaActual = avanzarLinea(lineaActual);
        //whitespaceDecl
        while (true){
        boolean space = whiteSpaceDeclaration(lineaActual);
            if (space)
                lineaActual = avanzarLinea(lineaActual);
            else{
                break;
            }
        }
        
      // lineaActual = avanzarLinea(lineaActual);
              
        ArrayList outputScan = new ArrayList();
        outputScan.add(lineaActual);
        outputScan.add(true);
       return outputScan;
    }
    
    /**
     * Método que representa el whitespace declaration
     * @param lineaActual
     * @return 
     */
    public boolean whiteSpaceDeclaration(int lineaActual){
        return this.cadena.get(lineaActual).contains("IGNORE");
    }
    
    public ArrayList keywordDeclaration(int lineaActual){
        
        if (this.cadena.get(lineaActual).contains("END")||this.cadena.get(lineaActual).contains("CHARACTERS")
                ||this.cadena.get(lineaActual).contains("IGNORE"))
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
    public boolean setDeclaration(int lineaActual){
        
        if (this.cadena.get(lineaActual).contains("\"END\"")||this.cadena.get(lineaActual).contains("KEYWORDS"))
            return false;
        //revisar identificador
        //ArrayList res1 = checkExpression(this.ident,lineaActual,0);
        
        try{
            int indexSearch = this.cadena.get(lineaActual).indexOf("=")-1;
            while (this.cadena.get(lineaActual).substring(0, indexSearch).contains(" "))
                indexSearch--;
           
            boolean identifier = checkAutomata(this.ident_,this.cadena.get(lineaActual).substring(0,indexSearch));
            ///  int index1  = returnArray(identifier);
              if (!identifier){
                  return false;
              }
        }catch(Exception e){
            System.out.println("Error, no hay \"=\" en la línea " + lineaActual);
        }
        
      
        
        //revisar '='
        //ArrayList res2 = checkExpression(this.espacio+'='+this.espacio,lineaActual,index1);
        
         try{
            int indexSearch = this.cadena.get(lineaActual).indexOf("=");
            int indexSearch2 = indexSearch + 1;
            boolean equals = checkAutomata(this.igual_,this.cadena.get(lineaActual).substring(indexSearch, indexSearch2));
            //int index2 = returnArray(equals);
            if (!equals)
                return false;
        }catch(Exception e){
            System.out.println("Error, no hay \"=\" en la línea " + lineaActual);
        }
     
        
        //revisar Set
         try{
            int indexSearch = this.cadena.get(lineaActual).indexOf("=")+1;
            String cadenaRevisar = this.cadena.get(lineaActual).substring(indexSearch);
            while(cadenaRevisar.startsWith(" "))
                cadenaRevisar = this.cadena.get(lineaActual).substring(++indexSearch);
            if (cadenaRevisar.contains("."))
                cadenaRevisar = cadenaRevisar.replace(".", "");
            boolean resSet = set(lineaActual,cadenaRevisar);
         }catch(Exception e){}
        //ArrayList resSet = set(lineaActual,index2+index1);
      
       // if (resSet.isEmpty())
        //    return new ArrayList();
        //int index3 = returnArray(resSet);
        
       //revisar '.'
       // ArrayList res4 = checkExpression(this.espacio+"."+this.espacio,lineaActual,index3);
        //if (res4.isEmpty())
          //  return new ArrayList();
        
        
        return true;
    }
    /**
     * Método para revisar el Set
     * @param regex string a revisar
     * @param lineaActual del archivo
     * @return ArrayList
     * Set = BasicSet (('+'|'-') BasicSet)*.
     */
    public boolean set(int lineaActual,String regex){
        int index = 0;
        
        String revisar =regex;
        //Set = BasicSet
        if (regex.contains("+"))
            revisar = regex.substring(0,regex.indexOf("+"));
        else if (regex.contains("-"))
            revisar = regex.substring(0,regex.indexOf("-"));
        
        boolean basic = basicSet(lineaActual,revisar);
        
        if (!basic)
            return false;
        
        
        
        while(true){
            if (regex.contains("+"))
                revisar = regex.substring(regex.indexOf("+"),regex.indexOf("+")+1);
            else
                break;
            boolean bl = checkAutomata(this.plusOrMinus_,revisar);
            if (!bl)
                break;
                
            regex = regex.substring(regex.indexOf("+")+1);
            
            boolean b = basicSet(lineaActual,regex.trim());
            if (!b)
                return false;



        }

        
        return true;
    }
    /**
     * 
     * @param lineaActual 
     * @param regex string a revisar
     * @return ArrayList
     * BasicSet = string | ident | Char [ "..." Char].
     */
    public boolean basicSet(int lineaActual,String regex){
        //ArrayList<String> cadenas = new ArrayList();
      
        //BasicSet = {string}
        
       // ident | string | "CHR(number).."CHR"(number).
       boolean resBasicSet = checkAutomata(this.basicSet_,regex);
       /*if (!resBasicSet.isEmpty()){
            cadenas.add((String)resBasicSet.get(1));

        }*/
        
       if (!resBasicSet)
            System.out.println("Error línea archivo "+ lineaActual + regex + " no fue reconocido");
    
       
        
        
       
        return resBasicSet;
        
    }
    
    
    /**
     * Método para revisar si un character
     * @param lineaActual
     * @param lastIndex
     * @return 
     */
    public ArrayList<String> Char(int lineaActual,int lastIndex){
        
        ArrayList res = checkAutomata(this.character_,lineaActual,lastIndex);
        if (!res.isEmpty()){
           
            return res;
        }
        
        return new ArrayList();
    }
    /**
     * Revisasr si el archivo no tiene errores
     */
    
    public void getOutput(){
        if (output){
            System.out.println("Archivo Cocol/R Aceptado");
        }
        else{
            System.out.println("Archivo  Cocol/R no aceptado, tiene errores de estructura");
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
               cadena_revisar = cadena_revisar.substring(0, cadena_revisar.lastIndexOf("."));
        }catch(Exception e){}
        
        ArrayList resultado = new ArrayList();
        
        boolean returnValue=sim.simular(cadena_revisar.trim(), param);
        
        
      
        
        if (returnValue){
            resultado.add(cadena_revisar.length()+preIndex);
            resultado.add(cadena_revisar);
            return resultado;
        }
        else{
            if (!cadena_revisar.isEmpty()){
                System.out.println("Error en la linea " + lineaActual + ": la cadena " + cadena_revisar + " es no es:" + param.getTipo());
                this.output=false;
            }
            
            
        }
        
        
        return resultado;
        
    }
    
    
    public boolean checkAutomata(Automata param, String regex){
        return sim.simular(regex, param);
    }
    
    public void crearAutomata(String cadena){
        if (cadena.startsWith("\"")){
            String or = "";
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
            

            RegexConverter convert = new RegexConverter();
            String regex = convert.infixToPostfix("("+or+")+");
           
            AFNConstruct ThomsonAlgorithim = new AFNConstruct(regex);
            ThomsonAlgorithim.construct();
            Automata temp = ThomsonAlgorithim.getAfn();
           

            if (union==true){
                if (indexAutomata!=-1)
                    temp = ThomsonAlgorithim.concatenacion(temp, generador.get(indexAutomata));
            }
            temp.setTipo((String)this.compare.pop());
           
           
            union=false;
            generador.add(temp);
        }
        else{
            indexAutomata = buscarAFN(cadena.trim());
            System.out.println(indexAutomata);
            union=true;
        }
        
    }
    
    public int buscarAFN(String tipo){
        for (int i = 0;i<generador.size();i++){
            if (generador.get(i).getTipo().equals(tipo))
                return i;
        }
        return -1;
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
     * Método para retroceder de línea, busca la línea anterior actual
     * @param lineaActual
     * @return lineaActual
     */
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
     * @param conjunto arreglo de autómatas
     */
    public void checkIndividualAutomata(String regex, ArrayList<Automata> conjunto,int lineaActual){
        
        ArrayList<Boolean> resultado = new ArrayList();
       
            
            for (int j = 0;j<conjunto.size();j++){
                resultado.add(sim.simular(regex, conjunto.get(j)));
               
            }
           
            ArrayList<Integer> posiciones = checkBoolean(resultado);
            //resultado.clear();
            
           
            for (int k = 0;k<posiciones.size();k++){
                
                System.out.println(regex+ ": " + conjunto.get(posiciones.get(k)).getTipo());
            }
            if (posiciones.isEmpty()){
               System.out.println("Error línea archivo " + lineaActual +" : "+regex+ " no fue reconocido");
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

    public ArrayList<Automata> getGenerador() {
        return generador;
    }
}


