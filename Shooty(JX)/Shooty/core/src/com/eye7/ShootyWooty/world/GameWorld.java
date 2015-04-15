package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.MainLoader;
import com.eye7.ShootyWooty.helper.TurnHandler;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.ActionMenu;
import com.eye7.ShootyWooty.object.HourGlass;
import com.eye7.ShootyWooty.object.Observer;

/**
 * @author PT
 * Handles interaction with ActionResolver
 */
public class GameWorld implements Observer {
    private final String TAG = "GameWorld";

    private Stage stage;
    private GameState gameState;

    private ActionMenu input;
    private String[] moves = { "0B0", "0B0", "0B0", "0B0" };
    private HourGlass hourGlass;
    private TurnHandler th;
    private ActionResolver actionResolver;

    public GameWorld(Stage stage, ActionResolver actionResolver) {
        GameConstants.subscribeTurnStart(this);
        GameConstants.subscribeTurnEnd(this);
        this.stage = stage;
        this.actionResolver = actionResolver;

        input = new ActionMenu(actionResolver);
        stage.addActor(input);

        gameState = gameState.DECIDING;
        hourGlass = new HourGlass(stage);
        hourGlass.start(); // start timer

        MainLoader.bgMusic.setLooping(true);
        MainLoader.bgMusic.setVolume(0.4f);
        MainLoader.bgMusic.play();
    }

    // constantly call this method
    public void update(float delta) {
        if (!GameConstants.PLAYERS.get(GameConstants.myID+1).isDead()) {
            // always get move from button even if no change
            moves = input.getMoves();
        }

        switch (gameState) {
            case DECIDING:
                    if(actionResolver.getMultiplayer() && actionResolver.getActive()!=null) {
                        int numMoves = 0;
                        for(int i =0; i<4; i++){
                            if (!moves[i].equals(("0B0"))){
                                numMoves++;
                            }
                        }
                        actionResolver.sendContMessage(Integer.toString(numMoves));

                    }
//                    Gdx.app.log("GameWorld Cont Moves", allPlayerMoves);


                break;
            case WAITING:
                break;
        }

    }


    //Observer Methods for TurnStart & TurnEnd
    public void observerUpdate(int i) {
        if (i == 0) { // Turn Start
            Gdx.app.log("GameWorld", "Creating TurnHandler");
            th = new TurnHandler(actionResolver);

            if (actionResolver.getMultiplayer()) {
                String myMove = moves[0] + " " + moves[1] + " " + moves[2] + " " + moves[3];
                if (GameConstants.PLAYERS.get(GameConstants.myID + 1).dead) {
                    myMove = "0B0 0B0 0B0 0B0";
                }
                actionResolver.sendMessageAll("!", myMove);
                Gdx.app.log(TAG, "Sending move: " + myMove);
            } else {
                //Code to add turns to TurnHandler here (SINGLE PLAYER)
                for (int l = 1; l <= GameConstants.NUM_PLAYERS; l++) {
                    th.addTurn(l, moves);
                }
            }

            Gdx.app.log("GameWorld", "Starting TurnHandler");
            th.start();
            gameState = GameState.WAITING;
        }
        if (i == 1) { // Turn End
            gameState = GameState.DECIDING;
        }
        if (i == 2) { // Game End
            actionResolver.setEndGame();
            MainLoader.bgMusic.stop();
        }
    }

    public int observerType() {
        return 0;
    }

    // getters for renderer
    public Stage getStage() {
        return stage;
    }
    public void dispose(){
        hourGlass.dispose();

    }

}

enum GameState {
    DECIDING,
    WAITING,
}

