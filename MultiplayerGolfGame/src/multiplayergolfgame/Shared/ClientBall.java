package multiplayergolfgame.Shared;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.dyn4j.geometry.Transform;

import multiplayergolfgame.framework.GameObject;

/**
 * A ball that the client uses to render it's balls.
 * @author James Eastwood
 */
public class ClientBall extends GameObject {
    
    final static Color DEFUALT_FILL_COLOR = Color.white;
    final static Color DEFUALT_LINE_COLOR = Color.black;
    final static double BALL_RADIUS = 0.028575;

    private Color ballColor;
    private Color lineColor = Color.BLACK;

    /**
     *
     * @param ball
     * @param ballColor
     */
    public ClientBall(Ball ball, Color ballColor)
    {
        Transform transform = new Transform();
        transform.setTranslation(ball.getPosition().x, ball.getPosition().y);
        this.setTransform(transform);

        this.ballColor = ballColor;
    }

    /**
     *
     * @param g
     * @param scale
     */
    @Override
    public void render(Graphics2D g, double scale) {        
        //save original transform
        AffineTransform ot = g.getTransform();
        AffineTransform lt = super.toLocalCoordinates(scale);
        
        //apply the transformation to the graphics object
        g.transform(lt);
        
        //draw the object itself 
        double radius2 = 2.0 * BALL_RADIUS;
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
}
