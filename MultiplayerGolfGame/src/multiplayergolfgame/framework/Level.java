package multiplayergolfgame.framework;

import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.Random;
import multiplayergolfgame.Shared.Hole;
import multiplayergolfgame.Shared.Spawn;
import multiplayergolfgame.Shared.Wall;
import org.dyn4j.geometry.Transform;
import org.dyn4j.world.World;

/**
 * Class for creating a level and placing objects within it
 *  Inspiration: https://www.youtube.com/watch?v=1TFDOT1HiBo&t=242s
 */
public class Level extends World<GameObject> {

    /**
     *
     */
    public static int WIDTH,

    /**
     *
     */
    HEIGHT;
    
    private BufferedImage lvl = null;

    private int currentLevelID;
    private Transform spawnTransform;

    /**
     * Constructor that takes the number of the level as a param
     *
     * @param levelID
     */
    public Level(int levelID) {
        super();
        this.setGravity(World.ZERO_GRAVITY);

        currentLevelID = levelID;

        //init the wanted level
        init(levelID);
    }

    /**
     * Get current level
     *
     * @return ID of current level
     */
    public int getCurrentLevelID() {
        return this.currentLevelID;
    }

    /**
     * init level based of ID
     *
     * @param levelID, ID of wanted level
     */
    private void init(int levelID) {
        //block h/w for level
        WIDTH = 32;
        HEIGHT = 32;

        /*
        if level id is -99, call a random one
        otherwise choose level with corresponding id
         */
        if(levelID == -1)
            return; //Lobby Level
        if(levelID == -99)
            RandLevel(); 
        else {
            levelChooser(levelID);
        }

    }
    /**
     * Calls a random level
     */
    public void RandLevel() {
        Random rand = new Random(); //instance of random class

        //generate random values from 0-10
        int int_random = rand.nextInt(10);
        //Do not include testlevel 0
        while (int_random == 0) {
            int_random = rand.nextInt(10);
        }

        //Choose a random level and set it to current
        levelChooser(int_random);
        currentLevelID = int_random;
    }

    /**
     * Gets the level from the Images
     * @param i 
     */
    public void levelChooser(int i) {
        
        //Helper class for getting the image
        BufferedImageLoader loader = new BufferedImageLoader();
        
        //make sure the id is valid
        assert (0 < i && i < 10);

        /*
        Get the correct level
        */
        try {
            System.out.println("Loading Level" + "levels/Level" + i + ".png");
            lvl = loader.loadImage("levels/Level" + i + ".png");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //load the correct bufferedImage
        loadImage(lvl);

    }

    /**
     * takes a bufferedImage and creates a world based of pixelCOlor
     * @param img, The image to create 
     */
    private void loadImage(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        /*
        Traverse pixel and place objects based of color found
        Translate the position to match canvas
        */
        for (int xx = 0; xx < h; xx++) {
            for (int yy = 0; yy < w; yy++) {
                int pix = img.getRGB(xx, yy);

                int red = (pix >> 16) & 0xff;
                int green = (pix >> 8) & 0xff;
                int blue = (pix) & 0xff;

                if (red == 153 && green == 102 && blue == 51) {
                    //System.out.println("Walls");

                    Wall wall = new Wall();

                    wall.translate(Wall.WALL_WIDTH * xx - 1.32, Wall.WALL_HEIGHT * yy - 1.32);
                   

                    this.addBody(wall);

                    // System.out.println(xx + " , " + yy);
                }
                if (red == 0 && green == 0 && blue == 0) {
                    Hole hole = new Hole();

                    hole.translate(Wall.WALL_WIDTH * xx - 1.32, Wall.WALL_HEIGHT * yy - 1.32);

                    this.addBody(hole);
                }
                if (red == 255 && green == 255 && blue == 255) {

                    Spawn spawn = new Spawn();

                    spawn.translate(Wall.WALL_WIDTH * xx - 1.32, Wall.WALL_HEIGHT * yy - 1.32);
                    spawnTransform = spawn.getTransform();
                    this.addBody(spawn);

                }
            }
        }
    }
    
    /**
     * Gets the transform/location of the level spawn
     * @return the transform of the spawn
     */
    public Transform getSpawnTransform()
    {
        return spawnTransform;
    }
}
