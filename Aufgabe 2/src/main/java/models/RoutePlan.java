package models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

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

    public Route estimateBestRoute() {
        long startTime = System.currentTimeMillis();
        ArrayList<Route> allAvailableRoutes = iterateHotel(hotels, new Route(), 0);
        System.out.println("Total path amount: " + allAvailableRoutes.size());
        allAvailableRoutes.sort((routeA, routeB) -> Float.compare(routeA.getLowestHotelRating(), routeB.getLowestHotelRating()));
        Collections.reverse(allAvailableRoutes);
        System.out.println("Algorithm took " + (System.currentTimeMillis() - startTime) + " ms");
        return allAvailableRoutes.get(0);
    }

    public ArrayList<Route> iterateHotel(ArrayList<Hotel> allHotels, Route currentRoute, long traveledDistance) {
        ArrayList<Route> resolvedRoutes = new ArrayList<>();

        if(traveledDistance < totalTripMinutes - MAX_STEPSIZE) {
            for(Hotel inRange : getHotelsInRange(allHotels, traveledDistance)) {
                if(inRange.averageRating > ratingThreshold && currentRoute.hotels.size() <= MAX_TRAVELTIME - 1)
                    resolvedRoutes.addAll(iterateHotel(allHotels, new Route(currentRoute).appendHotel(inRange), inRange.distanceFromStart));
            }
        } else {
            if(currentRoute.getLowestHotelRating() > ratingThreshold && currentRoute.hotels.size() <= MAX_TRAVELTIME) {
                ratingThreshold = currentRoute.getLowestHotelRating();
                resolvedRoutes.add(currentRoute);
            }
        }

        return resolvedRoutes;
    }

    public ArrayList<Hotel> getHotelsInRange(ArrayList<Hotel> availableHotels, long traveledDistance) {
        ArrayList<Hotel> hotelsInRange = new ArrayList<>();
        for(Hotel hotel : availableHotels)
            if(hotel.distanceFromStart > traveledDistance && hotel.distanceFromStart <= traveledDistance + MAX_STEPSIZE)
                hotelsInRange.add(hotel);

        hotelsInRange.sort((hotelA, hotelB) -> Float.compare(hotelA.averageRating, hotelB.averageRating));
        Collections.reverse(hotelsInRange);
        return hotelsInRange;
    }

}
