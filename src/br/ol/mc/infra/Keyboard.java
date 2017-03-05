package br.ol.mc.infra;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Keyboard class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Keyboard extends KeyAdapter {

    private static final int MAX_KEY_CODE_SIZE = 256;
    public static boolean[] keyPressed = new boolean[MAX_KEY_CODE_SIZE];
    public static boolean[] keyPressedConsumed = new boolean[MAX_KEY_CODE_SIZE];
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() > MAX_KEY_CODE_SIZE - 1) {
            return;
        }
        keyPressed[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() > MAX_KEY_CODE_SIZE - 1) {
            return;
        }
        keyPressed[e.getKeyCode()] = false;
        keyPressedConsumed[e.getKeyCode()] = false;
    }
    
}
