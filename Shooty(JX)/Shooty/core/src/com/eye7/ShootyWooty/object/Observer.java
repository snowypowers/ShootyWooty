package com.eye7.ShootyWooty.object;

/**
 * Observer Interface. Any class wishing to tap into the Global Turn Observer System will have to implement this interface.
 * Upon TurnStart(), TurnEnd() or GameEnd() being called, the respective list of observers will be updated.
 * observerType will be required for order of process.
 * For TurnStart(), UI will be processed first.
 * For TurnEnd(), GameObjects will be processed first.
 * If listening to more than 1 update, use the integer argument as an identifier.
 * 0 - TurnStart()
 * 1 - TurnEnd()
 * 2 - GameEnd()
 */
public interface Observer {

    //Update
    //0 for TurnStart
    //1 for TurnEnd
    //2 for GameEnd
    public void observerUpdate(int i);

    //Method to check what type of observer is this
    //Return 0 for UI
    //Return 1 for GameObject
    public int observerType();

}
