/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203d
*/

package lexergenerator;

import java.io.File;
import java.util.HashMap;
import javax.swing.JFileChooser;

/**
 *
 * @author Pablo
 */
public class LexerGeneratorMain {
    
    public static String EPSILON = "ε";
    public static char EPSILON_CHAR = EPSILON.charAt(0);
    public static Errors errores = new Errors();
  
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadFile read = new ReadFile();
        File file = new File("cocol"+".txt");
        
          HashMap cocol = read.leerArchivo(file);
        
        
        LexerAnalyzer lexer = new LexerAnalyzer(cocol);
        lexer.vocabulario();
        lexer.construct(cocol);
        
       
        
        if (lexer.getOutput()){
            System.out.println("");
            System.out.println("Generando Analizador Léxico....");
            CodeGenerator generator = new CodeGenerator(cocol);
            generator.encontrarNombre();
            generator.generarCharactersYKeywords();
            generator.generarTokens();
            generator.generarClaseAnalizadora();
            generator.generarMain();
            generator.generarSimulacion();
            generator.generarClaseToken();
            System.out.println("");
            System.out.println("Ejecute el Main de la carpeta generador para probar el input");
        }
        System.out.println("Cantidad Errores: " + errores.getCount());
      
        
    }

}
