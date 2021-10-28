package generator;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Diese Klasse generiert Wortfeld mit dem mittleren Schwierigkeitsgrad.
 * Dabei ist dieser Generator von dem der niedrigen Schwierigkeit abgeleitet und baut somit darauf auf.
 * <br>Merkmale für ein Wortfeld des mittleren Schwierigkeitsgrades (<b>MITTEL</b>):
 * <br>a. Wörter können horizontal, vertikal und diagonal (absteigend/aufsteigend) platziert werden
 * <br>b. Wörter können sich zufällig kreuzen, werden aber nicht automatisch so generiert
 * <br>c. Leerstellen werden mit zufälligen Buchstaben aus der Wortliste aufgefüllt
 */
public class MediumPatternGenerator extends EasyPatternGenerator {

    protected static final int FILL_MAX_RECURSIONS = 5;
    protected static final int FILL_CHECK_RADIUS = 3;

    protected final ArrayList<Point> filledPoints = new ArrayList<>();

    /**
     * Konstruktor der Generator-Klasse.
     * Dieser Konstruktor ruft nur den Super-Konstruktor {@link EasyPatternGenerator} dieser Klasse auf.
     *
     * @param filePath Dateipfad der Eingabedatei (Wortliste).
     */
    public MediumPatternGenerator(String filePath) {
        super(filePath);
    }

    /**
     * Hauptfunktion der Generator-Klasse, welche das Wortfeld generiert.
     * Diese Funktion generiert ein Wortfeld des Schwierigkeitsgrades "Mittel" mithilfe der angegebenen Parameter.
     * Diese Funktion ruft die Super-Implementierung der Funktion auf.
     *
     * @return Gibt das generierte Wortfeld zurück.
     */
    @Override
    public String generatePattern() {
        randomCrossingEnabled = true;
        filledPoints.clear();
        return super.generatePattern();
    }

