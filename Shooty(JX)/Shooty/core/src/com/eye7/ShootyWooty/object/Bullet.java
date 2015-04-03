package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    //Texture for bullet
    private Texture pic = new Texture(Gdx.files.internal("bullet_L.png"));

    private Vector2 position;
    private Vector2 velocity;

    private CircleMapObject collider; // for collision
    private boolean shoot;
    private float x;
    private float y;
    private int dir;
    private float returnX;
    private float returnY;
    private Player player;
    private float RADIUS = 3;

    public Bullet(int dir, float x, float y, Player player){
        this.dir = dir;
        this.x = x;
        this.y = y;
        this.player = player;
        returnX = player.getX();
        returnY = player.getY();
        collider = new CircleMapObject(x,y,RADIUS);
    }

    public void draw(SpriteBatch sb) {
        Sprite s = new Sprite(pic);
        s.setCenter(x,y);
        s.setRotation((dir + player.getDir() -270)%360);
        s.draw(sb);
    }

    // setters
    public void incrementX(float x){
        this.x += x;
        collider.getCircle().set(this.x,this.y,RADIUS);
    }
    public void incrementY(float y){
        this.y += y;
        collider.getCircle().set(this.x,this.y,RADIUS);
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
        collider.getCircle().set(this.x,this.y,RADIUS);

    }
    public CircleMapObject getCollider() {
        return collider;
    }
}
