package multiplayergolfgame.Shared;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.TimerTask;

import multiplayergolfgame.Server.ClientThread;
import multiplayergolfgame.Server.GenericImplementation.GenericServer;

/**
 * Creates a timer and sends data at the specified interval out thru the specified object
 * output stream.
 * @author James Eastwood
 * @param <T> the data stored inside the client thread and server
 */
public class IntervalDataSendAssitor<T> extends TimerTask
{
    private static final int PACKETS_PER_SECOND = 60;

    /**
     * The delay in miliseconds.
     */
    public static int DELAY = 1000 / PACKETS_PER_SECOND;
    private GenericServer<T> parentServer;
    private ClientThread<T> parentClient;
    private ObjectOutputStream outputStream;
    private Object dataToSend;

    /**
     * Creates a data sender for the specified output stream
     * @param parentServer the parent server that handles the connected clients
     * @param outputStream the stream to write to
     * @param parentClient the client to send the data to
     */
    public IntervalDataSendAssitor(GenericServer<T> parentServer, ClientThread<T> parentClient, ObjectOutputStream outputStream)
    {
        this.parentServer = parentServer;
        this.parentClient = parentClient;
        this.outputStream = outputStream;
    }

    /**
     * Run the thread handling the output stream
     */
    @Override
    public void run()
    {
        dataToSend = parentServer.handleSendingPacket();
        try 
        {
            /* https://stackoverflow.com/questions/8089583/why-is-javas-object-stream-returning-the-same-object-each-time-i-call-readobjec */
            //System.out.println("[Server] Sending...\n" + dataToSend);
            outputStream.writeUnshared(dataToSend);
            outputStream.flush();
            outputStream.reset();
        } 
        catch (IOException e) 
        {
            System.err.println("[Server] Failed to send data to client!");
            parentServer.handleLostClient(parentClient);
        }
    }
}
