package com.eye7.ShootyWooty.helper;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anvithaprashanth on 17/03/15.
 */
public interface ActionResolver {
    public boolean mapDecided();
    public String getMapDecided();
    public void sendMessageAll(String x,String s);
    public boolean getValid();
    public String getMoves();
    public void setValid(boolean v);
    public boolean getMultiplayer();
    public int getMyID();
    public int getNumPlayers();
    public int getNumDeadPlayers();
    public boolean getQuitGame();
    public Integer[] getImMoves();
    public void sendContMessage(String s);
    public String getActive();
    public void clearLeftPlayers();
    public ArrayList<Integer> getLeftPlayers();
    public void gameDecided(String s) throws InterruptedException;
    public void displayAchievements(final HashMap<String, Integer> myAchievements);
    public void leaveRoom();
    public void setEndGame();
    public void markDead(int i);


}
