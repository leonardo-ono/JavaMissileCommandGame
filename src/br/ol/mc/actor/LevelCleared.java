package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * LevelCleared class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class LevelCleared extends MissileCommandActor {
    
    private int bonusRemaingAllyMissiles;
    private int currentAllyMissilesIndex;
    private MissileAlly remainingMissileAlly;
    private int remainingMissileAllyCount;
    
    private int bonusRemaingCities;
    private City remainingCity;
    private int remainingCityCount;
    
    public LevelCleared(MissileCommandGame game) {
        super(game);
    }

    @Override
    public void init() {
        loadFrames("/res/missile_ally.png", "/res/city.png");
        setUseOverrideColor(false);
        setZorder(2000);
    }

    @Override
    public void updateLevelCleared() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case 0:
                    bonusRemaingAllyMissiles = 0;
                    bonusRemaingCities = 0;
                    currentAllyMissilesIndex = 0;
                    remainingMissileAllyCount = 0;
                    remainingCityCount = 0;
                    instructionPointer = 1;
                case 1:
                    remainingMissileAlly = game.getNextAvailableMissileAlly(currentAllyMissilesIndex);
                    if (remainingMissileAlly == null) {
                        currentAllyMissilesIndex++;
                        if (currentAllyMissilesIndex > 2) {
                            game.addScore(bonusRemaingAllyMissiles);
                            instructionPointer = 4;
                        }
                        break yield;
                    }
                case 2:
                    remainingMissileAllyCount++;
                    bonusRemaingAllyMissiles += 5;
                    remainingMissileAlly.setVisible(false);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 3;
                case 3:
                    if (System.currentTimeMillis() - waitTime < 75) {
                        break yield;
                    }
                    instructionPointer = 1;
                    break yield;
                case 4:
                    remainingCity = game.getNextAvailableCity();
                    if (remainingCity == null) {
                        game.addScore(bonusRemaingCities);
                        waitTime = System.currentTimeMillis();
                        instructionPointer = 7;
                        break yield;
                    }
                case 5:
                    remainingCityCount++;
                    bonusRemaingCities += 100;
                    remainingCity.setVisible(false);
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 6;
                case 6:
                    if (System.currentTimeMillis() - waitTime < 150) {
                        break yield;
                    }
                    instructionPointer = 4;
                    break yield;
                case 7:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    game.nextLevel();
                    break yield;
            }
        }
    }
    
    private String convertBonusPointToStr(int point) {
        String pointStr = "       " + point;
        pointStr = pointStr.substring(pointStr.length() - 7, pointStr.length());
        return pointStr;
    }
    
    @Override
    protected void draw(Graphics2D g) {
        game.drawText(g, "BONUS POINT", 72, 72, game.getFontColor2());
        
        if (remainingMissileAllyCount > 0) {
            game.drawText(g, convertBonusPointToStr(bonusRemaingAllyMissiles), 48, 104, game.getFontColor1());
            for (int m = 0; m < remainingMissileAllyCount; m++) {
                g.drawImage(getFrames()[0], 112 + m * 4, 108, null);
            }
        }
        
        if (remainingCityCount > 0) {
            game.drawText(g, convertBonusPointToStr(bonusRemaingCities), 48, 136, game.getFontColor1());
            for (int m = 0; m < remainingCityCount; m++) {
                g.drawImage(getFrames()[1], 112 + m * 17, 136, null);
            }
        }
    }

    // broadcast messages
    
    @Override
    public void stateChanged() {
        setVisible(game.getState() == State.LEVEL_CLEARED);
        if (game.getState() == State.LEVEL_CLEARED) {
            instructionPointer = 0;
        }
    }
    
}
