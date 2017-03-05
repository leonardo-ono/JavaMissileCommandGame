package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;

/**
 * Initialization class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Initialization extends MissileCommandActor {

    public Initialization(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        setVisible(true);
    }

    @Override
    public void updateInitializing() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 1;
                case 1:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    game.setState(State.OL_PRESENTS);
                    break yield;
            }
        }
    }

}
