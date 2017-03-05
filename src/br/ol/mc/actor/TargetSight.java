package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.infra.Mouse;
import java.awt.Point;

/**
 * TargetSight class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class TargetSight extends MissileCommandActor {
    
    private static final int MAX_Y = 180;
    
    public TargetSight(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/target_sight.png");
        setUseOverrideColor(false);
        setZorder(100);
    }
    
    // -- auto play in 'title' state ---
    private MissileFired enemyMissileFired;
    @Override
    public void updateTitle() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    set(game.screenSize.width / 2, 188);
                    instructionPointer = 1;
                case 1:
                    enemyMissileFired = game.getActiveMissileEnemyRandomically();
                    if (enemyMissileFired == null) {
                        break yield;
                    }
                    instructionPointer = 2;
                case 2:
                    Point interceptionPoint = enemyMissileFired.calculateInterceptPoint(game.getMissileAllyVelocity());
                    double dx = interceptionPoint.x - getX();
                    double dy = interceptionPoint.y - getY();
                    
                    if (!enemyMissileFired.isVisible() || enemyMissileFired.getY() > MAX_Y) {
                        instructionPointer = 1;
                        break yield;
                    }
                    if (Math.abs(dx) < 1 && Math.abs(dy) < 1) {
                        instructionPointer = 3;
                        break yield;
                    }
                    
                    double size = Math.sqrt(dx * dx + dy * dy);
                    dx /= size;
                    dy /= size;
                    translate(2 * dx, 2 * dy);
                    limitY();
                    break yield;
                case 3:
                    if (Math.random() < 0.1) {
                        game.fireMissile((int) getX() + 3, (int) getY() + 3);
                    }
                    else {
                        instructionPointer = 2;
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 4;
                case 4:
                    if (System.currentTimeMillis() - waitTime < 1000) {
                        break yield;
                    }
                    instructionPointer = 1;
                    break yield;
            }
        }
    }

    @Override
    public void updatePlaying() {
        set(Mouse.x - 3, Mouse.y - 3);
        limitY();
        if (Mouse.pressed()) {
            game.fireMissile((int) getX() + 3, (int) getY() + 3);
        }
        if (game.getLevelManager().isFinished()) {
            if (game.getNextAvailableCity() == null) {
                game.startGameOver();
            }
            else {
                game.levelCleared();
            }
        }
    }

    private void limitY() {
        setY(getY() > MAX_Y ? MAX_Y : getY());
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(game.getState() == MissileCommandGame.State.TITLE
            || game.getState() == MissileCommandGame.State.PLAYING);
    }
    
}
