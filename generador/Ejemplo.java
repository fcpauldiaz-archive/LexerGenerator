/**
 * Nombre del archivo: Ejemplo.java
 * Universidad del Valle de Guatemala
 * Descripción: Segundo proyecto. Generador de analizador léxico
**/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Ejemplo {

private Simulacion sim = new Simulacion();
	public Ejemplo(){

	}
	public void automatas(){
		ArrayList<Automata> automatas = new ArrayList();

		RegexConverter convert_0= new RegexConverter("0|1");
		String regex_0 = convert_0.infixToPostfix();
		AFNConstruct ThomsonAlgorithim_0 = new AFNConstruct(regex_0);
		ThomsonAlgorithim_0.construct();
		Automata temp_0 = ThomsonAlgorithim_0.getAfn();
		temp.setTipo("binario")
		automatas.add(temp_0);

		RegexConverter convert_1= new RegexConverter("(1|2|3|4|5|6|7|8|9)(A|B|C|D|E|F)");
		String regex_1 = convert_1.infixToPostfix();
		AFNConstruct ThomsonAlgorithim_1 = new AFNConstruct(regex_1);
		ThomsonAlgorithim_1.construct();
		Automata temp_1 = ThomsonAlgorithim_1.getAfn();
		temp.setTipo("hexdigit")
		automatas.add(temp_1);

		RegexConverter convert_2= new RegexConverter("a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z");
		String regex_2 = convert_2.infixToPostfix();
		AFNConstruct ThomsonAlgorithim_2 = new AFNConstruct(regex_2);
		ThomsonAlgorithim_2.construct();
		Automata temp_2 = ThomsonAlgorithim_2.getAfn();
		temp.setTipo("letter")
		automatas.add(temp_2);

		RegexConverter convert_3= new RegexConverter("L|O|L");
		String regex_3 = convert_3.infixToPostfix();
		AFNConstruct ThomsonAlgorithim_3 = new AFNConstruct(regex_3);
		ThomsonAlgorithim_3.construct();
		Automata temp_3 = ThomsonAlgorithim_3.getAfn();
		temp.setTipo("lol")
		automatas.add(temp_3);

		RegexConverter convert_4= new RegexConverter("1|2|3|4|5|6|7|8|9");
		String regex_4 = convert_4.infixToPostfix();
		AFNConstruct ThomsonAlgorithim_4 = new AFNConstruct(regex_4);
		ThomsonAlgorithim_4.construct();
		Automata temp_4 = ThomsonAlgorithim_4.getAfn();
		temp.setTipo("digit")
		automatas.add(temp_4);
	}
}
