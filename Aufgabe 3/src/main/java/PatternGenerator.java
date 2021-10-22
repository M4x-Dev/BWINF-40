import java.util.ArrayList;

/**
 * Diese abstrakte Klasse bildet die Grundlage für den Generator der Wortfelder.
 * Dieser Generator kann die Generierungsanweisungen aus einer Datei einlesen und dann ein Wortfeld dazu generieren.
 */
public abstract class PatternGenerator {

    private final int width;
    private final int height;
    private final int wordCount;
    private final ArrayList<String> words;

    public PatternGenerator(String filePath) {
        try {
            //Benötigte Instanzen zum Lesen der Datei
            StringBuilder contentBuilder = new StringBuilder();
        } catch (Exception e) {
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    abstract String generatePattern();

}
