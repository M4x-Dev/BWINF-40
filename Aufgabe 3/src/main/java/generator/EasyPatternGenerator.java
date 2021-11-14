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
        randomCrossingEnabled = false;
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
        int maxAttempts = 3;

        switch(instanceRandom.nextInt(2)) {
            case 0:
                for(int i = 0; i <= maxAttempts; i++) {
                    int[] position = getPositionForWord(word, WordPosition.Orientation.Horizontal);
                    if(placeWordHorizontally(word, position[0], position[1], false))
                        return true;
                }
                return false;
            case 1:
                for(int i = 0; i <= maxAttempts; i++) {
                    int[] position = getPositionForWord(word, WordPosition.Orientation.Vertical);
                    if(placeWordVertically(word, position[0], position[1], false))
                        return true;
                }
                return false;
            default: return false;
        }
    }

    /**
     * Diese Funktion generiert eine Position für ein bestimmtes Wort einer bestimmten Ausrichtung.
     * Dabei ist die Position jedoch nicht vollständig zufällig, sondern durch verschiedene Wahrscheinlichkeiten gekennzeichnet.
     * Diese Funktion wird verwendet, da es möglich ist, dass der Algorithmus keine Lösung findet, wenn sich die Wörter nicht kreuzen dürfen (Schwierigkeit "LEICHT").
     * <br>Bei horizontaler Ausrichtung:
     * Wenn das Wort horizontal positioniert werden soll, dann ist die X-Koordinate des Wortes zufällig gewählt, da sie für die weitere Verteilung keine Rolle spielt.
     * Die Y-Koordinate wird hier jedoch so berechnet, dass die Wahrscheinlichkeit, dass das Wort in der Mitte des Feldes positioniert wird am geringsten ist.
     * Dafür wird das Wortfeld in drei gleich große Bereiche eingeteilt, welche die Wahrscheinlichkeiten: 40 % (oben), 20 % (mitte) und 40 % (unten) haben.
     * Damit werden horizontal positionierte Wörter also oft oben/unten in dem Feld platziert, sodass noch genug Platz für die vertikal ausgerichteten Wörter bleibt.
     * <br>Bei vertikaler Ausrichtung:
     * Wenn das Wort vertikal positioniert werden soll, dann ist die Y-Koordinate des Wortes zufällig gewählt, da sie für die weitere Verteilung keine Rolle spielt.
     * Die X-Koordinate wird hier jedoch so berechnet, dass die Wahrscheinlichkeit, am äußeren Rand des Feldes zu landen, am höchsten ist.
     * Dafür wird das Wortfeld erneut in drei gleich große Bereiche mit den folgenden Wahrscheinlichkeiten eingeteilt: 40 % (links), 20 % (mitte) und 40 % (rechts).
     * Damit werden vertikal positionierte Wörter also oft am Rand des Feldes positioniert, sodass noch genug Platz für die horizontal ausgerichteten Wörter bleibt.
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     * @param orientation Ausrichtung des Wortes, welches auf dem Feld platziert werden soll.
     *
     * @return Gibt die generierte Position (X- und Y-Koordinate des Wortes zurück).
     */
    public int[] getPositionForWord(String word, WordPosition.Orientation orientation) {
        switch(orientation) {
            case Horizontal:
                //X-Koordinate wird zufällig generiert
                int hx = word.length() < width ? instanceRandom.nextInt(width - word.length()) : 0;

                int[][] verticalSections = new int[3][2];
                int verticalSectionIndex = 0;

                //Einteilung des Feldes in drei gleich große Bereiche
                //Erstes Drittel des Feldes
                verticalSections[0][0] = 0;
                verticalSections[0][1] = height / 3;

                //Zweites Drittel des Feldes
                verticalSections[1][0] = height / 3;
                verticalSections[1][1] = (height / 3) * 2;

                //Letztes Drittel des Feldes
                verticalSections[2][0] = (height / 3) * 2;
                verticalSections[2][1] = height;

                //Zufällige Auswahl des Bereiches nach den verschiedenen Wahrscheinlichkeiten
                int verticalSectionSelection = instanceRandom.nextInt(100);
                if(verticalSectionSelection < 40) verticalSectionIndex = 0; //Erstes Drittel (40 %)
                else if(verticalSectionSelection < 60) verticalSectionIndex = 1; //Zweites Drittel (20 %)
                else verticalSectionIndex = 2; //Letztes Drittel (40 %)

                //Auswahl einer zufälligen Koordinate innerhalb des ausgewählten Bereiches
                int hy = instanceRandom.nextInt(verticalSections[verticalSectionIndex][1] - verticalSections[verticalSectionIndex][0]) + verticalSections[verticalSectionIndex][0];

                //Rückgabe der berechneten Koordinaten
                return new int[] { hx, hy };
            case Vertical:
                //Y-Koordinate wird zufällig generiert
                int vy = word.length() < height ? instanceRandom.nextInt(height - word.length()) : 0;

                int[][] horizontalSections = new int[3][2];
                int horizontalSectionIndex = 0;

                //Einteilung des Feldes in drei gleich große Bereiche
                //Erstes Drittel des Feldes
                horizontalSections[0][0] = 0;
                horizontalSections[0][1] = width / 3;

                //Zweites Drittel des Feldes
                horizontalSections[1][0] = width / 3;
                horizontalSections[1][1] = (width / 3) * 2;

                //Letztes Drittel des Feldes
                horizontalSections[2][0] = (width / 3) * 2;
                horizontalSections[2][1] = width;

                //Zufällige Auswahl des Bereiches nach den verschiedenen Wahrscheinlichkeiten
                int horizontalSectionSelection = instanceRandom.nextInt(100);
                if(horizontalSectionSelection < 40) horizontalSectionIndex = 0; //Erstes Drittel (40 %)
                else if(horizontalSectionSelection < 60) horizontalSectionIndex = 1; //Zweites Drittel (20 %)
                else horizontalSectionIndex = 2; //Letztes Drittel (40 %)

                //Auswahl einer zufälligen Koordinate innerhalb des ausgewählten Bereiches
                int vx = instanceRandom.nextInt(horizontalSections[horizontalSectionIndex][1] - horizontalSections[horizontalSectionIndex][0]) + horizontalSections[horizontalSectionIndex][0];

                //Rückgabe der berechneten Koordinaten
                return new int[] { vx, vy };
            //Falls die Funktion mit einer anderen Ausrichtung aufgerufen wird, soll die Koordinate vollständig zufällig generiert werden (diagonal)
            default: return new int[] { -1, -1 };
        }
    }

    /**
     * Funktion, welche ein Wort in horizontaler Ausrichtung auf dem Feld platziert.
     * Dabei wird die Position des Wortes zufällig generiert, wenn diese nicht gegeben ist (-1).
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     * @param optionalX X-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param optionalY Y-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
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
            if(!pattern[positionY][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY][positionX + i].equals(String.valueOf(word.charAt(i)))) {
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
     * @param optionalY Y-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese geneirert werden soll).
     * @param crossingAllowed Legt fest, ob eine Überschreitung von Wörtern erlaubt sein soll, wenn dieses nicht ohne Weiteres platziert werden kann.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    protected boolean placeWordVertically(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        //Initialisieren der Koordindaten
        int positionX = optionalX;
        int positionY = optionalY;

        //Berechnen der Koordinaten
        if(positionY == COORDINATE_GENERATE) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) : 0;
        if(positionX == COORDINATE_GENERATE) positionX = xGenerator.generate(pattern[positionY].length);

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY + i][positionX].equals(CHARACTER_EMPTY) && (!pattern[positionY + i][positionX].equals(String.valueOf(word.charAt(i))))) {
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
                    pattern[y][x] = getRandomChar();
            }
        }
    }

}