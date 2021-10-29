package generator;

import java.awt.*;
import java.util.*;

/**
 * Diese Klasse generiert Wortfelder mit dem höchsten Schwierigkeitsgrad.
 * Dabei ist dieser Generator von dem der mittleren Schwierigkeit abgeleitet und baut somit darauf auf.
 * <br>Merkmale für ein Wortfeld des höchsten Schwierigkeitsgrades (<b>SCHWER</b>):
 * <br>a. Wörter können horizontal, vertikal und diagonal (absteigend/aufsteigend) platziert werden
 * <br>b. Wörter können sich unbegrenzt kreuzen
 * <br>c. Leerstellen werden mit Fragmenten der Wörter aus der Wortliste aufgefüllt
 */
public class HardPatternGenerator extends MediumPatternGenerator {

    /**
     * Konstruktor der Generator-Klasse.
     * Dieser Konstruktor ruft nur den Super-Konstruktor ({@link MediumPatternGenerator}) dieser Klasse auf.
     *
     * @param filePath Dateipfad der Eingabedatei (Wortliste).
     */
    public HardPatternGenerator(String filePath) {
        super(filePath);
    }

    /**
     * Hauptfunktion der Generator-Klasse, welche das Wortfeld generiert.
     * Diese Funktion generiert ein Wortfeld des Schwierigkeitsgrades "Schwer" mithilfe der angegebenen Parameter.
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
     * Dazu wird eine von drei Möglichkeiten zufällig generiert.
     *
     * <br><b>Möglichkeit 1: </b> Horizontale Platzierung auf dem Wortfeld
     * <br><b>Möglichkeit 2: </b> Vertikale Platzierung auf dem Wortfeld
     * <br><b>Möglichkeit 3: </b> Diagonale Platzierung auf dem Wortfeld (aufsteigend oder absteigend); mit Überschneidung
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    @Override
    protected boolean placeWord(String word) {
        return super.placeWord(word);
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
    @Override
    protected boolean placeWordHorizontally(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        if(!super.placeWordHorizontally(word, optionalX, optionalY, crossingAllowed))
            return crossingAllowed && placeWordCrossing(word);
        else return true;
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
    @Override
    protected boolean placeWordVertically(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        if(!super.placeWordVertically(word, optionalX, optionalY, crossingAllowed))
            return crossingAllowed && placeWordCrossing(word);
        else return true;
    }

    /**
     * Funktion, welche ein Wort in diagonaler Ausrichtung auf dem Feld platziert.
     * Dabei wird die Position des Wortes zufällig generiert, wenn diese nicht gegeben ist (-1).
     * Außerdem wird die Ausrichtung des Wortes (diagonal aufsteigend/absteigend) zufällig generiert, wenn diese nicht gegeben ist ({@link DiagonalDirection}).
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     * @param optionalX X-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param optionalY Y-Koordinate, an welcher das Wort platziert werden soll (-1, wenn diese generiert werden soll).
     * @param orientation Ausrichtung des Wortes: Diagonal aufsteigend oder absteigend (kann auch zufällig generiert werden).
     * @param crossingAllowed Legt fest, ob eine Überschreitung von Wörtern erlaubt sein soll, wenn dieses nicht ohne Weiteres platziert werden kann.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    protected boolean placeWordDiagonally(String word, int optionalX, int optionalY, DiagonalDirection orientation, boolean crossingAllowed) {
        if(!super.placeWordDiagonally(word, optionalX, optionalY, orientation, crossingAllowed))
            return crossingAllowed && placeWordCrossing(word);
        else return true;
    }

    /**
     * Funktion, welche ein Wort überschneidend über einem anderen Wort platzieren soll.
     * Dazu sucht die Funktion aus der Wortliste ein Wort heraus, welches sich mit dem gegebenen Wort überschneidet.
     * Anschließend wird die Ausrichtung des Wortes zufällig generiert und der Schnittpunkt wird ausgerechnet.
     * Danach wird das Wort mithilfe der Funktionen {@link #placeWordHorizontally}, {@link #placeWordVertically}, {@link #placeWordDiagonally} dem Feld hinzugefügt.
     * Falls das Wort nicht mehr auf das Wortfeld passen sollte, dann wird die Schleife fortgesetzt, bis ein passendes Wort gefunden werden konnte.
     * Wenn am Ende der Schleife kein Wort gefunden wurde, welches sich überschneidet, dann wird false zurückgegebenen.
     *
     * @param word Word, welches überschneidend auf dem Wortfeld platziert werden soll.
     *
     * @return Gibt zurück, ob das Wort auf dem Wortfeld platziert werden konnte.
     */
    protected boolean placeWordCrossing(String word) {
        //Überschneidung von Wörtern
        ArrayList<Map.Entry<String, WordPosition>> positionedWords = new ArrayList<>(placedWords.entrySet().stream().toList());
        positionedWords.sort(Comparator.comparingInt(wordA -> wordA.getKey().length()));
        Collections.reverse(positionedWords); //Höchste Chancen bei dem längsten Wort

        boolean matchFound = false;
        for(Map.Entry<String, WordPosition> placedWord : positionedWords) {
            //Überprüfen der Platzierungsmögichkeiten
            boolean lettersInCommon = false;

            int crossingIndexX = 0;
            int crossingIndexY = 0;

            //Bestimmen des Schnittpunktes zwischen den beiden Wörtern
            for(int a = 0; a < placedWord.getKey().length(); a++) {
                for(int b = 0; b < word.length(); b++) {
                    if(String.valueOf(placedWord.getKey().charAt(a)).equals(String.valueOf(word.charAt(b)))) {
                        lettersInCommon = true;
                        crossingIndexX = a;
                        crossingIndexY = b;
                        break;
                    }
                }
            }

            //Überspringen des momentanen Wortes, wenn kein Schnittpunkt gefunden werden konnte
            if(!lettersInCommon) continue;

            //Bestimmen der Orientierung des neuen Wortes
            WordPosition.Orientation newOrientation = WordPosition.randomOrientation(placedWord.getValue().orientation());

            //Berechnen der Koordinaten des kreuzenden Wortes, welches platziert werden soll
            int possiblePositionX = placedWord.getValue().crossWordX(crossingIndexX, crossingIndexY, newOrientation);
            int possiblePositionY = placedWord.getValue().crossWordY(crossingIndexX, crossingIndexY, newOrientation);

            //Überspringen des momentanen Wortes, wenn die Koordinaten außerhalb des Feldes liegen
            if(possiblePositionX < 0 || possiblePositionX >= pattern[0].length) continue;
            if(possiblePositionY < 0 || possiblePositionY >= pattern.length) continue;

            //Überspringen des momentanen Wortes, wenn das Wort nicht mehr auf das Feld passt
            if(newOrientation.equals(WordPosition.Orientation.Vertical) && possiblePositionY + word.length() > pattern.length) continue;
            if(newOrientation.equals(WordPosition.Orientation.Horizontal) && possiblePositionX + word.length() > pattern[possiblePositionY].length) continue;

            if(newOrientation.equals(WordPosition.Orientation.DiagonalUp) && (possiblePositionX + word.length() > pattern[possiblePositionY].length || possiblePositionY - word.length() < 0)) continue;
            if(newOrientation.equals(WordPosition.Orientation.DiagonalDown) && (possiblePositionX + word.length() > pattern[possiblePositionY].length || possiblePositionY + word.length() > pattern.length)) continue;

            //Platzieren des kreuzenden Wortes
            boolean result = switch (newOrientation) {
                case Horizontal -> placeWordHorizontally(word, possiblePositionX, possiblePositionY, false);
                case Vertical -> placeWordVertically(word, possiblePositionX, possiblePositionY, false);
                case DiagonalUp -> placeWordDiagonally(word, possiblePositionX, possiblePositionY, DiagonalDirection.Ascending, false);
                case DiagonalDown -> placeWordDiagonally(word, possiblePositionX, possiblePositionY, DiagonalDirection.Descending, false);
            };

            //Abbrechen der Schleife, wenn das kreuzende Wort erfolgreich platziert werden konnte
            if(result) {
                matchFound = true;
                break;
            }

            //Andernfalls wird die Schleife fortgesetzt, bis ein Schnittpunkt gefunden wurde oder alle Möglichkeiten ausprobiert wurden
        }

        return matchFound;
    }

