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

/**
 * Player class. This class takes a CircleMapObject and renders a player sprite on top of it. Contains information and manipulation method for the player.
 *
 * Hierarchy
 * GameMap -> Player -> Bullet
 */
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
    private boolean turn = false;

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

    //Achievements
    private int lifeTimeKills = 0;
    private int lifeTimeHits = 0;
    private int lifeTimeWater = 0;
    private int lifeTimeShotsFired = 0;
    private int lifeTimeDmgTaken = 0;

    //add sounds for collision
    private Sound cactiWalking;
    private Sound cactiShoot;
    private Sound cactiDead;
    private Sound cactiWaterCollected;
    private Sound soundDamaged;
    private long walkingSound;

    //CONSTRUCTOR
    public Player(GameMap map, CircleMapObject collider, int d, int id, ActionResolver actionResolver) {
        TAG = "Player" + String.valueOf(id+1);
        playerID = id+1;
        this.actionResolver = actionResolver;
        //Listen in to EVERYTHING
        GameConstants.subscribeTurnStart(this);
        GameConstants.subscribeTurnEnd(this);
        GameConstants.subscribeGameEnd(this);

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

        //Setup Sounds
        cactiWalking = CactusLoader.sound_walking;
        cactiShoot = CactusLoader.sound_shoot;
        cactiDead = CactusLoader.sound_dead;
        cactiWaterCollected = CactusLoader.sound_water_collected;
        soundDamaged = CactusLoader.sound_damaged;



        //Set up player position & stats
        //Coordinates are the middle of the circle
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
        lifeTimeDmgTaken = 0;

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
                        s = new Sprite(CactusLoader.death_animation.getKeyFrame(stateDelta));
                        stateDelta += delta;
                    if (stateDelta > CactusLoader.death_animation.getAnimationDuration()) {
                        stateDelta = CactusLoader.death_animation.getFrameDuration() * 2;
                    }
                } else if (playerState == PlayerState.LOSE) {
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
                    s = new Sprite(animations.get(getdirection() + ".moving").getKeyFrame(stateDelta));


                } else if (playerState == PlayerState.DAMAGED) {
                    if (stateDelta <= animations.get("shot").getAnimationDuration()) {
                        //Gdx.app.log(TAG, "CurrentDelta: " + String.valueOf(stateDelta) + "Animation length: " + String.valueOf(animations.get("shot").getAnimationDuration()));
                        s = new Sprite(animations.get("shot").getKeyFrame(stateDelta));
                        stateDelta += delta;
                    } else {
                        playerState = PlayerState.NONE;             // Clears damaged state so we can change animation
                        soundDamaged.stop();                        // Stop damagedSound
                        changeAnimation(previousState);             // Change to the previous state whatever that was.
                        previousState = PlayerState.DAMAGED;        // Set the previousState to DAMAGED.
                        Gdx.app.log(TAG, playerState.toString());    //
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
                } else if (playerState == PlayerState.EMOTE) {
                    if (stateDelta > animations.get("emote").getAnimationDuration()) {
                        changeAnimation(PlayerState.IDLE);
                    } else {
                        s = new Sprite(animations.get("emote").getKeyFrame(stateDelta));
                        stateDelta += delta;
                    }
                } else if (playerState == PlayerState.WIN) {
                    s = new Sprite(animations.get("emote").getKeyFrame(stateDelta));
                    stateDelta += delta;
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

        //connect the healthBar to the health of the player
        if(health>=60) {
            healthBarFG2.setScale((float)(health-50)/50f, 1f);
            healthBarFG3.setScale(1,1);
            healthBarFG.setScale(0,0);
        }
        else{
            healthBarFG.setScale((float)health/50f, 1f);
            healthBarFG2.setScale(0,0);
            healthBarFG3.setScale(0,0);

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
            int moves;
            //no of move bars
            if(actionResolver.getMultiplayer())
                moves = actionResolver.getImMoves()[playerID-1];
            else
                    moves = 4;


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

    //Damaged by collision with rock or other neutral obstacles
    public synchronized void decreaseHealth(int dmg){
        if (!isDead()) {
            health -= dmg;
            lifeTimeDmgTaken += dmg;
            water = 0;
            if (health <= 0) {
                health = 0;
                dead = true; // Just died
            }
            changeAnimation(PlayerState.DAMAGED);
        }
    }

    //Damaged by collision with other player
    public synchronized void decreaseHealth(Player p){
        if (!isDead()) {
            health -= 10;
            lifeTimeDmgTaken += 10;
            water = 0;
            if (health <= 0) {
                health = 0;
                p.killAwarded();
                dead = true; // Just died
            }
            changeAnimation(PlayerState.DAMAGED);
        }
    }

    //Damaged by bullet
    public synchronized void decreaseHealth(Bullet b){
        if (!isDead()) {
            health -= 20;
            lifeTimeDmgTaken += 20;
            water = 0;
            if (health <= 0) {
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
        if (!isDead() && previousState != PlayerState.DAMAGED) { //Must not be dead or damaged this turn
            water += 1;
            cactiWaterCollected.play(); // Play sound
            lifeTimeWater += 1;
            if (water == 3) {
                score += 1;
                water = 0;
            }
            changeAnimation(PlayerState.COLLECTING_WATER);
        }
    }

	//Move in x - direction
	public synchronized void incrementX(float x) {
        if (!isDead()) {
            this.x += x;
            collider.getCircle().setPosition(this.x, this.y);
            changeAnimation(PlayerState.MOVING);
        }
    }

    //Move in y - direction
	public synchronized void incrementY(float y) {
        if (!isDead()) {
            this.y += y;
            collider.getCircle().setPosition(this.x, this.y);
            changeAnimation(PlayerState.MOVING);
        }
    }

    //Rotation
    public void rotate (int r) {
        this.dir += r;
        if (dir >= 360) {
            dir -= 360;
        }
        while (dir < 0) {
            dir = 360 + dir;
        }
    }

    //Whether if player is shooting
    public boolean isShooting() {
        return (shootLeft || shootRight);
    }

    //Starts player shooting left
    public void startShootLeft() {
        shootLeft = true;
        bulletCount -= 1;
        lifeTimeShotsFired += 1;
        cactiShoot.play();
    }

    //Starts player shooting right
    public void startShootRight() {
        shootRight = true;
        bulletCount -= 1;
        lifeTimeShotsFired += 1;
        cactiShoot.play();
    }

    //Ends player shooting left
    public void endShootLeft() {
        if (shootLeft) {
            shootLeft = false;
            bulletl.getReturn();
        }
    }

    //Ends player shooting right
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


    //Call this to get achievments at the end of the game.
    public HashMap<String, Integer> getAchievments() {
        HashMap<String, Integer> a = new HashMap<String, Integer>();
        a.put ("Kills", lifeTimeKills);
        a.put ("Hits", lifeTimeHits);
        a.put ("Shots Fired", lifeTimeShotsFired);
        a.put ("Water", lifeTimeWater);
        a.put ("Dmg Taken", lifeTimeDmgTaken);

        return a;

    }

    //Changes Animation and PlayerState to the next State provided.
    //If player is damaged, next state is placed under previousState as a hold to allow damaged animation to run.
    //Damaged animation will handle the transition back to what it should be in when damaged animation runs to the end.
    private void changeAnimation(PlayerState newState) {
        synchronized (statusLock) {
            if (playerState == PlayerState.DEAD ||  playerState == PlayerState.LOSE) {//Check if dead
                return; // DONT DO ANYTHING WHEN DEAD
            } else { //Not dead
                if (playerState == PlayerState.DAMAGED && newState != PlayerState.DAMAGED) {
                    previousState = newState; // Allows damaged animation to run finish
                } else if (playerState != newState) {
                    previousState = PlayerState.getState(playerState);
                    playerState = newState;
                    stateDelta = 0f;
                    statusLock.notifyAll();
                    if (newState == PlayerState.MOVING) {
                        walkingSound = cactiWalking.loop(0.5f);
                    }
                    if (newState == PlayerState.DAMAGED) {
                        cactiWalking.stop(walkingSound);
                        soundDamaged.play();
                    }
                    if (newState == PlayerState.IDLE) {
                        cactiWalking.stop(walkingSound);
                    }
                    if (newState == PlayerState.DEAD) {
                        cactiDead.play();
                    }
                }
            }
        }
    }

    public void emote() {
        if (turn == false && playerState != PlayerState.EMOTE) {
            changeAnimation(PlayerState.EMOTE);
        }
    }

    //Observer Methods
    public void observerUpdate(int i) {
        if (i == 0) { //Turn Start
            turn = true;
            changeAnimation(PlayerState.IDLE);
        }
        if (i == 1) { // Turn End
            turn  = false;
            //Check if just died
            if (health <= 0 && !isDead()) { //Actions upon DEATH
                changeAnimation(PlayerState.DEAD);
                collider.getCircle().setPosition(-100,100); // Move colliders out so other players cannot collide with it anymore
                bulletl.setReturn(-100,-100);
                bulletr.setReturn(-100,-100);
                bulletl.getReturn();
                bulletr.getReturn();
            }
            if (!isDead()) { // HouseKeeping Actions
                bulletCount += 3; // Add 3 more bullets
                if (bulletCount > 10) { //Set max bullets that can be stored
                    bulletCount = 10;
                }
            }
        }
        if (i == 2) { // Game End
            if (health <= 0 && !isDead()) { //Actions upon DEATH
                changeAnimation(PlayerState.DEAD);
                collider.getCircle().setPosition(-100,-100); // Move colliders out so other players cannot collide with it anymore
                bulletl.setReturn(-100,-100);
                bulletr.setReturn(-100,-100);
                bulletl.getReturn();
                bulletr.getReturn();
            }

            if ((GameConstants.myID + 1) == playerID) { // If this character is the client's character
                if (GameConstants.gameStateFlag.equals("L") && !isDead()) { //Lose
                    changeAnimation(PlayerState.LOSE);
                } else if (GameConstants.gameStateFlag.equals("W") && !isDead()) { // Win
                    changeAnimation(PlayerState.WIN);
                } else if (GameConstants.gameStateFlag.equals("D") && !isDead()){ // Draw
                    changeAnimation(PlayerState.EMOTE);
                }
            } else { // Character is others
                if (GameConstants.gameStateFlag.equals("L") && !isDead()) { //Other player win
                    changeAnimation(PlayerState.WIN);
                } else if (GameConstants.gameStateFlag.equals("W") && !isDead()) { // Other player loses
                    changeAnimation(PlayerState.LOSE);
                } else if (GameConstants.gameStateFlag.equals("D") && !isDead()){ // Draw
                    changeAnimation(PlayerState.EMOTE);
                }
            }
        }
    }

    public int observerType() {
        return 1;
    }

    //Returns whether player is dead
    //This state is only set at the end of the turn. For players that just died, refer to boolean dead
    public boolean isDead() {
        return playerState == PlayerState.DEAD;
    }
}

/**
 * PlayerState that is used for animation.
 */
enum PlayerState {
    NONE,
    EMOTE,
    IDLE,
    MOVING,
    DAMAGED,
    COLLECTING_WATER,
    LOSE,
    WIN,
    DEAD;

    static PlayerState getState(PlayerState e) {
        switch (e) {
            case IDLE: return IDLE;
            case EMOTE: return EMOTE;
            case MOVING: return MOVING;
            case DAMAGED: return DAMAGED;
            case COLLECTING_WATER: return COLLECTING_WATER;
            case LOSE: return LOSE;
            case WIN: return WIN;
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
            case LOSE: return "LOSE";
            case WIN: return "WIN";
            default: return "null";
        }
    }


}

