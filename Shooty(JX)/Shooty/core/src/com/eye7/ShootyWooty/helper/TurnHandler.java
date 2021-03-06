package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.eye7.ShootyWooty.model.GameConstants;

import java.util.concurrent.CyclicBarrier;

/**
 * Turn handler that manages and moves the pieces on board.
 * Game must ensure that all moves have arrived before running this thread.
 * This thread will call TurnEnd() or GameEnd() to signal the end of turn or end of game respectively.
 *
 * Hierarchy
 * GameWorld -> TurnHandler -> MoveHandler
 */
public class TurnHandler extends Thread {
    private final String TAG = "TurnHandler";

    private ActionResolver actionResolver;
    private MoveHandler[] moveHandlers = null;
    private CyclicBarrier cyclicBarrier = null;
    private Game game;
    public boolean executing = false;

    public TurnHandler (ActionResolver ar, Game game) {
        actionResolver = ar;
        this.game = game;
        /*
        Phaser has a total of 12 phases, with each turn consisting of 3 phases. Phases are : movement, shooting, misc
         */
        cyclicBarrier = new CyclicBarrier(GameConstants.NUM_PLAYERS);
        moveHandlers = new MoveHandler[GameConstants.NUM_PLAYERS];
    }

    public void addTurn(int playerTag, String[] moves) {
        MoveHandler m = new MoveHandler (GameConstants.PLAYERS.get(playerTag), moves, cyclicBarrier, actionResolver);
        moveHandlers[playerTag-1] = m;


    }

    public void run() {
        if (actionResolver.getMultiplayer()) {
            while (!actionResolver.getValid()) {
//                if(actionResolver.getQuitGame()){
//                    game.dispose();
//                    return;
//                }
                continue;
            }
            String moves = actionResolver.getMoves();
            Gdx.app.log(TAG, moves);
            String[] moveArray = moves.split("!");

            //!move!move
            //!0F0 0B0 0B0 1F1!1F1 0B1 1B1 0F1
            for (int i = 1; i < moveArray.length; i++) {
                Gdx.app.log(TAG, "THIS" + moveArray[i]);
                addTurn(i, moveArray[i].split(" "));
            }
            actionResolver.setValid(false);
//            actionResolver.setSignal(false);
        }
        executing = true;
        Gdx.app.log(TAG, "Starting MoveHandlers");
        for (MoveHandler mh : moveHandlers) {
            mh.start();
        }
        Gdx.app.log(TAG, "Waiting...");
        for (MoveHandler mh : moveHandlers) {
            try {
                mh.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TURN END
        if (GameConstants.gameStateFlag.contains("U") || GameConstants.gameStateFlag.contains("dead")) {
            GameConstants.TurnEnd();
            Gdx.app.log(TAG, GameConstants.gameStateFlag + "| Turn Ended!");
        } else {
            GameConstants.GameEnd();
            Gdx.app.log(TAG, "Game Ended!");
        }



    }


    public boolean isExecuting() {
        return executing;
    }
}
