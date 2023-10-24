package multiplayergolfgame.Client.Panels;

import java.util.Timer;

import javax.swing.*;

import multiplayergolfgame.Client.GolfClientController;
import multiplayergolfgame.Client.util.ScoreboardUpdater;
import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.Player;

/**
 *
 * @author James Eastwood
 */
public class LobbyView extends JPanel 
{

    private ScoreboardUpdater scoreboardUpdater;
    private Timer scoreboardTimer;
    
    private ScoreboardPanel scoreboardPanel;
    private JButton readyButton;

    /**
     * Creates a lobby view object with the specified parent controller
     * @param controller
     */
    public LobbyView(GolfClientController controller)
    {   
        scoreboardPanel = new ScoreboardPanel();
        scoreboardUpdater = new ScoreboardUpdater(scoreboardPanel);
        readyButton = new JButton("Ready!");

        readyButton.addActionListener(e -> 
        {
            controller.getPlayer().setReady(true);
            controller.sendPacketToServer(new MathVector(0, 0));
        });

        this.add(scoreboardPanel);
        this.add(readyButton);
    }

    /**
     * Updates the scores on the scoreboard based on the players
     * @param players the players to update the scoreboard with
     */
    public void updateScoreboard(Player players[])
    {
        if(scoreboardTimer == null)
        {
            scoreboardPanel.updateScoreboard(players);
            this.scoreboardTimer = new Timer();
            this.scoreboardTimer.scheduleAtFixedRate(scoreboardUpdater, 0, 500);
        }
        
        this.scoreboardUpdater.setPlayerData(players);
    }
    
}
