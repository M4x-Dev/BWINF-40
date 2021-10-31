package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Klasse, welche die Simulation eines "Mensch-Ärger-Dich-Nicht"-Spieles darstellt.
 * Diese umfasst ein Spielfeld und zwei gegenüberliegende Spieler, welche gegeneinander antreten.
 */
public class LudoSimulation {

    public static boolean debugOutput = false; //Ausgabe der einzelnen Schritte

    public final LudoPlayer playerA; //Erster Spieler
    public final LudoPlayer playerB; //Zweiter Spieler

    public final LudoField field = new LudoField(); //Spielfeld der Simulation

    /**
     * Konstruktor der Simulation, welche die Spieler initialisiert und ihnen die Würfel zuweist.
     *
     * @param diceA Würfel des ersten Spielers.
     * @param diceB Würfel des zweiten Spielers.
     */
    public LudoSimulation(LudoDice diceA, LudoDice diceB) {
        playerA = new LudoPlayer("A", diceA, LudoField.POSITION_PLAYER_A_START);
        playerB = new LudoPlayer("B", diceB, LudoField.POSITION_PLAYER_B_START);
    }

    /**
     * Funktion, welche die Simulation startet und die Spieler gegeneinander antreten lässt.
     * Diese Funktion setzt die Parameter zurück und setzt jeweils die erste Figur auf das A-Feld der Spieler.
     * Falls einer der Würfel ungültig ist, wird das Spiel an dieser Stelle bereits abgebrochen.
     * Ein Würfel ist ungültig, wenn er die Augenzahl 6 nicht beeinhaltet, da so die Figuren die B-Felder gar nicht verlassen können.
     * Am Ende des Vorganges gibt diese Funktion den Gewinner des Spieles oder null, wenn das Spiel unentschieden endete, zurück.
     * Ein Spiel kann nur unentschieden enden, wenn beide Würfel ungültig sind oder beide Spieler das Ziel mit ihren Spielfiguren nicht erreicht werden können.
     *
     * @param startWithA Bestimmt, welcher Spieler beginnen wird.
     *
     * @return Gibt den Gewinner des Spieles zurück.
     */
    public LudoPlayer simulate(boolean startWithA) {
        if(debugOutput) System.out.println("Starting simulation...");

        //Platzieren der ersten Figur des ersten Spielers auf dem Spielfeld
        playerA.playersHome--;
        playerA.playerTurns = 0;
        playerA.playerFigures.get(0).currentPosition = LudoField.POSITION_PLAYER_A_START;
        field.mainField[LudoField.POSITION_PLAYER_A_START] = playerA.playerTag;

        //Platzieren der ersten Figur des zweiten Spielers auf dem Spielfeld
        playerB.playersHome--;
        playerB.playerTurns = 0;
        playerB.playerFigures.get(0).currentPosition = LudoField.POSITION_PLAYER_B_START;
        field.mainField[LudoField.POSITION_PLAYER_B_START] = playerB.playerTag;

        //Abbrechen des Spieles, falls einer der Würfel ungültig ist
        boolean diceAValid = playerA.playerDice.validate();
        boolean diceBValid = playerB.playerDice.validate();

        if(!diceAValid && !diceBValid) return null;

        if(!diceAValid) return playerB;
        if(!diceBValid) return playerA;

        //Starten der rekursiven Schleife des Algorithmuses
        return makeTurn(startWithA ? playerA : playerB);
    }

