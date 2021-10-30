package simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LudoSimulation {

    public final LudoPlayer playerA;
    public final LudoPlayer playerB;

    public final LudoField field = new LudoField();

    public LudoSimulation(LudoDice diceA, LudoDice diceB) {
        playerA = new LudoPlayer("A", diceA, LudoField.POSITION_PLAYER_A_START);
        playerB = new LudoPlayer("B", diceB, LudoField.POSITION_PLAYER_B_START);
    }

    public LudoPlayer simulate(boolean startWithA) {
        System.out.println("Starting simulation...");

        playerA.playersHome--;
        playerA.playerFigures.get(0).currentPosition = LudoField.POSITION_PLAYER_A_START;
        field.mainField[LudoField.POSITION_PLAYER_A_START] = playerA.playerTag;

        playerB.playersHome--;
        playerB.playerFigures.get(0).currentPosition = LudoField.POSITION_PLAYER_B_START;
        field.mainField[LudoField.POSITION_PLAYER_B_START] = playerB.playerTag;

        field.printField();

        return makeTurn(startWithA ? playerA : playerB);
    }

    private LudoPlayer makeTurn(LudoPlayer player) {
        int moves = player.playerDice.roll();
        player.playerTurns++;

        field.printField();
        System.out.println("Player " + player.playerTag + " will move " + moves + " spaces.");
        System.out.println("Figures on field:");
        for(int i = 0; i < player.playerFigures.size(); i++) System.out.println("Figure: " + player.playerFigures.get(i).currentPosition + " (" + player.playerFigures.get(i).distanceFromStart + ")");
        System.out.println();

        System.out.println("Goal of player A: " + Arrays.toString(playerA.playersGoal));
        System.out.println("Goal of player B: " + Arrays.toString(playerB.playersGoal));

        if(player.playersHome == 0 && player.goalComplete()) {
            if(getOtherPlayer(player).playersHome == 0 && getOtherPlayer(player).goalComplete()) {
                System.out.println("DRAW! Both players have all their figures in the goals.");
                return player;
            }

            System.out.println("Player " + player.playerTag + " won the game! Turns needed: " + player.playerTurns);
            return player;
        }

        if(moves == 6 && player.playersHome > 0) {
            if(field.mainField[player.playerStartPosition].equals(LudoField.FIELD_EMPTY)) {
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
                player.playersHome--;

                for(LudoFigure figure : player.playerFigures) {
                    if(figure.currentPosition == -1) {
                        figure.currentPosition = player.playerStartPosition;
                        break;
                    }
                }

                for(LudoFigure figure : getOtherPlayer(player).playerFigures) {
                    if(figure.currentPosition == player.playerStartPosition) {
                        figure.returnToHome();
                        getOtherPlayer(player).playersHome++;
                        break;
                    }
                }

                field.mainField[player.playerStartPosition] = player.playerTag;
                return makeTurn(player);
            }
        }

        for(LudoFigure targetFigure : getMoveList(player)) {
            int finalPosition = targetFigure.currentPosition + moves;
            finalPosition = finalPosition > field.mainField.length - 1 ? finalPosition - (field.mainField.length - 1) : finalPosition;

            if(targetFigure.distanceFromStart + moves >= LudoField.ROUND_COMPLETE) {
                if(targetFigure.distanceFromStart + moves >= LudoField.ROUND_COMPLETE + player.playersGoal.length)
                    continue;

                int positionInGoal = (targetFigure.distanceFromStart + moves) - LudoField.ROUND_COMPLETE;
                if(player.playersGoal[positionInGoal].equals(LudoField.FIELD_EMPTY)) {
                    player.playersGoal[positionInGoal] = player.playerTag;
                    field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                    targetFigure.moveToGoal(positionInGoal);
                    return makeTurn(moves == 6 ? player : getOtherPlayer(player));
                }
            }

            if(field.mainField[finalPosition].equals(LudoField.FIELD_EMPTY)) {
                field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                field.mainField[finalPosition] = player.playerTag;

                targetFigure.move(finalPosition, moves);
                return makeTurn(moves == 6 ? player : getOtherPlayer(player));
            } else if(field.mainField[finalPosition].equals(getOtherPlayer(player).playerTag)) {
                field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                field.mainField[finalPosition] = player.playerTag;

                targetFigure.move(finalPosition, moves);
                for(LudoFigure figure : getOtherPlayer(player).playerFigures) {
                    if(figure.currentPosition == finalPosition) {
                        figure.returnToHome();
                        getOtherPlayer(player).playersHome++;
                        break;
                    }
                }

                return makeTurn(moves == 6 ? player : getOtherPlayer(player));
            }
        }

        return makeTurn(moves == 6 ? player : getOtherPlayer(player));
    }

    private ArrayList<LudoFigure> getMoveList(LudoPlayer player) {
        ArrayList<LudoFigure> finalList = new ArrayList<>();

        for(LudoFigure figure : player.playerFigures) {
            if(figure.currentPosition == player.playerStartPosition)
                finalList.add(figure);
        }

        player.playerFigures.sort(Comparator.comparingInt(figure -> figure.distanceFromStart));
        Collections.reverse(player.playerFigures);

        for(LudoFigure remainingFigure : player.playerFigures) {
            if(remainingFigure.currentPosition != player.playerStartPosition && remainingFigure.currentPosition > -1 && remainingFigure.currentPosition <= 40)
                finalList.add(remainingFigure);
        }

        return finalList;
    }

    private LudoPlayer getOtherPlayer(LudoPlayer currentPlayer) {
        return currentPlayer == playerA ? playerB : playerA;
    }

}