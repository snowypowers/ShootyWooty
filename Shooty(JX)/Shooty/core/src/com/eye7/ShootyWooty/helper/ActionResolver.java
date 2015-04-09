package com.eye7.ShootyWooty.helper;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by anvithaprashanth on 17/03/15.
 */
public interface ActionResolver {
    public void sendMessageAll(String x,String s);
    //    public void sendMessageHost(String s);
    public boolean getValid();
    public String getMoves();
    public void setValid(boolean v);
    //    public void setSignal(boolean v);
//    public boolean getSignal();
    public boolean getMultiplayer();
    public int getMyID();
    public int getNumPlayers();
    public ArrayList<Integer> getDeadPlayers();
    public int getWinner();
    public Integer[] getImMoves();
    public void sendContMessage(String s);
    public String getActive();
    public void gameDecided(String s, HashMap<String ,Integer> myAchievements) throws InterruptedException;
    public void leaveRoom();
    //public void lostGameDecide();


}
