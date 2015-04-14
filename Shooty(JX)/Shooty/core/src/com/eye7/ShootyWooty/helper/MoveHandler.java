package com.eye7.ShootyWooty.helper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Bullet;
import com.eye7.ShootyWooty.object.Player;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*
 * MoveHandler. Created by TurnHandler to handle moves for each player.
 * Each moveHandler is attached to a player and executes the moves according to the Cyclic Barrier found in TurnHandler.
 * Contains methods to check for collisions with players, rocks and water.
 * If player is dead, execution is skipped over and thread immediately waits at Barrier.
 */
public class MoveHandler extends Thread{
    private final String TAG;
    //Phaser
    private CyclicBarrier cb = null;

    //Movement inputs
    float[] movement = {0f, 0f, 0f, 0f}; // X,Y,speed,Rotate
    private String[] moves; // movement input
    private int pointer; // Points at the current move in moves

    //The Player object which this handler is controlling
    private Player player;
    private Bullet bulletl;
    private Bullet bulletr;



    private final float PLAYER_DISTANCE = 64; // control player amount to move
    private final float PLAYER_INCREMENT = 1f; // pixels per tick

    private final float BULLET_DISTANCE= 128; // control bullet amount to move
    private final float BULLET_INCREMENT = 5f; // pixels per tick

    private float bullet_distance_R;
    private float bullet_distance_L;
    private ActionResolver actionResolver;


    public MoveHandler(Player player, String[] moves, CyclicBarrier cb, ActionResolver actionResolver){
        TAG = "MoveHandler of Player "+String.valueOf(player.getPlayerID());

        //Setup moveslist
        this.moves = moves;
        this.cb = cb;
        this.pointer = -1;

        //Player setup
        this.player =  player;
        this.bulletl = player.getBulletl();
        this.bulletr = player.getBulletr();
        this.actionResolver = actionResolver;
        bullet_distance_R = BULLET_DISTANCE;
        bullet_distance_L = BULLET_DISTANCE;
    }

    //Moves the pointer to the next move
    public void nextMove() {
        pointer += 1;
        movement = AmountToMove(moves[pointer]);


    }

