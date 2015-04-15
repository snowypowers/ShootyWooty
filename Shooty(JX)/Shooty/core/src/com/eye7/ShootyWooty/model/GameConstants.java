package com.eye7.ShootyWooty.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.object.Observer;
import com.eye7.ShootyWooty.object.Player;

import java.util.HashMap;

/**
 * Created by JunXiang on 14/3/2015.
 */
public class GameConstants {
    private static final String TAG = "GameConstants";
    public static boolean DEBUG = true;
    public static String gameStateFlag = "U";
    //Screen Size
    public static float SCALE_X;
    public static float SCALE_Y;

    //Game Properties
    public static int NUM_PLAYERS = 2; // Determines the number of players in the game
    public static int myID; // This client's player number
    public static float TIME_LIMIT = 15f;

    //Map properties
    public static float TILE_SIZE;
    public static float MAP_HEIGHT;
    public static float MAP_WIDTH;

    //Map Objects
    public static HashMap<Integer, Player> PLAYERS = new HashMap<Integer, Player>(); //Array of player objects
    public static Array<Rectangle> ROCKS;
    public static Array<Rectangle> WATER;

    //Global Turn Observer System.
    // TurnStart() is called by the Timer to start the processing of moves.
    // TurnEnd() is called by the TurnHandler before shutting down. This announces the end of the turn and allows content to update.

    public static Array<Observer> observersTurnStart = new Array<Observer>();
    public static void subscribeTurnStart (Observer o) {
        observersTurnStart .add(o);
    }

    //ONLY TIMER IS ALLOWED TO CALL THIS METHOD
    public static void TurnStart() {
        Gdx.app.log(TAG, "TURN START");
        for (int i = 0; i < 2; i++) {
            for (Observer o : observersTurnStart) {
                if (o.observerType() == i) {
                    o.observerUpdate(0);
                }
            }
        }
    }

    public static Array<Observer> observersTurnEnd = new Array<Observer>();
    public static void subscribeTurnEnd (Observer o) {
            observersTurnEnd.add(o);
    }

    //ONLY TURN HANDLER IS ALLOWED TO CALL THIS METHOD
    public static void TurnEnd() {
        Gdx.app.log(TAG, "TURN END");
        for (int i = 1; i > -1; i--) {
            for (Observer o : observersTurnEnd) {
                if (o.observerType() == i) {
                    o.observerUpdate(1);
                }
            }
        }
    }

    public static Array<Observer> observersGameEnd = new Array<Observer>();
    public static void subscribeGameEnd (Observer o) {
        observersGameEnd.add(o);
    }

    //ONLY TURN HANDLER IS ALLOWED TO CALL THIS METHOD
    public static void GameEnd() {
        Gdx.app.log(TAG, "GAME END");
        for (int i = 1; i > -1; i--) {
            for (Observer o : observersGameEnd) {
                if (o.observerType() == i) {
                    o.observerUpdate(2);
                }
            }
        }
    }
}
