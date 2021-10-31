package simulation;

/**
 * Klasse, welche die Spielfigur eines Spielers in dem Spiel darstellt.
 * Ein Spieler besitzt vier dieser Figuren.
 */
public class LudoFigure {

    public int currentPosition; //Momentane Position im Spielfeld (Index im Array)
    public int distanceFromStart; //Momentane Distanz vom Startpunkt

    /**
     * Konstruktor der Spielfigur, welcher die momentane Position initialisiert.
     *
     * @param currentPosition Startposition der Figur.
     */
    public LudoFigure(int currentPosition) {
        this.currentPosition = currentPosition;
        this.distanceFromStart = 0;
    }

    /**
     * Methode, welche die Spielfigur auf dem Spielfeld bewegt.
     * Dabei wird die momentane Position und der Abstand vom Startpunkt verändert.
     *
     * @param newPosition Neue Position auf dem Spielfeld.
     * @param delta Schritte, welche die Figur gegangen ist.
     */
    public void move(int newPosition, int delta) {
        this.currentPosition = newPosition;
        this.distanceFromStart += delta;
    }

    /**
     * Methode, welche die Spielfigur in eines der Zielfelder setzt.
     * Dabei wird die momentane Position im negativen Bereich verwendet, um dies Darzustellen.
     * Der Wert -1 wird nicht verwendet, da dieser die Position der B-Felder ist.
     * <br>Somit folgt:
     * <br>Zielfeld 1: currentPosition = -2 & distanceFromStart = 41
     * <br>Zielfeld 2: currentPosition = -3 & distanceFromStart = 42
     * <br>Zielfeld 3: currentPosition = -4 & distanceFromStart = 43
     * <br>Zielfeld 4: currentPosition = -5 & distanceFromStart = 44
     *
     * @param positionInGoal Position innerhalb des Zielfeldes (0 - 3).
     */
    public void moveToGoal(int positionInGoal) {
        this.currentPosition = - (positionInGoal + 1);
        this.distanceFromStart = LudoField.ROUND_COMPLETE + positionInGoal;
    }

    /**
     * Methode, welche eine Spielfigur in die B-Felder zurückbringt, nachdem diese von einer gegnerischen Figur besiegt wurde.
     * Somit wird die Position auf -1 gesetzt und die Distanz vom Start beträgt dann wieder 0.
     */
    public void returnToHome() {
        this.currentPosition = -1;
        this.distanceFromStart = 0;
    }

    /**
     * Funktion, welche überprüft, ob das Ziel vom momentanen Standpunkt der Figur aus überhaupt noch erreichbar ist.
     * Diese Funktion gibt zum Beispiel false zurück, wenn die kleinste Augenzahl 5 ist und die Figur ein Feld vor dem Ziel ist.
     * Denn dann ist das Ziel für diese Figur gar nicht mehr erreichbar und das Spiel muss abgebrochen werden.
     *
     * @param smallestMove Kleinste Augenzahl auf dem Würfel des übergeordneten Spielers.
     *
     * @return Gibt zurück, ob das Ziel vom momentanen Standpunkt aus überhaupt noch erreichbar ist.
     */
    public boolean isGoalReachable(int smallestMove) {
        return distanceFromStart + smallestMove < LudoField.ROUND_MAXIMUM;
    }

}
