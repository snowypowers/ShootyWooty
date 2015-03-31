package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.TurnHandler;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Button;
import com.eye7.ShootyWooty.object.Timer;

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

    private String[] moves = { "0B0", "0B0", "0B0", "0B0" };
    private String out;
    private int time;
    private Timer timer;
    private Object timerReset = new Object();
    private String timeStatus;
    private TurnHandler th;
    private ActionResolver actionResolver;
    public GameWorld(Stage stage, ActionResolver actionResolver) {
        this.stage = stage;
        this.actionResolver = actionResolver;
        button0 = new Button(800, 430, stage);
        button1 = new Button(800, 320, stage);
        button2 = new Button(800, 210, stage);
        button3 = new Button(800, 100, stage);
        timer = new Timer(timerReset);
        timer.start(); // start timer
    }

    // constantly call this method
    public void update(float delta) {

        // always get move from button even if no change
        moves[0] = button0.getMoves();
        moves[1] = button1.getMoves();
        moves[2] = button2.getMoves();
        moves[3] = button3.getMoves();

        out = "Player deciding...";
        if (time >= 30) {
            timer.reset();

            out = moves[0] +" "+ moves[1] +" "+ moves[2] +" "+ moves[3]; // this out stores player inputs
            Gdx.app.log("GameWorld", out);
            actionResolver.sendMessageHost(out);
            while(!actionResolver.getValid()){
                continue;
            }

            String OppMoves = actionResolver.getMoves();
            Gdx.app.log("Opponent Moves", OppMoves);
            actionResolver.setValid(false);
            button0.setLock(true); // lock the button from being pressed while executing moves
            button1.setLock(true);
            button2.setLock(true);
            button3.setLock(true);

            button0.resetButton(); // reset the button display
            button1.resetButton();
            button2.resetButton();
            button3.resetButton();
            Gdx.app.log("GameWorld", "Creating TurnHandler");
             th = new TurnHandler();

            //Code to add turns to TurnHandler here
            th.addTurn(GameConstants.PLAYER_TAG, moves);
            th.addTurn(2, moves);
            Gdx.app.log("GameWorld", "Starting TurnHandler");
            th.start();

            //Thread endTurn sees if current turn is complete and notifies Timer to start running again.
            Thread endTurn = new Thread(new Runnable() {

                public void run() {
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Gdx.app.log("GameWorld", "Turn End");
                    synchronized (timerReset) {
                        timerReset.notify();
                    }
                }
            });
            endTurn.start();

        }
        if (time == 0) {

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


}

