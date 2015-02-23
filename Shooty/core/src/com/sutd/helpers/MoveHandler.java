package com.sutd.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sutd.gameworld.GameWorld;
import com.sutd.object.MoveDisplay;

public class MoveHandler {
	private GameWorld world;
	private int getX = 0;
	private int getY = 0;

	public MoveHandler(GameWorld gameWorld) {
		world = gameWorld;
	}

	public void executeMove(float delta, String s) {
		Gdx.app.log("MoveHandler", "execute");
		// for(String s:moves){
		if (s.contentEquals("F")) {
			world.getPlayer1().isMovedX(20);
			world.getPlayer1().isMovedY(0);
		} else if (s.contentEquals("F&R")) {
			world.getPlayer1().isMovedX(20);
			world.getPlayer1().isMovedY(20);
		} else if (s.contentEquals("F&L")) {
			world.getPlayer1().isMovedX(20);
			world.getPlayer1().isMovedY(-20);
		} else if (s.contentEquals("B")) {
			world.getPlayer1().isMovedX(-20);
			world.getPlayer1().isMovedY(0);
		}
		Gdx.app.log("MoveHandler", "end");
	}

	public void render(float delta) {

		if (world.getPlayer1().getIsMoveX() || world.getPlayer1().getIsMoveY()) {
			// getX = (int) world.getPlayer1().getPosition().x;

			// while (world.getPlayer1().getPosition().x <= getX
			// + world.getPlayer1().getMoveX()) {
			// world.getPlayer1().update(delta);

			// }
			int startX = (int) world.getPlayer1().getPosition().x;
			world.getPlayer1().getPosition()
					.add(world.getPlayer1().getMoveX(), 0);
			world.getPlayer1().resetX();
			getX = (int) world.getPlayer1().getPosition().x;

			world.getPlayer1().getPosition()
					.add(0, world.getPlayer1().getMoveY());
			world.getPlayer1().resetY();
			getY = (int) world.getPlayer1().getPosition().y;

			world.getPlayer1().reset();

			world.getBullet().getPosition().x = world.getPlayer1()
					.getPosition().x;
			world.getBullet().getPosition().y = world.getPlayer1()
					.getPosition().y;
			Gdx.app.log("GameWorld", getX + "X");
			Gdx.app.log("GameWorld", getY + "Y");

			world.getBullet().setShoot(true);
		}
		if (world.getBullet().isShoot()) {
			if (world.getBullet().getPosition().y > getY + 80
					|| world.getBullet().getPosition().y > 160) {
				world.getBullet().setShoot(false);
				world.getBullet().reset(getX, getY);
			} else {
				world.getBullet().shoot(delta);
			}
		}

	}
}
