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
public class LexerGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadFile read = new ReadFile();
        HashMap input = read.leerArchivo();
        LexerAnalyzer lexer = new LexerAnalyzer(input);
        lexer.revisarSintaxisCocoL(input);
        //lexer.check(input);
        
    }

}
