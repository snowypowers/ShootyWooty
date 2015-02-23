package com.sutd.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sutd.helpers.MoveHandler;
import com.sutd.object.Bullet;
import com.sutd.object.Button;
import com.sutd.object.MoveDisplay;
import com.sutd.object.Player;

public class GameWorld {

	// private Rectangle button1 = new Rectangle(2+18,182,20,20);
	// private Rectangle button2 = new Rectangle(27+18,182,20,20);
	// private Rectangle button3 = new Rectangle(52+18,182,20,20);
	// private Rectangle button4 = new Rectangle(77+18,182,20,20);

	private Player player1;
	private Player player2;
	private Bullet bullet;
	private MoveHandler moveHandler;
	private MoveDisplay moveDisplay;
	
	private Stage stage;
	
	private Button buttonF;
	private Button buttonFR;
	private Button buttonFL;
	private Button buttonB;
	private Button buttonReset;
	

	public GameWorld(Stage stage) {
		player1 = new Player(10, 10);
		player2 = new Player(126, 130);
		bullet = new Bullet(player1.getY(), player1.getX(), player1);
		moveHandler = new MoveHandler(this);
		this.moveDisplay = new MoveDisplay(stage);
		this.stage = stage;
		buttonF = new Button(45, 35, this, "F", stage, moveDisplay);
		buttonFR = new Button(85, 35, this, "F&R", stage, moveDisplay);
		buttonFL = new Button(125, 35, this, "F&L", stage,	moveDisplay);
		buttonB = new Button(165, 35, this, "B", stage, moveDisplay);
		buttonReset = new Button(210, 35, this, "RESET", stage,moveDisplay);

	}
	// constantly call this method
	public void update(float delta) {
		// constantly display "Player input"
		moveDisplay.render(delta);
		// if there are 4 moves input execute the moves
		if (moveDisplay.isExecute()) {
			Gdx.app.log("GameWorld", player1.getIsMoveX() + "");
			for (String s : moveDisplay.getMoves()) {
				// change the coordinates
				moveHandler.executeMove(delta, s);		
				// draw the player
				moveHandler.render(delta);
			}
			// reset the arraylist for moves
			moveDisplay.reset();
		}
		
	}
	// getters

	public Stage getStage() {
		return stage;
	}

	public Button getButtonF() {
		return buttonF;
	}

	public Button getButtonFR() {
		return buttonFR;
	}

	public Button getButtonFL() {
		return buttonFL;
	}

	public Button getButtonB() {
		return buttonB;
	}

	public Button getButtonReset() {
		return buttonReset;
	}	

	public MoveDisplay getMoveDisplay() {
		return moveDisplay;
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
