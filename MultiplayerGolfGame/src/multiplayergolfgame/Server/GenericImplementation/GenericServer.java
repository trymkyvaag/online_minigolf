package multiplayergolfgame.Server.GenericImplementation;

import multiplayergolfgame.Shared.SocketPackets.ClientInitPacket;
import multiplayergolfgame.Shared.SocketPackets.ToServerPacket;
import multiplayergolfgame.Shared.SocketPackets.ToClientPacket;
import multiplayergolfgame.Server.ClientThread;

/**
 *
 * @author James Eastwood
 * @param <T> data type
 */
public abstract class GenericServer<T> extends Thread 
{

    /**
     * Set whether the server is running or not. Used to aid in stopping the server.
     * @param isRunning true to run, false to stop.
     */
    public abstract void setRunning(boolean isRunning);    

    /**
     * Stats the server, sets isRunning to ttue
     */
    public abstract void runServer();

    /**
     * Stops the server.
     */
    public abstract void stopServer();

    /**
     * Handle a recieved packet from the specified client thread
     * @param thread the thread that recieved the packet
     * @param packet the recieved packet
     */
    public abstract void handleRecievedPacket(ClientThread<T> thread, ToServerPacket packet);

    /**
     * Handles a initial handshake packet from the specified client thread
     * @param thread the thread that recieved the packet
     * @param packet the initial packet
     */
    public abstract void handleInitClientPacket(ClientThread<T> thread, ClientInitPacket packet);

    /**
     * Safely handles a lost connection to a client.
     * @param thread the thread that lost connection.
     */
    public abstract void handleLostClient(ClientThread<T> thread);


    /**
     * Sets the packet to send to all the connected Clients
     * @return packet the packet to send to each client
     */
    public abstract ToClientPacket handleSendingPacket();
}
