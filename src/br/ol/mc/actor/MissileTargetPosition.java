package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;

/**
 * MissileTargetPosition class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class MissileTargetPosition extends MissileCommandActor {
    
    public MissileTargetPosition(MissileCommandGame game, int x, int y) {
        super(game);
        set(x - 3, y - 3);
    }

    @Override
    public void init() {
        loadFrames("/res/missile_target_position.png");
        setUseOverrideColor(true);
        setAutomaticRandomColorUpdate(true);
        setZorder(99);
        setVisible(true);
    }

}
