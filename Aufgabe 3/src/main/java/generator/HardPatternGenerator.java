package generator;

import java.util.*;

/**
 * Diese Klasse generiert Wortfelder mit dem höchsten Schwierigkeitsgrad.
 * Dabei ist dieser Generator von dem der mittleren Schwierigkeit abgeleitet und baut somit darauf auf.
 * <br>Merkmale für ein Wortfeld des höchsten Schwierigkeitsgrades (<b>SCHWER</b>)
 * <br>a. Wörter können horizontal, vertikal und diagonal (absteigen/aufsteigend) platziert werden
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
     * Diese Funktion generiert ein Wortfeld des übergeordneten Schwierigkeitsgrades mithilfe der gegebenen Parameter.
     *
     * Diese Funktion durchläuft eine Queue (Warteschlange) an Wörtern, welche dem Feld hinzugefügt werden sollen.
     * Wenn ein Wort dem Feld nicht hinzugefügt werden kann, dann wird es der Warteschlange wieder hinzugefügt und zu einem späteren Zeitpunkt erneut abgearbeitet.
     *
     * Sobald die Queue abgearbeitet ist, werden all verbleibenden Felder mit Fragmenten der Wörter der Wortliste aufgefüllt.
     *
     * @return Gibt das fertige Wortfeld des Schwierigkeitsgrades <b>"SCHWER"</b> zurück.
     */
    @Override
    public String generatePattern() {
        super.generatePattern();

        while(wordQueue.peek() != null) {
            String word = wordQueue.getFirst();
            if(!placeWordRandomly(word)) enqueueRemoval(word);
            else wordQueue.removeFirst();
        }

        this.fillEmptySpaces();
        return formatMatrix(pattern);
    }

    /**
     * Methode, welche ein Wort aus dem Wortfeld entfernt und dieses wieder in die Warteschlange hinzufügt.
     * Dazu lädt die Methode die Position und die Ausrichtung des Wortes, welches entfernt werden soll, aus einer HashMap.
     * Abhängig von der Ausrichtung und Position des Wortes werden alle Felder des Wortes wieder in das Blankzeichen umgewandelt.
     *
     * @param removeWord Wort, welches aus dem Feld entfernt werden soll.
     */
    private void enqueueRemoval(String removeWord) {
        //Entfernen des Wortes vom Board
        if(placedWords.containsKey(removeWord)) {
            WordPosition removePosition = placedWords.remove(removeWord);

            //Leeren der Stellen des Wortes
            switch(removePosition.orientation()) {
                case Horizontal:
                    for(int i = 0; i < removeWord.length(); i++)
                        pattern[removePosition.positionY()][removePosition.positionX() + i] = CHARACTER_EMPTY;
                    break;
                case Vertical:
                    for(int i = 0; i < removeWord.length(); i++)
                        pattern[removePosition.positionY() + i][removePosition.positionX()] = CHARACTER_EMPTY;
                    break;
                case DiagonalUp:
                    for(int i = 0; i < removeWord.length(); i++)
                        pattern[removePosition.positionY() - i][removePosition.positionX() + i] = CHARACTER_EMPTY;
                    break;
                case DiagonalDown:
                    for(int i = 0; i < removeWord.length(); i++)
                        pattern[removePosition.positionY() + i][removePosition.positionX() + i] = CHARACTER_EMPTY;
                    break;
            }

            //Wort wird der Queue wieder hinzgefügt, damit es erneut platziert werden kann
            wordQueue.addFirst(removeWord);
        }
    }

    /**
     * Funktion, welche versucht, ein Wort zufällig auf dem Wortfeld zu platzieren.
     * Dazu wird eine von drei Möglichkeiten zufällig generiert.
     *
     * <br><b>Möglichkeit 1: </b> Horizontale Platzierung auf dem Wortfeld
     * <br><b>Möglichkeit 2: </b> Vertikale Platzierung auf dem Wortfeld
     * <br><b>Möglichkeit 3: </b> Diagonale Platzierung auf dem Wortfeld (aufsteigend oder absteigend)
     *
     * @param word Wort, welches auf dem Feld platziert werden soll.
     *
     * @return Gibt zurück, ob das Wort erfolgreich platziert werden konnte.
     */
    private boolean placeWordRandomly(String word) {
        int placement = instanceRandom.nextInt(3);

        switch(placement) {
            //Horizontale Positionierung des Wortes
            case 0 -> {
                if(!placeWordHorizontally(word, -1, -1, true))
                    return false;
            }
            //Vertikale Positionierung des Wortes
            case 1 -> {
                if(!placeWordVertically(word, -1, -1, true))
                    return false;
            }
            //Diagonale Positionierung des Wortes (aufsteigend/absteigend)
            case 2 -> {
                if(!placeWordDiagonally(word, -1, -1, DiagonalDirection.Random, true))
                    return false;
            }
        }

        return true;
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
    private boolean placeWordHorizontally(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        //Initialisieren der Koordinaten
        int positionX = optionalX;
        int positionY = optionalY;

        //Berechnen der Koordinaten
        if(positionY == -1) positionY = yGenerator.generate(pattern.length);
        if(positionX == -1) positionX = pattern[positionY].length - word.length() != 0 ? xGenerator.generate(pattern[positionY].length - word.length()) : 0;

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY][positionX + i].equals(String.valueOf(word.charAt(i)))) {
                return crossingAllowed && placeWordCrossing(word);
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
    private boolean placeWordVertically(String word, int optionalX, int optionalY, boolean crossingAllowed) {
        //Initialisieren der Koordindaten
        int positionX = optionalX;
        int positionY = optionalY;

        //Berechnen der Koordinaten
        if(positionY == -1) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) : 0;
        if(positionX == -1) positionX = xGenerator.generate(pattern[positionY].length);

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY + i][positionX].equals(CHARACTER_EMPTY) && !Objects.equals(pattern[positionY + i][positionX], String.valueOf(word.charAt(i)))) {
                return crossingAllowed && placeWordCrossing(word);
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int y = 0; y < word.length(); y++) pattern[positionY + y][positionX] = String.valueOf(word.charAt(y));
        placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.Vertical));
        addClosedCoordinate(positionX, positionY);
        return true;
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
    private boolean placeWordDiagonally(String word, int optionalX, int optionalY, DiagonalDirection orientation, boolean crossingAllowed) {
        //Initialisieren der Koordinaten und Ausrichtung
        int positionX = optionalX;
        int positionY = optionalY;
        boolean movingUp = orientation != DiagonalDirection.Descending && (orientation == DiagonalDirection.Ascending || instanceRandom.nextBoolean());

        //Berechnen der Koordinaten
        if(movingUp) {
            //Berechnen der Koordinaten für die Richtung: diagonal aufsteigend
            if(positionY == -1) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) + word.length() - 1 : pattern.length - 1;
            if(positionX == -1) positionX = pattern[positionY].length - word.length() != 0 ? xGenerator.generate(pattern[positionY].length - word.length()) : 0;

            //Überprüfen der Position auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) {
                if((positionX % 2 == 0 || positionY % 2 == 0) || (!pattern[positionY - i][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY - i][positionX + i].equals(String.valueOf(word.charAt(i))))) {
                    return crossingAllowed && placeWordCrossing(word);
                }
            }

            //Platzieren des Wortes auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) pattern[positionY - i][positionX + i] = String.valueOf(word.charAt(i));
            placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.DiagonalUp));
        } else {
            //Berechnen der Koordinaten für die Richtung: diagonal absteigend
            if(positionY == -1) positionY = pattern.length - word.length() != 0 ? yGenerator.generate(pattern.length - word.length()) : 0;
            if(positionX == -1) positionX = pattern[positionY].length - word.length() != 0 ? xGenerator.generate(pattern[positionY].length - word.length()) : 0;

            //Überprüfen der Position auf dem Wortfeld
            for(int i = 0; i < word.length(); i++) {
                if((positionX % 2 == 0 || positionY % 2 == 0) || (!pattern[positionY + i][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY + i][positionX + i].equals(String.valueOf(word.charAt(i))))) {
                    return crossingAllowed && placeWordCrossing(word);
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

    private boolean placeWordCrossing(String word) {
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

            //Berechnen der Koorindaten des kreuzenden Wortes, welches platziert werden soll
            int possiblePositionX = placedWord.getValue().crossWordX(crossingIndexX, crossingIndexY, newOrientation);
            int possiblePositionY = placedWord.getValue().crossWordY(crossingIndexX, crossingIndexY, newOrientation);

            //Überspringen des momentanen Wortes, wenn die Koorindaten außerhalb des Feldes liegen
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
        Random instanceRandom = new Random();
        ArrayList<String> letterSet = new ArrayList<>();

        //Sammeln aller Buchstaben, welche in den Wörtern aus der Wortliste enthalten sind
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
                    for (int i = key; i <= value; i++) pattern[y][i] = String.valueOf(wordFragment.charAt(i - key));
                }
            }
        }

        //Auffüllen der übrigen Buchstaben mit zufälligen Buchstaben aus der Wortliste
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x].equals(PatternGenerator.CHARACTER_EMPTY))
                    pattern[y][x] = letterSet.get(instanceRandom.nextInt(letterSet.size())); //String.valueOf((char)(instanceRandom.nextInt(26) + 'a')).toUpperCase();
            }
        }
    }

}