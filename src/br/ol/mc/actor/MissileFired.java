package br.ol.mc.actor;

import br.ol.mc.MissileCommandActor;
import br.ol.mc.MissileCommandGame;
import br.ol.mc.MissileCommandGame.State;
import static br.ol.mc.actor.LevelManager.TARGET_POSITIONS;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * MissileFired class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class MissileFired extends MissileCommandActor {
    
    private int startX;
    private int startY;
    private int destX;
    private int destY;
    private double velocity;
    private double remainingVelocity;
    private double totalPathDistance;
    private double currentPathDistance;
    private int currX;
    private int currY;
    private int lastCurrX;
    private int lastCurrY;
    private final List<Integer> path = new ArrayList<Integer>();
    private Color pathColor;
    private MissileTargetPosition missileTargetPosition;
    private boolean collided;
    private boolean enemy;
    
    public MissileFired(MissileCommandGame game, int startX, int startY, int destX, int destY, double velocity, Color pathColor, boolean showMissileTargetPosition) {
        super(game);
        this.startX = startX;
        this.startY = startY;
        this.destX = destX;
        this.destY = destY;
        this.velocity = velocity;
        this.pathColor = pathColor;
        
        if (showMissileTargetPosition) {
            this.missileTargetPosition = new MissileTargetPosition(game, destX, destY);
            game.addActor(missileTargetPosition);
        }
        
        int px = (destX - startX);
        int py = (destY - startY);
        enemy = py > 0;
        totalPathDistance = Math.sqrt(px * px + py * py);
        currentPathDistance = 0;
        
        setOverrideColor(pathColor);
        setVisible(true);
    }

    public boolean isEnemy() {
        return enemy;
    }

    @Override
    public void updateTitle() {
        updateMissile();
    }

    @Override
    public void updatePlaying() {
        updateMissile();
    }
    
    public void updateMissile() {
        remainingVelocity += velocity;
        while (remainingVelocity > 1) {
            remainingVelocity--;
            currentPathDistance += 1;
            if (currentPathDistance > totalPathDistance || (enemy && collided)) {
                game.spawnExplosion(currX, currY, enemy);
                clearPath();
                setVisible(false);
                game.removeActor(this);
                if (missileTargetPosition != null) {
                    game.removeActor(missileTargetPosition);
                }
                if (enemy && game.getState() == State.PLAYING) {
                    game.addScore(25);
                }
                return;
            }
            currX = startX + (int) ((destX - startX) * (currentPathDistance / totalPathDistance));
            currY = startY + (int) ((destY - startY) * (currentPathDistance / totalPathDistance));
            path.add(currX);
            path.add(currY);
            set(currX, currY);
            drawMissilePath();
            if (enemy && game.getLevelManager().isAllowEnemyMissileRamification() && Math.random() < 0.002 && game.getMissileEnemiesRamificationCount() <= game.getLevel()) {
                ramificate();
                game.incMissileEnemiesRamificationCount();
            }
        }
    }

    public void drawMissilePath() {
        Graphics2D g = game.getMissilePathG();
        g.setColor(pathColor);
        g.drawLine(lastCurrX, lastCurrY, lastCurrX, lastCurrY);
        g.setColor(game.getExplosionColor());
        g.drawLine(currX, currY, currX, currY);
        lastCurrX = currX;
        lastCurrY = currY;
    }

    protected void clearPath() {
        Graphics2D g = (Graphics2D) game.getMissilePathG();
        g.setColor(Color.BLACK);
        for (int i = 0; i < path.size(); i += 2) {
            int px = path.get(i);
            int py = path.get(i + 1);
            g.fillOval(px - 1, py - 1, 2, 2);
        }
    }

    @Override
    public void onCollision() {
        collided = true;
    }
    
    private final Point interceptPoint = new Point();
    
    public Point calculateInterceptPoint(double missileAllyVelocity) {
        double prev = 3 * missileAllyVelocity;
        interceptPoint.x = startX + (int) ((destX - startX) * ((currentPathDistance + prev) / totalPathDistance));
        interceptPoint.y = startY + (int) ((destY - startY) * ((currentPathDistance + prev) / totalPathDistance));
        return interceptPoint;
    }
    
    public void ramificate() {
        int ramificationsNumber = 2 + (int) (4 * Math.random());
        double enemyMissileVelocity = game.getLevelManager().getEnemyMissileVelocity();
        for (int i = 0; i < ramificationsNumber; i++) {
            int sX = currX;
            int sY = currY;
            int targetPositionIndex = (int) (TARGET_POSITIONS.length * Math.random());
            int dX = TARGET_POSITIONS[targetPositionIndex].x;
            int dY = TARGET_POSITIONS[targetPositionIndex].y;
            game.spawnMissile(sX, sY, dX, dY, enemyMissileVelocity, false, game.getMissileEnemyPathColor());
        }
        // clearPath();
        setVisible(false);
        game.removeActor(this);
    }
    
}
