/*
 * Copyright (c) 2010-2021 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package multiplayergolfgame.framework;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import org.dyn4j.world.listener.CollisionListener;


/**
 * A Panel used for making Game. 
 * Code Heavily Inspired by (and at times directly sourced from) William Bittle  
 * http://www.dyn4j.org/
 * <p>
 * @author ncrav
 */
public class GameSimulation 
{
    
    /** Nanosecond to second conversion rate */
    public static final double NANO_TO_BASE = 1.0e9;

    private static final Color grassColor = new Color(0x3a8438);
    
    /** Canvas to be drawn to*/
    protected Canvas canvas;
    
    /** The actual GameWorld itself*/
    protected Level world;
    
    /**last time in nanoseconds that the buffer has been buffered */
    private long last;
    
    /**Boolean for if the simulation has been terminated */
    private boolean stopped;
    
    private final Camera camera;

    private JPanel panel;
    
    /**
     * runs a simulation in a world of different bodies
     * @param levelID the level id to load into the sim
     * @param scale the scale of the world
     * @param createPanel whether to create a panel to dipslay the world to (for servers/clients)
     */
    public GameSimulation(int levelID, double scale, boolean createPanel)
    {
        this.world = new Level(levelID);

        Dimension size = new Dimension(720, 720);
        //set canvas within Panel
        canvas = new Canvas();        
        canvas.setMinimumSize(size);
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);

        if(createPanel)
        {
            panel = new JPanel();
    
            //Set the Panel
            panel.setMinimumSize(size);
            panel.setPreferredSize(size);
            panel.setMaximumSize(size);
            
            panel.add(this.canvas);
        }
        
        //Create Camera with proper scale
        this.camera = new Camera();
        this.camera.setScale(scale);
        