    /**
     * Funktion, welche den Zug eines Spielers ausführt.
     * Diese Funktion wird so lange rekursiv aufgerufen, bis ein Gewinner des Spielers ausgemacht werden konnte.
     * Dazu geht der Algorithmus wie folgt vor:
     * <br>1. Würfelzahl zufällig bestimmen
     * <br>2. Wenn eine 6 gewürfelt wurde und sich noch Spieler auf den B-Feldern befinden
     * <br>  - Wenn das A-Feld des Spielers leer ist, dann wird ein Spieler aus dem B-Feld dort platziert --> <b>Rekursion</b>
     * <br>  - Wenn sich ein Gegner auf dem A-Feld befindet, wird dieser besiegt und ein Spieler aus dem B-Feld dort platziert --> <b>Rekursion</b>
     * <br>  - Wenn sich ein eigener Spieler auf dem A-Feld befindet, dann wird der Algorithmus fortgesetzt, da der blockierende Spieler das Feld erst verlassen muss
     * <br>
     * <br>--- HAUPTSCHLEIFE ---
     * <br>3. Wenn das Ziel durch mindestens einen Spieler nicht mehr erreichbar ist
     * <br>Note: Bei bestimmten Würfeln kann es unter Umständen passieren, dass das Ziel nicht mehr erreichbar ist, da man mit keiner Augenzahl innerhalb der Zielfelder landen wird.
     * <br>      In diesem Fall muss das Spiel abgebrochen werden, da der Algorithmus ansonsten in einer unendlichen Schleife landet (bis StackOverflow).
     * <br>  - Wenn beide Spieler das Ziel nicht erreichen können, dann endet das Spiel unentschieden
     * <br>  - Wenn der gegnerische Spieler das Ziel nicht erreichen kann, gewinnt der momentane Spieler
     * <br>  - Wenn der momentane Spieler das Ziel nicht erreichen kann, gewinnt der gegnerische Spieler
     * <br>4. Wenn sich ein Spieler innerhalb des Zieles befindet, dann kann dieser noch weiter bis zum letzten Zielfeld bewegt werden kann
     * <br>  - Wenn das nächste Zielfeld leer ist, wird die Figur dahin bewegt --> <b>Rekursion</b>
     * <br>  - Wenn sich eine Figur auf dem Zielfeld befindet, dann wird der Algorithmus fortgesetzt
     * <br>5. Wenn der Spieler mit diesem Zug seine Runde vervollständigt und somit das Ziel erreichen muss dann kann dieser in das Ziel bewegt werden
     * <br>  - Wenn das Zielfeld leer ist, wird die Figur dahin bewegt --> <b>Rekursion</b>
     * <br>  - Wenn sich eine Figur auf dem Zeilfeld befindet, dann wird der Algorithmus forgesetzt
     * <br>  5a. An dieser Stelle wird überprüft, ob ein Spieler alle Figuren auf den Zielfeldern hat
     * <br>    - Wenn sich alle gegnerischen Figuren auf den Zielfeldern befinden, hat der Gegner das Spiel gewonnen
     * <br>    - Wenn sich alle eigenen Figuren auf den Zielfeldern befinden, hat der momentane Spieler gewonnen
     * <br>    - Wenn sich alle Figuren beider Spieler auf den Zielfeldern befinden, dann endet das Spiel unentschieden (Aufgrund der Tatsache, dass beide Spieler nacheinander antreten, sollte dieser Fall jedoch niemals eintreten)
     * <br>6. Die Figur wird um die Augenzahl auf dem Spielfeld bewegt
     * <br>  - Wenn das Spielfeld an dieser Stelle leer ist, dann wird die Figur an diese Stelle bewegt --> <b>Rekursion</b>
     * <br>  - Wenn sich an dieser Stelle eine gegnerische Figur befindet, dann wird diese besiegt und die eigene Figur bewegt --> <b>Rekursion</b>
     * <br>  - Wenn sich eine eigene Figur auf dem Feld befindet, dann wird der Algorithmus fortgesetzt, da diese Figur zuerst verschoben werden muss
     * <br>7. <b>Rekursion</b>
     *
     * In dieser Hauptschleife des Algorithmus gibt es eine definierte Abfolge, in welcher die Figuren auf dem Feld bewegt werden ({@link #getMoveList}).
     *
     * @param player Spieler, welcher am Zug ist.
     *
     * @return Gibt den Gewinner des Spieles zurück.
     */
    private LudoPlayer makeTurn(LudoPlayer player) {
        int moves = player.playerDice.roll(); //Würfel wird geworfen
        player.playerTurns++;

        if(debugOutput) {
            //Spielfeld wird ausgegeben
            field.printField();
            System.out.println("Player " + player.playerTag + " will move " + moves + " spaces.");
            System.out.println("Figures on field:");
            for(int i = 0; i < player.playerFigures.size(); i++) System.out.println("Figure: " + player.playerFigures.get(i).currentPosition + " (" + player.playerFigures.get(i).distanceFromStart + ")");
            System.out.println();

            System.out.println("Goal of player A: " + Arrays.toString(playerA.playersGoal));
            System.out.println("Goal of player B: " + Arrays.toString(playerB.playersGoal));
        }

        //Wenn eine 6 gewürfelt wurde und sich noch Figuren auf den B-Feldern befinden
        if(moves == 6 && player.playersHome > 0) {
            if(field.mainField[player.playerStartPosition].equals(LudoField.FIELD_EMPTY)) {
                //Wenn sich auf dem A-Feld keine Figur befindet
                player.playersHome--;

                for(LudoFigure figure : player.playerFigures) {
                    if(figure.currentPosition == -1) {
                        figure.currentPosition = player.playerStartPosition;
                        break;
                    }
                }

                field.mainField[player.playerStartPosition] = player.playerTag;
                return makeTurn(player);
            } else if(field.mainField[player.playerStartPosition].equals(getOtherPlayer(player).playerTag)) {
                //Wenn sich auf dem A-Feld eine gegnerische Figur befindet
                player.playersHome--;

                //Eine Figur von den B-Feldern wird auf das A-Feld verschoben
                for(LudoFigure figure : player.playerFigures) {
                    if(figure.currentPosition == -1) {
                        figure.currentPosition = player.playerStartPosition;
                        break;
                    }
                }

                //Die blockierende gegnerische Figur wird besiegt und auf deren B-Feld zurück verschoben
                for(LudoFigure figure : getOtherPlayer(player).playerFigures) {
                    if(figure.currentPosition == player.playerStartPosition) {
                        figure.returnToHome();
                        getOtherPlayer(player).playersHome++;
                        break;
                    }
                }

                //Die Position auf dem Hauptfeld wird aktualisiert
                field.mainField[player.playerStartPosition] = player.playerTag;
                return makeTurn(player); //Rekursion, momentaner Spieler ist erneut am Zug
            }
        }

        //Durchlaufen aller Spielfiguren des Spielers in einer festgelegten Reihenfolge
        for(LudoFigure targetFigure : getMoveList(player)) {
            //Berechnen der finalen Position, wenn die Spielfigur bewegt wird
            int finalPosition = targetFigure.currentPosition + moves;
            finalPosition = finalPosition > field.mainField.length - 1 ? finalPosition - (field.mainField.length - 1) : finalPosition;

            //Kann der eigene/gegnerische Spieler das Ziel überhaupt erreichen?
            boolean ownGoalReachable = targetFigure.isGoalReachable(player.playerDice.getSmallestSide());
            boolean enemyGoalReachable = getOtherPlayer(player).isGoalReachableByAll();

            //Wenn beide Spieler das Ziel nicht mehr erreichen können, endet das Spiel unentschieden, bzw. der Spieler mit den meisten Figuren im Ziel gewinnt
            if(!ownGoalReachable && !enemyGoalReachable) {
                if(player.getFiguresInGoal() == getOtherPlayer(player).getFiguresInGoal()) return null;
                return player.getFiguresInGoal() > getOtherPlayer(player).getFiguresInGoal() ? player : getOtherPlayer(player);
            } else if(!ownGoalReachable) return getOtherPlayer(player); //Der gegnerische Spieler gewinnt, wenn nur der eigene Spieler das Zielfeld nicht mehr erreichen kann

            //Wenn sich die Spielfigur innerhalb der Zielfelder befindet
            if(targetFigure.currentPosition < 0 || targetFigure.distanceFromStart > LudoField.ROUND_COMPLETE) {
                //Wenn die Figur jedoch auf dem letzten Feld ist, dann wird zur nächsten Figur gesprungen
                if(targetFigure.distanceFromStart + moves >= LudoField.ROUND_COMPLETE + player.playersGoal.length)
                    continue;

                //Neue Position innerhalb der Zielfelder wird berechnet
                int currentGoalPosition = targetFigure.distanceFromStart - LudoField.ROUND_COMPLETE;
                int positionInGoal = (targetFigure.distanceFromStart + moves) - LudoField.ROUND_COMPLETE;

                //Wenn das Zielfeld leer ist, dann wird die Figur bewegt
                if(player.playersGoal[positionInGoal].equals(LudoField.FIELD_EMPTY)) {
                    player.playersGoal[currentGoalPosition] = LudoField.FIELD_EMPTY;
                    player.playersGoal[positionInGoal] = player.playerTag;
                    targetFigure.moveToGoal(positionInGoal);
                    return makeTurn(moves == 6 ? player : getOtherPlayer(player));
                } else continue; //Andernfalls wird zur nächsten Figur gesprungen
            }

            //Wenn sich die Spielfigur kurz vor den Zielfeldern befindet
            if(targetFigure.distanceFromStart + moves >= LudoField.ROUND_COMPLETE) {
                //Wenn die Figur jedoch über das Ziel hinausspringen würde, dann wird zur nächsten Figur gesprungen
                if(targetFigure.distanceFromStart + moves >= LudoField.ROUND_COMPLETE + player.playersGoal.length)
                    continue;

                //Position innerhalb der Zielfelder wird berechnet
                int positionInGoal = (targetFigure.distanceFromStart + moves) - LudoField.ROUND_COMPLETE;

                //Wenn das Zielfeld leer ist, dann wird die Figur bewegt
                if(player.playersGoal[positionInGoal].equals(LudoField.FIELD_EMPTY)) {
                    //Figur wird auf das Zielfeld verschoben
                    player.playersGoal[positionInGoal] = player.playerTag;
                    field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                    targetFigure.moveToGoal(positionInGoal);

                    //Überprüfen, ob einer der beiden Spieler gewonnen hat
                    if(player.playersHome == 0 && player.goalComplete()) {
                        if(getOtherPlayer(player).goalComplete() && getOtherPlayer(player).goalComplete()) {
                            if(debugOutput) System.out.println("DRAW! Both players have all their figures in the goals.");
                            return null;
                        }

                        if(debugOutput) System.out.println("Player " + player.playerTag + " won the game! Turns needed: " + player.playerTurns);
                        return player;
                    }

                    //Weitergeben des Zuges
                    return makeTurn(moves == 6 ? player : getOtherPlayer(player));
                } else continue; //Andernfalls wird zur nächsten Figur gesprungen
            }

            if(field.mainField[finalPosition].equals(LudoField.FIELD_EMPTY)) {
                //Wenn das Feld, welches die Figur erreichen würde, leer ist, wird die Figur auf das Feld bewegt
                field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                field.mainField[finalPosition] = player.playerTag;
                targetFigure.move(finalPosition, moves);

                //Zug wird weitergegeben
                return makeTurn(moves == 6 ? player : getOtherPlayer(player));
            } else if(field.mainField[finalPosition].equals(getOtherPlayer(player).playerTag)) {
                //Wenn sich eine gegnerische Figur auf dem Feld befindet, so wird diese besiegt und muss auf deren B-Felder zurück
                field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                field.mainField[finalPosition] = player.playerTag;
                targetFigure.move(finalPosition, moves);

                //Die Figur des Gegners wird auf das B-Feld zurück bewegt
                for(LudoFigure figure : getOtherPlayer(player).playerFigures) {
                    if(figure.currentPosition == finalPosition) {
                        figure.returnToHome();
                        getOtherPlayer(player).playersHome++;
                        break;
                    }
                }

                //Zug wird weitergegeben
                return makeTurn(moves == 6 ? player : getOtherPlayer(player));
            }
        }

        //Zug wird weitergegeben
        return makeTurn(moves == 6 ? player : getOtherPlayer(player));
    }

