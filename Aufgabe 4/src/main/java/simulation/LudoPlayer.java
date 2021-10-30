package simulation;

import java.util.ArrayList;
import java.util.Arrays;

public class LudoPlayer {

    public final String playerTag;
    public final String[] playersGoal;

    public final int playerStartPosition;
    public int playersHome;
    public int playerTurns;

    public final LudoDice playerDice;
    public final ArrayList<LudoFigure> playerFigures = new ArrayList<>();

    public LudoPlayer(String playerTag, LudoDice playerDice, int playerStartPosition) {
        this.playerTag = playerTag;
        this.playerDice = playerDice;
        this.playersHome = 4;
        this.playersGoal = new String[4];
        Arrays.fill(playersGoal, LudoField.FIELD_EMPTY);
        this.playerStartPosition = playerStartPosition;
        for(int i = 0; i < playersHome; i++) playerFigures.add(new LudoFigure(-1));
    }

    public boolean goalComplete() {
        for(String goalSlot : playersGoal) {
            if(goalSlot.equals(LudoField.FIELD_EMPTY))
                return false;
        }

        return true;
    }

}
