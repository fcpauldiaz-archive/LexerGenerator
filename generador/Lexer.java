/**
 * Nombre del archivo: Lexer.java
 * Universidad del Valle de Guatemala
 * Pablo Diaz 13203 
 * Descripción: Segundo proyecto. Generador de analizador léxico
**/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

public class Lexer {

	private Simulacion sim = new Simulacion();
	private ArrayList<Automata> automatas = new ArrayList();
	private HashMap<Integer,String> input;
	private ArrayList keywords = new ArrayList();
	public Lexer(HashMap input){
		this.input=input;
	
	}
	public void automatas(){

		RegexConverter convert_0= new RegexConverter();
		String regex_0 = convert_0.infixToPostfix("ý þø");
		AFNConstruct ThomsonAlgorithim_0 = new AFNConstruct(regex_0);
		ThomsonAlgorithim_0.construct();
		Automata temp_0 = ThomsonAlgorithim_0.getAfn();
		temp_0.setTipo("espacio");
		automatas.add(temp_0);

		RegexConverter convert_1= new RegexConverter();
		String regex_1 = convert_1.infixToPostfix("ý0þø");
		AFNConstruct ThomsonAlgorithim_1 = new AFNConstruct(regex_1);
		ThomsonAlgorithim_1.construct();
		Automata temp_1 = ThomsonAlgorithim_1.getAfn();
		temp_1.setTipo("zero");
		automatas.add(temp_1);

		RegexConverter convert_2= new RegexConverter();
		String regex_2 = convert_2.infixToPostfix("ýýý0þøþ|ýý1|2|3þøþþø");
		AFNConstruct ThomsonAlgorithim_2 = new AFNConstruct(regex_2);
		ThomsonAlgorithim_2.construct();
		Automata temp_2 = ThomsonAlgorithim_2.getAfn();
		temp_2.setTipo("zeroToThree");
		automatas.add(temp_2);

		RegexConverter convert_3= new RegexConverter();
		String regex_3 = convert_3.infixToPostfix("ý	þ");
		AFNConstruct ThomsonAlgorithim_3 = new AFNConstruct(regex_3);
		ThomsonAlgorithim_3.construct();
		Automata temp_3 = ThomsonAlgorithim_3.getAfn();
		temp_3.setTipo("tab");
		automatas.add(temp_3);

		RegexConverter convert_4= new RegexConverter();
		String regex_4 = convert_4.infixToPostfix("ýýýýý0þøþ|ýý1|2|3|4|5|6|7|8|9þøþþøþ|ýýA|B|C|D|E|F|a|b|c|d|e|fþøþþø");
		AFNConstruct ThomsonAlgorithim_4 = new AFNConstruct(regex_4);
		ThomsonAlgorithim_4.construct();
		Automata temp_4 = ThomsonAlgorithim_4.getAfn();
		temp_4.setTipo("hexDigit");
		automatas.add(temp_4);

		RegexConverter convert_5= new RegexConverter();
		String regex_5 = convert_5.infixToPostfix("ýA|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Zþ|ýa|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|zþ|ý)þø|ý*þø|ý(þø|+");
		AFNConstruct ThomsonAlgorithim_5 = new AFNConstruct(regex_5);
		ThomsonAlgorithim_5.construct();
		Automata temp_5 = ThomsonAlgorithim_5.getAfn();
		temp_5.setTipo("letter");
		automatas.add(temp_5);

		RegexConverter convert_6= new RegexConverter();
		String regex_6 = convert_6.infixToPostfix("ý1|2|3|4|5|6|7|8|9þø");
		AFNConstruct ThomsonAlgorithim_6 = new AFNConstruct(regex_6);
		ThomsonAlgorithim_6.construct();
		Automata temp_6 = ThomsonAlgorithim_6.getAfn();
		temp_6.setTipo("nonZeroDigit");
		automatas.add(temp_6);

		RegexConverter convert_7= new RegexConverter();
		String regex_7 = convert_7.infixToPostfix("ýýý0þøþ|ýý1|2|3|4|5|6|7þøþþø");
		AFNConstruct ThomsonAlgorithim_7 = new AFNConstruct(regex_7);
		ThomsonAlgorithim_7.construct();
		Automata temp_7 = ThomsonAlgorithim_7.getAfn();
		temp_7.setTipo("octalDigit");
		automatas.add(temp_7);

		RegexConverter convert_8= new RegexConverter();
		String regex_8 = convert_8.infixToPostfix("ý!|\"|#|$|%|&|\'þ");
		AFNConstruct ThomsonAlgorithim_8 = new AFNConstruct(regex_8);
		ThomsonAlgorithim_8.construct();
		Automata temp_8 = ThomsonAlgorithim_8.getAfn();
		temp_8.setTipo("some_chars");
		automatas.add(temp_8);

		RegexConverter convert_9= new RegexConverter();
		String regex_9 = convert_9.infixToPostfix("ýýý0þøþ|ýý1|2|3|4|5|6|7|8|9þøþþø");
		AFNConstruct ThomsonAlgorithim_9 = new AFNConstruct(regex_9);
		ThomsonAlgorithim_9.construct();
		Automata temp_9 = ThomsonAlgorithim_9.getAfn();
		temp_9.setTipo("digit");
		automatas.add(temp_9);

		RegexConverter convert_10= new RegexConverter();
		String regex_10 = convert_10.infixToPostfix("ýýý!|\"|#|$|%|&|\'þþ|ýý\\þøþþø");
		AFNConstruct ThomsonAlgorithim_10 = new AFNConstruct(regex_10);
		ThomsonAlgorithim_10.construct();
		Automata temp_10 = ThomsonAlgorithim_10.getAfn();
		temp_10.setTipo("escape_chars");
		automatas.add(temp_10);
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
			System.out.println("<"+conjunto.get(posiciones.get(k)).getTipo()+ ", " +"\""+regex +"\""+">");
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

	public void revisar(){
		for (Map.Entry<Integer, String> entry : input.entrySet()) {
			Integer key = entry.getKey();
			String value = entry.getValue();
			String[] parts = value.split(" ");
			for (int j = 0;j<value.length();j++){
				if (!keywords.contains(value))
					this.checkIndividualAutomata(value.charAt(j)+"", automatas,key);
			}
			if (keywords.contains(value))
					System.out.println("<"+value +"," +"\""+ value+"\""+">");
		}
	}

	public void keyWords(){
		keywords.add("if");
		keywords.add("while");
		keywords.add("boolean");
		keywords.add("byte");
		keywords.add("char");
		keywords.add("class");
		keywords.add("double");
		keywords.add("false");
		keywords.add("final");
		keywords.add("float");
		keywords.add("int");
		keywords.add("long");
		keywords.add("new");
		keywords.add("null");
		keywords.add("short");
		keywords.add("static");
		keywords.add("super");
		keywords.add("this");
		keywords.add("true");
		keywords.add("void");
		keywords.add(":");
		keywords.add(",");
		keywords.add("--");
		keywords.add(".");
		keywords.add("++");
		keywords.add("{");
		keywords.add("[");
		keywords.add("(");
		keywords.add("-");
		keywords.add("!");
		keywords.add("+");
		keywords.add("}");
		keywords.add("]");
		keywords.add(")");
		keywords.add("~");
		keywords.add("~");
		}

}
