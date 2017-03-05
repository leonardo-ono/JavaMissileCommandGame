package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;

/**
 * EnemyUFO class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class EnemyUFO extends MissileCommandActor {
    
    private int direction; // 0 = left, 1 = right
    private boolean hit;
    private long hitTime;
    
    public EnemyUFO(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/enemy_ufo_0.png", "/res/enemy_ufo_1.png");
        setUseOverrideColor(false);
        setUseXorColor(true);
    }

    @Override
    public void updatePlaying() {
        int frameIndex = (int) ((System.nanoTime() * 0.0000001) % 2);
        setFrame(frameIndex);
        if (direction == 0) {
            translate(-0.5, 0);
            if (getX() < 0) {
                setVisible(false);
            }
        }
        else if (direction == 1) {
            translate(0.5, 0);
            if (getX() > 256) {
                setVisible(false);
            }
        }
        if (hit && System.currentTimeMillis() - hitTime > 200) {
            game.addScore(100);
            setVisible(false);
            game.spawnExplosion((int) (getX() + getFrame().getWidth() / 2), (int) (getY() + getFrame().getHeight() / 2), true);
        }
    }

    @Override
    public void onCollision() {
        if (hit) {
            return;
        }
        hit = true;
        hitTime = System.currentTimeMillis();
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        if (game.getState() == State.TITLE
                || game.getState() == State.LEVEL_CLEARED 
                || game.getState() == State.GAME_OVER) {
            setVisible(false);
        }
        setXorColor(game.getEnemyUFOColor());
    }
    
    public void spawn() {
        this.direction = (int) (2 * Math.random());
        if (direction == 0) {
            setX(300 + (500 * Math.random()));
        }
        else if (direction == 1) {
            setX(-50 - (500 * Math.random()));
        }
        setY(150 * Math.random());
        hit = false;
        setVisible(true);
    }
    
}
