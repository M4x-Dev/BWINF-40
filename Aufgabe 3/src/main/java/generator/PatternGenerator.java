package generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Diese abstrakte Klasse bildet die Grundlage für den Generator der Wortfelder.
 * Dieser Generator kann die Generierungsanweisungen aus einer Datei einlesen und dann ein Wortfeld dazu generieren.
 */
public abstract class PatternGenerator {

    public static final String CHARACTER_EMPTY = " ";
    public static final int MAX_ATTEMPTS_RECURSIVE = 10;

    public int height; //Höhe des Wortfeldes
    public int width; //Breite des Wortfeldes
    public int wordCount; //Anzahl der Wörter, welche eingebaut werden sollen
    public final ArrayList<String> words = new ArrayList<>(); //Wortliste, welche verwendet werden soll

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

    public abstract String generatePattern();

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

}
