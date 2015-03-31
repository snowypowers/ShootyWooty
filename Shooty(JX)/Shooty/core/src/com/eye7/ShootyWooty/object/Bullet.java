package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    private static Texture pic = new Texture(Gdx.files.internal("bullet_2.png"));

    private Vector2 position;
    private Vector2 velocity;

    private CircleMapObject boundingCircle; // for collision
    private boolean shoot;
    private float x;
    private float y;
    private float returnX;
    private float returnY;
    private Player player;
    private float RADIUS = 1;

    public Bullet(float x, float y, Player player){
        this.x = x;
        this.y = y;
        returnX = player.getX();
        returnY = player.getY();
        boundingCircle = new CircleMapObject(x,y,RADIUS);
    }

    public void draw(SpriteBatch sb) {
        Sprite s = new Sprite(pic);
        s.setPosition(x,y);
        s.draw(sb);
    }

    // setters
    public void incrementX(float x){
        this.x += x;
        boundingCircle.getCircle().set(this.x,this.y,RADIUS);
    }
    public void incrementY(float y){
        this.y += y;
        boundingCircle.getCircle().set(this.x,this.y,RADIUS);
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
        boundingCircle.getCircle().set(this.x,this.y,RADIUS);

    }
    public CircleMapObject getBoundingCircle() {
        return boundingCircle;
    }
}