    /**
     * Funktion, welche versucht, ein Wort zufällig auf dem Wortfeld zu platzieren.
     * Dazu wird eine von drei Möglichkeiten zufällig generiert.
     *
     * <br><b>Möglichkeit 1: </b> Horizontale Platzierung auf dem Wortfeld
     * <br><b>Möglichkeit 2: </b> Vertikale Platzierung auf dem Wortfeld
     * <br><b>Möglichkeit 3: </b> Diagonale Platzierung auf dem Wortfeld (aufsteigend oder absteigend); ohne Überschneidung
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    @Override
    protected boolean placeWord(String word) {
        int maxAttempts = 10;

        switch(instanceRandom.nextInt(3)) {
            case 0:
                for(int i = 0; i <= maxAttempts; i++) {
                    int[] position = getPositionForWord(word, WordPosition.Orientation.Horizontal);
                    if(placeWordHorizontally(word, position[0], position[1], true))
                        return true;
                }
                return false;
            case 1:
                for(int i = 0; i <= maxAttempts; i++) {
                    int[] position = getPositionForWord(word, WordPosition.Orientation.Vertical);
                    if(placeWordVertically(word, position[0], position[1], true))
                        return true;
                }
                return false;
            case 2:
                for(int i = 0; i <= maxAttempts; i++) {
                    if(placeWordDiagonally(word, COORDINATE_GENERATE, COORDINATE_GENERATE, DiagonalDirection.Random, true))
                        return true;
                }
                return false;
            default: return false;
        }
    }

    /**
     * Funktion, welche ein Wort in diagonaler Ausrichtung auf dem Feld platziert.
     * Dabei wird die Position des Wortes zufällig generiert, wenn diese nicht gegeben ist (-1).
     * Außerdem wird die Ausrichtung des Wortes (diagonal aufsteigend/absteigend) zufällig generiert, wenn diese nicht gegeben ist ({@link DiagonalDirection}).
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     * @param optionalX X-Koorindate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param optionalY Y-Koorindate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param orientation Ausrichtung des Wortes: Diagonal aufsteigend oder absteigend (kann auch zufällig generiert werden).
     * @param crossingAllowed Legt fest, ob eine Überschreitung von Wörtern erlaubt sein soll, wenn dieses nicht ohne Weiteres platziert werden kann.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    protected boolean placeWordDiagonally(String word, int optionalX, int optionalY, DiagonalDirection orientation, boolean crossingAllowed) {
        //Initialisieren der Koordinaten und Ausrichtung
        int positionX = optionalX;
        int positionY = optionalY;
        boolean movingUp = orientation != DiagonalDirection.Descending && (orientation == DiagonalDirection.Ascending || instanceRandom.nextBoolean());

        //Berechnen der Koordinaten
        if(movingUp) {
            //Berechnen der Koordinaten für die Richtung: diagonal aufsteigend
            if(positionY == COORDINATE_GENERATE) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) + word.length() - 1 : pattern.length - 1;
            if(positionX == COORDINATE_GENERATE) positionX = pattern[positionY].length - word.length() != 0 ? xGenerator.generate(pattern[positionY].length - word.length()) : 0;

            //Überprüfen der Position auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) {
                if((positionX % 2 == 0 || positionY % 2 == 0) || (!pattern[positionY - i][positionX + i].equals(CHARACTER_EMPTY) && (/*!randomCrossingEnabled && */!pattern[positionY - i][positionX + i].equals(String.valueOf(word.charAt(i)))))) {
                    return false;
                }
            }

            //Platzieren des Wortes auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) pattern[positionY - i][positionX + i] = String.valueOf(word.charAt(i));
            placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.DiagonalUp));
        } else {
            //Berechnen der Koordinaten für die Richtung: diagonal absteigend
            if(positionY == COORDINATE_GENERATE) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) : 0;
            if(positionX == COORDINATE_GENERATE) positionX = pattern[positionY].length - word.length() != 0 ? xGenerator.generate(pattern[positionY].length - word.length()) : 0;

            //Überprüfen der Position auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) {
                if((positionX % 2 == 0 || positionY % 2 == 0) || (!pattern[positionY + i][positionX + i].equals(CHARACTER_EMPTY) && (/*!randomCrossingEnabled && */!pattern[positionY + i][positionX + i].equals(String.valueOf(word.charAt(i)))))) {
                    return false;
                }
            }

            //Platzieren des Wortes auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) pattern[positionY + i][positionX + i] = String.valueOf(word.charAt(i));
            placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.DiagonalDown));
        }

        //Hinzufügen der Koordinate zu den belegten Koordinaten
        addClosedCoordinate(positionX, positionY);
        return true;
    }

    /**
     * Methode, welche die übrigen Felder des Wortfeldes auffüllt, sodass keine Lücken erhalten bleiben.
     * Dazu sammelt die funktion zuerst alle Buchstaben, welche in den Wörtern aus der Wortliste enthalten sind.
     * Anschließend werden alle Lücken mit zufälligen Buchstaben aus dieser Liste gefüllt.
     */
    @Override
    protected void fillEmptySpaces() {
        //Sammeln aller Buchstaben, welche in den Wörtern aus der Wortliste enthalten sind
        ArrayList<String> letterSet = new ArrayList<>();
        for(String word : words) {
            for(int i = 0; i < word.length(); i++) {
                if(!letterSet.contains(String.valueOf(word.charAt(i))))
                    letterSet.add(String.valueOf(word.charAt(i)));
            }
        }

        //Auffüllen der Leerstellen mit zufälligen Buchstaben aus der Liste
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(Objects.equals(pattern[y][x], CHARACTER_EMPTY)) {
                    //Hinzufügen des Buchstabens zum Wortfeld
                    pattern[y][x] = letterSet.get(instanceRandom.nextInt(letterSet.size()));

                    //Speichern des Punktes zur späteren Überprüfung der Auffüllung
                    filledPoints.add(new Point(x, y));
                }
            }
        }

        //Überprüfung der automatischen Auffüllung
        checkFilledSpaces(letterSet);
    }

    protected void checkFilledSpaces(ArrayList<String> letterSet) {
        //Überprüfen der platzierten Buchstaben, sodass nicht durch Zufall doppelte Wörter entstehen
        //Diese Überprüfung wird mehrfach durchgeführt, sodass so viele Fehler wie möglich ausgeschlossen werden können
        for(int n = 0; n <= FILL_MAX_RECURSIONS; n++) {
            int currentX = 0, currentY = 0;
            int deltaX = 0, deltaY = -1;

            int rows = height;
            int cols = width;

            for(int i = 0; i < Math.pow(Math.max(rows, cols), 2); i++) {
                if(currentX > -rows / 2 && currentX <= rows / 2 && currentY > -cols / 2 && currentY <= cols / 2) {
                    //Auffüllen der Buchstaben
                    int actualX = (int)Math.ceil((width / 2.0) + (double)currentX);
                    int actualY = (int)Math.ceil((height / 2.0) + (double)currentY);

                    if(actualX > 0 && actualX < width && actualY > 0 && actualY < height) {
                        //Überprüfen der Buchstaben
                        checkFilledPosition(actualX, actualY, letterSet);
                    }
                }

                if((currentX == currentY) || (currentX == -currentY && currentX < 0) || (currentX == 1 - currentY && currentX > 0)) {
                    int copy = deltaX;
                    deltaX = -deltaY;
                    deltaY = copy;
                }

                currentX = currentX + deltaX;
                currentY = currentY + deltaY;
            }

            for(int x = 0; x < width; x++) {
                checkFilledPosition(x, 0, letterSet);
                checkFilledPosition(x, 1, letterSet);
                checkFilledPosition(x, height - 1, letterSet);
                checkFilledPosition(x, height - 2, letterSet);
            }
        }
    }

    protected void checkFilledPosition(int posX, int posY, ArrayList<String> fullLetterSet) {
        //Überprüfen und Auffüllen der Buchstaben
        ArrayList<String> reducedLetterSet = new ArrayList<>(fullLetterSet);

        //Überprüfen aller Positionen im Umkreis von 5 Einheiten
        for(int x = -FILL_CHECK_RADIUS; x <= FILL_CHECK_RADIUS; x++) {
            for(int y = -FILL_CHECK_RADIUS; y <= FILL_CHECK_RADIUS; y++) {
                int checkX = posX + x;
                int checkY = posY + y;

                //Entfernen der Buchstaben aus dem Umfeld aus der Liste der verfügbaren Buchstaben
                if(checkX >= 0 && checkX < width && checkY >= 0 && checkY < height && pattern[checkY][checkX] != null)
                    reducedLetterSet.remove(pattern[checkY][checkX]);
            }
        }

        //Platzieren des neuen Buchstabens innerhalb des Feldes
        if(filledPoints.contains(new Point(posX, posY)))
            pattern[posY][posX] = reducedLetterSet.size() > 1 ? reducedLetterSet.get(instanceRandom.nextInt(reducedLetterSet.size() - 1)) : getRandomChar();
    }

}