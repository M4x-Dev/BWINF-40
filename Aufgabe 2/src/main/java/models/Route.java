package models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Route {

    public int totalHotels;
    public long totalTripMinutes;

    public ArrayList<Hotel> hotels = new ArrayList<>();

    public Route(String filePath) {
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

}
