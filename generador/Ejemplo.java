/**
 * Nombre del archivo: Ejemplo.java
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

public class Ejemplo {

	private Simulacion sim = new Simulacion();
	private ArrayList<Automata> automatas = new ArrayList();
	private HashMap<Integer,String> input;
	public Ejemplo(HashMap input){
		this.input=input;
	
	}
	public void automatas(){

		RegexConverter convert_0= new RegexConverter();
		String regex_0 = convert_0.infixToPostfix(" b");
		AFNConstruct ThomsonAlgorithim_0 = new AFNConstruct(regex_0);
		ThomsonAlgorithim_0.construct();
		Automata temp_0 = ThomsonAlgorithim_0.getAfn();
		temp_0.setTipo("character");
		automatas.add(temp_0);

		RegexConverter convert_1= new RegexConverter();
		String regex_1 = convert_1.infixToPostfix(" 0|1");
		AFNConstruct ThomsonAlgorithim_1 = new AFNConstruct(regex_1);
		ThomsonAlgorithim_1.construct();
		Automata temp_1 = ThomsonAlgorithim_1.getAfn();
		temp_1.setTipo("binario");
		automatas.add(temp_1);

		RegexConverter convert_2= new RegexConverter();
		String regex_2 = convert_2.infixToPostfix("digit+"ABCDEF"");
		AFNConstruct ThomsonAlgorithim_2 = new AFNConstruct(regex_2);
		ThomsonAlgorithim_2.construct();
		Automata temp_2 = ThomsonAlgorithim_2.getAfn();
		temp_2.setTipo("hexdigit");
		automatas.add(temp_2);

		RegexConverter convert_3= new RegexConverter();
		String regex_3 = convert_3.infixToPostfix(" a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z");
		AFNConstruct ThomsonAlgorithim_3 = new AFNConstruct(regex_3);
		ThomsonAlgorithim_3.construct();
		Automata temp_3 = ThomsonAlgorithim_3.getAfn();
		temp_3.setTipo("letter");
		automatas.add(temp_3);

		RegexConverter convert_4= new RegexConverter();
		String regex_4 = convert_4.infixToPostfix(" L|O|L");
		AFNConstruct ThomsonAlgorithim_4 = new AFNConstruct(regex_4);
		ThomsonAlgorithim_4.construct();
		Automata temp_4 = ThomsonAlgorithim_4.getAfn();
		temp_4.setTipo("lol");
		automatas.add(temp_4);

		Automata temp_5 =ThomsonAlgorithim_0.afnSimple(" ");
		temp_5.setTipo("space");
		automatas.add(temp_5);

		RegexConverter convert_6= new RegexConverter();
		String regex_6 = convert_6.infixToPostfix(" 1|2|3|4|5|6|7|8|9");
		AFNConstruct ThomsonAlgorithim_6 = new AFNConstruct(regex_6);
		ThomsonAlgorithim_6.construct();
		Automata temp_6 = ThomsonAlgorithim_6.getAfn();
		temp_6.setTipo("digit");
		automatas.add(temp_6);
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

	public void revisar(){
		for (Map.Entry<Integer, String> entry : input.entrySet()) {
			Integer key = entry.getKey();
			String value = entry.getValue();
			String[] parts = value.split(" ");
			for (int j = 0;j<value.length();j++){
				for (int i= 0;i<parts.length;i++){
					this.checkIndividualAutomata(value.charAt(j)+"", automatas,key);
				}
			}
		}
	}

}
