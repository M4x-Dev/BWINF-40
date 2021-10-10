package de.singleton.bwinf_pdf_generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {

    static String[][] parkingSpots;

    public static void main(String[] args) {
	    System.out.println("Eingabedatei: ");
        String inputFile = System.console().readLine();
        inputFile = readFile(inputFile);
        parseInputFile(inputFile);

        System.out.println("Eingabedatei der Lösung: ");
        String solutionFile = System.console().readLine();
        solutionFile = readFile(solutionFile);
        generateOutput(solutionFile);

        System.out.println("Inhalte werden generiert...");
    }

    private static void parseInputFile(String content) {
        try {
            //Aufteilen des gelesenen Textes in die einzelnen Dateien
            String[] contentLines = content.split("\n");

            //Interpretieren der Daten
            //Diese Anweisung berechnet die Breite des Parkplatzes, indem der zweite Buchstabe in ASCII konvertiert und vom Ergebnis 96 subtrahiert wird.
            //Somit erhält man die Stelle des Buchstabens im Alphabet
            int verticalCars = (int)Character.toLowerCase(contentLines[0].charAt(2)) - 96;

            //Initialisieren und Befüllen des Parkplatzes mit den gegebenen Daten
            parkingSpots = new String[2][verticalCars];

            //Diese Schleife füllt die erste Reihe des Arrays mit den Buchstaben A bis <Zielbuchstabe>, welche vorher aus der Datei gelesen wurden.
            //Dabei wird die Iterationsvariable i, der Schleife, in einen ASCII-Code umgewandelt.
            for(int i = 0; i < verticalCars; i++) parkingSpots[0][i] = Character.toString(Character.toUpperCase((char)i + 97));

            //Diese Schleife liest die horizontal geparkten Fahrzeuge aus der gegebenen Datei und füllt diese in das Array.
            //Dabei nimmt ein horizontal geparktes Fahrzeug immer zwei Einheiten ein (i und i + 1).
            int horizontalCars = Integer.parseInt(contentLines[1]);
            for(int i = 0; i < horizontalCars; i++) {
                String horizontalCar = contentLines[i + 2];
                int startPosition = Integer.parseInt(horizontalCar.split(" ")[1]);

                parkingSpots[1][startPosition] = horizontalCar.split(" ")[0];
                parkingSpots[1][startPosition + 1] = horizontalCar.split(" ")[0];
            }

            //Diese Schleife füllt die restlichen Parkplätze mit einem Leerzeichen, welches später benötigt wird.
            for(int i = 0; i < parkingSpots[1].length; i++) {
                if(parkingSpots[1][i] == null)
                    parkingSpots[1][i] = "-";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    private static void generateOutput(String solution) {
        String[] solutionLines = solution.split("\n");
        StringBuilder allBuilder = new StringBuilder();

        for(String line : solutionLines) {
            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append(line.split(":")[0]).append(":");

            if(line.length() <= 5) {
                contentBuilder.append("Dieses Fahrzeug kann ohne Weiteres bewegt werden.");
                allBuilder.append(contentBuilder);
                continue;
            } else {
                String mainContent = line.split(":")[1];
                int size = mainContent.split(",").length;

                switch (size) {
                    case 1 -> contentBuilder.append("Es muss ein weiteres Fahrzeug verschoben werden, damit dieses Fahrzeug bewegt werden kann.");
                    case 2 -> contentBuilder.append("Es müssen zwei weitere Fahrzeuge verschoben werden, damit dieses Fahrzeug bewegt werden kann.");
                    case 3 -> contentBuilder.append("Es müssen drei weitere Fahrzeuge verschoben werden, damit dieses Fahrzeug bewegt werden kann.");
                    case 4 -> contentBuilder.append("Es müssen vier weitere Fahrzeuge verschoben werden, damit dieses Fahrzeug bewegt werden kann.");
                }
            }

            contentBuilder.append("\n");


        }
    }

    public static String readFile(String filePath) {
        try {
            FileInputStream stream = new FileInputStream(filePath);
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder contentBuilder = new StringBuilder();

            String line;
            while((line = fileReader.readLine()) != null) contentBuilder.append(line).append("/n");

            fileReader.close();
            stream.close();

            return contentBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}