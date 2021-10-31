package simulation;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Klasse, welche einen Spieler in diesem Spiel darstellt.
 * Das Spiel besitzt zwei Spieler, welche jeweils vier Figuren besitzen und gegeneinander antreten.
 */
public class LudoPlayer {

    public final String playerTag; //Tag/Buchstabe des Spielers
    public final String[] playersGoal; //Vier Zielfelder des Spielers

    public final int playerStartPosition; //Startposition des Spielers auf dem Hauptfeld
    public int playersHome; //Anzahl der Spieler auf den B-Feldern
    public int playerTurns; //Anzahl der Züge, welche der Spieler hatte

    public final LudoDice playerDice; //Würfel des Spielers
    public final ArrayList<LudoFigure> playerFigures = new ArrayList<>(); //Figuren des Spielers

    /**
     * Konstruktor des Spielers, welcher die Variablen initialisiert und die Parameter annimmt.
     * Der Konstruktor initialisiert außerdem die vier Figuren des Spielers.
     *
     * @param playerTag Tag/Buchstabe des Spielers.
     * @param playerDice Würfel, der durch den Spieler verwendet wird.
     * @param playerStartPosition Startposition des Spielers auf dem Hauptfeld.
     */
    public LudoPlayer(String playerTag, LudoDice playerDice, int playerStartPosition) {
        this.playerTag = playerTag;
        this.playerDice = playerDice;
        this.playersHome = 4;
        this.playersGoal = new String[4];
        Arrays.fill(playersGoal, LudoField.FIELD_EMPTY); //Auffüllen der Zielfelder mit Leerzeichen
        this.playerStartPosition = playerStartPosition;
        for(int i = 0; i < playersHome; i++) playerFigures.add(new LudoFigure(-1)); //Initialisieren der Figuren
    }

    /**
     * Funktion, welche die Anzahl der Figuren innerhalb der Zielfelder des Spielers zurückgibt.
     *
     * @return Gibt die Anzahl der Spieler auf den Zielfeldern zurück.
     */
    public int getFiguresInGoal() {
        int figuresInGoal = 0;

        //Zielfelder werden überprüft
        for (String spot : playersGoal) {
            if (spot.equals(playerTag))
                figuresInGoal++;
        }

        //Anzahl wird zurückgegeben
        return figuresInGoal;
    }

    /**
     * Funktion, welche zurückgibt, ob sich alle Figuren des Spielers auf den Zielfeldern befinden.
     *
     * @return Gibt zurück, ob alle Figuren des Spielers sich im Ziel befinden (Sieg).
     */
    public boolean goalComplete() {
        for(String goalSlot : playersGoal) {
            if(goalSlot.equals(LudoField.FIELD_EMPTY))
                return false;
        }

        return true;
    }

    /**
     * Funktion, welche überprüft, ob alle Figuren des Spielers das Ziel erreichen können.
     * Dazu wird die Funktion {@link LudoFigure#isGoalReachable} der Figuren verwendet.
     *
     * @return Gibt zurück, ob alle Figuren des Spielers das Ziel erreichen können.
     */
    public boolean isGoalReachableByAll() {
        for(LudoFigure figure : playerFigures) {
            if(!figure.isGoalReachable(playerDice.getSmallestSide()))
                return false;
        }

        return true;
    }

}
