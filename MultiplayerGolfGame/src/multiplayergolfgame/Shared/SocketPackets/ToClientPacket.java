package multiplayergolfgame.Shared.SocketPackets;

import java.io.Serializable;
import multiplayergolfgame.Shared.Player;

/**
 * A packet that gets sent from the server to the client.
 * @author James Eastwood
 */
public class ToClientPacket implements Serializable
{
    Player players[];
    int levelID;

    /**
     * Creates a packet that can be used to send to a client
     * @param players the array of players to send
     * @param currentLevelID the level ID of the level
     */
    public ToClientPacket(Player players[], int currentLevelID)
    {
        this.players = players;
        this.levelID = currentLevelID;
    }

    /**
     * Packages a player array to send to cleints
     * @param players the player list to send
     */
    public void packagePlayerArray(Player players[])
    {
        this.players = players;
    }

    /**
     * Gets a player list contained inside the packet
     * @return
     */
    public Player[] getPlayers()
    {
        return this.players;
    }

    /**
     * Updates the level id stored in the packet
     * @param newLevelID the new level id
     */
    public void setLevelID(int newLevelID)
    {
        this.levelID = newLevelID;
    }

    /**
     * Gets the level id stored in the back
     * @return the stored level id
     */
    public int getLevelID()
    {
        return this.levelID;
    }

    @Override
    public String toString()
    {
        String string = "\t ToClientPacket:\n";
        for(Player player : this.players)
        {
            string += "\t\t" + player + ": " + player.getBall().getPosition() + "\n"; 
        }
        return string;
    }
}
