/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayergolfgame.Shared;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import multiplayergolfgame.framework.GameObject;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

/**
 * Wall game object
 * @author ncrav
 */
public class Wall extends GameObject{

    /**
     *
     */
    public final static double WALL_WIDTH = 0.085;

    /**
     *
     */
    public final static double WALL_HEIGHT = 0.085;
    private Color wallColor = Color.decode("#AA7942");
   // private Color lineColor = Color.black;
    
    /**
     *
     */
    public Wall()
    {
        super();
       
        this.addFixture(Geometry.createRectangle(WALL_WIDTH, WALL_HEIGHT));
        this.setMass(MassType.INFINITE);
    }
    @Override
    public void render(Graphics2D g, double scale) {
        
        //save original transform
        AffineTransform ot = g.getTransform();

        // transform the coordinate system from world coordinates to local coordinates
        AffineTransform lt = super.toLocalCoordinates(scale);

        //apply the transformation to the graphics object
        g.transform(lt);

        //draw the object itself 
        Rectangle2D.Double rectangle = new Rectangle2D.Double(
                (this.getLocalCenter().x - WALL_WIDTH/2) * scale,
                (this.getLocalCenter().y - WALL_HEIGHT/2) * scale,
                WALL_WIDTH * scale,
                WALL_HEIGHT * scale);
        g.setColor(wallColor);
        g.fill(rectangle);

       // g.setColor(lineColor);
        g.draw(rectangle);

        //return graphics to its original transform
        g.setTransform(ot);
    }

    /**
     * stes teh color of the wall
     * @param WALL_COLOR
     */
    public void setWallColor(Color WALL_COLOR) {
        this.wallColor = WALL_COLOR;
    }

    /**
     * sets the outline color
     * @param LINE_COLOR
     */
    public void setLineColor(Color LINE_COLOR) {
        //this.lineColor = LINE_COLOR;
    }
    
    
}
