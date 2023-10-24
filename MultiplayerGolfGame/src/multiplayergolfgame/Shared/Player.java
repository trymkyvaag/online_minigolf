package multiplayergolfgame.Shared;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author James Eastwood
 */
public class Player implements Serializable, Comparable<Player>
{
    private String nickname;
    private Ball ball;
    private Color ballColor;
    private int score;
    private boolean isReady;

    /**
     * Create a player with the associated nickname and ball color.
     * @param nickname the name of the player
     * @param color the color of the player's ball
     */
    public Player(String nickname, Color color)
    {
        this.nickname = nickname;
        this.ballColor = color;

        this.score = 0;
        this.isReady = false;
        
        this.ball = null;
    }

    /**
     * Gets the nickname associated with this player
     * @return the name
     */
    public String getNickname()
    {
        return nickname;
    }

    /**
     * The nickname of the parent
     * @param name
     */
    public void setNickname(String name)
    {
        this.nickname = name;
    }

    /**
     * gets the ball color associated with this player.
     * @return
     */
    public Color getBallColor()
    {
        return this.ballColor;
    }

    /**
     * sets teh player's ball color
     * @param color the color
     */
    public void setBallColor(Color color)
    {
        this.ballColor = color;
    }

    /**
     * Sets the ball 
     * @param ball the ball to set
     */
    public void setBall(Ball ball)
    {
        this.ball = ball;
    }

    /**
     * Gets the ball
     * @return
     */
    public Ball getBall()
    {
        return this.ball;
    }

    /**
     * Sets teh score of the player
     * @param score
     */
    public void setScore(int score)
    {
        this.score = score;
    }

    /**
     * Gets the score of the player
     * @return
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * gets whether the player is ready or not
     * @return true if ready, falase otherwise
     */
    public boolean isReady()
    {
        return this.isReady;
    }

    /**
     * sets whether the player is ready or not
     * @param isReady true if they are ready, false otherwise
     */
    public void setReady(boolean isReady)
    {
        this.isReady = isReady;
    }

    /**
     * Updates this player using data recieved from a player in a packet
     * Updates score and isReady
     * @param playerFromPacket the player to use to update this player
     */
    public void update(Player playerFromPacket)
    {
        this.score = playerFromPacket.getScore();
        this.isReady = playerFromPacket.isReady();
    }


    @Override
    public String toString()
    {
        return String.format("%s(#%06X)[%d]{rdy:%s}", nickname, ballColor.getRGB() & 0xFFFFFF, score, Boolean.toString(isReady));
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Player)
        {
            Player other = (Player) obj;
            if(other.getNickname().equals(this.nickname) && other.getBallColor().equals(this.ballColor))
                return true;
        }
        return false;
    }

    @Override
    public int compareTo(Player o) {
        return Integer.compare(score, o.getScore());
    }
}
