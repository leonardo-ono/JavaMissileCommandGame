package br.ol.mc.infra;

import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 * BitmapFontRenderer class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class BitmapFontRenderer {
    
    public BufferedImage bitmapFontImage;
    public BufferedImage[] letters;
    
    public int letterWidth;
    public int letterHeight;
    public int letterVerticalSpacing = 0;
    public int letterHorizontalSpacing = 0;

    public Color overrideColor = Color.WHITE;
    private final FontColorComposite fontColorComposite = new FontColorComposite();
    
    public BitmapFontRenderer(String fontRes, int cols, int rows) {
        loadFont(fontRes, cols, rows);
    }
    
    public void drawText(Graphics2D g, String text, int x, int y, Color overrideColor) {
        if (letters == null) {
            return;
        }
        this.overrideColor = overrideColor;
        int px = 0;
        int py = 0;
        Composite originalComposite = g.getComposite();
        g.setComposite(fontColorComposite);
        for (int i=0; i<text.length(); i++) {
            int c = text.charAt(i);
            if (c == (int) '\n') {
                py += letterHeight + letterVerticalSpacing;
                px = 0;
                continue;
            }
            else if (c == (int) '\r') {
                continue;
            }
            Image letter = letters[c];
            g.drawImage(letter, (int) (px + x), (int) (py + y + 1), null);
            px += letterWidth + letterHorizontalSpacing;
        }
        g.setComposite(originalComposite);
    }

    private void loadFont(String filename, Integer cols, Integer rows) {
        try {
            bitmapFontImage = ImageIO.read(getClass().getResourceAsStream(filename));
            loadFont(bitmapFontImage, cols, rows);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void loadFont(BufferedImage image, Integer cols, Integer rows) {
        int lettersCount = cols * rows; 
        bitmapFontImage = image;
        letters = new BufferedImage[lettersCount];
        letterWidth = bitmapFontImage.getWidth() / cols;
        letterHeight = bitmapFontImage.getHeight() / rows;

        for (int y=0; y<rows; y++) {
            for (int x=0; x<cols; x++) {
                letters[y * cols + x] = new BufferedImage(letterWidth, letterHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D ig = (Graphics2D) letters[y * cols + x].getGraphics();
                ig.drawImage(bitmapFontImage, 0, 0, letterWidth, letterHeight
                        , x * letterWidth, y * letterHeight
                        , x * letterWidth + letterWidth, y * letterHeight + letterHeight, null);
            }
        }
    }

    private class FontColorComposite implements Serializable, Composite {
        
        @Override
        public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
            return new FontColorCompositeContext();
        }
        
        private class FontColorCompositeContext implements CompositeContext {
            
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
                        src.getPixel(x, y, srcData);
                        dst.getPixel(x, y, dstData);
                        if (srcData[0] != 0) {
                            finalData[0] = overrideColor.getRed();
                            finalData[1] = overrideColor.getGreen();
                            finalData[2] = overrideColor.getBlue();
                            finalData[3] = 255;
                            dstOut.setPixel(x, y, finalData);
                        }
                    }                        
                }
            }
        }
    }
    
}
