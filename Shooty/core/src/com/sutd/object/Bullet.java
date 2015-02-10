package com.sutd.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	
	private Vector2 position;	
	private Vector2 velocity;
	
	private Circle bullet;
	private boolean shoot;
	private int x;
	private int y;
	private Player player;

	public Bullet(int x, int y, Player player){
		position = new Vector2(x,y);
		velocity = new Vector2(0,0);
		shoot = false;
		this.x = x;
		this.y = y;
		player = new Player(x, y);
	}
	public void shoot(float delta){
		position.add(velocity.cpy().scl(delta));
	}

	public void setShoot(boolean shoot){
		this.shoot = shoot;
	}
	public boolean isShoot() {
		velocity.add(0,2);
		return shoot;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void reset(int getX, int getY){
		velocity.set(0, 0);
		position.set(getX,getY);
	}
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getVelocity() {
		return velocity;
	}
	
}
