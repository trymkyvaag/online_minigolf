package multiplayergolfgame.Server;

import multiplayergolfgame.Shared.SocketPackets.ToClientPacket;
import multiplayergolfgame.Shared.SocketPackets.ToServerPacket;
import multiplayergolfgame.Shared.SocketPackets.ClientInitPacket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import multiplayergolfgame.Server.GenericImplementation.GenericServer;
import multiplayergolfgame.Shared.Player;

/**
 * GolfServer hosts the socket server handling the game balls
 * @author James Eastwood
 */
public class GolfServer extends GenericServer<Player>
{
    private GolfServerController controller;

    private Socket connectedClientSocket = null;
    private ServerSocket server;
    private boolean isRunning;

    private ArrayList<ClientThread<Player>> clients;
    private ToClientPacket packetForClients;

    /**
     * Creates a golf server on the specifed port.
     * @param controller the controller that handles the server.
     * @param port the port to create the socket on.
     */
    public GolfServer(GolfServerController controller, int port)
    {
        this.controller = controller;

        this.clients = new ArrayList<>();
        this.isRunning = true;
        
        packetForClients = new ToClientPacket(null, 0);

        try
        {
            server = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.err.println("Failed to create server on port " + port + ".");
        }
    }

    /**
     * run the thread handling the server accpect call
     */
    @Override
    public void run()
    {
        runServer();
    }

    /**
     * Set whether the server should run or not. Doesn't start the server.
     * @param isRunning true to run the server, false otherwise.
     */
    @Override
    public void setRunning(boolean isRunning)
    {
        this.isRunning = isRunning;

        if(isRunning == false)
        {
            try 
            {
                server.close();
            } 
            catch (IOException e) 
            {
                System.out.println("[Server] Failed to stop server. Perhaps it is already stopped?");
                e.printStackTrace();
            }
        }
    }

    /**
     * Run the server, doesn't create an addtional thread for the blocking call.
     */
    @Override
    public void runServer()
    {
        while(this.isRunning)
        {
            try 
            {
                System.out.println("[Server] Listening for potential clients...");
                connectedClientSocket = server.accept();
                System.out.println("[Server] Accepted Connection.");
                
                ClientThread<Player> newThread = new ClientThread<>(this, connectedClientSocket);
                newThread.start();
                clients.add(newThread);
            } 
            catch (IOException | NullPointerException e) { }
        }
    }

    /**
     * Safely stops the server.
     */
    @Override
    public void stopServer()
    {
        System.out.println("[Server] Shutting down server...");
        isRunning = false;

        for(ClientThread<Player> client : clients)
        {
            client.setThreadRunning(false);
        }

        try 
        {
            server.close();
            connectedClientSocket.close();
        } 
        catch (IOException | NullPointerException e) { }
    }

    @Override
    public void handleLostClient(ClientThread<Player> thread)
    {
        controller.getGolfGame().removePlayer(thread.getClientData());
    }

    @Override
    public void handleInitClientPacket(ClientThread<Player> thread, ClientInitPacket packet) 
    {
        thread.setClientData(packet.getPlayer());
        controller.getGolfGame().createBall(packet.getPlayer());   
    }

    @Override
    public void handleRecievedPacket(ClientThread<Player> thread, ToServerPacket packet)
    {
        thread.getClientData().update(packet.getPlayer());
        controller.getGolfGame().moveBall(thread.getClientData(), packet.getVelocity()); //update Ball

        controller.getGolfGame().checkPlayers(); //Check to see if level complete.
    }

    @Override
    public ToClientPacket handleSendingPacket()
    {
        packetForClients.packagePlayerArray(controller.getGolfGame().getPlayers());
        packetForClients.setLevelID(this.controller.getLevelID());
        //System.out.println(packetForClients);
        return packetForClients;
    }
}
