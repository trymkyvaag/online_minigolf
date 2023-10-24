package multiplayergolfgame.Server;

import multiplayergolfgame.Shared.SocketPackets.ClientInitPacket;
import multiplayergolfgame.Shared.IntervalDataSendAssitor;
import multiplayergolfgame.Shared.SocketPackets.ToServerPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;

import multiplayergolfgame.Server.GenericImplementation.GenericServer;

/**
 * A clientThread handles a client that has connected to a socket server.
 * @author James Eastwood
 * @param <T> data type (store client specific data here)
 */
public class ClientThread<T> extends Thread
{
    private GenericServer<T> parentServer;
    private ObjectInputStream  inServerStream = null;
    private ObjectOutputStream toClientStream = null;
    private boolean runThread = true;

    private Timer sendDataTimer;
    private IntervalDataSendAssitor<T> dataSendAssitor;

    private T data;

    /**
     * Creates a client thread that handles a client connected to the parent server.
     * @param parentServer the parent server
     * @param socket the socket handling the connection.
     */
    public ClientThread(GenericServer<T> parentServer, Socket socket)
    {
        this.parentServer = parentServer;
        this.sendDataTimer = new Timer();

        try
        {
            /* Why are we flushing here?
             * https://stackoverflow.com/questions/8088557/getinputstream-blocks
             * Thank you stack overflow, very cool.
             */
            toClientStream = new ObjectOutputStream(socket.getOutputStream());
            toClientStream.flush();
            inServerStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e)
        {
            System.err.println("Error establishing I/O with client");
        }
    }

    /**
     * Gets the data associated with the clientThread
     * @return the data.
     */
    public T getClientData()
    {
        return this.data; 
    }

    /**
     * sets the data ssociated with the client thread.
     * @param data
     */
    public void setClientData(T data)
    {
        this.data = data;
    }

    /**
     * Set whether the thread should be running or not.
     * @param isRunning true to run, false otherwise.
     */
    public void setThreadRunning(boolean isRunning)
    {
        this.runThread = isRunning;
    }

    /**
     * Run the thread, handling the client/server interaction.
     */
    @Override
    public void run()
    {
        dataSendAssitor = new IntervalDataSendAssitor<T>(parentServer, this, toClientStream);
        sendDataTimer.scheduleAtFixedRate(dataSendAssitor, 0, IntervalDataSendAssitor.DELAY);

        while(runThread)
        {
            Object recievedData = null;
            try 
            {
                recievedData = inServerStream.readObject();
            } 
            catch (ClassNotFoundException e)
            {
                System.out.println("[Server] Recieved unknown datatype from client!");
            } 
            catch (IOException e) 
            {
                //System.out.println("[Server] Error reading data from the client! (IO Exception)");
                //System.err.println(e);
                this.runThread = false;
            }

            if(recievedData != null)
            {
                /*TODO: Fix me! */
                if(recievedData instanceof ClientInitPacket)
                {
                    System.out.println("Recieved Initialization Packet for " + ((ClientInitPacket) recievedData).getPlayer());
                    parentServer.handleInitClientPacket(this, (ClientInitPacket) recievedData);
                }
                else if(recievedData instanceof ToServerPacket)
                {
                    System.out.println("[Server] Recieved " + ((ToServerPacket) recievedData));
                    parentServer.handleRecievedPacket(this, (ToServerPacket) recievedData);
                }
            }
        }

        System.out.println("[Server] Closed connection to a client.");
        parentServer.handleLostClient(this);
        sendDataTimer.cancel();
        try 
        {
            inServerStream.close();
            toClientStream.close();
        } catch (IOException e) 
        {
        }
    }
}
