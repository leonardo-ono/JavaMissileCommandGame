package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;

/**
 * TerrainFireBase class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class TerrainFireBase extends MissileCommandActor {
    
    private int index;
    private static final int[] position = { 0, 6, 114, 228 };
    
    public TerrainFireBase(MissileCommandGame game, int index) {
        super(game);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void init() {
        loadFrames("/res/terrain_fire_base_" + index + ".png");
        setUseOverrideColor(true);
        set(position[index], 205);
    }

    @Override
    public void onCollision() {
        game.destroyAllAvailableMissileAlly(index - 1);
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
