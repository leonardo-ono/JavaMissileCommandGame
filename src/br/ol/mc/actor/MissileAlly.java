package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import java.awt.Color;

/**
 * MissileAlly class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class MissileAlly extends MissileCommandActor {
    
    public MissileAlly(MissileCommandGame game, int x, int y) {
        super(game);
        set(x, y);
    }

    @Override
    public void init() {
        loadFrames("/res/missile_ally.png");
        setOverrideColor(Color.BLUE);
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        if (game.getState() == MissileCommandGame.State.LEVEL_CLEARED) {
            return;
        }
        setVisible(game.getState() == MissileCommandGame.State.TITLE
            || game.getState() == MissileCommandGame.State.READY
            || game.getState() == MissileCommandGame.State.PLAYING);
    }
    
}
