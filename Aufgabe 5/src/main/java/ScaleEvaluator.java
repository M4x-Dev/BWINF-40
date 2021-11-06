import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Klasse, welche die Versuche mit der {@link Scale}-Klasse durchführt.
 * Das Ziel des Algorithmuses ist herauszufinden, ob alle Gewichte zwischen 10g und 10kg mit den gegebenen Gewichten dargestellt werden können.
 * Diese Überprüfung übernimmt diese Klasse.
 */
public class ScaleEvaluator {

    public final ArrayList<ScaleWeight> availableWeights = new ArrayList<>(); //Zur Verfügung stehende Gewichte

    /**
     * Konstruktor der Klasse, welcher die verfügbaren Gewichte aus einer Datei einließt.
     *
     * @param filePath Dateipfad der Datei mit den verfügbaren Gewichten.
     */
    public ScaleEvaluator(String filePath) {
        try {
            //Initialisieren der benötigten Ressourcen
            StringBuilder contentBuilder = new StringBuilder();
            InputStream fileStream = new FileInputStream(filePath);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));

            //Auslesen der Inhalte der Datei
            String line;
            while((line = fileReader.readLine()) != null) contentBuilder.append(line).append("\n");
            String[] fileContent = contentBuilder.toString().split("\n");

            //Interpretieren der Daten
            int weightAmount = Integer.parseInt(fileContent[0]);

            //Verfügbaren Gewichte werden der Liste hinzugefügt
            for(int i = 0; i < weightAmount; i++) {
                String[] splitContent = fileContent[1 + i].split(" ");

                for(int a = 0; a < Integer.parseInt(splitContent[1]); a++)
                    availableWeights.add(new ScaleWeight(Integer.parseInt(splitContent[0])));
            }

            //Schließen der benötigten Ressourcen
            fileReader.close();
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Funktion, welche die eigentlichen Versuche durchführt.
     * Diese Funktion verwendet eine Schleife, deren Iterationsvariable von 10 bis 10000 in 10er Schritten aufsteigt.
     * Somit werden alle Gewichte zwischen 10g und 10kg in 10g-Schritten abgedeckt.
     * Dieser Wert wird verwendet, um herauszufinden, ob die Waage diesen Zielwert abbilden kann.
     * Das Ergebnis jedes einzelnen Versuches wird gespeichert und am Ende zurückgegeben.
     *
     * @return Gibt eine Liste an Versuchsergebnissen zurück.
     */
    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult(); //Instanz, welche das Ergebnis beeinhalten wird

        //Schleife, welche alle Gewichte zwischen 10g und 10kg ausprobiert
        for(int i = 1; i <= 10000 / 10; i++) {
            int targetWeight = 10 * i; //Zielgewicht
            Scale.ScaleState scaleResult = new Scale().balance(targetWeight, new ArrayList<>(availableWeights));
            result.resultEntries.add(new EvaluationResultEntry(
                    scaleResult.leftWeights(), //Gewichte auf der linken Seite der Waage
                    scaleResult.rightWeights(), //Gewichte auf der rechten Seite der Waage
                    Scale.sumWeights(scaleResult.rightWeights()), //Gewicht der rechten Seite
                    Scale.sumWeights(scaleResult.rightWeights()) - Scale.sumWeights(scaleResult.leftWeights()), //Differenz zum Zielgewicht, falls dieses nicht erreicht werden konnte
                    targetWeight, //Zielgewicht
                    scaleResult.balanced() //Konnte das Zielgewicht erreicht werden?
            ));
        }

        return result; //Rückgabe des Ergebnisses
    }

    /**
     * Klasse, welche die Versuchsergebnisse beeinhaltet.
     * Diese Klasse beeinhaltet eine Liste von allen probierten Gewichten zwischen 10g und 10kg und die dazugehörigen Ergebnisse.
     */
    public static class EvaluationResult {

        final ArrayList<EvaluationResultEntry> resultEntries = new ArrayList<>(); //Liste der Versuchsergebnisse

        /**
         * Methode, welche die Versuchsergebnisse in einer Datei speichert.
         *
         * @param filePath Pfad der Ausgabedatei.
         */
        public void export(String filePath) {
            try {
                //Schreiben der Inhalte in die Datei
                PrintWriter outputWriter = new PrintWriter(filePath, StandardCharsets.UTF_8);
                resultEntries.forEach(entry -> {
                    if(entry.scaleBalanced) outputWriter.println(entry.targetWeight + "g: " + entry.leftWeights + " --- " + entry.rightWeights);
                    else outputWriter.println(entry.achievedWeight + "g/" + entry.targetWeight + "g: " + entry.leftWeights + " --- " + entry.rightWeights);
                });

                //Schließen der benötigten Ressourcen
                outputWriter.flush();
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Something went wrong :/ - " + e.getMessage());
            }
        }

    }

    /**
     * Record, welcher die Versuchsergebnisse für ein einzelnes Zielgewicht beeinhaltet.
     */
    public record EvaluationResultEntry(ArrayList<ScaleWeight> leftWeights, ArrayList<ScaleWeight> rightWeights, int achievedWeight, int achievedDifference, int targetWeight, boolean scaleBalanced) {

        /**
         * Funktion, welche die Versuchsergebnisse formatiert, sodass zu erkennen ist was auf den Seiten der Waage steht.
         *
         * @return Gibt die formatierten Versuchsergebnisse zurück.
         */
        public String printState() {
            return leftWeights + " --- " + rightWeights;
        }

    }

}