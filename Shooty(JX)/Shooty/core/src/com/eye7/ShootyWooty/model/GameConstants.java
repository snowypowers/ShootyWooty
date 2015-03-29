package com.eye7.ShootyWooty.model;

import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.object.Player;
import com.eye7.ShootyWooty.object.Timer;

import java.util.HashMap;

/**
 * Created by JunXiang on 14/3/2015.
 */
public class GameConstants {

    public static int NUM_PLAYERS = 2; // Determines the number of players in the game
    public static int PLAYER_TAG = 1; // This is the client's player tag
    public static HashMap<Integer, Player> PLAYERS = new HashMap<Integer, Player>(); //Array of player objects
    public static Timer TURN_TIMER; // The turn timer

}
