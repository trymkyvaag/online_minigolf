package multiplayergolfgame.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.SocketPackets.ToServerPacket;

/**
 * A aid in sending data to the server via the input stream, not used for final revison.
 * @author James
 */
public class TemporarySerialSend extends Thread 
{

    private GolfClientController parentController;
    private ObjectOutputStream outputStream;
    private boolean runThread = true;

    /**
     * A aid in sending data to the server via the input stream, not used for final revison.
     * @param outputStream the stream to write the data to
     * @param parentController the controller that is object belongs to
     */
    public TemporarySerialSend(ObjectOutputStream outputStream, GolfClientController parentController)
    {
        this.parentController = parentController;
        this.outputStream = outputStream;
    }
    
    /**
     * set whether to run the thread or not
     * @param runThread true to run, false otherwise
     */
    public void runThread(boolean runThread)
    {
        this.runThread = runThread;
    }
    
    @Override
    public void run()
    {
        Scanner userInputScanner = new Scanner(System.in);
        
        /* Sending Packets */
        while(runThread)
        {
            if(userInputScanner.hasNextLine())
            {
                String vectorString = userInputScanner.nextLine();
                String[] vectorStrings = vectorString.split(" ");
    
                if(vectorStrings.length != 2)
                {
                    System.out.println("[Client] Please specify 2 doubles, sperated by a space.");
                    continue;
                }
    
                double x = Double.parseDouble(vectorStrings[0]);
                double y = Double.parseDouble(vectorStrings[1]);
                MathVector initVelocity = new MathVector(x, y);
                parentController.getPlayer().setScore(parentController.getPlayer().getScore() + 1);
    
                try 
                {
                    outputStream.writeObject(new ToServerPacket(parentController.getPlayer(), initVelocity));
                } 
                catch (IOException e) 
                {
                    System.out.println("[Client] Error sending data to the server. (IO Exception)");
                    System.err.println(e);
                    break;
                }
            }
        }
        userInputScanner.close();
    }
}
