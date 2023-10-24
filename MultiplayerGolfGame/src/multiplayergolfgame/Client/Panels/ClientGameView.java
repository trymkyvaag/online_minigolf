package multiplayergolfgame.Client.Panels;

import java.util.HashMap;
import java.util.Timer;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import java.awt.geom.AffineTransform;

import multiplayergolfgame.Client.GolfClientController;
import multiplayergolfgame.Client.util.ScoreboardUpdater;
import multiplayergolfgame.Shared.Ball;
import multiplayergolfgame.Shared.ClientBall;
import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.Player;
import multiplayergolfgame.framework.GameObject;
import multiplayergolfgame.framework.GameSimulation;

/**
 * THe CLient game view is the view the user sees when playing the game
 * @author James Eastwood
 */
public class ClientGameView extends JPanel
{
    private GolfClientController controller;

    private ScoreboardPanel scoreboardPanel;
    private GameSimulation gameSimulation;
    private HashMap<Player, ClientBall> clientBalls;
    private ClientBall playerBall;

    private ScoreboardUpdater scoreboardUpdater;
    private Timer scoreboardTimer;

    /**
     * Creates a client game viwe
     * @param controller the parent controller
     */
    public ClientGameView(GolfClientController controller)
    {
        this.controller = controller;

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(0x979ca4));
        this.scoreboardPanel = new ScoreboardPanel();
        this.gameSimulation = new GameSimulation(0, 250.0, true);
        this.clientBalls = new HashMap<>();

        this.scoreboardUpdater = new ScoreboardUpdater(scoreboardPanel);
        /* update the scoreboard every second */

        this.gameSimulation.addMouseListener(new VelocityListener());

        this.add(this.scoreboardPanel, BorderLayout.WEST);
        this.add(this.gameSimulation.getPanel(), BorderLayout.CENTER);
    }

    /**
     * Starts the simulation of the game
     */
    public void startSimulation()
    {
        gameSimulation.run();
    }

    /**
     * Creates a ball from the specified player (usually recieved from a packet)
     * @param playerFromPacket the player to create a ball for
     */
    public void createBall(Player playerFromPacket)
    {
        ClientBall clientBall = new ClientBall(playerFromPacket.getBall(), playerFromPacket.getBallColor());
        clientBalls.put(playerFromPacket, clientBall);
        gameSimulation.addGameObject(clientBall);
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
            this.scoreboardTimer.scheduleAtFixedRate(scoreboardUpdater, 0, 1000);
        }
        scoreboardUpdater.setPlayerData(players);
    }

    /**
     * Update the client balls of the specified players
     * @param players the array of balls whose balls should be updated
     */
    public void updateBalls(Player players[])
    {
        updateScoreboard(players);

        for(Player playerFromPacket : players)
        {
            boolean packetPlayerFound = false;

            for(Player player : clientBalls.keySet())
            {
                if(playerFromPacket.equals(player))
                {
                    /* Update the player */
                    player.update(playerFromPacket);

                    if(!player.isReady()) /*Meaning they haven't completed the hole */
                    {
                        if(this.gameSimulation.containsGameObject(clientBalls.get(player)))
                            updateBall(player, playerFromPacket.getBall());
                        else
                            createBall(playerFromPacket);
                    }
                    /*The player completed the hole, remove their ball */
                    else if(player.isReady() && gameSimulation.containsGameObject(clientBalls.get(player)))
                    {
                        System.out.printf("[Client] %s completed the hole. Removing their ball.\n", player);
                        gameSimulation.removeGameObject(clientBalls.get(player));
                    }
                    packetPlayerFound = true;
                    break;
                }
            }

            if(!packetPlayerFound)
            {
                createBall(playerFromPacket);
            }
        }
    }

    /**
     * Gets the currently loaded level ID
     * @return the level ID, as an integer
     */
    public int getLevelID()
    {
        return gameSimulation.getLevelID();
    }

    /**
     * Loads a new level based on the specified level id
     * @param levelID the level id to load
     */
    public void setLevelID(int levelID)
    {
        this.controller.getPlayer().setReady(false);

        this.remove(this.gameSimulation.getPanel());
        gameSimulation = new GameSimulation(levelID, 250.0, true);
        this.add(this.gameSimulation.getPanel(), BorderLayout.CENTER);
        
        clientBalls.clear();
        playerBall = null;

        this.gameSimulation.addMouseListener(new VelocityListener());
    }

    /* Update the position of a specified ball, and updates the playerball if equal */
    private void updateBall(Player player, Ball updatedBall)
    {
        Transform transform = clientBalls.get(player).getTransform();
        transform.setTranslation(updatedBall.getPosition().x - Ball.BALL_RADIUS, updatedBall.getPosition().y - Ball.BALL_RADIUS);
        clientBalls.get(player).setTransform(transform);

        /* The Player we are updating is the client */
        if(playerBall == null && controller.getPlayer().equals(player))
        {
            playerBall = clientBalls.get(player);
        }

    }

    /* class that aids in launching a player's ball */
    private class VelocityListener extends MouseInputAdapter
    {
        private Point srtPnt;
        private Point endPnt;

        private VelocityLine lineObject;

        public VelocityListener()
        {
            srtPnt = new Point();
            endPnt = new Point();
            lineObject = new VelocityLine();
            lineObject.translate(new Vector2(-100, -100));
        }

        @Override
        public void mousePressed(MouseEvent e) 
        {
            srtPnt.x = e.getX();
            srtPnt.y = e.getY();
            endPnt.x = e.getX();
            endPnt.y = e.getY();

            gameSimulation.addGameObject(lineObject);
        }

        @Override
        public void mouseDragged(MouseEvent e) 
        {
            endPnt.x = e.getX();
            endPnt.y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            endPnt.x = e.getX();
            endPnt.y = e.getY();

            gameSimulation.removeGameObject(lineObject);

            double dx = 0.04 * (srtPnt.x - endPnt.x);
            double dy = - (0.04 * (srtPnt.y - endPnt.y));
            
            if (dx > 4.0)
                dx = 4.0;
            else if(dx < -4.0)
                dx = -4.0;
            
            if(dy > 4.0)
                dy = 4.0;
            else if(dy < -4.0)
                dy = -4.0;
            
            controller.getPlayer().setScore(controller.getPlayer().getScore() + 1);
            
            if(gameSimulation.containsGameObject(playerBall))
                controller.sendPacketToServer(new MathVector(dx, dy));
        }

        /* visual guide for player's ball */
        private class VelocityLine extends GameObject
        {

            private float test[] = {6, 4};
            private Stroke stroke = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, test, 0.0f);
            private Color strokeColor = new Color(0xB0B0B0);
            

            @Override
            public void render(Graphics2D g, double scale) 
            {
                //save original transform
                AffineTransform ot = g.getTransform();
                AffineTransform lt = playerBall.toLocalCoordinates(scale);

                int x0 = (int) (playerBall.getLocalCenter().x + Ball.BALL_RADIUS * scale);
                int y0 = (int) (playerBall.getLocalCenter().y + Ball.BALL_RADIUS * scale);

                int x1 = (srtPnt.x - endPnt.x);
                int y1 = -(srtPnt.y - endPnt.y);

                //apply the transformation to the graphics object
                g.transform(lt);

                g.setStroke(stroke);
                g.setColor(strokeColor);
                g.drawLine(x0, y0, x1, y1);
            
                g.setTransform(ot);
            }
        }
    }
}
