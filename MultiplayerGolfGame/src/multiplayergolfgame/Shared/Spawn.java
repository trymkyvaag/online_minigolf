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
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

/**
 * Is an object that indicates where to spawn balls
 * @author Antonio Craveiro
 */
public class Spawn extends GameObject{
        final static double SPAWN_WIDTH = 0.085;
        final static double SPAWN_HEIGHT = 0.085;
        private static final Color SPAWN_COLOR = new Color(0x2F6B2D);

    /**
     * Spawn game object
     */
    public Spawn() {
        super();
        BodyFixture part = new BodyFixture(Geometry.createRectangle(SPAWN_WIDTH, SPAWN_HEIGHT));
        part.setFilter(NonCollidingObject);
        this.addFixture(part);
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
                (this.getLocalCenter().x - SPAWN_WIDTH / 2) * scale,
                (this.getLocalCenter().y - SPAWN_HEIGHT / 2) * scale,
                SPAWN_WIDTH * scale,
                SPAWN_HEIGHT * scale);
        g.setColor(SPAWN_COLOR);
        g.fill(rectangle);

        // g.setColor(lineColor);
        g.draw(rectangle);

        //return graphics to its original transform
        g.setTransform(ot);
    }
    
}
