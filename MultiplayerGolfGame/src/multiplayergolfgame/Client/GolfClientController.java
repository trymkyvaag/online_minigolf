package multiplayergolfgame.Client;

import java.awt.Color;

import multiplayergolfgame.Shared.MathVector;
import multiplayergolfgame.Shared.Player;
import multiplayergolfgame.Shared.SocketPackets.ToServerPacket;

/**
 *
 * @author James Eastwood
 */
public class GolfClientController 
{
    private ServerConnectionHandler serverConnectionHandler;
    private ClientView view;
    
    private Player player;

    /**
     *  Creates a golf client controller
     */
    public GolfClientController()
    {
        this.player = new Player("", Color.BLACK);

        view = new ClientView(this);
        
        view.getConnectionPanel().addServerConnectListener(l ->
        {
            establishServerConnection();   
        });
    }

    /* Attempts to connect to the server specified by the user */
    private void establishServerConnection()
    {
        String ip = view.getConnectionPanel().getIP();
        int port  = view.getConnectionPanel().getPort();

        player.setNickname(view.getConnectionPanel().getNickname());
        player.setBallColor(view.getConnectionPanel().getChosenColor());
        
        serverConnectionHandler = new ServerConnectionHandler(this, ip, port);

        if(serverConnectionHandler.connect())
        {
            Thread serverConnectionThread = new Thread(serverConnectionHandler);
            serverConnectionThread.start();
            view.gotoGameView();
        }
        else
        {
            //TODO: create some JDialog where it says failed to connect to server.
        }
    }

    /**
     * Sends a packet with the controller's set player and the specified velocity to
     * the server
     * @param velocity the velocity to send
     */
    public void sendPacketToServer(MathVector velocity)
    {
        serverConnectionHandler.sendDataToServer(new ToServerPacket(this.player, velocity));
    }

    /**
     * Gets the view of the controller
     * @return the Client view of the controller
     */
    public ClientView getView()
    {
        return this.view;
    }

    /**
     * Gets the player that is created by the user's customized settings.
     * @return the user's customized player
     */
    public Player getPlayer()
    {
        return this.player;
    }
}
