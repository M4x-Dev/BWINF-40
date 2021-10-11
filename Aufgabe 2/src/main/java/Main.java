import models.Route;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/hotels1.txt";
    public static String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0]; //Eingabedatei kann auch als Konsolenparameter angegeben werden.
        if(args.length > 1) OUTPUT_FILE = args[1]; //Ausgabedatei kann auch als Konsolenparameter angegeben werden.

        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 2 (Vollgeladen) - by Maximilian Flügel");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Hotelroute \"" + INPUT_FILE + "\" wird geladen...");
        Route route = new Route(INPUT_FILE);

        System.out.println("Hotelroute erfolgreich geladen");
        System.out.println();
        System.out.println("--- Routeninformationen ---");
        System.out.println("Gesamtzahl Hotels: " + route.totalHotels);
        System.out.println("Gesamtfahrtzeit: " + route.totalTripMinutes + " min");
        for(int i = 0; i < route.hotels.size(); i++) System.out.println("Hotel " + (i + 1) + ": Zeit ab Start: " + route.hotels.get(i).distanceFromStart + " min; Bewertung: " + route.hotels.get(i).averageRating + " Sterne");
    }

}
