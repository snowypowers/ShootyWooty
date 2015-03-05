package com.sutd.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sutd.gameworld.GameWorld;

//import com.sutd.object.MoveDisplay;

public class MoveHandler {
	private GameWorld world;
	private int getX = 0;
	private int getY = 0;
	private boolean shot = false;

    private int countR = 0;
    private int countL = 0;

	public boolean getShot(){
		return shot;
	}
	public void setShot(boolean set){
		shot = set;
	}

	public MoveHandler(GameWorld gameWorld) {
		world = gameWorld;
	}

	public void executeMove(float delta, String s) {
		Gdx.app.log("MoveHandler", "execute");
		Gdx.app.log("move", s);
		// for(String s:moves){
		if (s.contains("F")) {
			world.getPlayer1().isMovedX(0);
			world.getPlayer1().isMovedY(-20);
		} else if (s.contains("R")) {
			world.getPlayer1().isMovedX(20);
			world.getPlayer1().isMovedY(0);
		} else if (s.contains("L")) {
			world.getPlayer1().isMovedX(-20);
			world.getPlayer1().isMovedY(0);
		} else if (s.contains("D")) {
			world.getPlayer1().isMovedX(0);
			world.getPlayer1().isMovedY(20);
		} else if (s.contains("B")) {
			world.getPlayer1().isMovedX(0);
			world.getPlayer1().isMovedY(0);
		}
		if (world.getPlayer1().getIsMoveX())
			world.getPlayer1().setX(world.getPlayer1().getMoveX());

		if (world.getPlayer1().getIsMoveY())
			world.getPlayer1().setY(world.getPlayer1().getMoveY());

		Gdx.app.log("MoveHandler", "end");
	}

	public boolean shootBullet(float delta) {
//        Gdx.app.log("MoveHandler-Bulletl",world.getBulletl().getX()+"");
//        Gdx.app.log("MoveHandler-BulletR",world.getBulletl().getX()+"");
        if (world.getBulletl().isShoot()) {

//			if (world.getBulletr().getPosition().x > getX + 10
//					|| world.getBulletr().getPosition().x > 20
//					|| world.getBulletl().getPosition().x < getX - 10
//					|| world.getBulletl().getPosition().x < -20)
// {
            if (countL >= 60) {
//                world.getBulletl().reset(getX, getY);
//                world.getBulletr().reset(getX, getY);
//                Gdx.app.log("MoveHandler2",shot+"");
                countL = 0;
                return true;
            } else {
//				if (world.getBulletl().isShoot("l"))
//					world.getBulletl().shoot(delta);
//				if (world.getBulletr().isShoot("r"))
//					world.getBulletr().shoot(delta);
                Gdx.app.log("MoveHandler-Bulletl", world.getBulletl().getX() + "");
                Gdx.app.log("MoveHandler-Bulletl", world.getBulletl().getY() + "");
                world.getBulletl().setSpeed("l");
                countL++;

//                Gdx.app.log("MoveHandler",shot+"");
                return false;
            }
        }
        if (world.getBulletr().isShoot()) {
            if (countR >= 60) {

//                world.getBulletl().reset(getX, getY);
//                world.getBulletr().reset(getX, getY);
//                Gdx.app.log("MoveHandler2",shot+"");
                countR = 0;
                return true;
            } else {
//				if (world.getBulletl().isShoot("l"))
//					world.getBulletl().shoot(delta);
//				if (world.getBulletr().isShoot("r"))
//					world.getBulletr().shoot(delta);
                Gdx.app.log("MoveHandler-BulletR", world.getBulletr().getX() + "");
                Gdx.app.log("MoveHandler-BulletR", world.getBulletr().getY() + "");
                world.getBulletr().setSpeed("r");
                countR++;
//                Gdx.app.log("MoveHandler",shot+"");
                return false;
            }
        }
        return true;
		}



    public void setBulletPosition(String s){
        getX = (int) world.getPlayer1().getX();
        getY = (int) world.getPlayer1().getY();
        Gdx.app.log("MoveHandler-PlayerPosition","X:"+getX+",Y:"+getY);
        world.getBulletl().setX(getX);
        world.getBulletl().setY(getY);
        world.getBulletr().setX(getX);
        world.getBulletr().setY(getY);
        Gdx.app.log("GameWorld", "X");
        if (s.substring(0, 1).equals("1")) {
            Gdx.app.log("MoveHandler", "Shoot CommandL");
            world.getBulletl().setShoot(true);
        }
        if (s.substring(2).equals("1")) {
            Gdx.app.log("MoveHandler", "Shoot CommandR");
            world.getBulletr().setShoot(true);
        }
    }
    public void resetBullet(){
        world.getBulletl().setShoot(false);
        world.getBulletr().setShoot(false);
        world.getBulletl().setX(getX);
        world.getBulletl().setY(getY);
        world.getBulletr().setX(getX);
        world.getBulletr().setY(getY);
        Gdx.app.log("MoveHandler-BulletAfterShootPosition","X:"+getX+",Y:"+getY);
        Gdx.app.log("MoveHandler-BulletAfterShootL", world.getBulletl().getX() + "");
        Gdx.app.log("MoveHandler-BulletAfterShootR", world.getBulletr().getX() + "");
//        setShot(true);
//        world.getPlayer1().reset();
    }
}