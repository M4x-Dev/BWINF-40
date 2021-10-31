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

public class DiceEvaluator {

    public static int EVALUATION_RECURSIONS = 1; //Wiederholungen der gesamten Auswertung (statistisches Verfahren)
    public static int SIMULATION_RECURSIONS = 10; //Wiederholungen der Simulation

    private final ArrayList<LudoDice> dices = new ArrayList<>();

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

    public EvaluatorResult evaluate() {
        EvaluatorResult result = new EvaluatorResult();

        for(int retry = 0; retry < EVALUATION_RECURSIONS; retry++) {
            for(int a = 0; a < dices.size() - 1; a++) {
                for(int b = a + 1; b < dices.size(); b++) {
                    for(int i = 0; i < SIMULATION_RECURSIONS; i++) {
                        LudoSimulation simulation = new LudoSimulation(dices.get(a), dices.get(b));
                        LudoPlayer winner = simulation.simulate(i < SIMULATION_RECURSIONS / 2);

                        result.results.put(winner.playerDice, result.results.getOrDefault(winner.playerDice, 0) + 1);
                        result.results.put(simulation.getOtherPlayer(winner).playerDice, result.results.getOrDefault(simulation.getOtherPlayer(winner).playerDice, 0));
                    }
                }
            }
        }

        result.summarize();
        return result;
    }

    public static class EvaluatorResult {

        public final Map<LudoDice, Integer> results = new HashMap<>();
        public LudoDice bestDice;

        public void summarize() {
            LudoDice bestDice = new ArrayList<>(results.keySet()).get(0);
            int mostWins = results.get(bestDice);

            for(Map.Entry<LudoDice, Integer> resultEntry : results.entrySet()) {
                if(resultEntry.getValue() > mostWins) {
                    bestDice = resultEntry.getKey();
                    mostWins = resultEntry.getValue();
                }
            }

            this.bestDice = bestDice;
        }

        public void export(String filePath) {
            try {
                PrintWriter outputWriter = new PrintWriter(filePath, StandardCharsets.UTF_8);
                for(int i = 0; i < results.size(); i++) outputWriter.println("Würfel " + (i + 1) + " " + new ArrayList<>(results.keySet()).get(i).sides + " : " + new ArrayList<>(results.values()).get(i) + " Siege");
                outputWriter.println();
                outputWriter.println("Ergebnis: Der beste Würfel ist der Würfel " + bestDice.sides + " mit " + results.get(bestDice) + " Siegen.");

                outputWriter.flush();
                outputWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Something went wrong :/ - " + e.getMessage());
            }
        }

    }

}