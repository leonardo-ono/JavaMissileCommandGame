package br.ol.mc;


import br.ol.mc.actor.City;
import br.ol.mc.actor.EnemyPlane;
import br.ol.mc.actor.EnemyUFO;
import br.ol.mc.actor.LevelManager;
import br.ol.mc.actor.Explosion;
import br.ol.mc.actor.GameOver;
import br.ol.mc.actor.HUD;
import br.ol.mc.actor.Initialization;
import br.ol.mc.actor.Intro;
import br.ol.mc.actor.LevelCleared;
import br.ol.mc.actor.MissileAlly;
import br.ol.mc.actor.MissileFired;
import br.ol.mc.actor.MissileTargetPosition;
import br.ol.mc.actor.OLPresents;
import br.ol.mc.actor.TargetSight;
import br.ol.mc.actor.Terrain;
import br.ol.mc.actor.TerrainFireBase;
import br.ol.mc.actor.Title;
import br.ol.mc.infra.Actor;
import br.ol.mc.infra.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * MissileCommandGame class.
 * 
 * @author Leonardo Ono (ono.leo@gmail.com)
 */
public class MissileCommandGame extends Game {

    public static enum State { 
        INITIALIZING, 
        OL_PRESENTS, 
        INTRO, 
        TITLE, 
        READY, 
        PLAYING, 
        LEVEL_CLEARED, 
        GAME_OVER }
    
    private State state = State.INITIALIZING;

    private final BufferedImage offscreen;
    private final Graphics2D offscreenG;

    private final BufferedImage missilePath;
    private final Graphics2D missilePathG;

    private final BufferedImage explosionsLayer;
    private final Graphics2D explosionsLayerG;
    
    private final BufferedImage mask;
    private final Graphics2D maskG;

    private double missileAllyVelocity = 5.0;
    private final MissileAlly[][] missilesAllies = new MissileAlly[3][10];
    private LevelManager levelManager;
    private EnemyPlane enemyPlane;
    private EnemyUFO enemyUFO;
    private int missileEnemiesRamificationCount;
    
    // --- colors ---
    
    private final Color[] colors = {
        //new Color(0, 0, 0, 255),
        new Color(255, 255, 255, 255),
        new Color(255, 0, 0, 255),
        new Color(0, 255, 0, 255),
        new Color(0, 0, 255, 255),
        new Color(0, 255, 255, 255),
        new Color(255, 0, 255, 255),
        new Color(255, 255, 0, 255)
    };    

    private Color explosionColor;
    private double explosionColorIndex;

    private Color backgroundColor = Color.BLACK;
    private Color terrainColor = Color.YELLOW;
    private Color missileEnemyPathColor = Color.RED;
    private Color missileAllyPathColor = Color.BLUE;
    private Color enemyPlaneColor = Color.RED;
    private Color enemyUFOColor = Color.BLACK;
    private Color fontColor1 = Color.RED;
    private Color fontColor2 = Color.BLUE;
    
    // --- hud ---
    
    private int level;
    private int score;
    private int hiscore = 7500;
    
