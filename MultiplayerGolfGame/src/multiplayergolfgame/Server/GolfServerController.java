package multiplayergolfgame.Server;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import multiplayergolfgame.Server.GameSimulation.GolfGame;

/**
 *
 * @author James Eastwood
 */
public class GolfServerController {

    private GolfServer server;
    private GolfGame golfGame;
    private GolfServerView view;
    
    private int levelID;
    private ArrayList<Integer> levelsPlayed;
    private int numberOfLevelsPlayed;
    private int maxNumberOfLevels;

    private boolean renderGUI;

    /**
     *
     * @param renderGUI
     */
    public GolfServerController(boolean renderGUI) {

        this.levelID = -1;
        this.numberOfLevelsPlayed = 0;
        this.levelsPlayed = new ArrayList<>();
        this.maxNumberOfLevels = 5;
        this.renderGUI = renderGUI;

        this.server = new GolfServer(this, 5002);
        this.golfGame = new GolfGame(this, levelID, renderGUI);
        
        if (renderGUI) {
            view = new GolfServerView(this, golfGame.getGameSimulation());
        }
        
        golfGame.getGameSimulation().run();
        
        server.start();
        
        Scanner userInputScanner = new Scanner(System.in);
        
        while (true) {
            if (userInputScanner.hasNextLine()) {
                if (userInputScanner.nextLine().equals("stop"))
                {
                    server.setRunning(false);
                    server.stopServer();
                    
                    try {
                        server.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        userInputScanner.close();
        if (view != null) {
            view.setVisible(false);
        }
        System.out.println("[Server] Shutdown Complete!");
        System.exit(0);
    }

    /**
     * Gets the golf game tied to this controller
     * @return the golf game
     */
    public GolfGame getGolfGame()
    {
        return this.golfGame;
    }

    /**
     * Gets the server view tied to this 
     * @return
     */
    public GolfServerView getView()
    {
        return view;
    }
    
    /**
     * Gets the current level id stored in the controller
     * @return
     */
    public int getLevelID() {
        return this.levelID;
    }
    
    /**
     * Creates a new golf game with the specified level id, and resets the world.
     * @param newLevelID
     */
    public void setLevelID(int newLevelID) {
        this.levelID = newLevelID;

        this.golfGame = new GolfGame(this, this.levelID, renderGUI);

        if (renderGUI) {
            view.setVisible(false);
            view = new GolfServerView(this, golfGame.getGameSimulation());
        }

        golfGame.getGameSimulation().run();
    }
    
    public void goToNextLevel()
    {
        //there was less than max number of levels played
        if(this.numberOfLevelsPlayed < this.maxNumberOfLevels)
        {
            Random rand = new Random(System.currentTimeMillis());
            int nextLevel = rand.nextInt(10) + 1;
            
            while(this.levelsPlayed.contains(nextLevel))
                nextLevel = rand.nextInt(10) + 1;
            
            this.setLevelID(nextLevel);
            this.levelsPlayed.add(nextLevel);
            this.numberOfLevelsPlayed++;
        } else /* game is over */{
            this.setLevelID(-1);
            this.levelsPlayed.clear();
            this.numberOfLevelsPlayed = 0;
        }
    }

    /**
     * stops the server.
     */
    public void stopModel() {
        this.server.stopServer();
    }
}
