package multiplayergolfgame.Server;

import multiplayergolfgame.framework.GameSimulation;

import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author James Eastwood
 */
public class GolfServerView extends JFrame {
    
    private GameSimulation simulation;

    /**
     * Creates a golf server view, mianly used for debugging.
     * @param parentController the parent controller that controls the server
     * @param simulation the simulation to render to the display
     */
    public GolfServerView(GolfServerController parentController, GameSimulation simulation)
    {
        super("[Server] Final Project - Multiplayer Golf Game - Antonio Craveiro, James Eastwood, Trym Kyvaag)");
        this.simulation = simulation;

        /* TODO: On close, stop the server frist */
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // add a window listener
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // before we stop the JVM stop the simulation
                simulation.stop();
                parentController.stopModel();
                super.windowClosing(e);
            }
        });

        this.add(this.simulation.getPanel());
        this.pack();
        this.setVisible(true);
    }

    /**
     * Update the panel with a new simulation
     * @param simulation
     */
    public void updatePanel(GameSimulation simulation)
    {
        this.simulation = simulation;
        this.add(this.simulation.getPanel());
    }

}
