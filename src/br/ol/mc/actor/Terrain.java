package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;

/**
 * Terrain class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Terrain extends MissileCommandActor {

    public Terrain(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/terrain.png");
        setUseOverrideColor(true);
        set(0, 211);
    }

    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(game.getState() == MissileCommandGame.State.TITLE
            || game.getState() == MissileCommandGame.State.READY
            || game.getState() == MissileCommandGame.State.PLAYING
            || game.getState() == MissileCommandGame.State.LEVEL_CLEARED);
        setOverrideColor(game.getTerrainColor());
    }
    
}
