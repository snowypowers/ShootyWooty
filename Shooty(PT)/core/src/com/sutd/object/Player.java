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

    private Bullet bulletl;
    private Bullet bulletr;

    private Circle boundingCircle;
    private float RADIUS = 5;

    private float health;
    // takes in x,y as origin
	public Player(float x, float y) {
		this.x = x;
		this.y = y;
		position = new Vector2(x, y);
		velocity = new Vector2(0, 0);
        bulletl = new Bullet(this.x, this.y,this);
        bulletr = new Bullet(this.x, this.y,this);
        boundingCircle = new Circle(this.x,this.y,RADIUS);
        health=100;
	}

    // decrease health
    public void decreaseHealth(){
        health-=10;
    }

	// setters
	public void incrementX(float x) {
		this.x +=x;
        boundingCircle.set(this.x,y,RADIUS);
	}

	public void incrementY(float y) {
		this.y += y;
        boundingCircle.set(x,this.y,RADIUS);
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

    public Circle getBoundingCircle(){
        return boundingCircle;
    }

    public float getHealth(){
        return health;
    }

    public Bullet getBulletl() {return bulletl;}

    public Bullet getBulletr() {return bulletr;}




}
