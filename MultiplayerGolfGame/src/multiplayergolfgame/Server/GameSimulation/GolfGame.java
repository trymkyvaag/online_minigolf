package multiplayergolfgame.Server.GameSimulation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.NarrowphaseCollisionData;

import multiplayergolfgame.Server.GolfServerController;
import multiplayergolfgame.Shared.Hole;
import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.Player;
import multiplayergolfgame.framework.GameObject;
import multiplayergolfgame.framework.GameSimulation;

/**
 * A golf game handles the game simulation and manages updating the velocities of all
 * the balls that the server recieves.
 * @author James Eastwood
 */
public class GolfGame extends BallCollisionListener
{
    private GolfServerController parentController;
    private HashMap<Player, PhysicsBall> physicsBalls;
    private GameSimulation gameSimulation;

    /**
     * Creates a ball object
     * @param parentController the parent controller of the golf game
     * @param levelID the level id to load initially
     * @param renderGUI whether to render simulation panel or not. False for debug, true for headless server creation
     */
    public GolfGame(GolfServerController parentController, int levelID, boolean renderGUI)
    {
        this.parentController = parentController;
        this.physicsBalls = new HashMap<>();
        gameSimulation = new GameSimulation(levelID, 250, renderGUI);

        gameSimulation.addCollisionListener(this);
    }

    /**
     * Gets a list of players with their updated ball positiions
     * @return an array of players
     */
    public Player[] getPlayers()
    {
        for(PhysicsBall ball : physicsBalls.values())
        {
            if(ball != null)
                ball.updateBallPosition();
        }

        Player players[] = physicsBalls.keySet().toArray(new Player[physicsBalls.keySet().size()]);

        Arrays.sort(players);
        return players;
    }

    /**
     * Gets the game simulation inside this object
     * @return the game simulation
     */
    public GameSimulation getGameSimulation()
    {
        return this.gameSimulation;
    }

    /**
     * Creates a ball for the specified player.
     * @param player the specified player
     */
    public void createBall(Player player)
    {
        PhysicsBall ball = new PhysicsBall(player.getBallColor());
        ball.setTransform(gameSimulation.getSpawnTransform());

        physicsBalls.put(player, ball);
        player.setBall(ball.getBall());

        gameSimulation.addGameObject(ball);
    }

    /**
     * Removes the player's ball from the gamespace
     * @param player
     */
    public void removeBall(Player player)
    {
        gameSimulation.removeGameObject(physicsBalls.get(player));
    }

    /**
     * Remvoves a player and their ball form the simulation.
     * @param player
     */
    public void removePlayer(Player player)
    {
        removeBall(player);
        physicsBalls.remove(player);
    }

    

    /* Checks to see if all players are finished, if so, move to the next level */
    public void checkPlayers()
    {
        for(Player player : physicsBalls.keySet())
        {
            if(!player.isReady())
            {
                return;
            }
        }
        /* All players are ready, move to the next Level */
        System.out.println("[Server] Level Complete! Moving to the next level!");

        gameSimulation.stop();
        parentController.goToNextLevel();
    }

    /**
     * Moves the ball of the specified player using the specified velocity vector.
     * @param player The player's ball to move.
     * @param velocity The initial velocity to give the ball.
     */
    public void moveBall(Player player, MathVector velocity)
    {
        //System.out.printf("[Server] Moving %s's ball.\n", player);
        PhysicsBall ball = physicsBalls.get(player);

        if(ball != null)
        {
            ball.setLinearVelocity(velocity.x, velocity.y);
            ball.setAtRest(false);
        }
        else 
        {
            createBall(player);
            moveBall(player, velocity);
        }
    }

    private Player getPlayerOfBall(PhysicsBall ball)
    {
        for(Entry<Player, PhysicsBall> entry : physicsBalls.entrySet())
        {
            if(ball.equals(entry.getValue()))
                return entry.getKey();
        }
        return null;
    }

    @Override
    public boolean collision(NarrowphaseCollisionData<GameObject, BodyFixture> collision) 
    {
        if(collision.getBody1() instanceof PhysicsBall && collision.getBody2() instanceof Hole)
        {
            hitHole((PhysicsBall) collision.getBody1());
        }
        else if(collision.getBody1() instanceof Hole && collision.getBody2() instanceof PhysicsBall)
        {
            hitHole((PhysicsBall) collision.getBody2());
        }
        return true;
    }

    private void hitHole(PhysicsBall ball)
    {
        Player player = getPlayerOfBall(ball);
        player.setReady(true);
        removeBall(player);
        
        checkPlayers();
    }
}