    public MissileCommandGame() {
        screenSize = new Dimension(256, 231);
        screenScale = new Point2D.Double(2, 2);
        
        offscreen = new BufferedImage(256, 231, BufferedImage.TYPE_INT_ARGB);
        offscreenG = (Graphics2D) offscreen.getGraphics();

        missilePath = new BufferedImage(256, 231, BufferedImage.TYPE_INT_ARGB);
        missilePathG = (Graphics2D) missilePath.getGraphics();
        clearMissilePath();
        
        explosionsLayer = new BufferedImage(256, 231, BufferedImage.TYPE_INT_ARGB);
        explosionsLayerG = (Graphics2D) explosionsLayer.getGraphics();
        
        mask = new BufferedImage(256, 231, BufferedImage.TYPE_INT_ARGB);
        maskG = (Graphics2D) mask.getGraphics();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != state) {
            this.state = state;
            broadcastMessage("stateChanged");
        }
    }

    public BufferedImage getMissilePath() {
        return missilePath;
    }

    public Graphics2D getMissilePathG() {
        return missilePathG;
    }
    
    public void clearMissilePath() {
        missilePathG.setBackground(Color.BLACK);
        missilePathG.clearRect(0, 0, 256, 231);
    }

    public BufferedImage getExplosionsLayer() {
        return explosionsLayer;
    }

    public Graphics2D getExplosionsLayerG() {
        return explosionsLayerG;
    }

    public void clearExplosionLayer() {
        explosionsLayerG.setBackground(Color.BLACK);
        explosionsLayerG.clearRect(0, 0, 256, 231);
    }
 
    public BufferedImage getMask() {
        return mask;
    }

    public Graphics2D getMaskG() {
        return maskG;
    }

    public void clearMask() {
        maskG.setBackground(Color.BLACK);
        maskG.clearRect(0, 0, 256, 231);
    }

    public Color[] getColors() {
        return colors;
    }

    public Color getExplosionColor() {
        return explosionColor;
    }

    public double getExplosionColorIndex() {
        return explosionColorIndex;
    }

    public int getMissileEnemiesRamificationCount() {
        return missileEnemiesRamificationCount;
    }

    public void setMissileEnemiesRamificationCount(int missileEnemiesRamificationCount) {
        this.missileEnemiesRamificationCount = missileEnemiesRamificationCount;
    }

    public void incMissileEnemiesRamificationCount() {
        missileEnemiesRamificationCount++;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getTerrainColor() {
        return terrainColor;
    }

    public Color getMissileEnemyPathColor() {
        return missileEnemyPathColor;
    }

    public Color getMissileAllyPathColor() {
        return missileAllyPathColor;
    }
    
    public Color getEnemyPlaneColor() {
        return enemyPlaneColor;
    }

    public Color getEnemyUFOColor() {
        return enemyUFOColor;
    }

    public Color getFontColor1() {
        return fontColor1;
    }

    public void setFontColor1(Color fontColor1) {
        this.fontColor1 = fontColor1;
    }

    public Color getFontColor2() {
        return fontColor2;
    }

    public void setFontColor2(Color fontColor2) {
        this.fontColor2 = fontColor2;
    }

    public double getMissileAllyVelocity() {
        return missileAllyVelocity;
    }

    public void setMissileAllyVelocity(double missileAllyVelocity) {
        this.missileAllyVelocity = missileAllyVelocity;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public int getLevel() {
        return level;
    }

    public void clearScore() {
        score = 0;
    }

    public void addScore(int point) {
        score += point;
    }
    
    public int getScore() {
        return score;
    }

    public String getScoreStr() {
        String scoreStr = "       " + score;
        scoreStr = scoreStr.substring(scoreStr.length() - 7, scoreStr.length());
        return scoreStr;
    }

    public int getHiscore() {
        return hiscore;
    }
    
    public String getHiscoreStr() {
        String hiscoreStr = "       " + hiscore;
        hiscoreStr = hiscoreStr.substring(hiscoreStr.length() - 7, hiscoreStr.length());
        return hiscoreStr;
    }
    
    public void updateHiscore() {
        if (score > hiscore) {
            hiscore = score;
        }
    }

    @Override
    public void init() {
        createAllActors();
        initAllActors();
    }
  
    private void createAllActors() {
        actors.add(new Initialization(this));
        actors.add(new OLPresents(this));
        actors.add(new Intro(this));
        actors.add(new Terrain(this));
        actors.add(new TerrainFireBase(this, 1));
        actors.add(new TerrainFireBase(this, 2));
        actors.add(new TerrainFireBase(this, 3));
        actors.add(new City(this, 36, 208));
        actors.add(new City(this, 63, 209));
        actors.add(new City(this, 87, 210));
        actors.add(new City(this, 140, 208));
        actors.add(new City(this, 172, 205));
        actors.add(new City(this, 200, 209));
        createAllMissilesAlly();
        actors.add(enemyPlane = new EnemyPlane(this));
        actors.add(enemyUFO = new EnemyUFO(this));
        actors.add(new Title(this));
        actors.add(new HUD(this));
        actors.add(new TargetSight(this));
        actors.add(new LevelCleared(this));
        actors.add(new GameOver(this));
        actors.add(levelManager = new LevelManager(this));
    }

    private void createAllMissilesAlly() {
        createMissilesAllyTriangle(0, 23, 206);
        createMissilesAllyTriangle(1, 126, 206);
        createMissilesAllyTriangle(2, 243, 206);
    }

    private void createMissilesAllyTriangle(int index, int sx, int sy) {
        int index2 = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x <= y; x++) {
                int mx = sx - (y + 1) * 3 + x * 6;
                int my = sy + y * 3;
                MissileAlly missileAlly = new MissileAlly(this, mx, my);
                actors.add(missileAlly);
                missilesAllies[index][index2++] = missileAlly;
            }
        }
    }

    private void initAllActors() {
        for (Actor actor : actors) {
            actor.init();
        }
    }

    @Override
    public void update() {
        updateExplosionColor();
        super.update();
        checkFiredMissilesCollisions();
    }
    
    public void updateExplosionColor() {
        explosionColorIndex = (explosionColorIndex + 0.2) % colors.length;
        explosionColor = colors[(int) explosionColorIndex];
    }        
    
    public void drawMask(Graphics2D g) {
        for (Actor actor : actors) {
            ((MissileCommandActor) actor).drawMask(g);
        }
    }

    public void drawExplosions(Graphics2D g) {
        for (Actor actor : actors) {
            ((MissileCommandActor) actor).internalDrawExplosions(g);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        clearMask();
        maskG.setColor(Color.WHITE);
        drawMask(maskG);

        clearExplosionLayer();
        drawExplosions(explosionsLayerG);
        
        offscreenG.setBackground(backgroundColor);
        offscreenG.clearRect(0, 0, 256, 231);
        super.draw(offscreenG);
        g.drawImage(offscreen, 0, 0, null);

        g.setXORMode(Color.BLACK);
        g.drawImage(missilePath, 0, 8, 256, screenSize.height, 0, 8, screenSize.width, screenSize.height, null);
        g.drawImage(explosionsLayer, 0, 8, 256, screenSize.height, 0, 8, screenSize.width, screenSize.height, null);
        g.setPaintMode();
        
        // for debugging purposes
        //g.setXORMode(Color.BLACK);
        //g.drawImage(mask, 0, 0, null);
        //g.setPaintMode();
    }
    
    // --- fired missiles collisions check ---

    public void checkFiredMissilesCollisions() {
        // check between explosions and enemies
        for (Actor actor1 : actors) {
            if (!(actor1 instanceof Explosion) || !actor1.isVisible()) {
                continue;
            }
            Explosion explosion = (Explosion) actor1;
            for (Actor actor2 : actors) {
                if (actor1 == actor2 || !actor2.isVisible()) {
                    continue;
                }
                if (actor2 instanceof EnemyPlane
                    || actor2 instanceof EnemyUFO) {
                    MissileCommandActor mcActor1 = (MissileCommandActor) actor1;
                    MissileCommandActor mcActor2 = (MissileCommandActor) actor2;
                    if (mcActor2.checkCollidesWithExplosion(explosion)) {
                        mcActor1.onCollision();
                        mcActor2.onCollision();
                    }
                }
            }
        }
        // check between missileFired and others actors
        for (Actor actor1 : actors) {
            if (!(actor1 instanceof MissileFired) || !actor1.isVisible()) {
                continue;
            }
            MissileFired missileFired = (MissileFired) actor1;
            if (!missileFired.isEnemy() && missileFired.getY() > 200) {
                continue;
            }
            for (Actor actor2 : actors) {
                if (actor1 == actor2 || !actor2.isVisible()) {
                    continue;
                }
                if (actor2 instanceof Terrain
                    || actor2 instanceof TerrainFireBase
                    || actor2 instanceof City
                    || actor2 instanceof Explosion) {
                    MissileCommandActor mcActor1 = (MissileCommandActor) actor1;
                    MissileCommandActor mcActor2 = (MissileCommandActor) actor2;
                    if (mcActor2.checkPointCollision((int) mcActor1.getX(), (int) mcActor1.getY())) {
                        mcActor1.onCollision();
                        mcActor2.onCollision();
                    }
                }
            }
        }
    }

    // --- cities ---

    public City getNextAvailableCity() {
        for (Actor actor : actors) {
            if (actor instanceof City && actor.isVisible() && !((City) actor).isDestroyed()) {
                return (City) actor;
            }
        }
        return null;
    }

    public void restoreAllCities() {
        for (Actor actor : actors) {
            if (actor instanceof City) {
                ((City) actor).restore();
            }
        }
    }
    
    // --- missile target position  ---

    public void removeAllMissileTargetPositions() {
        for (Actor actor : actors) {
            if (actor instanceof MissileTargetPosition) {
                removeActor(actor);
            }
        }
    }
    
    // --- explosions ---

    public void removeAllExplosions() {
        for (Actor actor : actors) {
            if (actor instanceof Explosion) {
                removeActor(actor);
            }
        }
    }
    
    // --- missiles enemies ---
    
    private final List<MissileFired> activeMissileEnemies = new ArrayList<MissileFired>();
    
    public MissileFired getActiveMissileEnemyRandomically() {
        activeMissileEnemies.clear();
        for (Actor actor : actors) {
            if (actor instanceof MissileFired) {
                MissileFired missileFired = (MissileFired) actor;
                if (missileFired.isVisible() && missileFired.isEnemy()) {
                    activeMissileEnemies.add(missileFired);
                }
            }
        }
        if (activeMissileEnemies.isEmpty()) {
            return null;
        }
        return activeMissileEnemies.get((int) (activeMissileEnemies.size() * Math.random()));
    }
    
    public void removeAllMissileEnemies() {
        for (Actor actor : actors) {
            if (actor instanceof MissileFired) {
                removeActor(actor);
            }
        }
    }
    
    // --- missiles allies ---
    
    public void resetMissilesAllies(Color overrideColor) {
        for (int i1 = 0; i1 < 3; i1++) {
            for (int i2 = 0; i2 < 10; i2++) {
                missilesAllies[i1][i2].setOverrideColor(overrideColor);
                missilesAllies[i1][i2].setVisible(true);
            }
        }
    }
    
    public int getAvailableMissilesAlliesCount(int index1) {
        int availableMissilesAllies = 0;
        for (int index2 = 0; index2 < 10; index2++) {
            if (missilesAllies[index1][index2].isVisible()) {
                availableMissilesAllies++;
            }
        }
        return availableMissilesAllies;
    }

    public MissileAlly getNextAvailableMissileAlly(int index) {
        for (int i = 0; i < 10; i++) {
            if (missilesAllies[index][i].isVisible()) {
                return missilesAllies[index][i];
            }
        }
        return null;
    }
    
    public MissileAlly getNextAvailableMissileAllyRandomically() {
        int index = (int) (3 * Math.random());
        for (int t = 0; t < 3; t++) {
            for (int i = 0; i < 10; i++) {
                if (missilesAllies[index][i].isVisible()) {
                    return missilesAllies[index][i];
                }
            }
            index = (index + 1) % 3;
        }
        return null;
    }

    public void destroyAllAvailableMissileAlly(int index) {
        for (int i = 0; i < 10; i++) {
            missilesAllies[index][i].setVisible(false);
        }
    }
    
    // --- spawn actors ---

    public void spawnEnemyPlane() {
        enemyPlane.spawn();
    }

    public void spawnEnemyUFO() {
        enemyUFO.spawn();
    }
    
    public void spawnIntroExplosion(int x, int y) {
        addActor(new Explosion(this, x, y));
    }

    public void spawnExplosion(int x, int y, boolean affectedByMask) {
        int maxRadius = 10 + (int) (6 * Math.random());
        addActor(new Explosion(this, x, y, maxRadius, affectedByMask));
    }

    public void spawnMissile(int startX, int startY, int destX, int destY, double velocity, boolean showMissileTargetPosition, Color pathColor) {
        addActor(new MissileFired(this, startX, startY, destX, destY, velocity, pathColor, showMissileTargetPosition));
    }
    
    public void fireMissile(int x, int y) {
        MissileAlly missileAlly = getNextAvailableMissileAllyRandomically();
        if (missileAlly == null) {
            return;
        }
        missileAlly.setVisible(false);
        spawnMissile((int) missileAlly.getX(), (int) missileAlly.getY(), x, y, missileAllyVelocity, true, missileAllyPathColor);
    }
    
    // --- levels ---
    
    private void setLevelColors() {
        if ((level % 3) == 0) {
            backgroundColor = Color.BLACK;
            terrainColor = Color.YELLOW;
            missileEnemyPathColor = Color.RED;
            missileAllyPathColor = Color.BLUE;
            enemyPlaneColor = Color.RED;
            enemyUFOColor = Color.BLACK;
            fontColor1 = Color.RED;
            fontColor2 = Color.BLUE;
        }
        else if ((level % 3) == 1) {
            backgroundColor = Color.BLACK;
            terrainColor = Color.YELLOW;
            missileEnemyPathColor = Color.GREEN;
            missileAllyPathColor = Color.BLUE;
            enemyPlaneColor = Color.GREEN;
            enemyUFOColor = Color.GREEN;            
            fontColor1 = Color.GREEN;
            fontColor2 = Color.BLUE;
        }
        else if ((level % 3) == 2) {
            backgroundColor = Color.BLACK;
            terrainColor = Color.BLUE;
            missileEnemyPathColor = Color.RED;
            missileAllyPathColor = Color.GREEN;
            enemyPlaneColor = Color.RED;
            enemyUFOColor = Color.BLACK;            
            fontColor1 = Color.RED;
            fontColor2 = Color.GREEN;
        }
    }
    
    private void prepareLevel() {
        setLevelColors();
        if (level == 0) {
            levelManager.start(0.25, 3000, false, false, false, 3, 2, 1, 3, 2, 1);
        }
        else if (level == 1) {
            levelManager.start(0.45, 2500, true, false, true, 3, 2, 1, 3, 2, 1, 3);
        }
        else if (level == 2) {
            levelManager.start(0.65, 2000, true, true, true, 3, 2, 1, 3, 2, 1, 3, 2);
        }
        else if (level > 2) {
            levelManager.start(0.65 + ((level - 2) * 0.1), 1500, true, true, true, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 1);
        }
    }
    
    // --- game flow ---
    
    public void backToIntro() {
        setState(State.INTRO);
    }
    
    public void startTitleDemo() {
        levelManager.start(0.25, 3000, false, false, false, 3, 2, 1, 3, 2, 1, 3, 2, 1);
    }
    
    private void clearAllGraphics() {
        removeAllExplosions();
        removeAllMissileTargetPositions();
        removeAllMissileEnemies();
        clearMask();
        clearMissilePath();
        clearExplosionLayer();
    }
    
    public void startReady() {
        missileEnemiesRamificationCount = 0;
        clearAllGraphics();
        prepareLevel();
        setState(State.READY);
    }
    
    public void startGame() {
        setState(State.PLAYING);
    }
    
    public void levelCleared() {
        clearAllGraphics();
        setState(State.LEVEL_CLEARED);
    }

    public void nextLevel() {
        level++;
        startReady();
    }

    public void startGameOver() {
        level = 0;
        clearAllGraphics();
        restoreAllCities();
        setState(State.GAME_OVER);
    }
    
    public void backToTitle() {
        setState(State.TITLE);
    }
    
}
