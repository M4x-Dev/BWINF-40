import java.io.*;

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

            streamReader.close(); //Schließen des Readers
            fileStream.close(); //Schließen des Streams
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Diese Funktion speichert die benötigten Lösungsschritte, damit alle Fahrzeuge den Parkplatz verlassen können, in einer separaten Datei.
     *
     * @param filePath Dateipfad der Ausgabedatei.
     */
    public void clearParkingArea(String filePath) {
        try {
            PrintWriter outputWriter = new PrintWriter(this.getClass().getResource(filePath).getPath());
            for(int i = 0; i < parkingSpots[0].length; i++) outputWriter.println(leaveSpot(parkingSpots[0][i]));
            outputWriter.flush();
            outputWriter.close();
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
            //Ansonsten: Das Fahrzeug wird von mindestens einem weiteren Fahrzeug blockiert, welches zuerst verschoben werden muss.
            String blockingCar = parkingSpots[1][carIndex];
            boolean overlappingLeft = carIndex == 0 || parkingSpots[1][carIndex - 1].equalsIgnoreCase(blockingCar); //Ragt das Fahrzeug nach links oder rechts über das ausparkende hinaus?

            if(overlappingLeft) {
                //Horizontales Fahrzeug muss nach links bewegt werden
                chainBuilder.append(moveHorizontalCar(blockingCar, -1, ""));
            } else {
                //Horizontales Fahrzeug muss nach rechts bewegt werden
                chainBuilder.append(moveHorizontalCar(blockingCar, 1, ""));
            }

            return chainBuilder.toString();
        }
    }

    /**
     * Diese Funktion bewegt ein horizontales Fahrzeug auf dem Parkplatz, um Platz für ein verikales Fahrzeug zu schaffen.
     * Dabei kann diese Funktion mehrfach rekursiv aufgerufen werden, falls mehrere horizontale Fahrzeuge bewegt werden müssen, um die Lücke freizugeben.
     *
     * @param car Horizontales Fahrzeug, welches um die gegebenen Einheiten bewegt werden soll.
     * @param move Einheiten, um welche das horizontale Fahrzeug bewegt werden soll.
     * @param instructions Bereits ausgeführte Lösungsschritte zum Freigeben des Parkplatzes, welche rekursiv aufgebaut werden können.
     *
     * @return Ermittelt die benötigten Lösungsschritte, um ein horizontales Fahrzeug in eine bestimmte Richtung zu bewegen.
     */
    public String moveHorizontalCar(String car, int move, String instructions) {
        StringBuilder chainBuilder = new StringBuilder(instructions);
        int carStartPosition = getHorizontalCarPosition(car);

        if(move < 0 && carStartPosition == 0) return moveHorizontalCar(car, 2, instructions);
        if(move > 0 && carStartPosition == parkingSpots[1].length - 2) return moveHorizontalCar(car, -2, instructions);

        String change = ", " + car + " " + Math.abs(move) + (move > 0 ? " rechts" : " links");
        chainBuilder.insert(0, change);

        if(canMove(carStartPosition, move)) {
            //Das horizontale Fahrzeug kann, ohne ein weiteres Fahrzeug zu bewegen, bewegt werden, um dem senkrechten Fahrzeug Platz zu machen.
            return chainBuilder.toString().startsWith(", ") ? chainBuilder.substring(2) : chainBuilder.toString();
        } else {
            //Das horizontale Fahrzeug kann nur bewegt werden, wenn ein weiteres horizontales Fahrzeug bewegt wird (rekursiver Funktionsaufruf).
            return moveHorizontalCar(getNextCar(carStartPosition, move), getDifference(carStartPosition, move), chainBuilder.toString());
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

    /**
     * Diese Funktion ermittelt die Position eines horizontal geparkten Fahrzeuges (zweite Reihe).
     * Dafür durchläuft die Funktion alle Parkplätze der ersten Reihe, um das Fahrzeug zu finden.
     * Falls das Fahrzeug nicht gefunden werden kann, wird der Wert -1 zurückgegeben.
     * Außerdem gibt die Funktion immer die erste Position des horizontalen Fahrzeuges zurück, da diese immer zwei Einheiten breit sind.
     *
     * @param car Bezeichnung des Fahrzeuges, welches gefunden werden soll (zum Beispiel "A").
     *
     * @return Gibt die Position des Fahrzeuges oder -1 zurück, falls die Position nicht ermittelt werden kann.
     */
    public int getHorizontalCarPosition(String car) {
        for(int i = 0; i < parkingSpots[1].length; i++) {
            if(parkingSpots[1][i].equalsIgnoreCase(car))
                return i; //Rückgabe der ersten Position
        }

        return -1; //Rückgabe des Ausweichwertes
    }

    /**
     * Diese Funktion überprüft, ob sich ein horizontales Fahrzeug um die gegebenen Einheiten bewegen kann.
     * Dafür durchläuft die Funktion die Pläätze neben dem Fahrzeug und ist erfolgreich, wenn kein weiteres Fahrzeug in unmittelbarer Nähe ist.
     *
     * @param start Startposition des horizontalen Fahrzeuges.
     * @param amount Anzahl der Felder, durch welche das horizontale Fahrzeug bewegt werden soll.
     *
     * @return Gibt zurück, ob sich das horizontale Fahrzeug ohne Kollision mit einem anderen Fahrzeug um die gegebenen Einheiten bewegt werden kann.
     */
    public boolean canMove(int start, int amount) {
        if(amount < 0) {
            //Linke Seite überprüfen
            for(int i = 1; i <= -amount; i++)
                if(!parkingSpots[1][start - i].equalsIgnoreCase(SPOT_EMPTY))
                    return false;
        } else if(amount > 0) {
            //Rechte Seite überprüfen
            start++; //Startposition um eine Einheit nach rechts verschieben, da ein horizontales Fahrzeug zwei Einheiten breit ist
            for(int i = 1; i <= amount; i++) {
                if(!parkingSpots[1][start + i].equalsIgnoreCase(SPOT_EMPTY))
                    return false;
            }
        }

        return true; //Bewegung ist möglich
    }

    /**
     * Diese Funktion sucht das nächste horizontale Fahrzeug in Nähe der Startposition in einer bestimmten Richtung.
     * <br><br>Falls der "direction" Parameter negativ ist, sucht die Funktion auf der linken Seite des Fahrzeuges
     * <br>Falls der "direction" Parameter positiv ist, sucht die Funktion auf der rechten Seite des Fahrzeuges<br>
     * Falls sich kein Fahrzeug in unmittelbarer Nähe befindet, gibt die Funktion ein Leerzeichen ("-") zurück.
     *
     * @param start Startposition der Suchfunktion.
     * @param direction Richtung, in welche die Funktion suchen soll (links oder rechts).
     *
     * @return Gibt das nächste Fahrzeug in der gegebenen Richtung oder ein Leerzeichen ("-") zurück, wenn sich kein Fahrzeug in der Nähe befindet.
     */
    public String getNextCar(int start, int direction) {
        if(direction < 0) {
            //Auf der linken Seite suchen
            for(int i = start - 1; i >= 0; i--) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY))
                    return parkingSpots[1][i];
            }
        } else {
            //Auf der rechten Seite suchen
            for(int i = start + 2; i < parkingSpots[1].length - 1; i++) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY))
                    return parkingSpots[1][i];
            }
        }

        //Es wurde kein horizontales Fahrzeug in unmittelbarer Nähe in der gegebenen Richtung gefunden.
        return SPOT_EMPTY;
    }

    /**
     * Diese Funktion berechnet die Distanz zwischen zwei horizontalen Fahrzeugen.
     * <br><br>Falls der "direction" Parameter negativ ist, sucht die Funktion auf der linken Seite des Fahrzeuges<br>
     * <br>Falls der "direction" Parameter positiv ist, sucht die Funktion auf der rechten Seite des Fahrzeuges<br>
     *
     * @param start Position des ersten horizontalen Fahrzeuges.
     * @param direction Richtung, in welche die Funktion suchen soll.
     *
     * @return Ermittelt die Distanz zum nächsten horizontalen Fahrzeug in der gegebenen Richtung.
     */
    public int getDifference(int start, int direction) {
        //Distanz wird mit dem Wert 1 bzw. -1 initialisiert
        int distance = direction < 0 ? -1 : 1;

        if(direction < 0) {
            //Auf der linken Seite suchen
            for(int i = start - 1; i >= 0; i--) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY)) {
                    return distance;
                } else {
                    distance--;
                }
            }
        } else {
            //Auf der rechten Seite suchen
            for(int i = start + 2; i < parkingSpots[1].length - 1; i++) {
                if(!parkingSpots[1][i].equalsIgnoreCase(SPOT_EMPTY)) {
                    return distance;
                } else {
                    distance++;
                }
            }
        }

        return distance;
    }

}