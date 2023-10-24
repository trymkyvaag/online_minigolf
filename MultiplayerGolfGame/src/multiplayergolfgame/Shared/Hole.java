/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayergolfgame.Shared;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import multiplayergolfgame.framework.GameObject;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
/**
 *  Creates a Hole Object, that destroys balls upon contact
 * @author ncrav
 */
public class Hole extends GameObject{
    
    static final Color HOLE_COLOR = Color.black;
    
    static final double HOLE_RADIUS = 0.028575 * 1.5;
    static final double HOLE_COLLISION_RADIUS = HOLE_RADIUS * .5;
    
    /**
     * A Hole Game Object
     */
    public Hole()
    {
        super();
        this.addFixture(Geometry.createCircle(HOLE_COLLISION_RADIUS));
        this.setMass(MassType.INFINITE);
    }
    
    @Override
    public void render(Graphics2D g, double scale) {
                //save original transform
        AffineTransform ot = g.getTransform();
        AffineTransform lt = super.toLocalCoordinates(scale);
        
        //apply the transformation to the graphics object
        g.transform(lt);
        
        //draw the object itself 
        double radius2 = 2.0 * HOLE_RADIUS;
        Ellipse2D.Double circle = new Ellipse2D.Double(
                (this.getLocalCenter().x - HOLE_RADIUS) * scale,
                (this.getLocalCenter().y - HOLE_RADIUS) * scale,
                radius2 * scale,
                radius2 * scale);
        g.setColor(HOLE_COLOR);
        g.fill(circle);
        
        g.setColor(HOLE_COLOR);
        g.draw(circle);
        
        //return graphics to its original transform
        g.setTransform(ot);
    }
}
