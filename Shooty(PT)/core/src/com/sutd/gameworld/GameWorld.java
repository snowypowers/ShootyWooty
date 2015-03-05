package com.sutd.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sutd.helpers.MoveHandler;
import com.sutd.object.Bullet;
import com.sutd.object.Button;
import com.sutd.object.Player;
import com.sutd.object.Timer;

/**
 * @author PT
 * handles all the object in game
 * feed update to gameScreen
 */
public class GameWorld {
	private Stage stage;
	private Button button0;
	private Button button1;
	private Button button2;
	private Button button3;
	private Player player1;
	private String[] moves = { "0B0", "0B0", "0B0", "0B0" };

	private String out;
	private int time;
	private Timer timer;
	private String timeStatus;
	private Bullet bulletl;
	private Bullet bulletr;
	private MoveHandler moveHandler;
	private Boolean check;

    private int pointer;
    private float oldPositionX;
    private float oldPositionY;
    private boolean stop;

    private float[] movement;
     private String command;
    private float bulletDistanceR;
    private float bulletDistanceL;

    public GameWorld(Stage stage) {
		this.stage = stage;
		player1 = new Player(10,100);
		bulletl = new Bullet(player1.getX(), player1.getY(),player1);
		bulletr = new Bullet(player1.getX(), player1.getY(),player1);
		button0 = new Button(320, 350, stage);
		button1 = new Button(320, 240, stage);
		button2 = new Button(320, 130, stage);
		button3 = new Button(320, 20, stage);
		moveHandler = new MoveHandler();
		timer = new Timer(0); 
		timer.start(); // start timer
		check = false;
		pointer = 0;
        movement = new float[3];
        stop = false;
        bulletDistanceR = 20;
        bulletDistanceL = 20;
        command = moves[0];
	}

	// constantly call this method
	public void update(float delta) {

		// always get move from button even if no change
		moves[0] = button0.getMoves();
		moves[1] = button1.getMoves();
		moves[2] = button2.getMoves();
		moves[3] = button3.getMoves();
		out = "Player deciding...";
		if (time == 10) {
			out = moves[0] + moves[1] + moves[2] + moves[3]; // this out stores player inputs

			button0.setLock(true); // lock the button from being pressed while executing moves
			button1.setLock(true);
			button2.setLock(true);
			button3.setLock(true);
			// if there are 4 moves input execute the moves
            if(!stop) {
                if (check) {
                    if (Math.abs(movement[0]) > 0) {
                        movement[0] += movement[2];
                        player1.incrementX(movement[2] * -1);
                        bulletl.incrementX(movement[2] * -1);
                        bulletr.incrementX(movement[2] * -1);
//                        Gdx.app.log("GameWorld-X", movement[0] + "");
//                        Gdx.app.log("GameWorld-X", player1.getX() + "");
                        bulletl.setReturn(player1.getX(),player1.getY());
                        bulletr.setReturn(player1.getX(),player1.getY());

                    } else if (Math.abs(movement[1]) > 0) {
                        movement[1] += movement[2];
                        player1.incrementY(movement[2] * -1);
                        bulletl.incrementY(movement[2] * -1);
                        bulletr.incrementY(movement[2] * -1);
//                        Gdx.app.log("GameWorld-Y", player1.getY() + "");
                        bulletl.setReturn(player1.getX(),player1.getY());
                        bulletr.setReturn(player1.getX(),player1.getY());
                    } else {

                         if(command.substring(2).equals("1")&&command.substring(0,1).equals("0")){
                            if(bulletDistanceR>0){
                                bulletDistanceR-=1f;
                                bulletr.incrementX(1);
                                Gdx.app.log("GameWorld-Bullet",bulletr.getX()+"");
                            }
                            else{
                                bulletDistanceR=20;
                                bulletr.getReturn();
                                check = false;

                            }

                        }
                        else if(command.substring(0,1).equals("1")&&command.substring(2).equals("0")){
                            if(bulletDistanceL>0){
                                bulletDistanceL-=1f;
                                bulletl.incrementX(-1);
//                                Gdx.app.log("GameWorld-Bullet",bulletl.getX()+"");
                            }
                            else{
                                bulletDistanceL=20;
                                bulletl.getReturn();
                                check = false;
                            }
                        }
                        else if(command.substring(0,1).equals("1")&&command.substring(2).equals("1")){
                             if(bulletDistanceL>0 && bulletDistanceR>0){
                                 bulletDistanceL-=1f;
                                 bulletl.incrementX(-1);
                                 bulletDistanceR-=1f;
                                 bulletr.incrementX(1);

                             }
                             else{
                                 bulletDistanceR=20;
                                 bulletr.getReturn();
                                 bulletDistanceL=20;
                                 bulletl.getReturn();
                                 check = false;
                             }
                         }
                        else check = false;
                    }
                } else {
                    command = moves[pointer];
                    Gdx.app.log("GameWorld", command);
                    movement = moveHandler.AmountToMove(command);
                    pointer += 1;
                    if (pointer == 4) {
                        pointer = 0;
                        stop=true;

                    }
                    check = true;
                }
            }

			button0.resetButton(); // reset the button display
			button1.resetButton();
			button2.resetButton();
			button3.resetButton();

		}
		if (time == 0) {
            stop = false;
            check = false;
			button0.setLock(false); // release the lock
			button1.setLock(false);
			button2.setLock(false);
			button3.setLock(false);
			
			button0.resetMoves(); // reset the moves to "0B0"
			button1.resetMoves();
			button2.resetMoves();
			button3.resetMoves();

		}
		
		time = timer.getTime(); // get time from time thread
		timeStatus = timer.getTimeStatus();

	}

	// getters for renderer

	public String getOut() {
		return out;
	}

	public int getTime() {
		return time;
	}

	public Stage getStage() {
		return stage;
	}

	public Button getButton1() {
		return button1;
	}

	public Button getButton2() {
		return button2;
	}

	public Button getButton3() {
		return button3;
	}

	public Button getButton0() {
		return button0;
	}
	public String getTimeStatus() {
		return timeStatus;
	}
	public Bullet getBulletl(){
		return bulletl;
	}
	public Bullet getBulletr(){
		return bulletr;
	}
	public Player getPlayer1(){
		return player1;
	}

}
