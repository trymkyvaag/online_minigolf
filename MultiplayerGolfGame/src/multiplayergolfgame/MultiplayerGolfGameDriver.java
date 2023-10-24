/*
 * Student: James Eastwood
 * Course: CSC2610
 * Date: 4/15/22
 */
package multiplayergolfgame;

import multiplayergolfgame.Client.GolfClientController;
import multiplayergolfgame.Server.GolfServerController;

/**
 *
 * @author James Eastwood
 */
public class MultiplayerGolfGameDriver
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        GolfServerController server;

        if(args.length != 0)
        {
            if(args[0].equals("-server"))
            {
                System.out.println("Starting Server...");
                if(args.length > 1)
                {
                    if(args[1].equals("-gui"))
                        server = new GolfServerController(true);
                }
                else
                    server = new GolfServerController(false);
            }
            System.out.println("Unknown arguments.");
        }
        else
        {
            System.out.println("Starting Client...");
            GolfClientController client = new GolfClientController();
        }
    }
}
