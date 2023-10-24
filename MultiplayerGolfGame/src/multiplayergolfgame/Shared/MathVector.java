package multiplayergolfgame.Shared;

import java.io.Serializable;

/**
 * Simple X Y Vector.
 * @author James
 */
public class MathVector implements Serializable
{

    /**
     * x component
     */
    public double x;

    /**
     * y component
     */
    public double y;
    
    /**
     * creates a x,y vector of the specified values
     * @param x x component
     * @param y y component
     */
    public MathVector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString()
    {
        return String.format("(%.2f, %.2f)", this.x, this.y);
    }
}