    /**
     * Methode, welche die übrigen Felder des Wortfeldes auffüllt, sodass keine Lücken erhalten bleiben.
     * Dazu sammelt die Funktion zuerst alle Buchstaben, welche in den Wörtern aus der Wortliste enthalten sind.
     * Anschließend werden größere Leerstellen (größer als 2 Einheiten) mit Fragmenten der Wörter aus der Wortliste aufgefüllt.
     * Alle übrigen Leerstellen, welche folglich kleiner als 2 Einheiten groß sind, werden zufällig mit der zuvor gesammelten Buchstabenliste aufgefüllt.
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

        //Auffüllen von bestimmten Teilen von Reihen mit Fragmenten der Wörter aus der Wortliste
        for (int y = 0; y < pattern.length; y++) {
            Map<Integer, Integer> emptySpaces = new HashMap<>();

            boolean inPart = false;
            int lastStart = 0;

            //Ermitteln von Leerstellen innerhalb einer Zeile
            for (int letter = 0; letter < pattern[y].length; letter++) {
                if (pattern[y][letter].equals(PatternGenerator.CHARACTER_EMPTY)) {
                    if (!inPart) {
                        inPart = true;
                        lastStart = letter;
                    }

                    //Hinzufügen der Stelle zu der Liste
                    emptySpaces.put(lastStart, letter);
                } else {
                    //Leerstelle ist beendet
                    inPart = false;
                }
            }

            //Auffüllen der größeren Leerstellen mit den Fragmenten der Wortliste
            for (Map.Entry<Integer, Integer> entry : emptySpaces.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                //Leerstelle wird nur aufgefüllt, wenn diese größer, als zwei Einheiten ist
                if (value - key > 1) {
                    //Generieren eines Fragments der Wortliste
                    String wordInstance = words.get(instanceRandom.nextInt(words.size()));

                    //Abbrechen der Schleife, wenn das Fragment zu kurz ist
                    if(wordInstance.length() - (value - key) <= 0) continue;

                    String wordFragment = wordInstance.substring(instanceRandom.nextInt(wordInstance.length() - (value - key)));

                    //Hinzufügen des Fragmentes in das Wortfeld
                    for (int i = key; i <= value; i++) {
                        pattern[y][i] = String.valueOf(wordFragment.charAt(i - key));
                        filledPoints.add(new Point(i, y));
                    }
                }
            }
        }

        //Auffüllen der übrigen Buchstaben mit zufälligen Buchstaben
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x].equals(PatternGenerator.CHARACTER_EMPTY)) {
                    pattern[y][x] = getRandomChar();
                    filledPoints.add(new Point(x, y));
                }
            }
        }

        //Überprüfung der platzierten Buchstaben
        checkFilledSpaces(letterSet);
    }
}