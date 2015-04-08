package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.TurnHandler;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.ActionMenu;
import com.eye7.ShootyWooty.object.InputButton;
import com.eye7.ShootyWooty.object.InputButtons;
import com.eye7.ShootyWooty.object.Timer;

/**
 * @author PT
 * handles all the object in game
 * feed update to gameScreen
 */
public class GameWorld {
    private final String TAG = "GameWorld";

    private Stage stage;
    private GameState gameState;


    private InputButton inputButton0;
    private InputButton inputButton1;
    private InputButton inputButton2;
    private InputButton inputButton3;
    private ActionMenu input;
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
        /*
        inputButton0 = new InputButton(800, 430, stage);
        inputButton1 = new InputButton(800, 320, stage);
        inputButton2 = new InputButton(800, 210, stage);
        inputButton3 = new InputButton(800, 100, stage);
        */
        input = new ActionMenu();
        //input.setPosition(800,225);
        input.debug();
        stage.addActor(input);

        gameState = gameState.DECIDING;
        timer = new Timer(timerReset);
        timer.start(); // start timer
    }

    // constantly call this method
    public void update(float delta) {
        if (!GameConstants.PLAYERS.get(GameConstants.myID+1).isDead()) {
            // always get move from button even if no change
            /*
            moves[0] = inputButton0.getMoves();
            moves[1] = inputButton1.getMoves();
            moves[2] = inputButton2.getMoves();
            moves[3] = inputButton3.getMoves();
            */
            moves = input.getMoves();
        }


        switch (gameState) {
            case DECIDING:
                if (time < 15) {
                    out = "Player deciding...";
                    if (GameConstants.PLAYERS.get(GameConstants.myID+1).isDead()){
                        out = "You are dead!";
                        /*
                        inputButton0.setLock(true); // lock the button from being pressed while executing moves
                        inputButton1.setLock(true);
                        inputButton2.setLock(true);
                        inputButton3.setLock(true);
                        */
                        input.setLock(true);
                    }
                    if(actionResolver.getMultiplayer() && actionResolver.getActive()!=null) {
                        int numMoves = 0;
                        for(int i =0; i<4; i++){
                            if (!moves[i].equals(("0B0"))){
                                numMoves++;
                            }
                        }
                        actionResolver.sendContMessage(Integer.toString(numMoves));
                        Integer[] allPlayerMoves = actionResolver.getImMoves();//Continuously gets the player moves to render
                    }
//                    Gdx.app.log("GameWorld Cont Moves", allPlayerMoves);
                } else {
                    out = "Waiting for other players";
                    timer.reset();
                    /*
                    inputButton0.setLock(true); // lock the button from being pressed while executing moves
                    inputButton1.setLock(true);
                    inputButton2.setLock(true);
                    inputButton3.setLock(true);
                    */
                    input.setLock(true);

                    Gdx.app.log("GameWorld", "Creating TurnHandler");
                    th = new TurnHandler(actionResolver);

                    if (actionResolver.getMultiplayer()) {
                        String myMove = moves[0] + " " + moves[1] + " " + moves[2] + " " + moves[3];
                        if(GameConstants.PLAYERS.get(GameConstants.myID+1).dead){
                            myMove = "0B0 0B0 0B0 0B0";
                        }
                        actionResolver.sendMessageAll("!", myMove);
                        Gdx.app.log(TAG, "Sending move: " + myMove);
                    } else {
                        //Code to add turns to TurnHandler here (SINGLE PLAYER)
                        for (int i = 1; i <= GameConstants.NUM_PLAYERS; i++) {
                            th.addTurn(i, moves);
                        }
                    }

                    Gdx.app.log("GameWorld", "Starting TurnHandler");
                    th.start();
                    gameState = GameState.WAITING;

                }
                break;
            case WAITING:
                if (th.isExecuting()) {
                    /*
                    inputButton0.resetButton(); // reset the button display
                    inputButton1.resetButton();
                    inputButton2.resetButton();
                    inputButton3.resetButton();
                    */
                    input.reset();

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
                            /*
                            inputButton0.setLock(false); // release the lock
                            inputButton1.setLock(false);
                            inputButton2.setLock(false);
                            inputButton3.setLock(false);

                            inputButton0.resetMoves(); // reset the moves to "0B0"
                            inputButton1.resetMoves();
                            inputButton2.resetMoves();
                            inputButton3.resetMoves();
                            */
                            input.setLock(false);
                            input.reset();
                            gameState = GameState.DECIDING;
                        }
                    });
                    endTurn.start();

                    gameState = GameState.EXECUTING;
                }
                break;
            case EXECUTING:
                break;
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

    public InputButton getInputButton1() {
        return inputButton1;
    }

    public InputButton getInputButton2() {
        return inputButton2;
    }

    public InputButton getInputButton3() {
        return inputButton3;
    }

    public InputButton getInputButton0() {
        return inputButton0;
    }
    public String getTimeStatus() {
        return timeStatus;
    }


}

enum GameState {
    DECIDING,
    WAITING,
    EXECUTING
}

