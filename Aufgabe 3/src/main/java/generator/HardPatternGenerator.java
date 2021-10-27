package generator;

import java.util.*;

public class HardPatternGenerator extends MediumPatternGenerator {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final List<String> colors = Arrays.asList(ANSI_RESET, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE);

    public HardPatternGenerator(String filePath) {
        super(filePath);
    }

    @Override
    public String generatePattern() {
        pattern = new String[height][width];

        Collections.sort(words);
        Collections.reverse(words);

        fillEmptySpaces(null, CHARACTER_EMPTY);
        for (String word : words) {
            if (!placeWordRandomly(word))
                return generatePattern();
        }

        calculateEmptySpaces(CHARACTER_EMPTY);
        return formatMatrix(pattern);
    }

    private boolean placeWordRandomly(String word) {
        int placement = new Random().nextInt(3);

        switch(placement) {
            case 0 -> {
                if(!placeWordHorizontally(word, 0, 0, 0))
                    return false;
            }
            case 1 -> {
                if(!placeWordVertically(word, 0, 0, 0))
                    return false;
            }
            case 2 -> {
                if(!placeWordDiagonally(word, 0, 0, 0, 0))
                    return false;
            }
        }

        return true;
    }

    private boolean placeWordHorizontally(String word, int posX, int posY, int attempt) {
        //TODO: DIESE FUNKTION SOLLTE WIEDERVERWENDBAR SEIN
        /*if(attempt > MAX_ATTEMPTS_RECURSIVE / 2 && attempt < MAX_ATTEMPTS_RECURSIVE) return placeWordCrossing(word, attempt);
        else if(attempt > MAX_ATTEMPTS_RECURSIVE) return false;*/
        Random instanceRandom = new Random();

        String wordColor = getRandomColor();

        //Bestimmen der Koordinaten
        int positionX = posX;
        int positionY = posY;

        if(positionY == 0) positionY = instanceRandom.nextInt(pattern.length);
        if(positionX == 0 && pattern[positionY].length - word.length() != 0) positionX = instanceRandom.nextInt(pattern[positionY].length - word.length());

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY][positionX + i].equals(String.valueOf(word.charAt(i)))) {
                if(attempt == 0) return placeWordCrossing(word);
                else return false;
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int x = 0; x < word.length(); x++) pattern[positionY][positionX + x] = wordColor + String.valueOf(word.charAt(x));
        placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.Horizontal));
        return true;
    }

    private boolean placeWordVertically(String word, int posX, int posY, int attempt) {
        //TODO: DIESE FUNKTION SOLLTE WIEDERVERWENDBAR SEIN
        /*if(attempt > MAX_ATTEMPTS_RECURSIVE / 2 && attempt < MAX_ATTEMPTS_RECURSIVE) return placeWordCrossing(word, attempt);
        else if(attempt > MAX_ATTEMPTS_RECURSIVE) return false;*/
        Random instanceRandom = new Random();

        String wordColor = getRandomColor();

        //Bestimmen der Koorindaten
        int positionX = posX;
        int positionY = posY;

        if(positionY == 0 && pattern.length - word.length() != 0) positionY = instanceRandom.nextInt(pattern.length - word.length());
        if(positionX == 0) positionX = instanceRandom.nextInt(pattern[positionY].length);

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY + i][positionX].equals(CHARACTER_EMPTY) && !pattern[positionY + i][positionX].equals(String.valueOf(word.charAt(i)))) {
                if(attempt == 0) return placeWordCrossing(word);
                return false;
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int y = 0; y < word.length(); y++) pattern[positionY + y][positionX] = wordColor + String.valueOf(word.charAt(y));
        placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.Vertical));
        return true;
    }

    private boolean placeWordDiagonally(String word, int posX, int posY, int orientation, int attempt) {
        //TODO: DIESE FUNKTION SOLLTE WIEDERVERWENDBAR SEIN
        /*if(attempt > MAX_ATTEMPTS_RECURSIVE / 2 && attempt < MAX_ATTEMPTS_RECURSIVE) return placeWordCrossing(word, attempt);
        else if(attempt > MAX_ATTEMPTS_RECURSIVE) return false;*/

        Random instanceRandom = new Random();

        //Bestimmen der Koordinaten
        int positionX = posX;
        int positionY = posY;
        boolean movingUp = orientation != -1 && (orientation == 1 || instanceRandom.nextBoolean());

        String wordColor = getRandomColor();

        if(movingUp) {
            if(positionY == 0) positionY = pattern.length - word.length() != 0 ? instanceRandom.nextInt(pattern.length - word.length()) + word.length() - 1 : pattern.length - 1;
            if(positionX == 0 && pattern[positionY].length - word.length() != 0) positionX = instanceRandom.nextInt(pattern[positionY].length - word.length());

            for(int i = 0; i < word.length(); i++) {
                if(!pattern[positionY - i][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY - i][positionX + i].equals(String.valueOf(word.charAt(i)))) {
                    if(attempt == 0) return placeWordCrossing(word);
                    else return false;
                }
            }

            for(int i = 0; i < word.length(); i++) pattern[positionY - i][positionX + i] = wordColor + String.valueOf(word.charAt(i));
            placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.DiagonalUp));
        } else {
            if(positionY == 0 && pattern.length - word.length() != 0) positionY = instanceRandom.nextInt(pattern.length - word.length());
            if(positionX == 0 && pattern[positionY].length - word.length() != 0) positionX = instanceRandom.nextInt(pattern[positionY].length - word.length());

            for(int i = 0; i < word.length(); i++) {
                if(!pattern[positionY + i][positionX + i].equals(CHARACTER_EMPTY) && !pattern[positionY + i][positionX + i].equals(String.valueOf(word.charAt(i)))) {
                    if(attempt == 0) return placeWordCrossing(word);
                    else return false;
                }
            }

            for(int i = 0; i < word.length(); i++) pattern[positionY + i][positionX + i] = wordColor + String.valueOf(word.charAt(i));
            placedWords.put(word, new WordPosition(positionX, positionY, WordPosition.Orientation.DiagonalDown));
        }

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

            if(!lettersInCommon) continue;

            //Bestimmen der Orientierung des neuen Wortes
            WordPosition.Orientation newOrientation = WordPosition.randomOrientation(placedWord.getValue().orientation());

            //Berechnen der Koorindaten des kreuzenden Wortes, welches platziert werden soll
            int possiblePositionX = placedWord.getValue().crossWordX(crossingIndexX, crossingIndexY, newOrientation);
            int possiblePositionY = placedWord.getValue().crossWordY(crossingIndexX, crossingIndexY, newOrientation);

            //Abbrechen der Schleife, wenn das Wort nicht mehr auf das Feld passt
            if(possiblePositionX < 0 || possiblePositionX >= pattern[0].length) continue;
            if(possiblePositionY < 0 || possiblePositionY >= pattern.length) continue;

            if(newOrientation.equals(WordPosition.Orientation.Vertical) && possiblePositionY + word.length() > pattern.length) continue;
            if(newOrientation.equals(WordPosition.Orientation.Horizontal) && possiblePositionX + word.length() > pattern[possiblePositionY].length) continue;

            if(newOrientation.equals(WordPosition.Orientation.DiagonalUp) && (possiblePositionX + word.length() > pattern[possiblePositionY].length || possiblePositionY - word.length() < 0)) continue;
            if(newOrientation.equals(WordPosition.Orientation.DiagonalDown) && (possiblePositionX + word.length() > pattern[possiblePositionY].length || possiblePositionY + word.length() > pattern.length)) continue;

            //Platzieren des kreuzenden Wortes

            boolean result = switch (newOrientation) {
                case Horizontal -> placeWordHorizontally(word, possiblePositionX, possiblePositionY, 1);
                case Vertical -> placeWordVertically(word, possiblePositionX, possiblePositionY, 1);
                case DiagonalUp -> placeWordDiagonally(word, possiblePositionX, possiblePositionY, 1, 1);
                case DiagonalDown -> placeWordDiagonally(word, possiblePositionX, possiblePositionY, -1, 1);
            };

            if(result) {
                matchFound = true;
                break;
            }
        }

        return matchFound;
    }

    private void fillEmptySpaces(String empty, String replacement) {
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x] == null || pattern[y][x].equals(empty))
                    pattern[y][x] = replacement;
            }
        }
    }

    @Override
    protected void calculateEmptySpaces(String empty) {
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

            for (int letter = 0; letter < pattern[y].length; letter++) {
                if (pattern[y][letter].equals(empty)) {
                    if (!inPart) {
                        inPart = true;
                        lastStart = letter;
                    }

                    emptySpaces.put(lastStart, letter);
                } else {
                    inPart = false;
                }
            }

            for (Map.Entry<Integer, Integer> entry : emptySpaces.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                if (value - key > 1) {
                    String wordInstance = words.get(instanceRandom.nextInt(words.size()));
                    if(wordInstance.length() - (value - key) <= 0) continue;

                    String wordFragment = wordInstance.substring(instanceRandom.nextInt(wordInstance.length() - (value - key)));

                    for (int i = key; i <= value; i++) {
                        pattern[y][i] = String.valueOf(wordFragment.charAt(i - key));
                    }
                }
            }
        }

        //Auffüllen der übrigen Buchstaben mit zufälligen Buchstaben aus der Wortliste
        /*for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x].equals(empty))
                    pattern[y][x] = ANSI_BLACK + letterSet.get(instanceRandom.nextInt(letterSet.size())); //String.valueOf((char)(instanceRandom.nextInt(26) + 'a')).toUpperCase();
            }
        }*/
    }

    private static String getRandomColor() {
        return colors.get(new Random().nextInt(colors.size()));
    }

}