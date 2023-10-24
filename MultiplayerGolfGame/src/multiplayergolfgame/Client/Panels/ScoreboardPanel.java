package multiplayergolfgame.Client.Panels;

import java.awt.*;
import javax.swing.*;

import multiplayergolfgame.Client.ClientView;
import multiplayergolfgame.Shared.Player;

/**
 * A panel that displays the user's scores
 * @author James Eastwood
 */
public class ScoreboardPanel extends JPanel 
{
    private GridBagConstraints gbcScores;
    private JPanel scoresPanel;

    /**
     * 
     */
    public ScoreboardPanel()
    {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        scoresPanel = new JPanel();
        scoresPanel.setLayout(new GridBagLayout());
        gbcScores = new GridBagConstraints();
        gbcScores.fill = GridBagConstraints.BOTH;
        gbcScores.anchor = GridBagConstraints.PAGE_START;
        gbcScores.weighty = 1;
        gbcScores.ipadx = 10;
        gbcScores.ipady = 5;


        Insets inset = new Insets(0, 10, 0, 10);
        gbcScores.insets = inset;

        this.setBorder(BorderFactory.createTitledBorder("Scoreboard"));
        //JScrollPane scoreboardPane = new JScrollPane(scoresPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scoreboardPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(scoresPanel);
        this.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 660), new Dimension(0, 660)));
    }

    /**
     * Updates the scoreboard with the specified players
     * @param players the players to populate the scoreboard with
     */
    public void updateScoreboard(Player players[])
    {
        scoresPanel.removeAll();

        JLabel playerHeaderLabel = new JLabel("Player");
        playerHeaderLabel.setFont(ClientView.labelFont);
        JLabel scoreHeaderLabel = new JLabel("Score");
        scoreHeaderLabel.setFont(ClientView.labelFont);
        
        gbcScores.gridx = 0;
        gbcScores.gridwidth = 2;
        scoresPanel.add(playerHeaderLabel, gbcScores);
        gbcScores.gridx += 2;
        scoresPanel.add(scoreHeaderLabel, gbcScores);
        gbcScores.gridy++;
        
        gbcScores.gridx = 0;
        gbcScores.gridwidth = 4;
        scoresPanel.add(new JSeparator(JSeparator.HORIZONTAL), gbcScores);
        gbcScores.gridwidth = 1;
        gbcScores.gridy++;

        for(Player player : players)
        {
            JLabel colorLabel = new JLabel("█");
            colorLabel.setFont(ClientView.fieldFont);
            colorLabel.setForeground(player.getBallColor());

            JLabel playerLabel = new JLabel(player.getNickname());
            playerLabel.setFont(ClientView.fieldFont);
            JLabel scoreLabel = new JLabel(String.valueOf(player.getScore()));
            scoreLabel.setFont(ClientView.fieldFont);
            JLabel readyLabel = new JLabel();

            if(player.isReady())
                readyLabel.setText("⛳");
            else
                readyLabel.setText("");

            readyLabel.setFont(ClientView.fieldFont);

            gbcScores.gridx = 0;
            scoresPanel.add(colorLabel, gbcScores);
            gbcScores.gridx++;
            scoresPanel.add(playerLabel, gbcScores);
            gbcScores.gridx++;
            scoresPanel.add(scoreLabel, gbcScores);
            gbcScores.gridx++;
            scoresPanel.add(readyLabel, gbcScores);
            gbcScores.gridy++;
        }

        this.revalidate();
    }
}
