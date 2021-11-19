/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

package models;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Klasse, welche eine Route vom Start, über mehrere Hotels bis zum Ziel, repräsentiert.
 */
public class Route {

    public ArrayList<Hotel> hotels;

    /**
     * Konstruktor der Routen-Klasse.
     */
    public Route() {
        this.hotels = new ArrayList<>();
    }

    /**
     * Copy-Konstruktor der Routen Klasse.
     * Dieser Konstruktor kopiert eine existierende Route.
     *
     * @param route Existierende Route, welche kopiert werden soll.
     */
    public Route(Route route) {
        this.hotels = new ArrayList<>(route.hotels);
    }

    /**
     * Methode, welche die verschiedenen Ziele in eine Datei schreibt.
     *
     * @param output Pfad der Ausgabedatei
     */
    public void exportToFile(String output) {
        try {
            PrintWriter outputWriter = new PrintWriter(output, StandardCharsets.UTF_8);
            for(int i = 0; i < hotels.size(); i++) outputWriter.println("Hotel " + i + ": " + hotels.get(i).distanceFromStart + " min, " + hotels.get(i).averageRating + " stars");
            outputWriter.flush();
            outputWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Funktion, welche die Route um das angegebene Hotel erweitert.
     *
     * @param hotel Hotel, welches der Route hinzugefügt werden soll.
     *
     * @return Gibt die Route mit dem hinzugefügten Hotel (diese Instanz) zurück.
     */
    public Route appendHotel(Hotel hotel) {
        hotels.add(hotel);
        return this;
    }

    /**
     * Funktion, welche die niedrigste Bewertung aus der Liste der Hotels heraussucht.
     * Dabei verwendet die Funktion eine Schleife, welche die niedrigste Bewertung ermittelt.
     *
     * @return Gibt die niedrigste Hotelbewertung der Route zurück.
     */
    public float getLowestHotelRating() {
        float lowest = 5.0f;
        for(Hotel hotel : hotels)
            if(hotel.averageRating < lowest)
                lowest = hotel.averageRating;
        return lowest;
    }

    /**
     * Funktion, welche die durchschnittliche Hotelbewertung ermittelt.
     * Diese Funktion addiert alle Hotelbewertungen und teilt diese dann durch die Gesamtanzahl an Hotels.
     *
     * @return Gibt die durchschnittliche Hotelbewertung zurück.
     */
    public float getAverageHotelRating() {
        float sum = 0f;
        for(Hotel hotel : hotels) sum += hotel.averageRating;
        return sum / hotels.size();
    }

    /**
     * Funktion, welche alle Hotels dieser Route in einen String zusammenhängt und dann ausgibt.
     *
     * @return Gibt einen String zurück, welcher alle Hotels der Route beeinhaltet.
     */
    public String print() {
        StringBuilder instanceBuilder = new StringBuilder();
        for(Hotel hotel : hotels) instanceBuilder.append(hotel.distanceFromStart).append("/").append(hotel.averageRating).append(" --> ");
        return instanceBuilder.length() > 0 ? instanceBuilder.substring(0, instanceBuilder.length() - " --> ".length()) : instanceBuilder.toString();
    }

    /**
     * Funktion, welche alle Hotels dieser Route mit Trennzeichen in einen String zusammenhängt und dann ausgibt.
     *
     * @return Gibt einen String zurück, welcher alle Hotels der Route beeinhaltet.
     */
    public String printPretty() {
        StringBuilder instanceBuilder = new StringBuilder();
        for(Hotel hotel : hotels) instanceBuilder.append(hotel.distanceFromStart).append("/").append(hotel.averageRating).append(" --> ");
        return instanceBuilder.length() > 0 ? instanceBuilder.substring(0, instanceBuilder.length() - " --> ".length()) : instanceBuilder.toString();
    }

}
