import simulation.LudoDice;
import simulation.LudoSimulation;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/wuerfel0.txt";
    public static String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0]; //Eingabedatei kann auch als Konsolenparameter angegeben werden
        if(args.length > 1) OUTPUT_FILE = args[1]; //Ausgabedatei kann auch als Konsolenparameter angegeben werden

        //Laden der Eingabedatei
        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 4 (Würfelglück) - by Maximilian Flügel");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Würfel \"" + INPUT_FILE + "\" werden geladen...");

        LudoSimulation testSimulation = new LudoSimulation(LudoDice.sixSided(), LudoDice.sixSided());
        testSimulation.field.printField();
        System.out.println("PLAYER " + testSimulation.simulate(true).playerTag + " WON THE GAME!");
    }

}
