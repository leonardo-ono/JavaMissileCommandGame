package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * HUD class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class HUD extends MissileCommandActor {
    
    private final int missileAlliesPositions[] = {8, 114, 224};
    private boolean showArrowLeft;
    
    public HUD(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/arrow_left.png");
        setUseOverrideColor(false);
        setUpdatebleJustWhenVisible(false);
        setZorder(1000);
    }

    @Override
    public void updateReady() {
        updateArrowLeft();
    }
    
    @Override
    public void updatePlaying() {
        updateArrowLeft();
    }

    @Override
    public void updateLevelCleared() {
        updateArrowLeft();
    }

    private void updateArrowLeft() {
        showArrowLeft = ((int) (System.nanoTime() * 0.00000001)) % 10 > 2;        
    }
    
    @Override
    protected void draw(Graphics2D g) {
        game.drawText(g, game.getScoreStr(), 16, 0, game.getFontColor1());
        game.drawText(g, game.getHiscoreStr(), 88, 0, game.getFontColor1());
        
        if (game.getState() == State.PLAYING) {
            for (int i = 0; i < 3; i++) {
                int availableMissilesAlliesCount = game.getAvailableMissilesAlliesCount(i);
                if (availableMissilesAlliesCount == 0) {
                    game.drawText(g, "OUT", missileAlliesPositions[i], 223, game.getFontColor2());
                }
                else if (availableMissilesAlliesCount < 4) {
                    game.drawText(g, "LOW", missileAlliesPositions[i], 223, game.getFontColor2());
                }
            }
        }
        if (showArrowLeft) {
            g.drawImage(getFrame(), 72, 0, null);
        }
    }

    @Override
    public void drawMask(Graphics2D g) {
        if (game.getState() == State.GAME_OVER) {
            game.drawText(g, game.getScoreStr(), 16, 0, Color.WHITE);
        }
    }
    
    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(game.getState() == State.TITLE
            || game.getState() == State.READY
            || game.getState() == State.PLAYING
            || game.getState() == State.LEVEL_CLEARED);
        showArrowLeft = game.getState() == State.READY
            || game.getState() == State.PLAYING
            || game.getState() == State.LEVEL_CLEARED;
    }
    
}
