/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

import models.Route;
import models.RoutePlan;

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
        RoutePlan route = new RoutePlan(INPUT_FILE);

        System.out.println("Hotelroute erfolgreich geladen");
        System.out.println();
        System.out.println("--- Routeninformationen ---");
        System.out.println("Gesamtzahl Hotels: " + route.totalHotels);
        System.out.println("Gesamtfahrtzeit: " + route.totalTripMinutes + " min");

        System.out.println();
        System.out.println("Beste Route wird berechnet...");
        System.out.println();
        Route bestRoute = route.calculateOptimalRoute();
        System.out.println("Route erfolgreich berechnet:");
        System.out.println();
        System.out.println("--- Route ---");
        System.out.println(bestRoute.printPretty());
        bestRoute.exportToFile(OUTPUT_FILE);
    }

}
