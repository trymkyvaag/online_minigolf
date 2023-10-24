package multiplayergolfgame.Client;

import multiplayergolfgame.Shared.SocketPackets.ToClientPacket;
import multiplayergolfgame.Shared.SocketPackets.ToServerPacket;
import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.SocketPackets.ClientInitPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * The Socket Connection Handler for a Client
 * @author James Eastwood
 */
public class ServerConnectionHandler implements Runnable
{
    private GolfClientController parentController;

    private Socket socketToServer;
    private String address;
    private int port;

    private ObjectInputStream inClientStream = null;
    private ObjectOutputStream toServerStream = null;

    /**
     * Constructor 
     * @param parentController the parent controller that houses the client
     * @param address the adress to connect to
     * @param port the port to connect to
     */
    public ServerConnectionHandler(GolfClientController parentController, String address, int port)
    {
        this.parentController = parentController;

        this.address = address;
        this.port = port;
    }

    /**
     * Try and connect to the server socket
     * @return true on success, false otherwise
     */
    public boolean connect()
    {
        try
        {
            System.out.println("[Client] Attempting to connect to server.");
            socketToServer = new Socket(address, port);

            /* Why are we flushing here?
             * https://stackoverflow.com/questions/8088557/getinputstream-blocks
             * Thank you stack overflow, very cool.
             */
            inClientStream = new ObjectInputStream(socketToServer.getInputStream());
            toServerStream = new ObjectOutputStream(socketToServer.getOutputStream());
            toServerStream.flush();
        }
        catch(UnknownHostException e)
        {
            System.err.println("[Client] Can't find the host " + address + ":" + port);
        }
        catch(IOException e)
        {
            System.err.println("[Client] Failed to create I/O for connection to Server.");
        }

        if(socketToServer != null && socketToServer.isConnected())
            System.out.println("[Client] Connected!");

        return socketToServer != null && socketToServer.isConnected();
    }

    /**
     * Send/Recieve data to the server.
     */
    @Override
    public void run() 
    {
        /* First, send the client initialization packet, sending the player information */
        try 
        {
            toServerStream.writeObject(new ClientInitPacket(parentController.getPlayer()));
            toServerStream.flush();
        } 
        catch (IOException e1) { }

        TemporarySerialSend serialSend = new TemporarySerialSend(toServerStream, parentController);
        serialSend.start();

        while(socketToServer.isConnected())
        {
            /*Recieving Packets */
            Object recievedData = null;
            try 
            {
                recievedData = inClientStream.readUnshared();
            } 
            catch (ClassNotFoundException e)
            {
                System.out.println("[Client] Recieved unknown datatype from the server!");
            } 
            catch (IOException e) 
            {
                System.out.println("[Client] Error reading data from the server! (IO Exception)");
                System.err.println(e);
                break;
            }

            if(recievedData != null)
            {
                if(recievedData instanceof ToClientPacket)
                {
                    ToClientPacket recievedPacket = (ToClientPacket) recievedData;
                    //System.out.println("[Client] Receiving...\n" + recievedPacket);
                    this.parentController.getView().getLobbyViewPanel().updateScoreboard((recievedPacket).getPlayers());
                    this.parentController.getView().getGameViewPanel().updateBalls((recievedPacket).getPlayers());

                    /* What view do we display to the user? */
                    /* only update if the level ID is a different one */
                    if(recievedPacket.getLevelID() == -1 && (this.parentController.getView().getGameViewPanel().getLevelID() != recievedPacket.getLevelID()))
                    {
                        this.parentController.getView().gotoLobbyView();
                        this.parentController.getView().getGameViewPanel().setLevelID(-1);
                        /* Send a dummy packet to notify the server we are ready */
                        sendDataToServer(new ToServerPacket(parentController.getPlayer(), new MathVector(0, 0)));
                    }
                    else if(this.parentController.getView().getGameViewPanel().getLevelID() != recievedPacket.getLevelID())
                    {
                        this.parentController.getView().getGameViewPanel().setLevelID(recievedPacket.getLevelID());
                        this.parentController.getView().gotoGameView();
                    }
                }
            }
        }

        serialSend.runThread(false);
    }

    /**
     * Sends a the specified ToServerPacket to the server.
     * @param packet the specified packet to send
     */
    public void sendDataToServer(ToServerPacket packet)
    {
        try 
        {
            System.out.println("[Client] Sending..." + packet + "\n");
            toServerStream.writeObject(packet);
            toServerStream.flush();
            toServerStream.reset();
        } 
        catch (IOException e) {
            System.out.println("[Client] Lost Connection to the Server.");
            e.printStackTrace();
        }
    }
}
