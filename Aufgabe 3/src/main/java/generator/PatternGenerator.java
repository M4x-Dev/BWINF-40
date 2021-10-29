package generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
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
    public static final int COORDINATE_GENERATE = -1;

    protected String[][] pattern; //Wortfeld
    protected HashMap<String, WordPosition> placedWords = new HashMap<>(); //Bereits platzierte Wörter in der Wortliste
    protected HashMap<Integer, Integer> closedPositions = new HashMap<>(); //Bereits belegte Positionen in dem Wortfeld, welche nicht mehr betrachtet werden sollen.

    public int height; //Höhe des Wortfeldes
    public int width; //Breite des Wortfeldes
    public int wordCount; //Anzahl der Wörter, welche eingebaut werden sollen
    protected boolean randomCrossingEnabled = false; //Dürfen sich Wörter zufällig überschneiden?

    public final ArrayList<String> words = new ArrayList<>(); //Wortliste, welche verwendet werden soll
    protected final Deque<String> wordQueue = new ConcurrentLinkedDeque<>(); //Wortliste, welche noch zur Verfügung steht

    protected final Random instanceRandom = new Random();
    protected final PositionGenerator xGenerator = new PositionGenerator(instanceRandom); //Benutzerdefinierter Zufallsgenerator für alle X-Koordinaten
    protected final PositionGenerator yGenerator = new PositionGenerator(instanceRandom); //Benutzerdefinierter Zufallsgenerator für alle Y-Koordinaten

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

            //Schließen der benötigten Ressourcen zum Lesen der Datei
            streamReader.close();
            fileStream.close();
        } catch (Exception e) {
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Methode, welche das Wortfeld in einer Datei abspeichert.
     *
     * @param filePath Pfad der Ausgabedatei.
     */
    public void exportToFile(String filePath) {
        try {
            PrintWriter printWriter = new PrintWriter(filePath, StandardCharsets.UTF_8);
            printWriter.print(formatMatrix(pattern));
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Hauptfunktion der Generator-Klasse, welche das Wortfeld generiert.
     * Diese Funktion generiert ein Wortfeld eines bestimmten Schwierigkeitsgrades mithilfe der angegebenen Parameter.
     *
     * Die Basisimplementierung dieser Funktion initialisiert die benötigten Variablen des Generators.
     * Dazu versucht der Algorithmus alle Wörter der Liste abzuarbeiten und auf dem Feld zu platzieren.
     * Diese geschieht mit einer Queue, welche die Wörter nach der Größe geordnet (größte zuerst) hinzufügt.
     * Wenn ein Wort nicht hinzugefügt werden kann, wird es der Queue wieder hinzugefügt und später erneut abgearbeitet.
     *
     * @return Gibt das generierte Wortfeld zurück.
     */
    public String generatePattern() {
        //Variablen werden initialisiert
        int placementAttempts = 0;
        pattern = new String[height][width];
        placedWords.clear();
        closedPositions.clear();

        //Wortliste wird nach der Größe absteigend sortiert und der Queue hinzugefügt
        Collections.sort(words);
        Collections.reverse(words);
        wordQueue.addAll(words);

        //Alle Felder des Wortfeldes werden mit einem Leerzeichen aufgefüllt
        prepareEmptySpaces();

        //Hauptschleife des Algorithmus
        while(wordQueue.peek() != null) { //Solange Elemente in der Queue enthalten sind
            placementAttempts++; //Versuchszahl wird erhöht
            if(placementAttempts > wordCount * 3) {
                //Bei zu vielen Versuchen wird der Algorithmus abgebrochen, sodass kein Stackoverflow entsteht
                System.err.println("Failed to fit all words on the board. Restarting...");
                return generatePattern();
            }

            //Das Wort wird aus der Queue entfernt, wenn es erfolgreich platziert werden konnte
            if(placeWord(wordQueue.peekFirst())) wordQueue.removeFirst();
        }

        //Verbleibende Leerstellen auffüllen
        fillEmptySpaces();

        //Rückgabe des generierten Wortfeldes
        return formatMatrix(pattern);
    }

    /**
     * Funktion, welche ein bestimmtes Wort auf dem Wortfeld platzieren soll.
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    protected abstract boolean placeWord(String word);

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
     * Methode, welche die Koordinaten eines neuen Wortes in zwei HashMaps hinzufügt.
     * Die eine HashMap beeinhaltet alle Positionen, welche bereits belegt sind.
     * Damit wird der Algorithmus insofern optimiert, sodass belegte Felder, wenn genügend Platz vorhanden ist, nicht mehr betrachtet werden.
     * Die Koordinaten des Wortes werden dazu in eine Ausnahmeliste des Zufallsgenerators hinzugefügt, sodass diese Zahlen nicht mehr generiert werden.
     *
     * @param x X-Koordinate des Wortes, welches dem Feld hinzugefügt wurde.
     * @param y Y-Koordinate des Wortes, welches dem Feld hinzugefügt wurde.
     */
    protected void addClosedCoordinate(int x, int y) {
        closedPositions.put(x, y);
        xGenerator.addExclusion(x);
        yGenerator.addExclusion(y);
    }

    /**
     * Funktion, welche einen zufälligen Buchstaben aus dem Alphabet zurückgibt.
     *
     * @return Gibt einen zufälligen Buchstaben zurück.
     */
    protected String getRandomChar() {
        return String.valueOf((char)(instanceRandom.nextInt(26) + 'a')).toUpperCase();
    }

}
