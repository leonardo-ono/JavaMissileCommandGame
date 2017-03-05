package br.ol.mc;


import br.ol.mc.actor.Explosion;
import br.ol.mc.infra.Actor;
import br.ol.mc.infra.PixelCollider;
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * MissileCommandActor class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class MissileCommandActor extends Actor<MissileCommandGame> {
    
    private final MCComposite composite = new MCComposite();
    
    private Color overrideColor = Color.WHITE;
    private boolean useOverrideColor = true;
    private boolean automaticRandomColorUpdate = false;
    private double randomColorIndex;
    
    private boolean useXorColor = false;
    private Color xorColor = Color.BLACK;
    
    private BufferedImage frame;
    private BufferedImage[] frames;
    
    private boolean drawMask = false;
    private boolean affectedByMask = false;
    private boolean maskInverted = false;
    
    private PixelCollider collider;
    
    public MissileCommandActor(MissileCommandGame game) {
        super(game);
        collider = new PixelCollider(this);
        randomColorIndex = 4;
    }

    public Color getOverrideColor() {
        return overrideColor;
    }

    public void setOverrideColor(Color color) {
        this.overrideColor = color;
    }

    public BufferedImage getFrame() {
        return frame;
    }

    public void setFrame(int frameIndex) {
        this.frame = frames[frameIndex];
        collider.setImage(this.frame);
    }

    public boolean isUseOverrideColor() {
        return useOverrideColor;
    }

    public void setUseOverrideColor(boolean useComposite) {
        this.useOverrideColor = useComposite;
    }

    public boolean isAutomaticRandomColorUpdate() {
        return automaticRandomColorUpdate;
    }

    public void setAutomaticRandomColorUpdate(boolean automaticRandomColorUpdate) {
        this.automaticRandomColorUpdate = automaticRandomColorUpdate;
    }

    public double getRandomColorIndex() {
        return randomColorIndex;
    }

    public void setRandomColorIndex(double randomColorIndex) {
        this.randomColorIndex = randomColorIndex;
    }

    public boolean isUseXorColor() {
        return useXorColor;
    }

    public void setUseXorColor(boolean useXorColor) {
        this.useXorColor = useXorColor;
    }

    public Color getXorColor() {
        return xorColor;
    }

    public void setXorColor(Color xorColor) {
        this.xorColor = xorColor;
    }

    public BufferedImage[] getFrames() {
        return frames;
    }

    public void setFrames(BufferedImage[] frames) {
        this.frames = frames;
    }

    public boolean isDrawMask() {
        return drawMask;
    }

    public void setDrawMask(boolean drawMask) {
        this.drawMask = drawMask;
    }

    public boolean isAffectedByMask() {
        return affectedByMask;
    }

    public void setAffectedByMask(boolean affectedByMask) {
        this.affectedByMask = affectedByMask;
    }

    public boolean isMaskInverted() {
        return maskInverted;
    }

    public void setMaskInverted(boolean maskInverted) {
        this.maskInverted = maskInverted;
    }

    public PixelCollider getCollider() {
        return collider;
    }

    public void setCollider(PixelCollider collider) {
        this.collider = collider;
    }
   
    public void drawMask(Graphics2D g) {
    }

    protected void internalDrawExplosions(Graphics2D g) {
        Composite originalComposite = g.getComposite();
        g.setComposite(composite);
        drawExplosions(g);
        g.setComposite(originalComposite);
    }
    
    public void drawExplosions(Graphics2D g) {
    }
    
    @Override
    protected void internalDraw(Graphics2D g) {
        if (!isVisible()) {
            return;
        }
        Composite originalComposite = g.getComposite();
        g.setComposite(composite);
        draw(g);
        g.setComposite(originalComposite);
    }

    @Override
    protected void draw(Graphics2D g) {
        if (frame != null) {
            g.drawImage(frame, (int) getX(), (int) getY(), null);
        }
    }
    
    public void loadFrames(String ... resources) {
        frames = new BufferedImage[resources.length];
        int frameIndex = 0;
        for (String resource : resources) {
            try {
                InputStream is = getClass().getResourceAsStream(resource);
                frames[frameIndex++] = ImageIO.read(is);
            } catch (IOException ex) {
                Logger.getLogger(MissileCommandActor.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1);
            }
        }
        setFrame(0);
    }

    private class MCComposite implements Serializable, Composite {
        
        @Override
        public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
            return new MCCompositeContext();
        }
        
        private class MCCompositeContext implements CompositeContext {
            
            int[] srcData = new int[4];
            int[] dstData = new int[4];
            int[] finalData = new int[4];

            @Override
            public void dispose() {
            }

            @Override
            public void compose(Raster src, Raster dst, WritableRaster dstOut) {
                for (int y = 0; y < dst.getHeight(); y++) {
                    for (int x = 0; x < dst.getWidth(); x++) {
                        int sx = (int) ((x - dst.getSampleModelTranslateX()));
                        int sy = (int) ((y - dst.getSampleModelTranslateY()));
                        int currentMaskColor = game.getMask().getRGB(sx, sy);
                        boolean insideMask = maskInverted ? currentMaskColor == 0xFF000000 : currentMaskColor == 0xFFFFFFFF;
                        if (!affectedByMask || (affectedByMask && insideMask)) {
                            src.getPixel(x, y, srcData);
                            dst.getPixel(x, y, dstData);
                            if (srcData[0] != 0 || srcData[1] != 0 || srcData[2] != 0) {
                                if (useOverrideColor) {
                                    finalData[0] = combineWithXor(overrideColor.getRed(), dstData[0], xorColor.getRed());
                                    finalData[1] = combineWithXor(overrideColor.getGreen(), dstData[1], xorColor.getGreen());
                                    finalData[2] = combineWithXor(overrideColor.getBlue(), dstData[2], xorColor.getBlue());
                                    finalData[3] = 255;
                                    dstOut.setPixel(x, y, finalData);
                                }
                                else {
                                    finalData[0] = combineWithXor(srcData[0], dstData[0], xorColor.getRed());
                                    finalData[1] = combineWithXor(srcData[1], dstData[1], xorColor.getGreen());
                                    finalData[2] = combineWithXor(srcData[2], dstData[2], xorColor.getBlue());
                                    finalData[3] = 255;
                                    dstOut.setPixel(x, y, finalData);
                                }
                            }
                        }                        
                    }
                }
            }
            
            private int combineWithXor(int srcColor, int dstColor, int xorColor) {
                if (useXorColor) {
                    return srcColor ^ dstColor ^ xorColor;
                }
                return srcColor;
            }
        }
    }    
    
    // TODO replace with more generic collision detection method later
    public boolean checkCollidesWithExplosion(Explosion explosion) {
        double circleDistanceX = Math.abs(explosion.getX() - (getX() + 2));
        double circleDistanceY = Math.abs(explosion.getY() - (getY() + 2));

        double rectWidth = getFrame().getWidth() - 4;
        double rectHeight = getFrame().getHeight() - 4;
        
        if (circleDistanceX > (rectWidth/2 + explosion.getCurrentRadius())) { 
            return false; 
        }
        if (circleDistanceY > (rectHeight/2 + explosion.getCurrentRadius())) { 
            return false; 
        }

        if (circleDistanceX <= (rectWidth/2)) { 
            return true; 
        } 
        if (circleDistanceY <= (rectHeight/2)) { 
            return true; 
        }

        double cornerDistance_sq = Math.pow(circleDistanceX - rectWidth/2, 2) +
                             Math.pow(circleDistanceY - rectHeight/2, 2);

        return (cornerDistance_sq <= Math.pow(explosion.getCurrentRadius(), 2));        
    }
    
    public boolean checkPointCollision(int sx, int sy) {
        return collider.isInside(sx, sy);
    }
    
    public void onCollision() {
    }
    
    // --- updates ---
    
    @Override
    public void update() {
        if (automaticRandomColorUpdate) {
            updateRandomColor();
        }
        switch (game.getState()) {
            case INITIALIZING: updateInitializing(); break;
            case OL_PRESENTS: updateOLPresents(); break;
            case INTRO: updateIntro(); break;
            case TITLE: updateTitle(); break;
            case READY: updateReady(); break;
            case PLAYING: updatePlaying(); break;
            case LEVEL_CLEARED: updateLevelCleared(); break;
            case GAME_OVER: updateGameOver(); break;
        }
    }

    public void updateInitializing() {
    }

    public void updateOLPresents() {
    }

    public void updateIntro() {
    }

    public void updateTitle() {
    }

    public void updateReady() {
    }

    public void updatePlaying() {
    }

    public void updateLevelCleared() {
    }

    public void updateGameOver() {
    }
    
    public void updateRandomColor() {
        randomColorIndex = (randomColorIndex + 0.2) % game.getColors().length;
        setOverrideColor(game.getColors()[(int) randomColorIndex]);
    }        
    
    // --- broadcast messages ---
        
    public void stateChanged() {
    }    
    
}
