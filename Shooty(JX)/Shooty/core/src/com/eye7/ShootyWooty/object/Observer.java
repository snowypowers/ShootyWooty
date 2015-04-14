package com.eye7.ShootyWooty.object;

/**
 * Created by Yak Jun Xiang on 13/4/2015.
 */
public interface Observer {

    //Update
    //0 for TurnStart
    //1 for TurnEnd
    public void observerUpdate(int i);

    //Method to check what type of observer is this
    //Return 0 for UI
    //Return 1 for GameObject
    public int observerType();

}
