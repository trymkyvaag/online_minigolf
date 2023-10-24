package multiplayergolfgame.Server.GameSimulation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.awt.geom.Ellipse2D;

import multiplayergolfgame.Shared.Ball;
import multiplayergolfgame.framework.GameObject;

/**
 * A ball that has physics colliders.
 * @author James Eastwood
 */
public class PhysicsBall extends GameObject
{
    //Collision Stuff
    final static double BALL_COLLISION_RADIUS = 0.028575;
    final static double BALL_DENSITY = 217.97925;
    final static double BALL_FRICTION = 1.5;
    final static double BALL_RESTITUTION = 0.9;
    
    final static double DEFAULT_LINEAR_DAMPENING = 1.0;
    final static double DEFAULT_ANGULAR_DAMPENING = 100.0;

    //Rendering Debug Stuff
    final static Color DEFUALT_FILL_COLOR = Color.white;
    final static Color DEFUALT_LINE_COLOR = Color.black;
    //final static double BALL_RADIUS = 0.028575;
    private Color ballColor = Color.WHITE;
    private Color lineColor = Color.BLACK;

    private Ball ball;

    /**
     * Create a physics ball of the specified color.
     * @param ballColor the color of the bal
     */
    public PhysicsBall(Color ballColor)
    {
        super();
        
        this.ballColor = ballColor;

        BodyFixture body = new BodyFixture(Geometry.createCircle(BALL_COLLISION_RADIUS));
        body.setDensity(BALL_DENSITY);
        body.setFriction(BALL_FRICTION);
        body.setRestitution(BALL_RESTITUTION);
        body.setFilter(GameObject.CollidingObject);
        body.setRestitutionVelocity(0.0);
        this.addFixture(body);
        this.setLinearDamping(1);
        this.setAngularDamping(100);
        this.setMass(MassType.NORMAL);

        ball = new Ball();
    }

    /**
     * Render method to render the ball to the screen
     * @param g the graphics2D object to render to
     * @param scale the scale to render the ball at.
     */
    @Override
    /* Mainly used for debugging on the server end */
    public void render(Graphics2D g, double scale) 
    {

        //save original transform
        AffineTransform ot = g.getTransform();
        
        // transform the coordinate system from world coordinates to local coordinates
        AffineTransform lt = super.toLocalCoordinates(scale);
        
        //apply the transformation to the graphics object
        g.transform(lt);
        
        //draw the object itself 
        double radius2 = 2.0 * Ball.BALL_RADIUS;
        Ellipse2D.Double circle = new Ellipse2D.Double(
                (this.getLocalCenter().x - radius) * scale,
                (this.getLocalCenter().y - radius) * scale,
                radius2 * scale,
                radius2 * scale);
        g.setColor(this.ballColor);
        g.fill(circle);
        
        g.setColor(this.lineColor);
        g.draw(circle);
        
        //return graphics to its original transform
        g.setTransform(ot);
    }

    /**
     * Gets the ball stored in teh physics object
     * @return
     */
    public Ball getBall()
    {
        return this.ball;
    }

    /**
     * Updates the physics' ball location based on the ball object's location
     */
    public void updateBallPosition()
    {
        Vector2 objectLocation = this.getWorldPoint(this.getLocalCenter());
        this.ball.setPosition(objectLocation.x, objectLocation.y);
    }
}
