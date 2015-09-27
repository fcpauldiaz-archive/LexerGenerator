/**
* Universidad del Valle de Guatemala
* Pablo Diaz 13203d
*/

package lexergenerator;

import java.util.HashMap;

/**
 *
 * @author Pablo
 */
public class LexerGeneratorMain {
    
    public static String EPSILON = "ε";
    public static char EPSILON_CHAR = EPSILON.charAt(0);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadFile read = new ReadFile();
        HashMap cocol = read.leerArchivo("cocol");
        HashMap input = read.leerArchivo("input");
        LexerAnalyzer lexer = new LexerAnalyzer(cocol);
        lexer.vocabulario();
        lexer.construct(cocol);
        
       
        
        if (lexer.getOutput()){
            System.out.println("");
            System.out.println("Generando Analizador Léxico....");
            CodeGenerator generator = new CodeGenerator(cocol);
            generator.encontrarNombre();
            generator.generarCharactersYKeywords();
            generator.generarClaseAnalizadora();
            generator.generarMain();
            System.out.println("");
            System.out.println("Ejecute el Main de la carpeta generador para probar el input");
        }
      
        
    }

}
