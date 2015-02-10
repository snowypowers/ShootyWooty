package com.sutd.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.sutd.object.Bullet;
import com.sutd.object.Button;
import com.sutd.object.Player;

public class GameWorld {

	// private Rectangle button1 = new Rectangle(2+18,182,20,20);
	// private Rectangle button2 = new Rectangle(27+18,182,20,20);
	// private Rectangle button3 = new Rectangle(52+18,182,20,20);
	// private Rectangle button4 = new Rectangle(77+18,182,20,20);

	private Player player1;
	private Player player2;
	private int getX;
	private int getY;
	private Bullet bullet;
	private int originX;
	private int originY;

	public GameWorld() {
		player1 = new Player(10, 10);
		player2 = new Player(126, 130);
		bullet = new Bullet(player1.getY(), player1.getX(), player1);
	}

	public void update(float delta) {
		// Gdx.app.log("GameWorldX", player1.getX()+"");
		// Gdx.app.log("GameWorldY", player1.getY()+"");
		player1.update(delta);
		originX = (int) player1.getPosition().x;
		originY = (int) player1.getPosition().y;
		if (player1.getIsMoveX() || player1.getIsMoveY()) {
//			while (player1.getPosition().x < (originX + player1.getMoveX())) {
//				player1.update(delta);
//			}
				Gdx.app.log("GameWorld", "moveX");
				player1.getPosition().add(player1.getMoveX(),0);
				Gdx.app.log("GameWorld", "satisfy");
				player1.resetX();
				getX = (int) player1.getPosition().x;

				Gdx.app.log("GameWorld", getX + "");
				// if (player1.getIsMoveY()) {
				// if (player1.getPosition().y == (player1.getPosition().y +
				// player1
				// .getMoveY())) {
				player1.getPosition().add(0, player1.getMoveY());
				player1.resetY();
				getY = (int) player1.getPosition().y;
				// } else
				// player1.update(delta);
				// }
			
			
			player1.reset();

			bullet.getPosition().x = player1.getPosition().x;
			bullet.getPosition().y = player1.getPosition().y;
			Gdx.app.log("GameWorld", getX + "");

			bullet.setShoot(true);
		}
		if (bullet.isShoot()) {
			if (bullet.getPosition().y > getY+80 || bullet.getPosition().y>160) {
				bullet.setShoot(false);
				bullet.reset(getX, getY);
			} else {
				bullet.shoot(delta);
			}
		}

	}

	public Bullet getBullet() {
		return bullet;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}

}
