import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Diese Klasse repräsentiert einen Parkplatz, wie in der Aufgabenstellung beschrieben.
 * Ein Parkplatz besteht aus einem 2-dimensionalem Array, welches X Einheiten breit und 2 Einheiten hoch ist.
 */
public class ParkingArea {

    public static final String SPOT_EMPTY = "-"; //Zeichen für einen leeren Parkplatz

    String[][] parkingSpots;

    /**
     * Konstruktor der Parkplatzklasse, welcher die Instanz aus einer Datei erstellt.
     * Das Auslesen der Dateien erfolgt wie in den Beispielen beschrieben.
     *
     * Beispiel "parkplatz0.txt":
     * <br>A B C D E F G H I J K L M N O
     * <br>- - P P Q Q - - R R - - S S -
     *
     * @param filePath Dateipfad der Datei, welche den Parkplatz beeinhaltet.
     */
    public ParkingArea(String filePath) {
        try {
            StringBuilder contentBuilder = new StringBuilder();
            InputStream fileStream = getClass().getResourceAsStream(filePath);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(fileStream));

            String line;
            while((line = streamReader.readLine()) != null) contentBuilder.append(line).append("\n");

            String[] contentLines = contentBuilder.toString().split("\n");

            //Interpretieren der Daten
            int verticalCars = (int)Character.toLowerCase(contentLines[0].charAt(2)) - 96; //Berechnen der Position des zweiten Buchstaben im Alphabet

            //Initialisieren und Befüllen des Parkplatzes mit den gegebenen Daten
            parkingSpots = new String[2][verticalCars];
            for(int i = 0; i < verticalCars; i++) parkingSpots[0][i] = Character.toString(Character.toUpperCase((char)i + 97)); //Befüllen der Fahrzeuge der ersten Reihe

            int horizontalCars = Integer.parseInt(contentLines[1]);
            for(int i = 0; i < horizontalCars; i++) {
                String horizontalCar = contentLines[i + 2];
                int startPosition = Integer.parseInt(horizontalCar.split(" ")[1]);

                parkingSpots[1][startPosition] = horizontalCar.split(" ")[0];
                parkingSpots[1][startPosition + 1] = horizontalCar.split(" ")[0];
            }

            for(int i = 0; i < parkingSpots[1].length; i++) {
                if(parkingSpots[1][i] == null)
                    parkingSpots[1][i] = SPOT_EMPTY;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Diese Funktion ermittelt die Schritte, welche erfolderlich sind, damit ein Auto ausparken kann.
     * Dabei wird eine temporäre Kopie des Parkplatzes erstellt, mit welcher der Algorithmus durchgeführt wird.
     *
     * @param car Bezeichnung des Fahrzeuges, welches ausgeparkt werden soll (zum Beispiel "A").
     *
     * @return Gibt die erforderlichen Schritte zurück.
     */
    public String leaveSpot(String car) {
        StringBuilder chainBuilder = new StringBuilder(car + ": "); //StringBuilder, welcher das Zusammenschließen der Lösungsschritte übernimmt
        int carIndex = getVerticalCarPosition(car); //Ermitteln der Position des Fahrzeuges auf dem Parkplatz

        if(parkingSpots[1][carIndex].equalsIgnoreCase(SPOT_EMPTY)) {
            //Einfachster Fall: Das Fahrzeug wird von keinem weiteren Fahrzeug blockiert.
            return chainBuilder.toString();
        } else {
            //Sonstiger Fall: Das Fahrzeug wird von mindestens einem weiteren Fahrzeug blockiert, welches zuerst verschoben werden muss.
            String blockingCar = parkingSpots[1][carIndex];
            boolean overlappingLeft = carIndex == 0 || parkingSpots[1][carIndex - 1].equalsIgnoreCase(blockingCar); //Ragt das Fahrzeug nach links oder rechts über das ausparkende hinaus?
            //System.out.println("Ragt das Fahrzeug nach links heraus? " + overlappingLeft);

            //Fahrzeug ohne ein weiteres Fahrzeug verschieben
            if(overlappingLeft) {
                //Move to the left
                //System.out.println("Bewege Fahrzeug " + blockingCar + " nach links");
                chainBuilder.append(moveHorizontalCar(blockingCar, -1, ""));
            } else {
                //Move to the right
                //System.out.println("Bewege Fahrzeug " + blockingCar + " nach rechts");
                chainBuilder.append(moveHorizontalCar(blockingCar, 1, ""));
            }

            return chainBuilder.toString();
        }
    }

    public String moveHorizontalCar(String car, int move, String instructions) {
        //System.out.println("Bewege Fahrzeug " + car + " um " + move + " Einheiten");
        StringBuilder chainBuilder = new StringBuilder(instructions);
        int carStartPosition = getHorizontalCarPosition(car);

        if(move < 0 && carStartPosition == 0) return moveHorizontalCar(car, 2, instructions);
        if(move > 0 && carStartPosition == parkingSpots[1].length - 2) return moveHorizontalCar(car, -2, instructions);

        //System.out.println("Bewege das Fahrzeug");
        String change = (chainBuilder.toString().length() == 0 ? ", " : "") + car + " " + Math.abs(move) + (move > 0 ? " rechts" : " links");
        chainBuilder.insert(0, change);

        if(canMoveBy(carStartPosition, move)) {
            //System.out.println("Bewegung ist möglich, Fahrzeug wird verschoben");
            return chainBuilder.toString().startsWith(", ") ? chainBuilder.substring(2) : chainBuilder.toString();
        } else {
            //System.out.println("Bewegung nicht möglich, anderes Fahrzeug wird bewegt");
            //TODO: Ausrechnen der erforderlichen Schritte um das Fahrzeug zu verschieben
            //return moveHorizontalCar(getNextCar(carStartPosition, move), move, chainBuilder.toString());
            return moveHorizontalCar(getNextCar(carStartPosition, move), getEmptySpaces(carStartPosition, move), chainBuilder.toString());
        }
    }

    /**
     * Diese Funktion ermittelt die Position eines vertikal geparkten Fahrzeuges (erste Reihe).
     * Dafür durchläuft die Funktion alle Parkplätze der ersten Reihe, um das Fahrzeug zu finden.
     * Falls das Fahrzeug nicht gefunden werden kann, wird der Wert -1 zurückgegeben.
     *
     * @param car Bezeichung des Fahrzeuges, welches gefunden werden soll (zum Beispiel "A").
     *
     * @return Gibt die Position des Fahrzeuges oder -1 zurück, falls die Position nicht ermittelt werden kann.
     */
    public int getVerticalCarPosition(String car) {
        for(int i = 0; i < parkingSpots[0].length; i++) {
            if(parkingSpots[0][i].equalsIgnoreCase(car))
                return i; //Rückgabe der Position
        }

        return -1; //Rückgabe des Ausweichwertes
    }

    public int getHorizontalCarPosition(String car) {
        for(int i = 0; i < parkingSpots[1].length; i++) {
            if(parkingSpots[1][i].equalsIgnoreCase(car))
                return i; //Rückgabe der ersten Position
        }

        return -1; //Rückgabe des Ausweichwertes
    }

    public boolean canMoveBy(int start, int amount) {
        if(amount < 0) {
            //Nach links bewegen
            for(int i = 0; i < -amount; i++) {
                if(!parkingSpots[1][start - i - 1].equalsIgnoreCase(SPOT_EMPTY))
                    return false;
            }
        } else {
            //Nach rechts bewegen
            for(int i = 0; i < amount - 1; i++) {
                if(!parkingSpots[1][start + i + 1].equalsIgnoreCase(SPOT_EMPTY))
                    return false;
            }
        }

        return true; //Bewegung ist möglich
    }

    public String getNextCar(int start, int direction) {
        if(direction < 0) {
            //Nach links bewegen
            for(int i = start - 1; i >= 0; i--) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY))
                    return parkingSpots[1][i];
            }
        } else {
            //Nach rechts bewegen
            for(int i = start + 2; i < parkingSpots[1].length - 1; i++) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY))
                    return parkingSpots[1][i];
            }
        }

        return SPOT_EMPTY;
    }

    public int getEmptySpaces(int start, int direction) {
        int spots = 0;

        if(direction < 0) {
            //Links suchen
            for(int i = start - 1; i >= 0; i--) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY)) {
                    return spots;
                } else {
                    spots++;
                }
            }
        } else {
            //Nach rechts bewegen
            for(int i = start + 2; i < parkingSpots[1].length - 1; i++) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY)) {
                    return spots;
                } else {
                    spots++;
                }
            }
        }

        return spots;
    }

}