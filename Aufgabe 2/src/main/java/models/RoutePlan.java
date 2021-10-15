package models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Klasse, welche die geplante Urlaubsroute repräsentiert.
 */
public class RoutePlan {

    private static final int MAX_STEPSIZE = 360;
    private static final int MAX_TRAVELTIME = 5;

    public int totalHotels;
    public long totalTripMinutes;

    private float ratingThreshold = 0.0f;

    public ArrayList<Hotel> hotels = new ArrayList<>();

    public RoutePlan(String filePath) {
        try {
            //Benötigte Instanzen zum Lesen der Datei
            StringBuilder contentBuilder = new StringBuilder();
            FileInputStream fileStream = new FileInputStream(filePath);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(fileStream));

            String line;
            while((line = streamReader.readLine()) != null) contentBuilder.append(line).append("\n");

            String[] contentLines = contentBuilder.toString().split("\n");

            //Interpretieren der Daten
            totalHotels = Integer.parseInt(contentLines[0]);
            totalTripMinutes = Long.parseLong(contentLines[1]);

            for(int i = 0; i < totalHotels; i++) {
                String hotelLine = contentLines[i + 2];
                hotels.add(new Hotel(hotelLine));
            }

            //Schließen der benötigten Ressourcen zum Lesen der Datei
            streamReader.close();
            fileStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Diese Funktion berechnet die optimalste Route des gegebenen Plans.
     * Dabei werden alle Möglichkeiten mithilfe der {@link #iterateHotel}-Funktion ausprobiert.
     * Alle probierten Routen landen schließlich in der "allAvailableRoutes"-Variable, welche anschließend nach der besten Route durchsucht wird.
     * Eine Route ist optimal, wenn die Bewertung des schlechtesten Hotels am höchsten ist und die Durchschnittsbewertung aller Hotels am höchste ist.
     * Die ausgewählte Route wird zurückgegeben.
     *
     * @return Gibt die optimalste Route zurück.
     */
    public Route calculateOptimalRoute() {
        ArrayList<Route> allAvailableRoutes = iterateHotel(hotels, new Route(), 0); //Start des Algorithmuses

        System.out.println();
        allAvailableRoutes.forEach(route -> System.out.println(route.print()));
        System.out.println();

        Route finalRoute = allAvailableRoutes.get(0);
        for(Route candidate : allAvailableRoutes) {
            if(candidate.getLowestHotelRating() > finalRoute.getLowestHotelRating() && candidate.getAverageHotelRating() > finalRoute.getAverageHotelRating())
                finalRoute = candidate; //Beste Route wird ermittelt (bestes schlechtestes Hotel & bester Durchschnittswert)
        }
        return finalRoute; //Finale Route wird zurückgegeben
    }

    /**
     * Funktion, welche rekursiv alle Verbindungsmöglichkeiten der Hotels ausprobiert.
     * Dabei gelten verschiedene Abbruchbedingungen, da die Speicherkapazität / die Zeit ansonsten für die Berechnung nicht ausreichen würde.
     * <br>Abbruchbedingung 1: Wenn die Route bereits länger als 5 Hotels ist (Reise ist folglich länger als 5 Tage), wird die Iteration abgebrochen.
     * <br>Abbruchbedingung 2: Wenn ein Hotel auf der Route liegt, welches schlechter ist, als das bis jetzt beste schlechteste Hotel, wird die Iteration abgebrochen.
     * <br>Abbruchbedingung 3: Wenn das Ziel vom momentanen Standpunkt aus erreichbar ist, dann wird die Iteration abgebrochen.
     * <br><br>Anschließend arbeitet der Algorithmus wie folgt:
     * <br>1. Es werden alle Hotels im erreichbaren Umkreis ermittelt
     * <br> - Dabei muss die Distanz vom Startpunkt größer sein, als die bereits gefahrene Zeit
     * <br> - Und kleiner sein, als die bereits gefahrene Zeit -360 Minuten
     * <br>2a. Wenn das Ziel von diesem Hotel aus erreichbar ist, dann wird die Route gespeichert.
     * <br>2b. Wenn das Ziel von diesem Hotel aus nicht erreichbar ist, dann wird die Funktion rekursiv für alle umliegenden Hotels aufgerufen.
     * <br>3. Nachdem alle Möglichkeiten berechnet wurden, wird die finale Liste an möglichen Pfaden (optimiert, da unmögliche/sehr schlechte Versuche nicht gespeichert werden) zurückgegeben.
     *
     * @param allHotels Vollständige Liste an Hotels, welche aus der Datei ausgelesen wird.
     * @param currentRoute Momentane Route (baut sich auf, da die Funktion rekursiv aufgerufen wird.
     * @param traveledDistance Zeitlicher Abstand vom Startpunkt (in Minuten).
     *
     * @return Gibt eine List aller möglichen Routen der Hotels zurück.
     */
    public ArrayList<Route> iterateHotel(ArrayList<Hotel> allHotels, Route currentRoute, long traveledDistance) {
        ArrayList<Route> resolvedRoutes = new ArrayList<>();

        if(traveledDistance < totalTripMinutes - MAX_STEPSIZE) { //Abbruchbedingung 3: Ist das Ziel vom momentanen Standort aus erreichbar?
            if(currentRoute.hotels.size() <= MAX_TRAVELTIME - 1) { //Abbruchbedingung 1: Dauert die Reise schon zu lange?
                for(Hotel inRange : getHotelsInRange(allHotels, traveledDistance)) {
                    if(inRange.averageRating > ratingThreshold) //Abbruchbedingung 2: Ist das Hotel schlechter, als bereits gewählt Pfade?
                        resolvedRoutes.addAll(iterateHotel(allHotels, new Route(currentRoute).appendHotel(inRange), inRange.distanceFromStart));
                }
            }
        } else {
            System.out.println("Threshold set: " + currentRoute.getLowestHotelRating());
            if(currentRoute.getLowestHotelRating() > ratingThreshold && currentRoute.hotels.size() <= MAX_TRAVELTIME) { //Abbruchbedingungen 1 und 2
                ratingThreshold = currentRoute.getLowestHotelRating(); //Neuer Mindestwert für Abbruchbedingung 2 wird gesetzt
                resolvedRoutes.add(currentRoute); //Route wird den möglichen Routen hinzugefügt
            }
        }

        return resolvedRoutes; //Rückgabe aller möglichen Routen (optimiert)
    }

    /**
     * Diese Funktion ermittelt alle erreichbaren Hotels im umliegenden Bereich.
     * Dafür darf ein Hotel maximal 360 Minuten von der momemtanen Position entfernt sein.
     *
     * @param availableHotels Vollständige Liste an verfügbaren Hotels.
     * @param traveledDistance Momentane Position (in Minuten vom Startpunkt).
     *
     * @return Gibt die Liste an erreichbaren Hotels zurück.
     */
    public ArrayList<Hotel> getHotelsInRange(ArrayList<Hotel> availableHotels, long traveledDistance) {
        ArrayList<Hotel> hotelsInRange = new ArrayList<>();
        for(Hotel hotel : availableHotels)
            if(hotel.distanceFromStart > traveledDistance && hotel.distanceFromStart <= traveledDistance + MAX_STEPSIZE) //Ist das Hotel erreichbar
                hotelsInRange.add(hotel); //Hotel wird der Liste hinzugefügt

        hotelsInRange.sort((hotelA, hotelB) -> Float.compare(hotelA.averageRating, hotelB.averageRating)); //Liste wird sortiert, damit der Algorithmus optimaler arbeitet
        Collections.reverse(hotelsInRange); //Die Liste wird umgekehrt, damit die besten Bewertungen zuerst berechnet werden

        return hotelsInRange; //Rückgabe der Liste
    }

}
