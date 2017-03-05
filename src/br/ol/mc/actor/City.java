package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;

/**
 * City class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class City extends MissileCommandActor {
    
    private boolean destroyed;
    
    public City(MissileCommandGame game, int x, int y) {
        super(game);
        set(x, y);
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void init() {
        loadFrames("/res/city.png");
        setUseOverrideColor(false);
    }

    @Override
    public void updateTitle() {
        updateCity();
    }

    @Override
    public void updatePlaying() {
        updateCity();
    }
    
    public void updateCity() {
        if (!destroyed) {
            return;
        }
        if (System.currentTimeMillis() - waitTime < 1000) {
            return;
        }
        setUseOverrideColor(true);
        setOverrideColor(game.getTerrainColor());
    }
    
    
    @Override
    public void onCollision() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        waitTime = System.currentTimeMillis();
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        if (game.getState() == MissileCommandGame.State.LEVEL_CLEARED) {
            return;
        }
        if (game.getState() == State.TITLE 
                || game.getState() == State.GAME_OVER) {
            restore();
        }
        setVisible(!isDestroyed() 
            && (game.getState() == State.TITLE
            || game.getState() == State.READY
            || game.getState() == State.PLAYING));
        
    }

    public void restore() {
        destroyed = false;
        setUseOverrideColor(false);
    }
    
}
