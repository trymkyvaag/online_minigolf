/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multiplayergolfgame.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author trymkyvag
 */
public class BufferedImageLoader {
    private BufferedImage img;

    /**
     * Loads a buffered image
     * @param path the filepath of the iamge to load
     * @return the buffered image
     * @throws IOException if file not found
     */
    public BufferedImage loadImage(String path) throws IOException{
        img = ImageIO.read(new File(path));
        return img;
    }
    
}
