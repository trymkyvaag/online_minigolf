/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package multiplayergolfgame.framework;

import java.awt.Point;
import org.dyn4j.geometry.Vector2;

/**
 * Inspired by and uses part of Camera from org.dyn4j.framework.camera
 * Copyright (c) 2010-2021 William Bittle http://www.dyn4j.org/
 * 
 * Stores the state of the camera within the GamePanel.
 * @author ncrav
 */
public class Camera {
    //zoom factor in pixels per meter
    private double scale;
    //camera X offset in pixels 
    private double offsetX;
    //camera Y offset in pixels
    private double offsetY;

    /**
     *
     * @return
     */
    public double getScale() {
        return scale;
    }

    /**
     *
     * @param scale
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     *
     * @return
     */
    public double getOffsetX() {
        return offsetX;
    }

    /**
     *
     * @param offsetX
     */
    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    /**
     *
     * @return
     */
    public double getOffsetY() {
        return offsetY;
    }

    /**
     *
     * @param offsetY
     */
    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }
    
    /**
     * Sets the offset of the Camera in world space
     * @param offsetX the X position in pixels
     * @param offsetY the Y position in pixels
     */
    public void setOffset(double offsetX, double offsetY)
    {
        this.setOffsetX(offsetX);
        this.setOffsetY(offsetY);
    }

    /**
     * Returns
     * World coordinates for the given point given the width/height of the
     * viewport.
     *
     * @param width the viewport width
     * @param height the viewport height
     * @param p the point
     * @return Vector2
     */
    public final Vector2 toWorldCoordinates(double width, double height, Point p) {
        if (p != null) {
            Vector2 v = new Vector2();
            // convert the screen space point to world space
            v.x = (p.getX() - width * 0.5 - this.offsetX) / this.scale;
            v.y = -(p.getY() - height * 0.5 + this.offsetY) / this.scale;
            return v;
        }

        return null;
    }
}
