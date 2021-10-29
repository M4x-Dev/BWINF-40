package simulation;

public class LudoSimulation {

    private final int maxTurns = 10;
    private int currentTurn = 0;

    public final LudoPlayer playerA;
    public final LudoPlayer playerB;

    public final LudoField field = new LudoField();

    public LudoSimulation(LudoDice diceA, LudoDice diceB) {
        playerA = new LudoPlayer("A", diceA);
        playerB = new LudoPlayer("B", diceB);
    }

    public void start(boolean startWithA) {
        System.out.println("Starting simulation...");

        playerA.playerFigures.get(0).currentPosition = LudoField.POSITION_PLAYER_A_START;
        field.mainField[LudoField.POSITION_PLAYER_A_START] = playerA.playerTag;

        playerB.playerFigures.get(0).currentPosition = LudoField.POSITION_PLAYER_B_START;
        field.mainField[LudoField.POSITION_PLAYER_B_START] = playerB.playerTag;

        field.printField();

        makeTurn(startWithA ? playerA : playerB);
    }

    private void makeTurn(LudoPlayer player) {
        if(currentTurn > maxTurns) return;

        int moves = player.playerDice.roll();
        currentTurn++;

        for(int i = 0; i < player.playerFigures.size(); i++) {
            if(player.playerFigures.get(i).currentPosition != -1) {
                LudoFigure targetFigure = player.playerFigures.get(i);
                int finalPosition = targetFigure.currentPosition + moves;
                finalPosition = finalPosition > field.mainField.length - 1 ? finalPosition - (field.mainField.length - 1) : finalPosition;

                if(field.mainField[finalPosition].equals(LudoField.FIELD_EMPTY)) {
                    field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                    field.mainField[finalPosition] = player.playerTag;

                    targetFigure.move(finalPosition, moves);
                    makeTurn(moves == 6 ? player : getOtherPlayer(player));
                    break;
                } else if(!field.mainField[finalPosition].equals(player.playerTag)) {
                    field.mainField[targetFigure.currentPosition] = LudoField.FIELD_EMPTY;
                    field.mainField[finalPosition] = player.playerTag;

                    targetFigure.move(finalPosition, moves);
                    for(int a = 0; a < getOtherPlayer(player).playerFigures.size(); a++) {
                        if(getOtherPlayer(player).playerFigures.get(a).currentPosition == finalPosition) {
                            getOtherPlayer(player).playerFigures.get(a).returnToHome();
                            getOtherPlayer(player).playersHome++;
                            break;
                        }
                    }
                    makeTurn(moves == 6 ? player : getOtherPlayer(player));
                    break;
                }
            }
        }

        System.out.println("Move by player " + player.playerTag + " with " + moves + " steps has been completed.");
        field.printField();
    }

    private LudoPlayer getOtherPlayer(LudoPlayer currentPlayer) {
        return currentPlayer == playerA ? playerB : playerA;
    }

}