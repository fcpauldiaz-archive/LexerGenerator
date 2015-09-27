
import java.util.HashMap;
/**
*
* @author Pablo
*/
public class resultadoGeneradorMain {
public static String EPSILON = "ε";
public static char EPSILON_CHAR = EPSILON.charAt(0);
/**
* @param args the command line arguments
 */
public static void main(String[] args) {
	// TODO code application logic here
	ReadFile read = new ReadFile();
	HashMap input = read.leerArchivo("input");
	Ejemplo resGenerator = new Ejemplo(input);
	resGenerator.automatas();
	resGenerator.keyWords();
	resGenerator.revisar();
	}
}

