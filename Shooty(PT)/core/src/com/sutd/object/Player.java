package com.sutd.object;

import sun.net.www.content.text.plain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private float x;
	private float y;

	private Vector2 position;
	private Vector2 velocity;
	// takes in x,y as origin
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
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
