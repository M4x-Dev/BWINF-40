package generator;

import java.util.Collections;
import java.util.Random;

public class HardPatternGenerator extends PatternGenerator {

    String[][] pattern;

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
                if(!placeWordHorizontally(word, 0))
                    return false;
            }
            case 1 -> {
                if(!placeWordVertically(word, 0))
                    return false;
            }
            case 2 -> {
                if(!placeWordDiagonally(word, 0))
                    return false;
            }
        }

        return true;
    }

    private boolean placeWordHorizontally(String word, int attempt) {
        //TODO: DIESE FUNKTION SOLLTE WIEDERVERWENDBAR SEIN
        if(attempt > MAX_ATTEMPTS_RECURSIVE) return false;
        Random instanceRandom = new Random();

        //Bestimmen der Koordinaten
        int positionY = instanceRandom.nextInt(pattern.length);
        int positionX = pattern[positionY].length - word.length() != 0 ? instanceRandom.nextInt(pattern[positionY].length - word.length()) : 0;

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY][positionX + i].equals(CHARACTER_EMPTY)) {
                return placeWordHorizontally(word, attempt + 1);
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int x = 0; x < word.length(); x++) pattern[positionY][positionX + x] = String.valueOf(word.charAt(x));
        return true;
    }

    private boolean placeWordVertically(String word, int attempt) {
        //TODO: DIESE FUNKTION SOLLTE WIEDERVERWENDBAR SEIN
        if(attempt > MAX_ATTEMPTS_RECURSIVE) return false;
        Random instanceRandom = new Random();

        //Bestimmen der Koorindaten
        int positionY = pattern.length - word.length() != 0 ? instanceRandom.nextInt(pattern.length - word.length()) : 0;
        int positionX = instanceRandom.nextInt(pattern[positionY].length);

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        for(int i = 0; i < word.length(); i++) {
            if(!pattern[positionY + i][positionX].equals(CHARACTER_EMPTY)) {
                return placeWordVertically(word, attempt + 1);
            }
        }

        //Platzieren des Wortes auf dem Board
        for(int y = 0; y < word.length(); y++) pattern[positionY + y][positionX] = String.valueOf(word.charAt(y));
        return true;
    }

    private boolean placeWordDiagonally(String word, int attempt) {
        //TODO: DIESE FUNKTION SOLLTE WIEDERVERWENDBAR SEIN
        if(attempt > MAX_ATTEMPTS_RECURSIVE) return false;
        Random instanceRandom = new Random();

        //Bestimmen der Koordinaten
        int positionX, positionY;
        boolean invert = instanceRandom.nextBoolean();

        positionY = pattern.length - word.length() != 0 ? instanceRandom.nextInt(pattern.length - word.length()) : 0;
        if(invert) {
            //Von rechts nach links
            positionX = pattern[positionY].length - word.length() != 0 ? instanceRandom.nextInt(pattern[positionY].length - word.length()) + (word.length() - 1) : 0;
        } else {
            //Von links nach rechts
            positionX = pattern[positionY].length - word.length() != 0 ? instanceRandom.nextInt(pattern[positionY].length - word.length()) : 0;
        }

        if(positionX == 0) invert = false;

        //Überprüfen der Position, in welcher das Wort platziert werden soll
        if(invert) {
            for(int i = 0; i < word.length(); i++) {
                if(!pattern[positionY + i][positionX - i].equals(CHARACTER_EMPTY)) {
                    return placeWordDiagonally(word, attempt + 1);
                }
            }
        } else {
            for(int i = 0; i < word.length(); i++) {
                if(!pattern[positionY + i][positionX + i].equals(CHARACTER_EMPTY)) {
                    return placeWordDiagonally(word, attempt + 1);
                }
            }
        }

        //Platzieren des Wortes auf dem Board
        if(invert) for(int i = 0; i < word.length(); i++) pattern[positionY + i][positionX - i] = String.valueOf(word.charAt(i));
        //TODO: ALSO IRGENDWIE PIMMELT DER HIER IMMERNOCH RAUS? #INDEXOUTOFBOUNDS
        else for(int i = 0; i < word.length(); i++) pattern[positionY + (word.length() - (i + 1))][positionX + i] = String.valueOf(word.charAt(i));
        return true;
    }

    private void fillEmptySpaces(String empty, String replacement) {
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                if(pattern[y][x] == null || pattern[y][x].equals(empty))
                    pattern[y][x] = replacement;
            }
        }
    }

    private void calculateEmptySpaces(String empty) {
        Random instanceRandom = new Random();
        for(int y = 0; y < pattern.length; y++) {
            for(int x = 0; x < pattern[y].length; x++) {
                //TODO: DAS NICH MACHEN --> IST NICHT DAS SCHWIERIGSTE
                if(pattern[y][x].equals(empty))
                    pattern[y][x] = String.valueOf((char)(instanceRandom.nextInt(26) + 'a')).toUpperCase();
            }
        }
    }

}