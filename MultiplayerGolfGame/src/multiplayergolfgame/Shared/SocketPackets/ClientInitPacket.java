package multiplayergolfgame.Shared.SocketPackets;

import java.io.Serializable;

import multiplayergolfgame.Shared.Player;

/**
 * The initial packet send from the client to the server on connect
 * @author James Eastwood
 */
public class ClientInitPacket implements Serializable 
{
    private Player player;

    /**
     * Constructor
     * @param player player to send
     */
    public ClientInitPacket(Player player)
    {
        this.player = player;
    }

    /**
     * Get the player stored in the packet
     * @return the player
     */
    public Player getPlayer()
    {
        return this.player;
    }
}
