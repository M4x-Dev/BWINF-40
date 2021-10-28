package generator;

/**
 * Diese Klasse generiert Wortfelder mit dem niedrigsten Schwierigkeitsgrad.
 * Merkmale für ein Wortfeld des höchsten Schwierigkeitsgrades (<b>LEICHT</b>):
 * <br>a. Wörter können horizontal und vertikal platziert werden
 * <br>b. Wörter können sich nicht kreuzen
 * <br>c. Leerstellen werden mit zufälligen Buchstaben aufgefüllt
 */
public class EasyPatternGenerator extends PatternGenerator {

    /**
     * Konstruktor der Generator-Klasse.
     * Dieser Konstruktor ruft nur den Super-Konstruktor {@link PatternGenerator} dieser Klasse auf-.
     *
     * @param filePath Dateipfad der Eingabedatei (Wortliste).
     */
    public EasyPatternGenerator(String filePath) {
        super(filePath);
    }

    /**
     * Hauptfunktion der Generator-Klasse, welche das Wortfeld generiert.
     * Diese Funktion generiert ein Wortfeld des Schwierigkeitsgrades "Leicht" mitfhilfe der angegebenen Parameter.
     * Diese Funktion ruft die Super-Implementierung der Funktion auf.
     *
     * @return Gibt das generierte Wortfeld zurück.
     */
    @Override
    public String generatePattern() {
        randomCrossingEnabled = true;
        return super.generatePattern();
    }

    /**
     * Funktion, welche versucht, ein Wort zufällig auf dem Wortfeld zu platzieren.
     * Dazu wird eine von zwei Möglichkeiten zufällig generiert.
     * <br><b>Möglichkeit 1: </b>Horizontale Positionierung auf dem Wortfeld
     * <br><b>Möglichkeit 2: </b>Vertikale Positionierung auf dem Wortfeld
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    @Override
    protected boolean placeWord(String word) {
        return switch(instanceRandom.nextInt(2)) {
            case 0 -> placeWordHorizontally(word, COORDINATE_GENERATE, COORDINATE_GENERATE, true);
            case 1 -> placeWordVertically(word, COORDINATE_GENERATE, COORDINATE_GENERATE, true);
            default -> false;
        };
    }

    /**
     * Funktion, welche ein Wort in horizontaler Ausrichtung auf dem Feld platziert.
     * Dabei wird die Position des Wortes zufällig generiert, wenn diese nicht gegeben ist (-1).
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     * @param optionalX X-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param optionalY Y-Koorindate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param crossingAllowed Legt fest, ob eine Überschneidung von Wörtern erlaubt sein soll, wenn dieses nicht ohne Weiteres platziert werden kann.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    protected boolean placeWordHorizontally(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        //Initialisieren der Koordinaten
        int positionX = optionalX;
        int positionY = optionalY;

        //Berechnen der Koordinaten
        if(positionY == COORDINATE_GENERATE) positionY = yGenerator.generate(pattern.length);
        if(positionX == COORDINATE_GENERATE) positionX = pattern[positionY].length - word.length() != 0 ? xGenerator.generate(pattern[positionY].length - word.length()) : 0;

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY][positionX + i].equals(CHARACTER_EMPTY) && /*(!randomCrossingEnabled ||*/ !pattern[positionY][positionX + i].equals(String.valueOf(word.charAt(i)))) {
                return false;
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int x = 0; x < word.length(); x++) pattern[positionY][positionX + x] = String.valueOf(word.charAt(x));
        placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.Horizontal));
        addClosedCoordinate(positionX, positionY);
        return true;
    }

    /**
     * Funktion, welche ein Wort in vertikaler Ausrichtung auf dem Feld platziert.
     * Dabei wird die Position des Wortes zufällig generiert, wenn diese nicht gegeben ist (-1).
     *
     * @param word Wort, welches auf dem Feld platzert werden soll.
     * @param optionalX X-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param optionalY Y-Koorindate, an welcher das Wort platziert werden soll (-1, wenn diese geneirert werden soll).
     * @param crossingAllowed Legt fest, ob eine Überschreitung von Wörtern erlaubt sein soll, wenn dieses nicht ohne Weiteres platziert werden kann.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    protected boolean placeWordVertically(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        //Initialisieren der Koordindaten
        int positionX = optionalX;
        int positionY = optionalY;

        //Berechnen der Koordinaten
        if(positionY == -1) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) : 0;
        if(positionX == -1) positionX = xGenerator.generate(pattern[positionY].length);

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY + i][positionX].equals(CHARACTER_EMPTY) && (/*!randomCrossingEnabled ||*/ !pattern[positionY + i][positionX].equals(String.valueOf(word.charAt(i))))) {
                return false;
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int y = 0; y < word.length(); y++) pattern[positionY + y][positionX] = String.valueOf(word.charAt(y));
        placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.Vertical));
        addClosedCoordinate(positionX, positionY);
        return true;
    }

    /**
     * Methode, welche die übrigen Felder des Wortfeldes auffüllt, sodass keine Lücken erhalten bleiben.
     * Dazu füllt diese Methode alle Leerstellen mit zufälligen Buchstaben aus dem Alphabet auf.
     */
    @Override
    protected void fillEmptySpaces() {
        //Auffüllen der übrigen Buchstaben mit zufälligen Buchstaben
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x].equals(CHARACTER_EMPTY))
                    pattern[y][x] = String.valueOf((char)(instanceRandom.nextInt(26) + 'a')).toUpperCase();
            }
        }
    }

}