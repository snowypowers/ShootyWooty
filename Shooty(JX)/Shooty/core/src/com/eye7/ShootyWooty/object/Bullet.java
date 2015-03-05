package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	
	private Vector2 position;
	private Vector2 velocity;
	
	private Circle bullet;
	private boolean shoot;
	private float x;
	private float y;
    private float returnX;
    private float returnY;
	private Player player;

	public Bullet(float x, float y, Player player){
		this.x = x;
		this.y = y;
	}
    public void incrementX(float x){
        this.x += x;
    }
    public void incrementY(float y){
        this.y += y;
    }
    public void setReturn(float x, float y){
        returnX = x;
        returnY = y;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
	public void getReturn(){
        x=returnX;
        y=returnY;
    }
}
