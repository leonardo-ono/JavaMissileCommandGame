package br.ol.mc.actor;


import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import java.awt.Color;

/**
 * Intro class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Intro extends MissileCommandActor {
    
    private long explosionsStartTime;
    
    public Intro(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/intro.png");
        setAffectedByMask(true);
        setMaskInverted(true);
        setUpdatebleJustWhenVisible(false);
    }
    
    @Override
    public void updateIntro() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    setRandomColorIndex((game.getExplosionColorIndex() + 4) % game.getColors().length);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 1500) {
                        break yield;
                    }
                    setVisible(true);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    while (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    setAutomaticRandomColorUpdate(true);
                    waitTime = explosionsStartTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 10) {
                        break yield;
                    }
                    int ex = (int) (game.screenSize.width * Math.random());
                    int ey = (int) ((game.screenSize.height - 50) * Math.random());
                    game.spawnIntroExplosion(ex, ey);
                    waitTime = System.currentTimeMillis();
                    if (System.currentTimeMillis() - explosionsStartTime > 5000) {
                        setVisible(false);
                        instructionPointer = 4;
                    }
                    break yield;
                case 4:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    game.setState(State.TITLE);
                    break yield;
            }
        }
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(false);
        if (game.getState() == State.INTRO) {
            game.clearMask();
            game.clearMissilePath();
            game.clearExplosionLayer();
            setOverrideColor(Color.RED);
            setAutomaticRandomColorUpdate(false);
            instructionPointer = 0;
        }
    }
        
}
