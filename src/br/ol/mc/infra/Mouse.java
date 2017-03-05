package br.ol.mc.infra;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Mouse class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Mouse extends MouseAdapter {
    
    private static Game game;
    public static int x;
    public static int y;
    public static boolean pressed;
    public static boolean pressedConsumed;

    public Mouse(Game game) {
        Mouse.game = game;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed = false;
        pressedConsumed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = (int) (e.getX() / game.screenScale.getX());
        y = (int) (e.getY() / game.screenScale.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = (int) (e.getX() / game.screenScale.getX());
        y = (int) (e.getY() / game.screenScale.getY());
    }
    
    public static boolean pressed() {
        if (pressed && !pressedConsumed) {
            pressedConsumed = true;
            return true;
        }
        return false;
    } 
    
}