        this.initializeGame();
    }

    /**
     *
     */
    protected void initializeGame()
    {
    }
    
    
    /**
     * Source 2010-2021 William Bittle  http://www.dyn4j.org/
     * Start active rendering the simulation.
     * <p>
     * This should be called after the JFrame has been shown.
     */
    private void start(){
        // initialize the last update time
        this.last = System.nanoTime();

        if(panel != null)
        {
            // don't allow AWT to paint the canvas since we are
            this.canvas.setIgnoreRepaint(true);
            // enable double buffering (the JFrame has to be
            // visible before this can be done)
            this.canvas.createBufferStrategy(2);
            // run a separate thread to do active rendering
            // because we don't want to do it on the EDT
        }
        Thread thread = new Thread() {
            public void run() {
                // perform an infinite loop stopped
                // render as fast as possible
                while (!isStopped()) {
                    gameLoop();
                    // you could add a Thread.yield(); or
                    // Thread.sleep(long) here to give the
                    // CPU some breathing room
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        // set the game loop thread to a daemon thread so that
        // it cannot stop the JVM from exiting
        thread.setDaemon(true);
        // start the game loop
        thread.start();
    }
    
    	/**
	 * The method calling the necessary methods to update
	 * the game, graphics, and poll for input.
	 */
    private void gameLoop() {
        
    Graphics2D g;
        // get the current time
        long time = System.nanoTime();
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        // set the last time
        this.last = time;
        // convert from nanoseconds to seconds
        double elapsedTime = (double) diff / NANO_TO_BASE;
        
        if(panel != null)
        {
            // get the graphics object to render to
            g = (Graphics2D) this.canvas.getBufferStrategy().getDrawGraphics();

            // by default, set (0, 0) to be the center of the screen with the positive x axis
            // pointing right and the positive y axis pointing up
            this.transform(g);

            // reset the view
            this.clear(g);

            // render anything about the simulation (will render the World objects)
            AffineTransform tx = g.getTransform();
            g.translate(this.camera.getOffsetX(), this.camera.getOffsetY());
            this.render(g, elapsedTime);
            g.setTransform(tx);

            // dispose of the graphics object
            g.dispose();
        }

        // update the World
        // we had to do this becuase dyn4j has issues with being placed in a thread undergoing
        // heavy use
	    try 
        {    
            this.world.update(elapsedTime);
        } 
        catch (Exception e)
        {
            System.out.println("Internal Physics Error. Handled appropriately by simulation.");
        }

        if(panel != null)
        {
            // blit/flip the buffer
            BufferStrategy strategy = this.canvas.getBufferStrategy();
            if (!strategy.contentsLost()) {
                strategy.show();
            }
        }

        // Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
    }
    
    /**
     * Performs any transformations to the graphics.
     * <p>
     * By default, this method puts the origin (0,0) in the center of the window
     * and points the positive y-axis pointing up.
     *
     * @param g the graphics object to render to
     */
    protected void transform(Graphics2D g) {
        final int w = this.canvas.getWidth();
        final int h = this.canvas.getHeight();

        // before we render everything im going to flip the y axis and move the
        // origin to the center (instead of it being in the top left corner)
        AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
        AffineTransform move = AffineTransform.getTranslateInstance(w / 2, -h / 2);
        g.transform(yFlip);
        g.transform(move);
    }
    
    /**
     * Clears the previous frame.
     *
     * @param g the graphics object to render to
     */
    protected void clear(Graphics2D g) {
        final int w = this.canvas.getWidth();
        final int h = this.canvas.getHeight();

        // lets draw over everything with a white background
        g.setColor(Color.WHITE);
        g.fillRect(-w / 2, -h / 2, w, h);
    }
    
    /**
     *
     * @param i
     */
    protected void drawMap(int i){
        
    }
    
    /**
     * Renders the example.
     *
     * @param g the graphics object to render to
     * @param elapsedTime the elapsed time from the last update
     */
    protected void render(Graphics2D g, double elapsedTime) {
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(grassColor);
        g.fillRect(-canvas.getWidth() / 2, -canvas.getWidth() / 2, canvas.getWidth(), canvas.getHeight());

        // draw all the objects in the world
        for (int i = 0; i < this.world.getBodyCount(); i++) {
            // get the object
            GameObject body = (GameObject) this.world.getBody(i);
            this.render(g, elapsedTime, body);
        }
        
    }

    /**
     * Renders the body.
     *
     * @param g the graphics object to render to
     * @param elapsedTime the elapsed time from the last update
     * @param obj
     */
    protected void render(Graphics2D g, double elapsedTime, GameObject obj) {
        // draw the object 
        obj.render(g, this.camera.getScale());
    }
        
    /**
     *
     * @param p
     * @return
     */
    protected Vector2 toWorldCoordinates(Point p) {
        return this.camera.toWorldCoordinates(this.canvas.getWidth(), this.canvas.getHeight(), p);
    }

    
    /**
     * Stops the simulation.
     */
    public synchronized void stop() {
        this.stopped = true;
    }
    
    /**
     * Returns true if the simulation is stopped.
     *
     * @return boolean true if stopped
     */
    public boolean isStopped() {
        return this.stopped;
    }
    
    /**
     * Starts the simulation.
     */
    public void run() {
        // set the look and feel to the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // show it
        if(panel != null)
            panel.setVisible(true);

        // start it
        this.start();
    }

    /**
     * Adds a mouseListneer to the canvas for interacting with the objects
     * @param l the mouse input listener to add
     */
    public void addMouseListener(MouseInputListener l)
    {
        this.canvas.addMouseListener(l);
        this.canvas.addMouseMotionListener(l);
    }

    /**
     * Gets the graphics of the canvas
     * @return
     */
    public Graphics2D getGraphics()
    {
        return (Graphics2D) this.canvas.getGraphics();
    }

    /**
     * Gets the panel that is where the world renders to
     * @return the panel, but will be null if the sim was created with renderGUI set to false
     * when construcvted.
     */
    public JPanel getPanel()
    {
        return this.panel;
    }

    /**
     * Add an additional game object to the level.
     * @param object
     */
    public void addGameObject(GameObject object)
    {
        this.world.addBody(object);
    }

    /**
     * Checks to see if the world contains the spcified game object
     * @param object the object to look for
     * @return true if the object is in teh wrold, false otherwise
     */
    public boolean containsGameObject(GameObject object)
    {
        return this.world.containsBody(object);
    }

    /**
     * Remove a game object from the world
     * @param object the object to remove
     */
    public void removeGameObject(GameObject object)
    {
        this.world.removeBody(object);
    }

    /**
     * Adds a listener to the world that triggers when two objects collide.
     * @param l the listener to add
     */
    public void addCollisionListener(CollisionListener<GameObject, BodyFixture> l)
    {
        this.world.addCollisionListener(l);
    }

    /**
     * Gets the level id of the world
     * @return the level id, as an int
     */
    public int getLevelID()
    {
        return this.world.getCurrentLevelID();
    }

    /**
     * Gets the transform/location of the level spawn
     * @return the transform of the spawn
     */
    public Transform getSpawnTransform()
    {
        return world.getSpawnTransform();
    }
}
