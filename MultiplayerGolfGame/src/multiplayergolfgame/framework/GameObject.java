/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayergolfgame.framework;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import org.dyn4j.collision.CategoryFilter;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Transform;

/**
 * An abstract class for GameObjects.
 * <p>
 * A GameObject is any object directly within the world that has some importance
 * for collisions or actions within the game.
 * <p>
 * @author ncrav
 */
public abstract class GameObject extends Body {

    /**
     *
     */
    protected static final CategoryFilter CollidingObject = new CategoryFilter(1,1);

    /**
     *
     */
    protected static final CategoryFilter NonCollidingObject = new CategoryFilter(2,2);
    /**
     * Creates a new GameObject
     */
    public GameObject()
    {
        super();
    }
    
    /**
     * Draws the GameObject
     * <p>
     * @param g the graphics object to render to
     * @param scale the scaling factor
     */
    public abstract void render(Graphics2D g, double scale);
    
    /**
     * Transforms the rendered object from global coordinates to local 
     * (camera) coordinates.
     * <p>
     * Invoke this method before drawing the gameObject
     * <p>
     * @param scale the scaling factor
     * @return a transformation matching the local coordinate system
     */
    public AffineTransform toLocalCoordinates(double scale)
    {
        Transform transform = this.getTransform();
        // transform the coordinate system from world coordinates to local coordinates
        AffineTransform lt = new AffineTransform();
        lt.translate(transform.getTranslationX() * scale, transform.getTranslationY() * scale);
        lt.rotate(transform.getRotationAngle());
        
        return lt;
    }
}
