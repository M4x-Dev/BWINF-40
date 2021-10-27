package generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Diese abstrakte Klasse bildet die Grundlage für den Generator der Wortfelder.
 * Dieser Generator kann die Generierungsanweisungen aus einer Datei einlesen und dann ein Wortfeld dazu generieren.
 */
public abstract class PatternGenerator {

    public enum DiagonalDirection {
        Ascending,
        Descending,
        Random
    }

    public static final String CHARACTER_EMPTY = " ";

    public String[][] pattern; //Wortfeld
    public HashMap<String, WordPosition> placedWords = new HashMap<>(); //Bereits platzierte Wörter in der Wortliste
    public HashMap<Integer, Integer> closedPositions = new HashMap<>(); //Bereits belegte Positionen in dem Wortfeld, welche nicht mehr betrachtet werden sollen.

    public int height; //Höhe des Wortfeldes
    public int width; //Breite des Wortfeldes
    public int wordCount; //Anzahl der Wörter, welche eingebaut werden sollen
    public final ArrayList<String> words = new ArrayList<>(); //Wortliste, welche verwendet werden soll
    public final Deque<String> wordQueue = new ConcurrentLinkedDeque<>(); //Wortliste, welche noch zur Verfügung steht

    public final Random instanceRandom = new Random();
    public final PositionGenerator xGenerator = new PositionGenerator(instanceRandom); //Benutzerdefinierter Zufallsgenerator für alle X-Koordinaten
    public final PositionGenerator yGenerator = new PositionGenerator(instanceRandom); //Benutzerdefinierter Zufallsgenerator für alle Y-Koorindaten

    /**
     * Konstruktor der Generator-Klasse.
     * Dieser Konstruktor liest die Eingabedatei und somit die Wortliste und die Feldgröße ein.
     * Die eingelesenen Daten werden in den Variablen der Klasse gespeichert.
     *
     * @param filePath Dateipfad der Eingabedatei (Wortliste).
     */
    public PatternGenerator(String filePath) {
        try {
            //Benötigte Instanzen zum Lesen der Datei
            StringBuilder contentBuilder = new StringBuilder();
            FileInputStream fileStream = new FileInputStream(filePath);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(fileStream));

            //Lesen der einzelnen Zeilen der Datei
            String line;
            while((line = streamReader.readLine()) != null) contentBuilder.append(line).append("\n");

            //Aufteilen des gelesenen Textes in die einzelnen Dateien
            String[] contentLines = contentBuilder.toString().split("\n");

            //Interpretieren der Daten
            //Einlesen der Höhe und der Breite der Wortliste
            height = Integer.parseInt(contentLines[0].split(" ")[0]);
            width = Integer.parseInt(contentLines[0].split(" ")[1]);

            //Einlesen der Wortanzahl
            wordCount = Integer.parseInt(contentLines[1]);

            //Einlesen der Wortliste
            words.addAll(Arrays.asList(contentLines).subList(2, wordCount + 2));

            //Sortieren der Wortliste
            Collections.sort(words);
            Collections.reverse(words);

            wordQueue.addAll(words);

            //Schließen der benötigten Ressourcen zum Lesen der Datei
            streamReader.close();
            fileStream.close();
        } catch (Exception e) {
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Hauptfunktion der Generator-Klasse, welche das Wortfeld generiert.
     * Diese Funktion generiert ein Wortfeld eines bestimmten Schwierigkeitsgrades mithilfe der angegebenen Parameter.
     *
     * Die Basisimplementierung dieser Funktion initialisiert die benötigten Variablen des Generators.
     *
     * @return Gibt das generierte Wortfeld zurück.
     */
    public String generatePattern() {
        pattern = new String[height][width];

        placedWords.clear();
        closedPositions.clear();

        prepareEmptySpaces();

        return "";
    }

    /**
     * Methode, welche alle Felder des Wortfeldes mit dem Blankzeichen auffüllt.
     * Diese Methode wird verwendet, damit kein Feld des Wortfeldes den Wert "null" aufweist.
     */
    protected void prepareEmptySpaces() {
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x] == null)
                    pattern[y][x] = PatternGenerator.CHARACTER_EMPTY;
            }
        }
    }

    /**
     * Funktion, welche das Wortfeld, welches als zweidimensionales Array vorliegt, in einen String umwandeln.
     * Dafür werden all Zeilen und Spalten aneinandergehängt, sodass das Feld in der Console ausgegebenen werden kann.
     *
     * @param matrix Matrix, welche konvertiert und ausgegeben werden soll.
     *
     * @return Gibt die formatierte Matrix zurück.
     */
    protected static String formatMatrix(String[][] matrix) {
        StringBuilder matrixBuilder = new StringBuilder();
        for (String[] strings : matrix) {
            for (String string : strings) {
                matrixBuilder.append(string).append(" ");
            }
            matrixBuilder.append("\n");
        }
        return matrixBuilder.toString();
    }

    /**
     * Abstrakte Methode, welche die übrigen Felder des Wortfeldes auffüllen soll.
     */
    protected abstract void fillEmptySpaces();

    /**
     * Methode, welche die Koorindaten eines neuen Wortes in zwei HashMaps hinzufügt.
     * Die eine HashMap beeinhaltet alle Positionen, welche bereits belegt sind.
     * Damit wird der Algorithmus insofern optimiert, sodass belegte Felder, wenn genügend Platz vorhanden ist, nicht mehr betrachtet werden.
     * Die Koordinaten des Wortes werden dazu in eine Ausnahmeliste des Zufallsgenerators hinzugefügt, sodass diese Zahlen nicht mehr generiert werden.
     *
     * @param x X-Koorindate des Wortes, welches dem Feld hinzugefügt wurde.
     * @param y Y-Koordinate des Wrotes, welches dem Feld hinzugefügt wurde.
     */
    public void addClosedCoordinate(int x, int y) {
        closedPositions.put(x, y);
        xGenerator.addExclusion(x);
        yGenerator.addExclusion(y);
    }

}
