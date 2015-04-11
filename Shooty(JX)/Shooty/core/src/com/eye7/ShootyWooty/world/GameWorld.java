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

        input = new ActionMenu();
        input.debug();
        stage.addActor(input);

        gameState = gameState.DECIDING;
        timer = new Timer(stage, timerReset);
        timer.start(); // start timer
    }

    // constantly call this method
    public void update(float delta) {
        if (!GameConstants.PLAYERS.get(GameConstants.myID+1).isDead()) {
            // always get move from button even if no change
            moves = input.getMoves();
        }


        switch (gameState) {
            case DECIDING:
                if (time < GameConstants.TIME_LIMIT) {
                    out = "Player deciding...";
                    if (GameConstants.PLAYERS.get(GameConstants.myID+1).isDead()){
                        out = "You are dead!";
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

    public String getTimeStatus() {
        return timeStatus;
    }

}

enum GameState {
    DECIDING,
    WAITING,
    EXECUTING
}

