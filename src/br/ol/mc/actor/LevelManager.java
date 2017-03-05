package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import java.awt.Point;

/**
 * LevelManager class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class LevelManager extends MissileCommandActor {
    
    private double enemyMissileVelocity;
    private boolean allowEnemyMissileRamification;
    private boolean showEnemyUFO;
    private boolean showEnemyPlane;
    private int[] waveMissileCounts;
    private int currentWaveIndex;
    private long waitTimeBetweenWaves;
    private boolean finished;
    
    public static final Point[] TARGET_POSITIONS = {
        new Point(20, 211),
        new Point(44, 215),
        new Point(71, 217),
        new Point(95, 217),
        new Point(123, 211),
        new Point(148, 215),
        new Point(180, 213),
        new Point(208, 217),
        new Point(240, 211)
    };
    
    public LevelManager(MissileCommandGame game) {
        super(game);
    }

    public boolean isAllowEnemyMissileRamification() {
        return allowEnemyMissileRamification;
    }

    public double getEnemyMissileVelocity() {
        return enemyMissileVelocity;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void init() {
        instructionPointer = -1;
        setVisible(true);
    }

    @Override
    public void update() {
        yield:
        while (true) {
            switch (instructionPointer) {
                case -1:
                    break yield;
                case 0:
                    currentWaveIndex = 0;
                    instructionPointer = 1;
                case 1:
                    for (int i = 0; i < waveMissileCounts[currentWaveIndex]; i++) {
                        int startX = (int) (game.screenSize.width * Math.random());
                        int startY = 0;
                        int targetPositionIndex = (int) (TARGET_POSITIONS.length * Math.random());
                        int destX = TARGET_POSITIONS[targetPositionIndex].x;
                        int destY = TARGET_POSITIONS[targetPositionIndex].y;
                        game.spawnMissile(startX, startY, destX, destY, enemyMissileVelocity, false, game.getMissileEnemyPathColor());
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 2;
                case 2:
                    if (System.currentTimeMillis() - waitTime < waitTimeBetweenWaves) {
                        break yield;
                    }
                    currentWaveIndex++;
                    instructionPointer = 1;
                    if (currentWaveIndex > waveMissileCounts.length - 1) {
                        instructionPointer = 3;
                    }
                    break yield;
                case 3:
                    // wait until all enemy missiles are destroyed
                    if (game.getActiveMissileEnemyRandomically() != null) {
                        break yield;
                    }
                    waitTime = System.currentTimeMillis();
                    instructionPointer = 4;
                case 4:
                    if (System.currentTimeMillis() - waitTime < 3000) {
                        break yield;
                    }
                    finished = true;
                    instructionPointer = 5;
                case 5:
                    break yield;
            }
        }
    }

    public void start(double enemyMissileVelocity, long waitTimeBetweenWaves, boolean allowEnemyMissileRamification, boolean showEnemyUFO, boolean showEnemyPlane, int ... waveMissileCounts) {
        this.enemyMissileVelocity = enemyMissileVelocity;
        this.waitTimeBetweenWaves = waitTimeBetweenWaves;
        this.allowEnemyMissileRamification = allowEnemyMissileRamification;
        this.showEnemyUFO = showEnemyUFO;
        this.showEnemyPlane = showEnemyPlane;
        this.waveMissileCounts = waveMissileCounts;
        finished = false;
        instructionPointer = 0;
        if (this.showEnemyPlane) {
            game.spawnEnemyPlane();
        }
        if (this.showEnemyUFO) {
            game.spawnEnemyUFO();
        }
    }
    
}