    public void run() {

        try {
            cb.await(); // Wait for all moveHandlers to initialize and arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 4;i++) {
            nextMove();//Initializes the moves
            /////////////////////////////////////////EXECUTE MOVEMENT///////////////////////////////////////
            if (GameConstants.DEBUG) {
                Gdx.app.log("Move Info Player" + String.valueOf(player.getPlayerID()), String.valueOf(movement[0]) + " " + String.valueOf(movement[1]) + " " + String.valueOf(movement[2]) + " " + String.valueOf(movement[3]));
            }
            if (!player.isDead()) {

                CircleMapObject collider = player.getCollider(); //Get collider
                float[] oldMove = AmountToMove(moves[pointer]); //Record of original move

                while (movement[0] > 0f || movement[1] > 0f || movement[3] != 0f) { //While there is movement to be done
                    //Gdx.app.log("Move Info Player" + String.valueOf(player.getPlayerID()), String.valueOf(movement[0]) +" "+ String.valueOf(movement[1]) +" "+ String.valueOf(movement[2]) +" "+ String.valueOf(movement[3]));

                    // move along x axis
                    if (movement[0] > 0f) {
                        collider.getCircle().setPosition((collider.getCircle().x + movement[2]), collider.getCircle().y);
                        if (!checkPlayerHit()) {
                            player.incrementX(movement[2]);
                            movement[0] -= PLAYER_INCREMENT;
                            bulletl.incrementX(movement[2]); // move bullet with player
                            bulletr.incrementX(movement[2]);


                        } else {
                            collider.getCircle().setPosition((collider.getCircle().x - movement[2]), collider.getCircle().y);
                            movement[0] = oldMove[0] - movement[0];
                            movement[2] = movement[2] * -1;
                        }
                    }

                    // move along Y axis
                    if (movement[1] > 0f) {
                        collider.getCircle().setPosition(collider.getCircle().x, (collider.getCircle().y + movement[2]));
                        if (!checkPlayerHit()) {
                            player.incrementY(movement[2]);
                            movement[1] -= PLAYER_INCREMENT;
                            bulletl.incrementY(movement[2]);
                            bulletr.incrementY(movement[2]);


                        } else {
                            collider.getCircle().setPosition(collider.getCircle().x, (collider.getCircle().y - movement[2]));
                            movement[1] = oldMove[1] - movement[1];
                            movement[2] = movement[2] * -1;
                        }
                    }

                    //Rotation
                    if (movement[3] != 0f) {
                        if (movement[3] > 0f) {
                            player.rotate(90);
                            movement[3] -= 90f;
                        } else {
                            player.rotate(-90);
                            movement[3] += 90f;
                        }

                    }

                    try {
                        Thread.sleep(20); //Sleep so that the numan eye can see the animation
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                player.snapInGrid(); // make sure the player is aligned to a grid
                try {
                    decideWin(); //
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(300);
                cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            if(!player.isDead()) {
                /////////////////////////////////////////EXECUTE SHOOTING///////////////////////////////////////
                bulletl.setReturn(player.getX(), player.getY());
                bulletr.setReturn(player.getX(), player.getY());
                if (moves[pointer].substring(2).equals("1")) {
                    bullet_distance_R = BULLET_DISTANCE;
                    player.startShootRight();
                }
                if (moves[pointer].substring(0, 1).equals("1")) {
                    bullet_distance_L = BULLET_DISTANCE;
                    player.startShootLeft();
                }

                //While shooting
                while (player.isShooting()) {
                    // shoot after every move
                    shootBullets();
                    if (checkBulletHit(bulletl)) {
                        bullet_distance_L = 0;
                        player.endShootLeft();
                    }

                    if (checkBulletHit(bulletr)) {
                        bullet_distance_R = 0;
                        player.endShootRight();
                    }

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                player.snapInGrid(); // make sure the player is aligned to a grid
                try {
                    decideWin();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(300);
                cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            /////////////////////////////////////////MISC EFFECTS///////////////////////////////////////
            if (!player.isDead()) {
                if (checkInWater()) {
                    player.collectWater();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (pointer == 3) {
                continue;
            } else {
                try {
                    cb.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        } // End of For Loop

        /////////////////////////////////////////END OF TURN///////////////////////////////////////
        if (GameConstants.DEBUG) {
            Gdx.app.log("MoveHandler", "End!");
        }
    } // End of run()


    /////////////////////////////////////METHODS FOR BULLET CONTROL///////////////////////////////////
    // shooting method
    public void shootBullets(){
        // shoot right
        if(moves[pointer].substring(2).equals("1")){
            if(bullet_distance_R>0){
                bullet_distance_R-=BULLET_INCREMENT;
                if (player.getDir()==0)
                    bulletr.incrementX(BULLET_INCREMENT);
                else if(player.getDir()==90)
                    bulletr.incrementY(BULLET_INCREMENT);
                else if(player.getDir()==180)
                    bulletr.incrementX(BULLET_INCREMENT*-1);
                else if(player.getDir()==270)
                    bulletr.incrementY(BULLET_INCREMENT*-1);
            }
            else{
                player.endShootRight();
            }
        }
        // shoot left
        if(moves[pointer].substring(0, 1).equals("1")){
            if(bullet_distance_L>0){
                bullet_distance_L-=BULLET_INCREMENT;
                if (player.getDir()==0)
                    bulletl.incrementX(BULLET_INCREMENT*-1);
                else if(player.getDir()==90)
                    bulletl.incrementY(BULLET_INCREMENT*-1);
                else if(player.getDir()==180)
                    bulletl.incrementX(BULLET_INCREMENT);
                else if(player.getDir()==270)
                    bulletl.incrementY(BULLET_INCREMENT);
            }
            else{
                player.endShootLeft();
            }
        }

    }

    /////////////////////////////////////METHODS FOR MOVE CONTROL//////////////////////////////////////
    public float[] AmountToMove(String s) { // X,Y,increment,rotate
        int dir = player.getDir();
        float[] output = new float[4];
        if (s.contains("F")) {
            if (dir == 0) {//FACING NORTH
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = PLAYER_INCREMENT;
                output[3] = 0f;
            } else if (dir == 90) {//FACING WEST
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = -PLAYER_INCREMENT;
                output[3] = 0f;
            } else if (dir == 180) {//FACING SOUTH
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = -PLAYER_INCREMENT;
                output[3] = 0f;
            } else if (dir == 270) {//FACING EAST
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = PLAYER_INCREMENT;
                output[3] = 0f;
            }
        } else if (s.contains("R")) {
            if (dir == 0) {//FACING NORTH
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = PLAYER_INCREMENT;
                output[3] = -90f;
            } else if (dir == 90) {//FACING WEST
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = PLAYER_INCREMENT;
                output[3] = -90f;
            } else if (dir == 180) {//FACING SOUTH
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = -PLAYER_INCREMENT;
                output[3] = -90f;
            } else if (dir == 270) {//FACING EAST
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = -PLAYER_INCREMENT;
                output[3] = -90f;
            }
        } else if (s.contains("L")) {
            if (dir == 0) {//FACING NORTH
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = -PLAYER_INCREMENT;
                output[3] = 90f;
            } else if (dir == 90) {//FACING WEST
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = -PLAYER_INCREMENT;
                output[3] = 90f;
            } else if (dir == 180) {//FACING SOUTH
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = PLAYER_INCREMENT;
                output[3] = 90f;
            } else if (dir == 270) {//FACING EAST
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = PLAYER_INCREMENT;
                output[3] = 90f;
            }
        } else if (s.contains("D")) {
            if (dir == 0) {//FACING NORTH
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = -PLAYER_INCREMENT;
                output[3] = 180f;
            } else if (dir == 90) {//FACING WEST
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = PLAYER_INCREMENT;
                output[3] = 180f;
            } else if (dir == 180) {//FACING SOUTH
                output[0] = 0f;
                output[1] = PLAYER_DISTANCE;
                output[2] = PLAYER_INCREMENT;
                output[3] = 180f;
            } else if (dir == 270) {//FACING EAST
                output[0] = PLAYER_DISTANCE;
                output[1] = 0f;
                output[2] = -PLAYER_INCREMENT;
                output[3] = 180f;
            }
        } else if (s.contains("B")) {
            output[0] = 0f;
            output[1] = 0f;
            output[2] = 0f;
            output[3] = 0f;
        }

        return output;
    }

    //Method to check for bullets hitting stuff
    public boolean checkBulletHit(Bullet b) {
        //collision with rocks
        for (int i = 0; i < GameConstants.ROCKS.size;i++) {
            if (Intersector.overlaps(b.getCollider().getCircle(), GameConstants.ROCKS.get(i))) {
                if (GameConstants.DEBUG) {
                    Gdx.app.log(TAG, "Collision with rock at " + GameConstants.ROCKS.get(i).getX() + " " + GameConstants.ROCKS.get(i).getY());
                }
                return true;
            }
        }

        //collision with players
        for (Player p: GameConstants.PLAYERS.values()) {
            if (p == player) {
                continue; // pass if checking if hit himself
            }
            if (Intersector.overlaps(b.getCollider().getCircle(), p.getCollider().getCircle())) {
                p.decreaseHealth(b);
                player.hitAwarded();
                return true;
            }
        }

        //No Collision
        return false;
    }

    // Method to check for player hitting stuff
    public boolean checkPlayerHit(){
        //Collision with rocks
        for (int i = 0; i < GameConstants.ROCKS.size;i++) {
            if (Intersector.overlaps(player.getCollider().getCircle(), GameConstants.ROCKS.get(i))) {
                if (GameConstants.DEBUG) {
                    Gdx.app.log(TAG, "Collision with rock at " + GameConstants.ROCKS.get(i).getX() + " " + GameConstants.ROCKS.get(i).getY());
                }
                player.decreaseHealth(10);
                return true;
            }
        }

        for (Player p: GameConstants.PLAYERS.values()) {
            if (p == player) {
                continue; // pass if checking if hit himself
            }
            if (Intersector.overlaps(player.getCollider().getCircle(), p.getCollider().getCircle())) {
                if (GameConstants.DEBUG) {
                    Gdx.app.log(TAG, "Collision with player at " + String.valueOf(p.getCollider().getCircle().x) + " " + String.valueOf(p.getCollider().getCircle().y));
                }
                player.decreaseHealth(p);
                p.decreaseHealth(player);
                return true;
            }
        }
        // No Collisions
        return false;
    }

    //Check if player is standing in water
    public boolean checkInWater() {
        for (int i = 0; i < GameConstants.WATER.size;i++) {
            if (Intersector.overlaps(player.getCollider().getCircle(), GameConstants.WATER.get(i))) {
                return true;
            }
        }
        return false;
    }
    public void decideWin() throws InterruptedException {
        ArrayList<Integer> recentDead = new ArrayList<Integer>();
        ArrayList<Integer> playersLeft = actionResolver.getLeftPlayers();
        if(playersLeft.size()!=0){
            for(int i:playersLeft){
                GameConstants.PLAYERS.get(i+1).dead = true;
            }
            actionResolver.clearLeftPlayers();

        }
        int numScoreFull = 0;
        boolean meFull = false;
        for(int i=0; i<GameConstants.NUM_PLAYERS; i++){
            if((GameConstants.PLAYERS.get(i+1).dead == true) && !actionResolver.getDeadPlayers().contains(i)){
                recentDead.add(i);
                actionResolver.sendMessageAll("@",Integer.toString(i) );
                if(i==GameConstants.myID)
                    GameConstants.gameStateFlag = "dead";
            }
            if(GameConstants.PLAYERS.get(i+1).getScore()>=3){
                if(i==GameConstants.myID){
                    meFull = true;
                }
                numScoreFull++;
            }
        }
        if(meFull){
            if(numScoreFull==1){
                //actionResolver.gameDecided("win",GameConstants.PLAYERS.get(GameConstants.myID+1).getAchievments());
                GameConstants.gameStateFlag = "W";
            }
            else{
                //actionResolver.gameDecided("draw",GameConstants.PLAYERS.get(GameConstants.myID+1).getAchievments());
                GameConstants.gameStateFlag = "D";
            }
        }
        else if(numScoreFull>0){
            //actionResolver.gameDecided("lose",GameConstants.PLAYERS.get(GameConstants.myID+1).getAchievments());
            GameConstants.gameStateFlag = "L";
        }

        if(actionResolver.getDeadPlayers().size()==GameConstants.NUM_PLAYERS-1){
            if(!GameConstants.PLAYERS.get(GameConstants.myID+1).dead){
                GameConstants.gameStateFlag = "W";
                //actionResolver.gameDecided("win",GameConstants.PLAYERS.get(GameConstants.myID+1).getAchievments());
            }
            else{
                GameConstants.gameStateFlag = "L";
                //actionResolver.gameDecided("lose",GameConstants.PLAYERS.get(GameConstants.myID+1).getAchievments());
            }
        }
        if(actionResolver.getDeadPlayers().size()==GameConstants.NUM_PLAYERS){
            checkDraw(recentDead);
            //actionResolver.gameDecided(state, GameConstants.PLAYERS.get(GameConstants.myID + 1).getAchievments());
        }
    }
    public void checkDraw(ArrayList<Integer> recentDead){
        ArrayList<Integer> checkIn = recentDead;
        int maxScore = 0;
        for(int player:checkIn){
            if(GameConstants.PLAYERS.get(player+1).getScore()>maxScore){
                maxScore = GameConstants.PLAYERS.get(player+1).getScore();
            }
        }
        int numPlay = 0;
        for(int player:checkIn){
            if(GameConstants.PLAYERS.get(player+1).getScore() == maxScore){
                numPlay++;
            }
        }
        if(GameConstants.PLAYERS.get(GameConstants.myID+1).getScore()==maxScore){
            if(numPlay==1)
                GameConstants.gameStateFlag = "W";
            else
                GameConstants.gameStateFlag = "D";
        }
        GameConstants.gameStateFlag = "L";

    }
}