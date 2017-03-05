package br.ol.mc.infra;

import java.awt.image.BufferedImage;

/**
 * PixelCollider class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class PixelCollider {

    private Actor actor;
    private BufferedImage image;
    private int nonCollidingColor = 0xFF000000;

    public PixelCollider() {
    }

    public PixelCollider(Actor actor) {
        this.actor = actor;
    }
    
    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getNonCollidingColor() {
        return nonCollidingColor;
    }

    public void setNonCollidingColor(int nonCollidingColor) {
        this.nonCollidingColor = nonCollidingColor;
    }

    public boolean isInside(int px, int py) {
        int ix = px - (int) actor.getX();
        int iy = py - (int) actor.getY();
        if (ix < 0 || ix > image.getWidth() - 1 || iy < 0 || iy > image.getHeight() - 1) {
            return false;
        }
        return (nonCollidingColor != image.getRGB(ix, iy));
    }
    
}
