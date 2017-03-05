package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Explosion class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Explosion extends MissileCommandActor {
    
    private long startTime;
    private double desiredRadius;
    private double currentRadius;
    private boolean currentRadiusDecreasing;
    private double desiredMaskRadius;
    private double currentMaskRadius;
    private boolean maskRadiusDecreasing;
    
    // spawn for 'intro' state
    public Explosion(MissileCommandGame game, int x, int y) {
        super(game);
        startTime = System.currentTimeMillis();
        currentRadius = 3 + (int) (8 * Math.random());
        instructionPointer = 0;
        set(x, y);
        setUseOverrideColor(true);
        setVisible(true);
    }

    public double getCurrentRadius() {
        return currentRadius;
    }
    
    // spawn for 'title' & 'playing' states
    public Explosion(MissileCommandGame game, int x, int y, int maxRadius, boolean affectedByMask) {
        super(game);
        desiredRadius = desiredMaskRadius = maxRadius;
        setAffectedByMask(affectedByMask);
        setUseXorColor(false);
        set(x, y);
        setUseOverrideColor(true);
        setVisible(true);
    }

    @Override
    public void init() {
        setZorder(99);
    }

    @Override
    public void updateIntro() {
        setOverrideColor(game.getExplosionColor());
        if (System.currentTimeMillis() - startTime < 250) {
            return;
        }
        setVisible(false);
    }

    @Override
    public void updateTitle() {
        updateExplosion();
    }
    
    @Override
    public void updatePlaying() {
        updateExplosion();
    }

    public void updateExplosion() {
        setOverrideColor(game.getExplosionColor());
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    while (System.currentTimeMillis() - waitTime < 0) {
                        break yield;
                    }
                    currentRadius = currentMaskRadius = 0;
                    currentRadiusDecreasing = false;
                    maskRadiusDecreasing = false;
                    instructionPointer = 2;
                case 2:
                    currentRadius++;
                    currentMaskRadius++;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    while (System.currentTimeMillis() - waitTime < 50) {
                        break yield;
                    }
                    instructionPointer = 2;
                    if (currentRadius >= desiredRadius) {
                        currentRadiusDecreasing = true;
                        instructionPointer = 4;
                    }
                    break yield;
                case 4:
                    currentRadius--;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 5;
                case 5:
                    while (System.currentTimeMillis() - waitTime < 50) {
                        break yield;
                    }
                    instructionPointer = 4;
                    if (currentRadius <= 0) {
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 6;
                    }
                    break yield;
                case 6:
                    while (System.currentTimeMillis() - waitTime < 500) {
                        break yield;
                    }
                    maskRadiusDecreasing = true;
                    instructionPointer = 7;
                    break yield;
                case 7:
                    currentMaskRadius--;
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 8;
                case 8:
                    while (System.currentTimeMillis() - waitTime < 50) {
                        break yield;
                    }
                    instructionPointer = 7;
                    if (currentMaskRadius <= 0) {
                        instructionPointer = 9;
                    }
                    break yield;
                case 9:
                    game.removeActor(this);
                    instructionPointer = 10;
                case 10:
                    break yield;
            }
        }
    }

    @Override
    protected void draw(Graphics2D g) {
        if (game.getState() == State.INTRO) {
            g.fillOval((int) (getX() - currentRadius), (int) (getY() - currentRadius), (int) (2 * currentRadius), (int) (2 * currentRadius));
        }
    }
    
    @Override
    public void drawExplosions(Graphics2D g) {
        if (game.getState() != State.INTRO) {
            g.setColor(game.getExplosionColor());
            g.fillOval((int) (getX() - currentRadius), (int) (getY() - currentRadius), (int) (2 * currentRadius), (int) (2 * currentRadius));
            
            game.getMissilePathG().setColor(Color.BLACK);
            game.getMissilePathG().fillOval((int) (getX() - currentRadius), (int) (getY() - currentRadius), (int) (2 * currentRadius), (int) (2 * currentRadius));
        }
    }

    @Override
    public void drawMask(Graphics2D g) {
        if (game.getState() == State.INTRO) {
            g.fillOval((int) (getX() - currentRadius), (int) (getY() - currentRadius), (int) (2 * currentRadius), (int) (2 * currentRadius));
        }
        else {
            g.setXORMode(Color.BLACK);
            g.setColor(Color.WHITE);
            g.fillOval((int) (getX() - currentMaskRadius), (int) (getY() - currentMaskRadius), (int) (2 * currentMaskRadius), (int) (2 * currentMaskRadius));
            g.setPaintMode();
        }
    }
    
    @Override
    public boolean checkPointCollision(int sx, int sy) {
        try {
            int currentMaskColor = game.getMask().getRGB(sx, sy);
            boolean maskColorCheck = isMaskInverted() ? currentMaskColor == 0xFF000000 : currentMaskColor == 0xFFFFFFFF;
            boolean maskCheck = !isUseXorColor() || (isUseXorColor() && maskColorCheck);
            double x1 = sx - getX();
            double y1 = sy - getY();
            double d1 = Math.sqrt(x1 * x1 + y1 * y1);
            return maskCheck && d1 <= currentRadius;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        if (game.getState() == State.TITLE) {
            game.removeActor(this);
        }
    }
    
}
