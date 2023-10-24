package multiplayergolfgame.Shared.SocketPackets;

import java.io.Serializable;

import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.Player;

/**
 * A server that is sent to the server
 * @author James Eastwood
 */
public class ToServerPacket implements Serializable
{
    private Player player;
    private MathVector velocity; //TODO: move this into ball

    /**
     * Constructor
     * @param player the player to update server side
     * @param velocity the velocity to send to the server
     */
    public ToServerPacket(Player player, MathVector velocity)
    {
        this.player = player;
        this.velocity = velocity;
    }

    /**
     * Gets the player associated with the packet.
     * @return the player
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Gets the velocity associated with the packet
     * @return the velocity as a x, y packet.
     */
    public MathVector getVelocity()
    {
        return velocity;
    }

    @Override
    public String toString()
    {
        return(player + ": (" + velocity.x + ", " + velocity.y + ")");
    }
}
