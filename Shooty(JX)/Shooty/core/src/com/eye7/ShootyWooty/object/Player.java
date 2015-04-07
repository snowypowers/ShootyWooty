package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.eye7.ShootyWooty.helper.CactusLoader;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.world.GameMap;

import java.util.HashMap;

public class Player {
    private final String TAG;
//    public static int nextID = 1;

    public GameMap map;

	private float x;
	private float y;
    private int dir;
    private int playerID;
    private Texture pic;
    public int health;

    private HashMap<String, Animation> animations = null;
    private PlayerState playerState;
    private PlayerState previousState;
    private long stateDelta;
    public Object statusLock;

	private Vector2 position;
	private Vector2 velocity;

    private Bullet bulletl;
    private Bullet bulletr;
    private boolean shootLeft;
    private boolean shootRight;

    private int score;
    private int water;

    private CircleMapObject collider;
    private float RADIUS = 2;


    private ShapeRenderer sr;


	// takes in x,y as origin
	public Player(GameMap map, CircleMapObject collider, int d, int id) {
        TAG = "Player" + String.valueOf(id+1);
        playerID = id+1;
     //   nextID++;

        this.map = map; // Reference to the GameMap object in order to get the positions of other objects;

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
        this.statusLock = new Object();
        playerState = PlayerState.IDLE;
        previousState = PlayerState.IDLE;

        this.pic = new Texture(Gdx.files.internal("players/player"+String.valueOf(playerID)+".png"));

        //Coordinates of the middle of the circle
		this.x = collider.getCircle().x;
		this.y = collider.getCircle().y;
        this.dir = d;

        health = 100;

		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);

        bulletl = new Bullet(270, this.x, this.y,this);
        bulletr = new Bullet(90, this.x, this.y,this);

        this.collider = collider;

        sr = new ShapeRenderer();

	}

    public void draw(SpriteBatch sb, float delta) {
        if (shootLeft) {
            bulletl.draw(sb);
        }
        if (shootRight) {
            bulletr.draw(sb);
        }
        Sprite s = null;
        synchronized (statusLock) {
            //Gdx.app.log(TAG, "Rendering");
            while (s == null) {
                if (playerState == PlayerState.DEAD) {
                    if (stateDelta <= animations.get("dead").getAnimationDuration()) {
                        stateDelta += delta;
                        s = new Sprite(animations.get("dead").getKeyFrame(stateDelta));
                    } else {
                        s = new Sprite(animations.get("dead").getKeyFrame(animations.get("dead").getAnimationDuration()));
                    }

                } else if (playerState == PlayerState.IDLE) {
                    if (previousState != PlayerState.IDLE) {
                        previousState = PlayerState.IDLE;
                        stateDelta = 0;
                    } else {
                        stateDelta += delta;
                    }
                    s = new Sprite(animations.get(getdirection() + ".idle").getKeyFrame(stateDelta));

                } else if (playerState == PlayerState.MOVING) {
                    if (previousState != PlayerState.MOVING) {
                        previousState = PlayerState.MOVING;
                        stateDelta = 0;
                    } else {
                        stateDelta += delta;
                    }
                    s = new Sprite(animations.get(getdirection()).getKeyFrame(stateDelta));

                } else if (playerState == PlayerState.DAMAGED) {
                    if (stateDelta <= animations.get("shot").getAnimationDuration()) {
                        s = new Sprite(animations.get("shot").getKeyFrame(stateDelta));
                    } else {
                        playerState = previousState; // Restore previous state
                        previousState = PlayerState.DAMAGED;
                        statusLock.notifyAll();
                    }

                } else if (playerState == PlayerState.COLLECTING_WATER) {
                    if (previousState != PlayerState.DAMAGED) { //If not damaged
                        if (stateDelta <= animations.get("score").getAnimationDuration()) {
                            s = new Sprite(animations.get("score").getKeyFrame(stateDelta));
                        } else {
                            playerState = previousState; // Restore previous state
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
        //s.setPosition(x,y);
        s.draw(sb);
        sb.end();
        //Draw stuff here
        if (GameConstants.DEBUG) {
            sr.setColor(Color.BLACK);
            sr.setProjectionMatrix(sb.getProjectionMatrix());
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.circle(collider.getCircle().x,collider.getCircle().y,collider.getCircle().radius);
            sr.end();
        }
        sb.begin();

    }

    public synchronized void decreaseHealth(int dmg){
        health-=dmg;
        water = 0;
        if (health < 0) {
            health = 0;
        }
        changeAnimation(PlayerState.DAMAGED);
    }

    public synchronized void collectWater() {
        water += 1;
         if (water == 3) {
             score += 1;
             water = 0;
         }
        changeAnimation(PlayerState.COLLECTING_WATER);
    }


	// setters
	public synchronized void incrementX(float x) {
		this.x +=x;
        collider.getCircle().setPosition(this.x,this.y);
        velocity = new Vector2(x,0);
//        Gdx.app.log(TAG,collider.x+" X");
        changeAnimation(PlayerState.MOVING);
	}

	public synchronized void incrementY(float y) {
		this.y += y;
        collider.getCircle().setPosition(this.x,this.y);
        velocity = new Vector2(0,y);
//        Gdx.app.log(TAG,collider.y+" Y");
        changeAnimation(PlayerState.MOVING);
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
    }

    public void startShootRight() {
        shootRight = true;
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
	public Vector2 getPosition() {
		return position;
	}

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

    public int getHealth(){
        return health;
    }

    public CircleMapObject getCollider(){
        return collider;
    }

    //Aligns player into the closest gridbox
    //Calling this will also end player movement animation
    public synchronized void snapInGrid() {
        velocity = new Vector2(0,0);
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

    private void setUpSprites() {
        String path = "Character"+String.valueOf(playerID);


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

    private void changeAnimation(PlayerState newState) {
        synchronized (statusLock) {
            if (playerState != PlayerState.DEAD) {
                if (playerState == PlayerState.DAMAGED) { //Check if not dead
                    previousState = newState; // Allows damaged animation to run finish
                } else {
                    previousState = playerState;
                    playerState = newState;
                    stateDelta = 0;
                    statusLock.notifyAll();
                }
            }
        }
    }
}

enum PlayerState {
    IDLE,
    MOVING,
    DAMAGED,
    COLLECTING_WATER,
    DEAD

}