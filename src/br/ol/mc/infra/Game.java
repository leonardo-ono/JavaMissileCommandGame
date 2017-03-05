package br.ol.mc.infra;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Game class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Game {
    
    public Dimension screenSize = new Dimension(256, 231);
    public Point2D screenScale = new Point2D.Double(2, 2);
    
    public List<Actor> actors = new ArrayList<Actor>();
    public List<Actor> actorsAdd = new ArrayList<Actor>();
    public List<Actor> actorsRemove = new ArrayList<Actor>();

    public BitmapFontRenderer bitmapFontRenderer = new BitmapFontRenderer("/res/font8x8.png", 16, 16);

    public void init() {
    }
    
    public void addActor(Actor actor) {
        actorsAdd.add(actor);
        actor.init();
    }

    public void removeActor(Actor actor) {
        actorsRemove.add(actor);
    }
    
    public void update() {
        for (Actor actor : actors) {
            actor.internalUpdate();
        }
        if (!actorsAdd.isEmpty()) {
            actors.addAll(actorsAdd);
            actorsAdd.clear();
            Collections.sort(actors);
        }
        if (!actorsRemove.isEmpty()) {
            actors.removeAll(actorsRemove);
            actorsRemove.clear();
        }
    }
    
    public void draw(Graphics2D g) {
        for (Actor actor : actors) {
            actor.internalDraw(g);
        }
    }

    public void broadcastMessage(String message) {
        for (Actor obj : actors) {
            try {
                Method method = obj.getClass().getMethod(message);
                if (method != null) {
                    method.invoke(obj);
                }
            } catch (Exception ex) {
            }
        }
    }
    
    public void drawText(Graphics2D g, String text, int x, int y, Color overrideColor) {
        bitmapFontRenderer.drawText(g, text, x, y, overrideColor);
    }
    
}
