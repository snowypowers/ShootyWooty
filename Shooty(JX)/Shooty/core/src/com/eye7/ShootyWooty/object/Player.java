package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.world.GameMap;

public class Player {
    public static int nextID = 1;

    public GameMap map;

	private float x;
	private float y;
    private int dir;
    private int playerID;
    private Texture pic;
    public int health;

	private Vector2 position;
	private Vector2 velocity;


    private Bullet bulletl;
    private Bullet bulletr;
    private boolean shootLeft;
    private boolean shootRight;

    private CircleMapObject collider;
    private float RADIUS = 2;

    private Animation animation_idle;
    private Animation animation_north;
    private Animation animation_south;
    private Animation animation_west;
    private Animation animation_east;
    private float animationFrameTime;

    private ShapeRenderer healthBar;


	// takes in x,y as origin
	public Player(GameMap map, CircleMapObject collider, int d) {
        playerID = nextID;
        nextID++;
        Gdx.app.log("Player", String.valueOf(playerID));

        this.map = map; // Reference to the GameMap object in order to get the positions of other objects;

        setUpSprites();
        this.pic = new Texture(Gdx.files.internal("players/player"+String.valueOf(playerID)+".png"));

        //Coordinates of the middle of the circle
		this.x = collider.getCircle().x;
		this.y = collider.getCircle().y;
        this.dir = d;

        health = 100;

		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);

        bulletl = new Bullet(this.x, this.y,this);
        bulletr = new Bullet(this.x, this.y,this);

        this.collider = collider;

        healthBar = new ShapeRenderer();
        healthBar.setColor(Color.BLACK);
	}

    public void draw(SpriteBatch sb, float delta) {
        if (shootLeft) {
            bulletl.draw(sb);
        }
        if (shootRight) {
            bulletr.draw(sb);
        }
        Sprite s = new Sprite(pic);
        s.setCenter(32,32);
        s.setRotation(dir);
        s.setPosition(x,y);
        s.draw(sb);

        if (GameConstants.DEBUG) {
            healthBar.begin(ShapeRenderer.ShapeType.Line);
            healthBar.circle(collider.getCircle().x,collider.getCircle().y,collider.getCircle().radius);
            healthBar.end();
        }
    }

    public synchronized void decreaseHealth(){
        health-=20;
    }


	// setters
	public void incrementX(float x) {
		this.x +=x;
        collider.getCircle().setPosition(this.x,this.y);
//        Gdx.app.log("Player",collider.x+" X");
	}

	public void incrementY(float y) {
		this.y += y;
        collider.getCircle().setPosition(this.x,this.y);
//        Gdx.app.log("Player",collider.y+" Y");
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
    public void snapInGrid() {
        //Snaps to 64 pixel grid
        float Xoff = x % 32;
        float Yoff = y % 32;
        int Roff = dir % 90;
        if (Xoff < 16) {
            incrementX(-Xoff);
            //Gdx.app.log("playerSnapX", String.valueOf(-Xoff));
        } else {
            incrementX(32 - Xoff);
            //Gdx.app.log("playerSnapX", String.valueOf(32 - Xoff));
        }

        if (Yoff < 16) {
            incrementY(-Yoff);
            //Gdx.app.log("playerSnapY", String.valueOf(-Yoff));
        } else {
            incrementY(32 - Yoff);
            //Gdx.app.log("playerSnapY", String.valueOf(32 -Yoff));
        }
        if (Roff < 45) {
            rotate(-Roff);
            //Gdx.app.log("playerSnapR", String.valueOf(-Roff));
        } else {
            rotate(90 - Roff);
            //Gdx.app.log("playerSnapR", String.valueOf(90 - Roff));
        }
    }

    private void setUpSprites() {
        String path = "Character"+String.valueOf(playerID);


    }



}
