package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eye7.ShootyWooty.model.Direction;

public class Player {
    public static int nextID = 1;

	private float x;
	private float y;
    private int dir;
    private int playerID;
    private Texture pic;

	private Vector2 position;
	private Vector2 velocity;
	// takes in x,y as origin
	public Player(int x, int y, int d) {
        playerID = nextID;
        nextID++;
        Gdx.app.log("Player", String.valueOf(playerID));

        this.pic = new Texture(Gdx.files.internal("players/player"+String.valueOf(playerID)+".png"));
		this.x = x;
		this.y = y;
        this.dir = d;

		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
	}

    public void draw(SpriteBatch sb) {
        Sprite s = new Sprite(pic);
        s.setPosition(x,y);
        s.rotate(dir);
        s.draw(sb);

    }


	// setters
	public void incrementX(float x) {
		this.x +=x;
	}

	public void incrementY(float y) {
		this.y += y;
	}
	
	// getters
	public Vector2 getPosition() {
		return position;
	}

	public float getX() {
		return x;
	}

    public float getY() {
        return y;
    }
	// reset methods



}
