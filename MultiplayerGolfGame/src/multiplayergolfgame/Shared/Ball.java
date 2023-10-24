package multiplayergolfgame.Shared;

import java.io.Serializable;

/**
 *
 * @author James Eastwood
 */
public class Ball implements Serializable
{
    private MathVector position;

    /**
     *
     */
    public final static double BALL_RADIUS =  0.028575;
        
    /**
     * Constructor
     */
    public Ball()
    {
        this(0, 0);
        
    }

   /**
    * Constructor given position
    * @param posX
    * @param posY 
    */
    public Ball(double posX, double posY)
    {
        position = new MathVector(posX, posY);
    }

    /**
     * Set position based of xy
     * @param x
     * @param y 
     */
    public void setPosition(double x, double y)
    {
        position.x = x;
        position.y = y;
    }

    /**
     * Set position based of vector
     * @param position 
     */
    public void setPostion(MathVector position)
    {
        this.position = new MathVector(position.x, position.y);
    }

    /**
     * getter
     * @return vector
     */
    public MathVector getPosition()
    {
        return position;
    }
    
    
}