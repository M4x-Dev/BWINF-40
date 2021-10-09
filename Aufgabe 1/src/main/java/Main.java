/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/parkplatz4.txt";
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
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 1 (Schiebeparkplatz) - by Maximilian Flügel");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Parkplatz \"" + INPUT_FILE + "\" wird geladen...");
        ParkingArea area = new ParkingArea(INPUT_FILE);

        //Ausgeben der Details über den eingelesenen Parkplatz
        System.out.println("Parkplatz erfolgreich geladen");
        System.out.println();
        System.out.println("Aufbau des Parkplatzes: ");
        System.out.println();
        for(int i = 0; i < area.parkingSpots[0].length; i++) System.out.print(area.parkingSpots[0][i] + " ");
        System.out.print("\n");
        for(int i = 0; i < area.parkingSpots[1].length; i++) System.out.print(area.parkingSpots[1][i] + " ");
        System.out.println();

        //Speichern der Ausgabedatei
        System.out.println();
        System.out.println("Lösungsabfolgen, um alle Fahrzeuge ausparken zu lassen:");
        for(int i = 0; i < area.parkingSpots[0].length; i++) System.out.println(area.leaveSpot(area.parkingSpots[0][i]));
        System.out.println();
        System.out.println("Ausgabedatei wird gespeichert: " + OUTPUT_FILE);
        area.clearParkingArea(OUTPUT_FILE);
        System.out.println("Ausgabe erfolgreich gespeichert. Programm beendet.");
        System.out.println();
    }

}