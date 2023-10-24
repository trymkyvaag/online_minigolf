package multiplayergolfgame.Client.util;

import java.util.TimerTask;

import multiplayergolfgame.Client.Panels.ScoreboardPanel;
import multiplayergolfgame.Shared.Player;

/**
 * Updates scoreboard on set interval based on timer 
 * @author James Eastwood
 */

public class ScoreboardUpdater extends TimerTask
{
    private ScoreboardPanel panel;
    private Player playerData[];

    /**
     * Creates a scoreboard updater with the specified panel
     * @param panel
     */
    public ScoreboardUpdater(ScoreboardPanel panel)
    {
        this.panel = panel;
    }

    /**
     * Sets the player data to destribute out the to the scoreboard
     * @param players
     */
    public void setPlayerData(Player players[])
    {
        this.playerData = players;
    }

    @Override
    public void run() 
    {
        if(playerData != null)
            this.panel.updateScoreboard(this.playerData);
    }
    
}
