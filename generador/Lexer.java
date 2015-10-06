/**
 * Nombre del archivo: Lexer.java
 * Universidad del Valle de Guatemala
 * Pablo Diaz 13203 
 * Descripción: Segundo proyecto. Generador de analizador léxico
**/

import java.util.Map;
import java.util.HashSet;
import javax.swing.JFileChooser;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Iterator;

public class Lexer {

	private Simulacion sim = new Simulacion();
	private ArrayList<Automata> automatas = new ArrayList();
	private HashMap<Integer,String> input;
	private ArrayList keywords = new ArrayList();
	private String ignoreSets = " ";
	private HashSet<Token> tokens = new HashSet();
	public Lexer(HashMap input){
		this.input=input;
	
	}
	public void automatas(){

		RegexConverter convert_0= new RegexConverter();
		String regex_0 = convert_0.infixToPostfix("≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥ ≤≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥≥∞         ∫ ≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥ ≤≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥≥∞ . ≤≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥≥∞ ≤E ≤+∫-≥Ω ≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥ ≤≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥≥∞≥Ω");
		AFNConstruct ThomsonAlgorithim_0 = new AFNConstruct(regex_0);
		ThomsonAlgorithim_0.construct();
		Automata temp_0 = ThomsonAlgorithim_0.getAfn();
		temp_0.setTipo("number");
		automatas.add(temp_0);

		RegexConverter convert_1= new RegexConverter();
		String regex_1 = convert_1.infixToPostfix("≤≤A∫B∫C∫D∫E∫F∫G∫H∫I∫J∫K∫L∫M∫N∫O∫P∫Q∫R∫S∫T∫U∫V∫W∫X∫Y∫Z≥∫≤a∫b∫c∫d∫e∫f∫g∫h∫i∫j∫k∫l∫m∫n∫o∫p∫q∫r∫s∫t∫u∫v∫w∫x∫y∫z≥∫+≥≤≤≤A∫B∫C∫D∫E∫F∫G∫H∫I∫J∫K∫L∫M∫N∫O∫P∫Q∫R∫S∫T∫U∫V∫W∫X∫Y∫Z≥∫≤a∫b∫c∫d∫e∫f∫g∫h∫i∫j∫k∫l∫m∫n∫o∫p∫q∫r∫s∫t∫u∫v∫w∫x∫y∫z≥∫+≥≥∞");
		AFNConstruct ThomsonAlgorithim_1 = new AFNConstruct(regex_1);
		ThomsonAlgorithim_1.construct();
		Automata temp_1 = ThomsonAlgorithim_1.getAfn();
		temp_1.setTipo("letter");
		automatas.add(temp_1);

		RegexConverter convert_2= new RegexConverter();
		String regex_2 = convert_2.infixToPostfix("≤  ≤0≥ ∫ ≤1∫2∫3∫4∫5∫6∫7∫8∫9≥ ≤ ≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥ ≥∞  ∫ ≤ 0x ∫ 0X ≥ ≤≤≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥≥∫≤≤A∫B∫C∫D∫E∫F∫a∫b∫c∫d∫e∫f≥≥≥ ≤ ≤≤≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7∫8∫9≥≥≥≥∫≤≤A∫B∫C∫D∫E∫F∫a∫b∫c∫d∫e∫f≥≥≥ ≥∞  ∫ 0 ≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7≥≥≥ ≤ ≤≤≤0≥≥∫≤≤1∫2∫3∫4∫5∫6∫7≥≥≥ ≥∞  ≥ ≤ l ∫ L ≥Ω");
		AFNConstruct ThomsonAlgorithim_2 = new AFNConstruct(regex_2);
		ThomsonAlgorithim_2.construct();
		Automata temp_2 = ThomsonAlgorithim_2.getAfn();
		temp_2.setTipo("intLit");
		automatas.add(temp_2);

		RegexConverter convert_3= new RegexConverter();
		String regex_3 = convert_3.infixToPostfix("≤≤A∫B∫C∫D∫E∫F∫G∫H∫I∫J∫K∫L∫M∫N∫O∫P∫Q∫R∫S∫T∫U∫V∫W∫X∫Y∫Z≥∫≤a∫b∫c∫d∫e∫f∫g∫h∫i∫j∫k∫l∫m∫n∫o∫p∫q∫r∫s∫t∫u∫v∫w∫x∫y∫z≥∫+≥ ≤_≥∞ *");
		AFNConstruct ThomsonAlgorithim_3 = new AFNConstruct(regex_3);
		ThomsonAlgorithim_3.construct();
		Automata temp_3 = ThomsonAlgorithim_3.getAfn();
		temp_3.setTipo("ident1");
		automatas.add(temp_3);
	}
	 /**
 	* Método para revisar que tipo de sub autómata es aceptado por una 
	* expresión regular
	* @param regex expresión regular a comparar
	* @param conjunto arreglo de autómatas
	*/
	public void checkIndividualAutomata(String regex, ArrayList<Automata> conjunto,int lineaActual){
		ArrayList<Boolean> resultado = new ArrayList();
		TreeMap aceptados = new TreeMap(new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			Integer a1 = o1.length();
			Integer a2 = o2.length();
			return a2-a1;
		}
		});
		for (int j = 0;j<conjunto.size();j++){
			ArrayList returnArray = (sim.simular(regex, conjunto.get(j)));
			String returnString = (String)returnArray.get(0);
			if (!returnString.isEmpty())
				 aceptados.put(returnString, conjunto.get(j).getTipo());
		}
		if (!aceptados.isEmpty()) 
			tokens.add(new Token(aceptados.firstEntry().getValue(),aceptados.firstKey()));
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
			String[] parts = value.split(ignoreSets);
			for (String part : parts) {
				if (!keywords.contains(part))
					 this.checkIndividualAutomata(part + "", automatas, key);
			if (keywords.contains(parts))
					tokens.add(new Token(part,part));
			}
		}
		 for (Token tk: tokens){
			System.out.println(tk);
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
		keywords.add("=");
		}

}
