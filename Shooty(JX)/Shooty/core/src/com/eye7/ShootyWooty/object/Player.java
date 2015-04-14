package com.eye7.ShootyWooty.object;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.CactusLoader;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.world.GameMap;

import java.util.HashMap;

public class Player implements Observer{
    private final String TAG;

    //GameMap
    public GameMap map;

    //Collider and Player Stats
    private CircleMapObject collider;
    private float x;
    private float y;
    private int dir;
    private int playerID;
    public int health;
    private int score;
    private int water;
    private int bulletCount;
    public boolean dead = false; // This boolean turns true when player just died. This prevents furthur movement in current Turn before finalising his death.
    private ActionResolver actionResolver;
    //PlayerStatus and Animations
    private HashMap<String, Animation> animations;
    private PlayerState playerState;
    private PlayerState previousState;
    private float stateDelta;
    public final Object statusLock = new Object();

    //Bullets
    private Bullet bulletl;
    private Bullet bulletr;
    private boolean shootLeft;
    private boolean shootRight;

    //HealthBar & WaterBar
    private Sprite healthBarFG;
    private Sprite healthBarFG2;
    private Sprite healthBarFG3;
    private Sprite healthBarBG;
    private Sprite waterBarFG;
    private Sprite waterBarBG;

    private float RADIUS = 2;

    //DisplayMoves
    private Sprite displayMoves1;
    private Sprite displayMoves2;
    private Sprite displayMoves3;
    private Sprite displayMoves4;

    //Debug Renderer
    private ShapeRenderer sr;

    //Achievemnts
    private int lifeTimeKills = 0;
    private int lifeTimeHits = 0;
    private int lifeTimeWater = 0;
    private int lifeTimeShotsFired = 0;

    //add sounds for collision
    private Sound cactiWalking = Gdx.audio.newSound(Gdx.files.internal("sounds/cactiWalking.mp3"));//add cacti footsteps
    private Sound sound_damaged = Gdx.audio.newSound(Gdx.files.internal("sounds/damaged.mp3"));
    private long walkingSound;

    //CONSTRUCTOR
    public Player(GameMap map, CircleMapObject collider, int d, int id, ActionResolver actionResolver) {
        TAG = "Player" + String.valueOf(id+1);
        playerID = id+1;
        this.actionResolver = actionResolver;
        //Listen in to TurnEnd
        GameConstants.subscribeTurnEnd(this);

        this.map = map; // Reference to the GameMap object in order to get the positions of other objects;

        //Set up player Sprites
        switch(playerID) {
            case 1: animations = CactusLoader.cactus1_animations;
                break;
            case 2: animations = CactusLoader.cactus2_animations;
                break;
            case 3: animations = CactusLoader.cactus3_animations;
                break;
            case 4: animations = CactusLoader.cactus4_animations;
                break;
        }

        //Setup playerStates
        playerState = PlayerState.IDLE;
        previousState = PlayerState.IDLE;

        walkingSound = cactiWalking.loop(0.7f);
        cactiWalking.pause(walkingSound);

        //Set up player position & stats
        //Coordinates of the middle of the circle
        this.collider = collider;
        this.x = collider.getCircle().x;
        this.y = collider.getCircle().y;
        this.dir = d;
        health = 100;
        score = 0;
        water = 0;
        bulletCount = 4;

        //Create the bullets
        bulletl = new Bullet(270, this.x, this.y,this);
        bulletr = new Bullet(90, this.x, this.y,this);

        //initialize the healthbar & waterbar
        healthBarBG = new Sprite(new Texture("players/healthBG.png"));
        healthBarFG3 = new Sprite(new Texture("players/healthFG3.png"));
        healthBarFG2 = new Sprite(new Texture("players/healthFG2.png"));
        healthBarFG = new Sprite(new Texture("players/healthFG.png"));
        waterBarBG = new Sprite(new Texture("players/waterBarBG.png"));
        waterBarFG = new Sprite(new Texture("players/waterBarFG.png"));

        //set origin of healthbar to 0,0. This allows the health bar to fix the the left and decrease from the right.
        healthBarFG.setOrigin(0,0);
        healthBarBG.setOrigin(0,0);

        //set origin of water bar to 0,0. This allows the bar to fix to the bottom and increase to the top.
        waterBarFG.setOrigin(0,0);

        //Initialize displayMoves sprites
        displayMoves1 = new Sprite(new Texture("players/1Move.png"));
        displayMoves2 = new Sprite(new Texture("players/2Moves.png"));
        displayMoves3 = new Sprite(new Texture("players/3Moves.png"));
        displayMoves4 = new Sprite(new Texture("players/4Moves.png"));

        //ShapeRenderer for debug
        if (GameConstants.DEBUG) {
            sr = new ShapeRenderer();
        }

        //Setup Achievements
        lifeTimeHits = 0;
        lifeTimeKills = 0;
        lifeTimeWater = 0;
        lifeTimeShotsFired = 0;

    }

