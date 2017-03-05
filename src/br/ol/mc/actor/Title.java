package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import br.ol.mc.infra.Mouse;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Title class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class Title extends MissileCommandActor {
    
    private double textX;
    private final String text = 
        "GAME OVER     " +
        "CLICK WITH MOUSE TO START     " +
        "ORIGINAL GAME BY ATARI (C) 1980     " +
        "PROGRAMMED BY O.L. (C) 2017";
    
    private boolean showArrowsDown;
    
    public Title(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/arrow_down.png");
        setOverrideColor(Color.RED);
        setUpdatebleJustWhenVisible(false);
    }

    @Override
    public void updateTitle() {
        textX -= 0.5;
        if (textX < -800) {
            textX = 300;
        }
        showArrowsDown = ((int) (System.nanoTime() * 0.00000001)) % 10 > 2;
        if (Mouse.pressed()) {
            game.startReady();
        }
        else if (game.getLevelManager().isFinished()) {
            game.backToIntro();
        }
    }

    @Override
    public void updateReady() {
        if (System.currentTimeMillis() - waitTime < 1500) {
            return;
        }
        game.startGame();
    }
    
    @Override
    protected void draw(Graphics2D g) {
        if (showArrowsDown) {
            g.drawImage(getFrame(), 40, 188, null);
            g.drawImage(getFrame(), 67, 188, null);
            g.drawImage(getFrame(), 91, 188, null);
            g.drawImage(getFrame(), 144, 188, null);
            g.drawImage(getFrame(), 176, 188, null);
            g.drawImage(getFrame(), 204, 188, null);
        }
        game.drawText(g, text, (int) textX, 223, Color.BLACK);
        game.drawText(g, "DEFEND        CITIES", 48, 168, Color.BLUE);
    }

    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(game.getState() == State.TITLE);
        if (game.getState() == State.TITLE) {
            textX = 300;
            game.startTitleDemo();
        }
        else if (game.getState() == State.READY) {
            waitTime = System.currentTimeMillis();
        }
    }
    
}
