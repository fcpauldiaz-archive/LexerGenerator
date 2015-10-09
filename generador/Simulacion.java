
 /**
* Universidad Del Valle de Guatemala* 07-ago-2015
* Pablo Díaz 13203
*/
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
/**
 * Clase para utilizar el metodo de move, e-closure y simulacion de
 * un automata
 * Incluye también un método para generar archivos DOT
 * @author Pablo
 */
public class Simulacion {
	private String resultado;
	private ArrayList caracteresIgnorar = new ArrayList();
	private Estado inicial_;
	public Simulacion(){
		caracteresIgnorar.add(resultadoGeneradorMain.EPSILON);
	}
	public HashSet<Estado> eClosure(Estado eClosureEstado){
		Stack<Estado> pilaClosure = new Stack();
		Estado actual = eClosureEstado;
		actual.getTransiciones();
		HashSet<Estado> resultado = new HashSet();
		pilaClosure.push(actual);
		while(!pilaClosure.isEmpty()){
			actual = pilaClosure.pop();
			for (Transicion t: (ArrayList<Transicion>)actual.getTransiciones()){
				if ((caracteresIgnorar.contains(t.getSimbolo()))&&!resultado.contains(t.getFin())){
					resultado.add(t.getFin());
					pilaClosure.push(t.getFin());
				}
			}
		}
		resultado.add(eClosureEstado); //la operacion e-Closure debe tener el estado aplicado
		return resultado;
	}
	public HashSet<Estado> move(HashSet<Estado> estados, String simbolo){
		HashSet<Estado> alcanzados = new HashSet();
		Iterator<Estado> iterador = estados.iterator();
		while (iterador.hasNext()){
			for (Transicion t: (ArrayList<Transicion>)iterador.next().getTransiciones()){
			Estado siguiente = t.getFin();
			String simb = (String) t.getSimbolo();
			if (simb.equals(simbolo)){
				alcanzados.add(siguiente);
			}
			}
		}
		return alcanzados;
	}
	public Estado move(Estado estado, String simbolo){
		ArrayList<Estado> alcanzados = new ArrayList();
		for (Transicion t: (ArrayList<Transicion>)estado.getTransiciones()){
			Estado siguiente = t.getFin();
			String simb = (String) t.getSimbolo();
			if (simb.equals(simbolo)&&!alcanzados.contains(siguiente)){
				alcanzados.add(siguiente);
			}
		}
		return alcanzados.get(0);
	}
	/**
	 * Método para simular un automata sin importar si es determinista o no deterministas
	 * 
	 * @param regex recibe la cadena a simular 
	 * @param automata recibe el automata a ser simulado
	 */
	public ArrayList simular(String regex, Automata automata)
	{
		 String returnString = "";
		 String returnString2 = "";
		 ArrayList returnArray = new ArrayList();
		 Estado final_ = null;
		Estado inicial = automata.getEstadoInicial();
		ArrayList<Estado> estados = automata.getEstados();
		ArrayList<Estado> aceptacion = new ArrayList(automata.getEstadosAceptacion());
		HashSet<Estado> conjunto = eClosure(inicial);
		char last = ' ';
		int currentState = 0;
		int finalState = 0;
		int init = 0;
		for (Character ch: regex.toCharArray()){
			conjunto = move(conjunto,ch.toString());
			HashSet<Estado> temp = new HashSet();
			Iterator<Estado> iter = conjunto.iterator();
			while (iter.hasNext()){
				Estado siguiente = iter.next();
				/**
				 * En esta parte es muy importante el metodo addAll
				 * porque se tiene que agregar el eClosure de todo el conjunto
				 * resultante del move y se utiliza un hashSet temporal porque
				 * no se permite la mutacion mientras se itera
				 */
					temp.addAll(eClosure(siguiente)); 
			}
			if (conjunto.isEmpty())
				returnString = (regex.substring(init,finalState));
			 if (temp.contains(aceptacion.get(0))){
				finalState++;
			}
			currentState++;
			conjunto=temp;
		}
		if (returnString.isEmpty())
			returnString = (regex.substring(init,finalState));
		 returnString2 = regex.substring(currentState);
		 returnArray.add(returnString);
		returnArray.add(returnString2);
		 return returnArray;
	}
	}
