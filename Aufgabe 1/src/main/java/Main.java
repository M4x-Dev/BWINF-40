public class Main {

    public static final String INPUT_FILE = "parkplatz0.txt";
    public static final String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        //Laden der Eingabedatei
        System.out.println("Parkplatz \"" + INPUT_FILE + "\" wird geladen...");
        ParkingArea area = new ParkingArea(INPUT_FILE);

        //Ausgeben der Details über den eingelesenen Parkplatz
        System.out.println("Parkplatz erfolgreich geladen: " + area.parkingSpots[0].length);
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
        area.clearParkingArea(OUTPUT_FILE);
    }

}