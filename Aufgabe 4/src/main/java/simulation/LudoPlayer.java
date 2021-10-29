package simulation;

import java.util.ArrayList;

public class LudoPlayer {

    public final String playerTag;
    public int playersHome;
    public final LudoDice playerDice;
    public final ArrayList<LudoFigure> playerFigures = new ArrayList<>();

    public LudoPlayer(String playerTag, LudoDice playerDice) {
        this.playerTag = playerTag;
        this.playerDice = playerDice;
        this.playersHome = 4;
        for(int i = 0; i < playersHome; i++) playerFigures.add(new LudoFigure(-1));
    }

}
