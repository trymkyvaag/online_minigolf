package multiplayergolfgame.Client;

import java.awt.CardLayout;
import java.awt.Font;
import javax.swing.JFrame;

import multiplayergolfgame.Client.Panels.*;
import multiplayergolfgame.Shared.MathVector;

/**
 * JFrame wrapper for the client's view.
 * @author James Eastwood
 */
public class ClientView extends JFrame 
{
    private GolfClientController controller;

    private CardLayout layout;
    
    private ClientConnectionPanel clientConnectionPanel;
    private LobbyView lobbyViewPanel;
    private ClientGameView gameViewPanel;

    /**
     * Font for the labels
     */
    public static final Font labelFont = new Font("Sans Serif", Font.BOLD, 16);

    /**
     * Font for the fields
     */
    public static final Font fieldFont = new Font("Sans Serif", Font.PLAIN, 16);

    /**
     * Creates a Client view that manages the different types of views, the connection view,
     *  the lobby view, and the game view.
     * @param controller
     */
    public ClientView(GolfClientController controller)
    {
        super("Final Project - Multiplayer Golf Game - Antonio Craveiro, James Eastwood, Trym Kyvaag)");
        this.controller = controller;
        
        layout = new CardLayout();
        clientConnectionPanel = new ClientConnectionPanel();
        lobbyViewPanel = new LobbyView(controller);
        gameViewPanel = new ClientGameView(controller);

        this.setLayout(layout);

        this.add("Settings", clientConnectionPanel);
        this.add("Lobby", lobbyViewPanel);
        this.add("Game", gameViewPanel);

        layout.show(this.getContentPane(), "Settings");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setSize(1080, 760);
        this.setVisible(true);
    }
    
    /**
     * Returns the instance of the client connection panel.
     * @return
     */
    public ClientConnectionPanel getConnectionPanel()
    {
        return this.clientConnectionPanel;
    }

    /**
     * Gets the lobby view panel
     * @return
     */
    public LobbyView getLobbyViewPanel()
    {
        return this.lobbyViewPanel;
    }

    /**
     * gets the game view panel
     * @return
     */
    public ClientGameView getGameViewPanel()
    {
        return this.gameViewPanel;
    }

    /**
     * switches the cardlayout to show the lobby view
     */
    public void gotoLobbyView()
    {
        layout.show(this.getContentPane(), "Lobby");
        gameViewPanel.setLevelID(-1);
    }

    /**
     * switches the card to the game view.
     */
    public void gotoGameView()
    {
        layout.show(this.getContentPane(), "Game");
        gameViewPanel.startSimulation();
        controller.sendPacketToServer(new MathVector(0, 0));
    }
}
