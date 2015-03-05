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

    public void setSpeed(String s){
        if (s.equalsIgnoreCase("r")) x+=1;
//			velocity.add(1,0);
        else if (s.equalsIgnoreCase("l")) x-=1;
//			velocity.add(-1,0);
    }


	public boolean isShoot() {
		return shoot;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

	public void reset(int getX, int getY){
		velocity.set(0, 0);
		position.set(getX,getY);
		shoot = false;
	}
	public Vector2 getPosition() {
		return position;
	}
	public Vector2 getVelocity() {
		return velocity;
	}
	
}
