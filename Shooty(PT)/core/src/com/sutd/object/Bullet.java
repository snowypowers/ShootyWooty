package com.sutd.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
	
	private Vector2 position;	
	private Vector2 velocity;
	
	private Circle boundingCircle; // for collision
	private boolean shoot;
	private float x;
	private float y;
    private float returnX;
    private float returnY;
	private Player player;
    private float RADIUS = 2;

	public Bullet(float x, float y, Player player){
		this.x = x;
		this.y = y;
        returnX = x;
        returnX = y;
        boundingCircle = new Circle(x,y,RADIUS);
	}

    // setters
    public void incrementX(float x){
        this.x += x;
        boundingCircle.set(this.x,y,RADIUS);
    }
    public void incrementY(float y){
        this.y += y;
        boundingCircle.set(this.x,y,RADIUS);
    }
    public void setReturn(float x, float y){
        returnX = x;
        returnY = y;
    }

    // getters
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
	public void getReturn(){
        x=returnX;
        y=returnY;
        boundingCircle.set(x,y,RADIUS);

    }
    public Circle getBoundingCircle() {
        return boundingCircle;
    }
}