    public void draw(SpriteBatch sb, float delta) {
        if (shootLeft) {
            bulletl.draw(sb);
        }
        if (shootRight) {
            bulletr.draw(sb);
        }
        Sprite s = null;
        //Find the correct sprite to draw based on PlayerState
        synchronized (statusLock) {
            //Gdx.app.log(TAG, "Rendering");
            while (s == null) {
                if (playerState == PlayerState.DEAD) {
                    if (stateDelta <= animations.get("lose").getAnimationDuration()) {
                        s = new Sprite(animations.get("lose").getKeyFrame(stateDelta));
                        stateDelta += delta;
                    } else {
                        s = new Sprite(animations.get("lose").getKeyFrame(animations.get("lose").getAnimationDuration()));
                    }

                } else if (playerState == PlayerState.IDLE) {
                    stateDelta += delta;
                    s = new Sprite(animations.get(getdirection() + ".idle").getKeyFrame(stateDelta));

                } else if (playerState == PlayerState.MOVING) {
                    stateDelta += delta;
                    s = new Sprite(animations.get(getdirection()+ ".moving").getKeyFrame(stateDelta));



                } else if (playerState == PlayerState.DAMAGED) {
                    if (stateDelta <= animations.get("shot").getAnimationDuration()) {
                        //Gdx.app.log(TAG, "CurrentDelta: " + String.valueOf(stateDelta) + "Animation length: " + String.valueOf(animations.get("shot").getAnimationDuration()));
                        s = new Sprite(animations.get("shot").getKeyFrame(stateDelta));
                        stateDelta += delta;
                    } else {
                        playerState = PlayerState.NONE; // Restore previous state
                        sound_damaged.stop();
                        changeAnimation(previousState);
                        Gdx.app.log(TAG,playerState.toString());
                        statusLock.notifyAll();
                    }

                } else if (playerState == PlayerState.COLLECTING_WATER) {
                    if (previousState != PlayerState.DAMAGED) { //If not damaged
                        if (stateDelta <= animations.get("score").getAnimationDuration()) {
                            s = new Sprite(animations.get("score").getKeyFrame(stateDelta));
                            stateDelta += delta;
                        } else {
                            playerState = PlayerState.getState(previousState); // Restore previous state
                            previousState = PlayerState.COLLECTING_WATER;
                            statusLock.notifyAll();
                        }
                    } else {
                        changeAnimation(PlayerState.IDLE);
                        s = new Sprite(animations.get(getdirection() + ".idle").getKeyFrame(stateDelta));
                    }
                }
            }
        }

        s.setCenter(x,y);
        s.draw(sb);
        sb.end();

        //Debug: Draws the collider
        if (GameConstants.DEBUG) {
            sr.setColor(Color.BLACK);
            sr.setProjectionMatrix(sb.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.circle(collider.getCircle().x,collider.getCircle().y,collider.getCircle().radius);
            sr.end();
        }
        //set coordinates
        healthBarBG.setX(x-RADIUS-25);
        healthBarBG.setY(y+RADIUS+30);

        healthBarFG.setX(x-RADIUS-25);
        healthBarFG.setY(y+RADIUS+30);

        healthBarFG2.setX(x-RADIUS-25);
        healthBarFG2.setY(y+RADIUS+30);

        healthBarFG3.setX(x-RADIUS-25);
        healthBarFG3.setY(y+RADIUS+30);

        //healthBar image is too big, so scaling it down
//        healthBarBG.setScale(0.7f, 1f);
//        healthBarFG.setScale(0.7f, 1f);

        //connect the healthBar to the health of the player
        if(health>=60) {
            healthBarFG2.setScale((float)(health-50f)/50f, 1f);
            healthBarFG3.setScale(1);
            healthBarFG.setScale(0);
        }
        else{
            healthBarFG.setScale((float)health/50f, 1f);
            healthBarFG2.setScale(0);
            healthBarFG3.setScale(0);

        }
        //initialize the waterBars
        int waterBarFill = score*3+water;

        //set the coordinates
        waterBarBG.setX(x+RADIUS+25);
        waterBarBG.setY(y-RADIUS-25);

        waterBarFG.setX(x+RADIUS+25);
        waterBarFG.setY(y-RADIUS-25);

        //connect the waterBar to the water content held by the player
        waterBarFG.setScale(1f, waterBarFill/(float)9);

        sb.begin();
        if (!isDead()) {//Only draw playerUI if not dead
            //draw the bars
            healthBarBG.draw(sb);
            healthBarFG.draw(sb);
            healthBarFG2.draw(sb);
            healthBarFG3.draw(sb);

            waterBarBG.draw(sb);
            waterBarFG.draw(sb);

            //no of move bars

            int moves = actionResolver.getImMoves()[playerID-1];

            if (moves != 0) {
                Sprite nOfMoves = getNoOfMoves(moves);
                nOfMoves.draw(sb);
            }
        }

    }


    //returns a sprite with the no of moves as input to the player. This will be drawn on the player.
    //pre condition: 1<=n<=4
    public synchronized Sprite getNoOfMoves(int n){
        Sprite nOfMoves;
        if(n==1){
            nOfMoves = displayMoves1;
        }
        else if(n==2){
            nOfMoves = displayMoves2;
        }
        else if(n==3){
            nOfMoves = displayMoves3;
        }
        else{
            nOfMoves = displayMoves4;
        }
        nOfMoves.setX(getX()-RADIUS-22);
        nOfMoves.setY(getY()+RADIUS+32);
        return nOfMoves;
    }

    public synchronized void decreaseHealth(int dmg){
        if (!isDead()) {
            health -= dmg;
            water = 0;
            if (health < 0) {
                health = 0;
                dead = true; // Just died
            }
            changeAnimation(PlayerState.DAMAGED);
        }
    }

    public synchronized void decreaseHealth(Bullet b){
        if (!isDead()) {
            health -= 20;
            water = 0;
            if (health < 0) {
                health = 0;
                if (playerState != PlayerState.DEAD && dead == false) { //If not previously declared dead
                    b.killAwarded();
                    dead = true; // Set frag to true so no one else can get the kill
                }

            }
            changeAnimation(PlayerState.DAMAGED);
        }
    }

    public void hitAwarded() {
        lifeTimeHits += 1;
    }

    public void killAwarded() {
        lifeTimeKills += 1;
    }

    public synchronized void collectWater() {
        if (!isDead()) {
            water += 1;
            lifeTimeWater += 1;
            if (water == 3) {
                score += 1;
                water = 0;
            }
            changeAnimation(PlayerState.COLLECTING_WATER);
        }
    }


    // setters
    public synchronized void incrementX(float x) {
        if (!isDead()) {
            this.x += x;
            collider.getCircle().setPosition(this.x, this.y);
            changeAnimation(PlayerState.MOVING);
        }
    }

    public synchronized void incrementY(float y) {
        if (!isDead()) {
            this.y += y;
            collider.getCircle().setPosition(this.x, this.y);
            changeAnimation(PlayerState.MOVING);
        }
    }

    public void rotate (int r) {
        this.dir += r;
        if (dir >= 360) {
            dir -= 360;
        }
        while (dir < 0) {
            dir = 360 + dir;
        }
    }

    public boolean isShooting() {
        return (shootLeft || shootRight);
    }

    public void startShootLeft() {
        shootLeft = true;
        bulletCount -= 1;
        lifeTimeShotsFired += 1;
    }

    public void startShootRight() {
        shootRight = true;
        bulletCount -= 1;
        lifeTimeShotsFired += 1;
    }

    public void endShootLeft() {
        if (shootLeft) {
            shootLeft = false;
            bulletl.getReturn();
        }
    }

    public void endShootRight() {
        if (shootRight) {
            shootRight = false;
            bulletr.getReturn();
        }
    }

    // getters
    public int getPlayerID() { return playerID; }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getDir() { return dir; }

    public Bullet getBulletl() {
        return bulletl;
    }

    public Bullet getBulletr() {
        return bulletr;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public int getHealth(){
        return health;
    }

    public CircleMapObject getCollider(){
        return collider;
    }

    //Aligns player into the closest gridbox
    //Calling this will also end player movement animation
    public synchronized void snapInGrid() {
        //Snaps to 64 pixel grid
        float Xoff = x % 32;
        float Yoff = y % 32;
        int Roff = dir % 90;
        if (Xoff < 16) {
            incrementX(-Xoff);
        } else {
            incrementX(32 - Xoff);
        }
        if (Yoff < 16) {
            incrementY(-Yoff);
        } else {
            incrementY(32 - Yoff);
        }
        if (Roff < 45) {
            rotate(-Roff);
        } else {
            rotate(90 - Roff);
        }

        if (playerState != PlayerState.DEAD) {
            changeAnimation(PlayerState.IDLE);
        }
    }

    public int getScore(){
        return score;
    }

    private String getdirection() {
        if (dir == 0) {
            return "north";
        } else if (dir == 90) {
            return "west";
        } else if (dir == 180) {
            return "south";
        } else  {
            return "east";
        }
    }

    //Call this to get achievments at the end of the game.
    public HashMap<String, Integer> getAchievments() {
        HashMap<String, Integer> a = new HashMap<String, Integer>();
        a.put ("Kills", lifeTimeKills);
        a.put ("Hits", lifeTimeHits);
        a.put ("Shots Fired", lifeTimeShotsFired);
        a.put ("Water", lifeTimeWater);

        return a;

    }

    private void changeAnimation(PlayerState newState) {
        synchronized (statusLock) {
            if (playerState != PlayerState.DEAD) {
                if (playerState == PlayerState.DAMAGED) { //Check if not dead
                    previousState = newState; // Allows damaged animation to run finish
                } else if (playerState != newState) {
                    previousState = PlayerState.getState(playerState);
                    playerState = newState;
                    stateDelta = 0f;
                    statusLock.notifyAll();
                    if (newState == PlayerState.MOVING) {
                        cactiWalking.resume(walkingSound);
                    }
                    if (newState == PlayerState.DAMAGED) {
                        cactiWalking.pause(walkingSound);
                        sound_damaged.play();
                    }
                    if (newState == PlayerState.IDLE) {
                        cactiWalking.pause(walkingSound);
                    }
                }
            }
        }
    }

    //Observer Methods for TurnEnd
    public void observerUpdate(int i) {
        //Check if just died
        if (health <= 0 && !isDead()) { //Actions upon DEATH
            changeAnimation(PlayerState.DEAD);
        }
        if (!isDead()) {
            bulletCount += 3; // Add 3 more bullets
            if (bulletCount > 12) { //Set max bullets that can be stored
                bulletCount = 10;
            }
        }
    }

    public int observerType() {
        return 1;
    }


    public boolean isDead() {
        return playerState == PlayerState.DEAD;
    }
}

enum PlayerState {
    NONE,
    EMOTE,
    IDLE,
    MOVING,
    DAMAGED,
    COLLECTING_WATER,
    DEAD;

    static PlayerState getState(PlayerState e) {
        switch (e) {
            case IDLE: return IDLE;
            case EMOTE: return EMOTE;
            case MOVING: return MOVING;
            case DAMAGED: return DAMAGED;
            case COLLECTING_WATER: return COLLECTING_WATER;
            case DEAD: return DEAD;
            default: return null;
        }
    }

    public String toString() {
        switch(this) {
            case IDLE: return "IDLE";
            case EMOTE: return "EMOTE";
            case MOVING: return "MOVING";
            case DAMAGED: return "DAMAGED";
            case COLLECTING_WATER: return "COLLECTING_WATER";
            case DEAD: return "DEAD";
            default: return "null";
        }
    }


}

