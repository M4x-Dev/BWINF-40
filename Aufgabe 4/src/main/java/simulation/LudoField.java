package simulation;

import java.util.Arrays;

/**
 * Klasse, welche das Spielfeld des Hauptspieles darstellt.
 * Dabei umfasst diese Klasse ein lineares Feld von 40 Feldern, welche durch ein Array dargestellt werden.
 */
public class LudoField {

    public static final String FIELD_EMPTY = " "; //Leerstelle des Feldes

    public static final int POSITION_PLAYER_A_START = 0; //Startposition des ersten Spielers (unten)
    public static final int POSITION_PLAYER_B_START = 20; //Startposition des zweiten Spielers (oben)

    public static final int ROUND_COMPLETE = 40; //Anzahl der Schritte für eine ganze Runde
    public static final int ROUND_MAXIMUM = 44; //Maximale Anzahl der Schritte (ganze Runde + Zielfelder)

    public final String[] mainField = new String[40]; //Spielfeld

    /**
     * Konstruktor des Spielfeldes, welcher das Spielfeld mit Leerzeichen auffüllt.
     */
    public LudoField() {
        //Auffüllen des Spielfeldes mit Leerstellen
        Arrays.fill(mainField, FIELD_EMPTY);
    }

    /**
     * Methode, welche den momentanen Zustand des Spielfeldes in der Konsole ausgibt.
     * Dazu werden alle Leerzeichen durch ein "X" ersetzt, damit der Pfad klarer erkennbar ist.
     */
    public void printField() {
        String[] fieldCopy = new String[mainField.length];
        for(int i = 0; i < mainField.length; i++) fieldCopy[i] = mainField[i].equals(FIELD_EMPTY) ? "X" : mainField[i];

        System.out.println();
        System.out.println(getPadding(4) + fieldCopy[18] + " " + fieldCopy[19] + " " + fieldCopy[20] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[17] + "   " + fieldCopy[21] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[16] + "   " + fieldCopy[22] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[15] + "   " + fieldCopy[23] + getPadding(4));
        System.out.println(fieldCopy[10] + " " + fieldCopy[11] + " " + fieldCopy[12] + " " + fieldCopy[13] + " " + fieldCopy[14] + "   " + fieldCopy[24] + " " + fieldCopy[25] + " " + fieldCopy[26] + " " + fieldCopy[27] + " " + fieldCopy[28]);
        System.out.println(fieldCopy[9] + getPadding(9) + " " + fieldCopy[29]);
        System.out.println(fieldCopy[8] + " " + fieldCopy[7] + " " + fieldCopy[6] + " " + fieldCopy[5] + " " + fieldCopy[4] + "   " + fieldCopy[34] + " " + fieldCopy[33] + " " + fieldCopy[32] + " " + fieldCopy[31] + " " + fieldCopy[30]);
        System.out.println(getPadding(4) + fieldCopy[3] + "   " + fieldCopy[35] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[2] + "   " + fieldCopy[36] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[1] + "   " + fieldCopy[37] + getPadding(4));
        System.out.println(getPadding(4) + fieldCopy[0] + " " + fieldCopy[39] + " " + fieldCopy[38] + getPadding(4));
        System.out.println();
    }

    /**
     * Funktion, welche einen String für den Abstand zwischen den Feldern zurückgibt.
     * Diese Funktion gibt einen String von Leerzeichen zurück, welcher die Länge width * 2 besitzt.
     *
     * @param width Breite der Leerzeichen.
     *
     * @return Gibt einen String von Leerzeichen zum Füllen der Lücken zwischen den Feldern zurück.
     */
    private String getPadding(int width) {
        return "  ".repeat(width);
    }

}