package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;

/**
 * GameOver class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class GameOver extends MissileCommandActor {
    
    private final Polygon bigExplosion = new Polygon();
    private double bigExplosionScale = 0.1;
    private double theEndBannerWidth = 0;
    
    public GameOver(MissileCommandGame game) {
        super(game);
        createBigExplosion();
    }
    
    private void createBigExplosion() {
        bigExplosion.reset();
        bigExplosion.addPoint(0, 10);
        bigExplosion.addPoint(7, 7);
        bigExplosion.addPoint(10, 0);
        bigExplosion.addPoint(7, -7);
        bigExplosion.addPoint(0, -10);
        bigExplosion.addPoint(-7, -7);
        bigExplosion.addPoint(-10, 0);
        bigExplosion.addPoint(-7, 7);
    }
    
    @Override
    public void init() {
        loadFrames("/res/game_over.png");
        setAutomaticRandomColorUpdate(true);
        setAffectedByMask(true);
    }

    @Override
    public void updateGameOver() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    theEndBannerWidth = 0;
                    bigExplosionScale = 0;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    bigExplosionScale += 0.15;
                    if (bigExplosionScale > 11) {
                        instructionPointer = 4;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 10) {
                        break yield;
                    }
                    instructionPointer = 2;
                    break yield;
                case 4:
                    theEndBannerWidth += 10;
                    if (theEndBannerWidth > 256) {
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 6;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 5;
                case 5:
                    if (System.currentTimeMillis() - waitTime < 5) {
                        break yield;
                    }
                    instructionPointer = 4;
                    break yield;
                case 6:
                    if (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    instructionPointer = 7;
                case 7:
                    bigExplosionScale -= 0.15;
                    if (bigExplosionScale < 0) {
                        bigExplosionScale = 0;
                        instructionPointer = 9;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 8;
                case 8:
                    if (System.currentTimeMillis() - waitTime < 10) {
                        break yield;
                    }
                    instructionPointer = 7;
                    break yield;
                case 9:
                    game.updateHiscore();
                    game.clearScore();
                    game.backToIntro();
                    break yield;
            }
        }
    }

    @Override
    protected void draw(Graphics2D g) {
        g.fillRect(0, 0, game.screenSize.width, game.screenSize.height);
    }

    @Override
    public void drawMask(Graphics2D g) {
        g.setColor(Color.WHITE);
        
        AffineTransform originalTransform = g.getTransform();
        g.translate(game.screenSize.width / 2, game.screenSize.height / 2);
        g.scale(bigExplosionScale, bigExplosionScale);
        g.fill(bigExplosion);
        g.setTransform(originalTransform);
        
        g.drawImage(getFrame(), 0, 0, (int) theEndBannerWidth, 231, 0, 0, (int) theEndBannerWidth, 231, null);
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(game.getState() == State.GAME_OVER);
        if (isVisible()) {
            instructionPointer = 0;
            game.setBackgroundColor(Color.RED);
        }
        else {
            game.setBackgroundColor(Color.BLACK);
        }
    }
    
}
