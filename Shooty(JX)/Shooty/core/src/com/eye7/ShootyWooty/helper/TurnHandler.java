package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.eye7.ShootyWooty.model.GameConstants;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

/**
 * Created by JunXiang on 25/3/2015. Turn handler that manages and moves the pieces on board.
 * Game must ensure that all moves have arrived before running this thread
 */
public class TurnHandler extends Thread {
    private final String TAG = "TurnHandler";

    private ActionResolver actionResolver;
    private MoveHandler[] moveHandlers = null;
    private CyclicBarrier cyclicBarrier = null;

    public boolean executing = false;

    public TurnHandler (ActionResolver ar) {
        actionResolver = ar;
        /*
        Phaser has a total of 8 phases, with each turn consisting of 2 phases. Odd numbered phases are for movement and even numbered phases are for shooting
         */
        cyclicBarrier = new CyclicBarrier(GameConstants.NUM_PLAYERS);
        moveHandlers = new MoveHandler[GameConstants.NUM_PLAYERS];
    }

    public void addTurn(int playerTag, String[] moves) {
        MoveHandler m = new MoveHandler (GameConstants.PLAYERS.get(playerTag), moves, cyclicBarrier);
        moveHandlers[playerTag-1] = m;


    }

    public void run() {
        if(actionResolver.getMultiplayer()) {
            while (!actionResolver.getValid()) {
//                try {
//                    actionResolver.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                continue;
            }
            String[] moveArray = actionResolver.getMoves().split("!");
            //!move!move
            //!0F00B00B01F1!1F10B11B10F1
            for (int i = 0; i < moveArray.length;i++) {
                addTurn(i+1, new String[]{moveArray[i].substring(0,3), moveArray[i].substring(3,6), moveArray[i].substring(6,9), moveArray[i].substring(9,12)});
            }
            actionResolver.setValid(false);
//            actionResolver.setSignal(false);
        }
        executing = true;
        Gdx.app.log(TAG, "Starting MoveHandlers");
        for (MoveHandler mh: moveHandlers) {
            mh.start();
        }
        Gdx.app.log(TAG, "Waiting...");
       for (MoveHandler mh: moveHandlers) {
           try {
               mh.join();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
        Gdx.app.log(TAG, "Turn Ended!");
    }

    public boolean isExecuting() {
        return executing;
    }
}
