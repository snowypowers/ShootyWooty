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
    private final String TAG = "GameWorld";

    private Stage stage;
    private GameState gameState;


    private Button button0;
    private Button button1;
    private Button button2;
    private Button button3;
    private boolean dead = false;
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

        gameState = gameState.DECIDING;
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


        switch (gameState) {
            case DECIDING:
                if (time < 30) {
                    out = "Player deciding...";
                    if (dead==true){
                        out = "You are dead!";
                        button0.setLock(true); // lock the button from being pressed while executing moves
                        button1.setLock(true);
                        button2.setLock(true);
                        button3.setLock(true);
                    }
                    if(actionResolver.getMultiplayer() && actionResolver.getActive()!=null) {
                        actionResolver.sendContMessage(moves[0] + " " + moves[1] + " " + moves[2] + " " + moves[3]);
                        String allPlayerMoves = actionResolver.getImMoves();//Continuously gets the player moves to render
                    }
//                    Gdx.app.log("GameWorld Cont Moves", allPlayerMoves);
                } else {
                    out = "Waiting for other players";
                    timer.reset();

                    button0.setLock(true); // lock the button from being pressed while executing moves
                    button1.setLock(true);
                    button2.setLock(true);
                    button3.setLock(true);

                    Gdx.app.log("GameWorld", "Creating TurnHandler");
                    th = new TurnHandler(actionResolver);

                    if (actionResolver.getMultiplayer()) {
                        String myMove = moves[0] + " " + moves[1] + " " + moves[2] + " " + moves[3];
                        if(dead){
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
                    button0.resetButton(); // reset the button display
                    button1.resetButton();
                    button2.resetButton();
                    button3.resetButton();

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
                            button0.setLock(false); // release the lock
                            button1.setLock(false);
                            button2.setLock(false);
                            button3.setLock(false);

                            button0.resetMoves(); // reset the moves to "0B0"
                            button1.resetMoves();
                            button2.resetMoves();
                            button3.resetMoves();
                            for(int i=0; i<GameConstants.NUM_PLAYERS; i++){
                                if(GameConstants.PLAYERS.get(i+1).getHealth()==0){
                                    if(i==GameConstants.myID) {
                                        dead = true;
                                        Gdx.app.log(TAG, "Sending dead message");
                                        actionResolver.sendMessageAll("@", Integer.toString(GameConstants.myID));
                                        if((GameConstants.NUM_PLAYERS-actionResolver.getDeadPlayers().size())>2)
                                            actionResolver.lostGameDecide();
                                    }
                                    else{
                                        actionResolver.sendMessageAll("@",Integer.toString(i) );
                                    }
                                }
                            }


                            if(GameConstants.PLAYERS.get(GameConstants.myID+1).getScore()==3){
                                actionResolver.sendMessageAll("+",Integer.toString(GameConstants.myID));
                                actionResolver.gameDecided("won");
                                Gdx.app.log(TAG, "I WON");
                            }
                            if(actionResolver.getDeadPlayers().size()==GameConstants.NUM_PLAYERS-1){
                                if(!dead){
                                    //win
                                    actionResolver.gameDecided("won");
                                    Gdx.app.log(TAG, "I WON");
                                }
                                else{
                                    //Game Over
                                    actionResolver.gameDecided("lost");
                                    Gdx.app.log(TAG, "I LOST");
                                }
                            }
                            if(actionResolver.getDeadPlayers().size()==GameConstants.NUM_PLAYERS){
                                if(checkDraw()==GameConstants.myID){
                                    actionResolver.gameDecided("won");
                                }
                                else if(checkDraw()==-1){
                                    actionResolver.gameDecided("draw");
                                }
                                else{
                                    actionResolver.gameDecided("lost");
                                }
                            }
                            if(actionResolver.getWinner()!=-1){
                                //winner decided
                                actionResolver.gameDecided("lost");
                                Gdx.app.log(TAG, "I LOST");
                            }
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
    public int checkDraw(){
        int play1 = GameConstants.PLAYERS.get(actionResolver.getDeadPlayers().get((GameConstants.NUM_PLAYERS-2)+1)).getScore();
        int play2 = GameConstants.PLAYERS.get(actionResolver.getDeadPlayers().get((GameConstants.NUM_PLAYERS-1)+1)).getScore();
        if(play1>play2){
            return GameConstants.NUM_PLAYERS-2;
        }
        if(play2>play1){
            return GameConstants.NUM_PLAYERS-1;
        }
        return -1;
    }

}

enum GameState {
    DECIDING,
    WAITING,
    EXECUTING
}

