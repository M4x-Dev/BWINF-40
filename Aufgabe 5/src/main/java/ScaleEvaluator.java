import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ScaleEvaluator {

    public final ArrayList<Integer> availableWeights = new ArrayList<>();

    public ScaleEvaluator(String filePath) {
        try {
            StringBuilder contentBuilder = new StringBuilder();
            InputStream fileStream = new FileInputStream(filePath);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));

            String line;
            while((line = fileReader.readLine()) != null) contentBuilder.append(line).append("\n");

            String[] fileContent = contentBuilder.toString().split("\n");

            //Interpretieren der Daten
            int weightAmount = Integer.parseInt(fileContent[0]);

            for(int i = 0; i < weightAmount; i++) {
                String[] splitContent = fileContent[1 + i].split(" ");

                for(int a = 0; a < Integer.parseInt(splitContent[1]); a++)
                    availableWeights.add(Integer.parseInt(splitContent[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    public EvaluationResult evaluate() {
        EvaluationResult result = new EvaluationResult();

        for(int i = 1; i <= 10000 / 10; i++) {
            int targetWeight = 10 * i;
            Scale.ScaleState scaleResult = new Scale().balance(targetWeight, new ArrayList<>(availableWeights));
            result.resultEntries.add(new EvaluationResultEntry(
                    scaleResult.leftWeights(),
                    scaleResult.rightWeights(),
                    Scale.sumIntegerList(scaleResult.rightWeights()),
                    Scale.sumIntegerList(scaleResult.rightWeights()) - Scale.sumIntegerList(scaleResult.leftWeights()),
                    targetWeight,
                    scaleResult.done()
            ));
        }

        return result;
    }

    public static class EvaluationResult {

        final ArrayList<EvaluationResultEntry> resultEntries = new ArrayList<>();

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

    public record EvaluationResultEntry(ArrayList<Integer> leftWeights, ArrayList<Integer> rightWeights, int achievedWeight, int achievedDifference, int targetWeight, boolean scaleBalanced) {

        public String printState() {
            return leftWeights + " --- " + rightWeights;
        }

    }

}