    /**
     * Funktion, welche eine priorisierte Liste erstellt, nach welcher die Figuren in der Hauptschleife des Algorithmus abgearbeitet werden sollen.
     * <br>Zuerst werden Figuren abgearbeitet, welche sich auf dem A-Feld befinden, da dieses zuerst geräumt werden muss.
     * <br>Danach werden zuerst die Figuren auf den Zielfeldern und dann alle restlichen Figuren (nach Abstand vom Start absteigend sortiert) abgearbeitet.
     *
     * @param player Spieler, welcher momentan am Zug ist.
     *
     * @return Gibt die priorisierte Liste zurück.
     */
    private ArrayList<LudoFigure> getMoveList(LudoPlayer player) {
        ArrayList<LudoFigure> finalList = new ArrayList<>();

        //Figuren auf dem A-Feld haben die höchste Priorität, da diese so bald wie möglich bewegt werden müssen
        for(LudoFigure figure : player.playerFigures) {
            if(figure.currentPosition == player.playerStartPosition)
                finalList.add(figure);
        }

        //Anschließend werden die verbleibenden Figuren der Position nach absteigend sortiert
        //Es werden die Figuren, welche am weitersten sind, priorisiert
        player.playerFigures.sort(Comparator.comparingInt(figure -> figure.distanceFromStart));
        Collections.reverse(player.playerFigures);

        //Restliche Figuren, welche sich nicht auf dem A-Feld oder auf den B-Feldern befinden
        for(LudoFigure remainingFigure : player.playerFigures) {
            if(remainingFigure.currentPosition != player.playerStartPosition && remainingFigure.currentPosition != -1)
                finalList.add(remainingFigure);
        }

        //Priorisierte Liste wird zurückgegeben
        return finalList;
    }

    /**
     * Funktion, welche den jeweils gegenerischen Spieler des momentanen Spielers (am Zug) zurückgibt.
     * Wenn also momentan Spieler A am Zug ist, wird Spieler B zurückgegeben und umgekehrt.
     *
     * @param currentPlayer Spieler, welcher momentan am Zug ist.
     *
     * @return Gibt den momentanen gegnerischen Spieler zurück (aus Perspektive des ziehenden Spielers).
     */
    public LudoPlayer getOtherPlayer(LudoPlayer currentPlayer) {
        return currentPlayer == playerA ? playerB : playerA;
    }

}