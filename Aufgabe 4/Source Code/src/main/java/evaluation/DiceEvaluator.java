/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

package evaluation;

import simulation.LudoDice;
import simulation.LudoPlayer;
import simulation.LudoSimulation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasse, welche für die Auswertung und den Vergleich der gegebenen Würfel zuständig ist.
 */
public class DiceEvaluator {

    public static int SIMULATION_RECURSIONS = 10000; //Wiederholungen der Simulationen

    private final ArrayList<LudoDice> dices = new ArrayList<>();

    /**
     * Konstruktor der Klasse, welche diese aus einer Beispieldatei erstellt.
     * Dieser Konstruktor liest alle Würfel ein und fügt diese einer Liste hinzu.
     *
     * @param filePath Dateipfad der Beispieldatei.
     */
    public DiceEvaluator(String filePath) {
        try {
            //Benötigte Instanzen zum Lesen der Datei
            StringBuilder contentBuilder = new StringBuilder();
            FileInputStream fileStream = new FileInputStream(filePath);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(fileStream));

            //Lesen der einzelnen Zeilen der Datei
            String line;
            while((line = streamReader.readLine()) != null) contentBuilder.append(line).append("\n");

            //Aufteilen des gelesenen Textes in die einzelnen Zeilen
            String[] contentLines = contentBuilder.toString().split("\n");

            //Interpretieren der Daten
            int diceCount = Integer.parseInt(contentLines[0]);

            for(int i = 0; i < diceCount; i++)
                dices.add(new LudoDice(contentLines[1 + i]));

            //Schließen der benötigten Ressourcen zum Lesen der Datei
            streamReader.close();
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Funktion, welche alle Würfel miteinander vergleicht und so den Würfel mit den höchsten Gewinnchancen ermittelt.
     * Dazu verwendet der Algorithmus insgesamt drei for-Schleifen.
     * <br>Die erste Schleife wählt den ersten Würfel aus und durchläuft dabei all Würfel vom ersten bis zum vorletzten.
     * <br>Die zweite Schleife wählt den zweiten Würfel aus und durchläuft dabei alle Würfel vom (a + 1)-sten Würfel bis zum letzten.
     * Durch diese beiden Schleifen wird gewährleistet, dass jede Kombination immer nur einmal vorkommt und die Statistik somit nicht verzerrt wird.
     * Die letzte Schleife führt dann die Simulationen des Spieles durch, deren Ergebnisse dann gespeichert werden.
     * Dabei beginnt in der ersten Hälfte der erste Spieler und in der zweiten Hälfte der zweite Spieler.
     *
     * @return Gibt das Ergebnis der Evaluation zurück.
     */
    public EvaluatorResult evaluate() {
        //Ergebnis-Klasse, welche am Ende zurückgegeben wird
        EvaluatorResult result = new EvaluatorResult();

        //Äußere Schleife, welche die Würfel durchläuft
        for (int a = 0; a < dices.size() - 1; a++) {
            //Innere Schleife, welche die Würfel durchläuft
            for (int b = a + 1; b < dices.size(); b++) {
                //Schleife, welche die Simulationen an sich startet
                for (int i = 0; i < SIMULATION_RECURSIONS; i++) {
                    //Simulation der beiden Würfel, welche nun gestartet wird
                    LudoSimulation simulation = new LudoSimulation(dices.get(a), dices.get(b));
                    LudoPlayer winner = simulation.simulate(i < SIMULATION_RECURSIONS / 2);

                    //Speichern der Ergebnisse
                    if (winner != null) {
                        //Das Spiel fiel unentschieden aus
                        result.results.put(winner.playerDice, result.results.getOrDefault(winner.playerDice, 0) + 1);
                        result.results.put(simulation.getOtherPlayer(winner).playerDice, result.results.getOrDefault(simulation.getOtherPlayer(winner).playerDice, 0));
                    } else {
                        //Das Spiel hatte einen Gewinner
                        result.results.put(simulation.playerA.playerDice, result.results.getOrDefault(simulation.playerA.playerDice, 0));
                        result.results.put(simulation.playerB.playerDice, result.results.getOrDefault(simulation.playerB.playerDice, 0));
                    }
                }
            }
        }

        //Das Ergebnis wird zusammengefasst, sodass der beste Würfel ermittelt werden kann
        result.summarize();

        //Rückgabe des Ergebnisses
        return result;
    }

    /**
     * Klasse, welche das Ergebnis der Auswertung darstellt.
     * Diese Klasse beeinhaltet alle Versuchsergebnisse (Würfel und wie oft diese gewonnen haben), sowie den besten Würfel der Liste.
     */
    public static class EvaluatorResult {

        public final Map<LudoDice, Integer> results = new HashMap<>();
        public LudoDice bestDice;

        /**
         * Methode, welche die Versuchsergebnisse zusammenfasst und den besten Würfel der Liste ermittelt.
         */
        public void summarize() {
            LudoDice bestDice = new ArrayList<>(results.keySet()).get(0);
            int mostWins = results.get(bestDice);

            //Würfel mit den meisten Siegen wird ermittelt
            for(Map.Entry<LudoDice, Integer> resultEntry : results.entrySet()) {
                if(resultEntry.getValue() > mostWins) {
                    bestDice = resultEntry.getKey();
                    mostWins = resultEntry.getValue();
                }
            }

            //Festlegen des besten Würfels
            this.bestDice = bestDice;
        }

        /**
         * Methode, welche die Versuchsergebnisse in der Ausgabedatei speichert.
         * Dabei speichert die Methode alle Ergebnisse und den besten Würfel.
         *
         * @param filePath Dateipfad der Ausgabedatei.
         */
        public void export(String filePath) {
            try {
                //Schreiben der Inhalte der Datei
                PrintWriter outputWriter = new PrintWriter(filePath, StandardCharsets.UTF_8);
                for(int i = 0; i < results.size(); i++) outputWriter.println("Würfel " + (i + 1) + " " + new ArrayList<>(results.keySet()).get(i).sides + " : " + new ArrayList<>(results.values()).get(i) + " Siege");
                outputWriter.println();
                outputWriter.println("Ergebnis: Der beste Würfel ist der Würfel " + bestDice.sides + " mit " + results.get(bestDice) + " Siegen.");

                //Schließen der benötigten Ressourcen
                outputWriter.flush();
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Something went wrong :/ - " + e.getMessage());
            }
        }

    }

}