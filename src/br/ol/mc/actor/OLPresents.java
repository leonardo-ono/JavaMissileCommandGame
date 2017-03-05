package br.ol.mc.actor;


import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * OLPresents class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class OLPresents extends MissileCommandActor {
    
    private final String text = "O.L. PRESENTS";
    private int textIndex;

    public OLPresents(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        setUpdatebleJustWhenVisible(false);
    }
    
    @Override
    public void updateOLPresents() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 100) {
                        break yield;
                    }
                    textIndex++;
                    if (textIndex < text.length()) {
                        instructionPointer = 0;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    while (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    setVisible(false);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    while (System.currentTimeMillis() - waitTime < 100) {
                        break yield;
                    }
                    game.setState(State.INTRO);
                    break yield;
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        game.drawText(g, text.substring(0, textIndex), 76, 111, Color.WHITE);
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(false);
        if (game.getState() == State.OL_PRESENTS) {
            setVisible(true);
            textIndex = 0;
        }
    }
        
}
