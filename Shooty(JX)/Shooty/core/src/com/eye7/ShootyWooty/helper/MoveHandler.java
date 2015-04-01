package com.eye7.ShootyWooty.helper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Bullet;
import com.eye7.ShootyWooty.object.Player;
import com.eye7.ShootyWooty.world.GameMap;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

import static com.badlogic.gdx.utils.Timer.Task;

public class MoveHandler extends Thread{
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



    private float PLAYER_DISTANCE = 64; // control player amount to move
    private float PLAYER_INCREMENT = 1f; // pixels per tick

    private float BULLET_DISTANCE= 128; // control bullet amount to move
    private float BULLET_INCREMENT = 5f; // pixels per tick

    private float bullet_distance_R;
    private float bullet_distance_L;

    private boolean stopShootr;
    private boolean stopShootl;

    private int PlayerTag;

    public MoveHandler(Player player, String[] moves, CyclicBarrier cb){
        this.moves = moves;
        this.cb = cb;
        this.pointer = -1;

        //Player setup
        this.player = player;
        this.bulletl = player.getBulletl();
        this.bulletr = player.getBulletr();

        bullet_distance_R = BULLET_DISTANCE;
        bullet_distance_L = BULLET_DISTANCE;

        stopShootr = true;
        stopShootl = true;
    }

    public void nextMove() {
        pointer += 1;
        movement = AmountToMove(moves[pointer]);


    }

    public void run() {
        try {
            cb.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        Gdx.app.log("MoveHandler", "Execution!");

        for (int i = 0; i < 4;i++) {
            nextMove();//Initalises the moves
            /////////////////////////////////////////EXECUTE MOVEMENT///////////////////////////////////////
            Gdx.app.log("MoveHandler","start");

            while (movement[0] > 0f || movement[1] > 0f){
                // move along x axis
                CircleMapObject collider = player.getBoundingCircle();

                if (movement[0] > 0f) {
                    collider.getCircle().setPosition((collider.getCircle().x+movement[2]),collider.getCircle().y);
                    if (!checkRock(collider)){
                        player.incrementX(movement[2]);
                        movement[0] -= PLAYER_INCREMENT;
                        bulletl.incrementX(movement[2]); // move bullet with player
                        bulletr.incrementX(movement[2]);
                    }
                    else{
                        collider.getCircle().setPosition((collider.getCircle().x-movement[2]),collider.getCircle().y);
                    }
                }

                // move along Y axis
                if (movement[1] > 0f) {
                    collider.getCircle().setPosition(collider.getCircle().x,(collider.getCircle().y+movement[2]));
                    if (!checkRock(collider)) {
                        player.incrementY(movement[2]);
                        movement[1] -= PLAYER_INCREMENT;
                        bulletl.incrementY(movement[2]);
                        bulletr.incrementY(movement[2]);
                    }
                    else{
                        collider.getCircle().setPosition(collider.getCircle().x,(collider.getCircle().y-movement[2]));
                    }
                }

                if (movement[3] != 0f) {
                    if (movement[3] > 0f) {
                        player.rotate(10);
                        movement[3] -= 10f;
                    } else {
                        player.rotate(-10);
                        movement[3] += 10f;
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            player.snapInGrid(); // make sure the player is aligned to a grid
            try {
                cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            /////////////////////////////////////////EXECUTE SHOOTING///////////////////////////////////////
            bulletl.setReturn(player.getX(), player.getY());
            bulletr.setReturn(player.getX(), player.getY());
            if(moves[pointer].substring(2).equals("1")){
                player.startShootRight();
                stopShootr = false;
            }
            if(moves[pointer].substring(0, 1).equals("1")){
                player.startShootLeft();
                stopShootl = false;
            }
            while (!stopShootl || !stopShootr) {

                // shoot after every move

                if (!stopShootl || !stopShootr) {
                    for (int j: GameConstants.PLAYERS.keySet()) {
                            if (!stopShootl && checkPlayerHit(player.getBulletl().getBoundingCircle(), GameConstants.PLAYERS.get(j).getBoundingCircle())) {
                                bullet_distance_L = 0;
                                bullet_distance_R = 0;
                                GameConstants.PLAYERS.get(j).decreaseHealth();
                                stopShootl = true;
                            }
                            if (!stopShootr && checkPlayerHit(player.getBulletr().getBoundingCircle(), GameConstants.PLAYERS.get(j).getBoundingCircle())) {
                                bullet_distance_L = 0;
                                bullet_distance_R = 0;
                                GameConstants.PLAYERS.get(j).decreaseHealth();
                                stopShootr = true;
                            }
                            if (checkRock(player.getBulletr().getBoundingCircle())) {
                                stopShootr = true;
                            }
                            if (checkRock(player.getBulletl().getBoundingCircle())) {
                                stopShootl = true;
                            }
                            if (!stopShootl || !stopShootr)
                                shootBullets();
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                    }
                }

                else break;

            }
            player.endShootLeft();
            player.endShootRight();
            bullet_distance_R = BULLET_DISTANCE;
            bullet_distance_L = BULLET_DISTANCE;
            stopShootl = true;
            stopShootr = true;

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
        }
        Gdx.app.log("MoveHandler", "End!");
    }
    /////////////////////////////////////METHODS FOR BULLET CONTROL///////////////////////////////////

    // shooting method
    public void shootBullets(){
        // shoot right
        if(moves[pointer].substring(2).equals("1")&&!stopShootr){
            if(bullet_distance_R>0){
                Gdx.app.log("PlayerDirection",Integer.toString(player.getDir()));
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
                bullet_distance_R = BULLET_DISTANCE;
                bulletr.getReturn();
                stopShootr = true;
                player.endShootRight();
            }
        }
        // shoot left
        if(moves[pointer].substring(0, 1).equals("1")&&!stopShootl){
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
                bullet_distance_L=BULLET_DISTANCE;
                bulletl.getReturn();
                stopShootl = true;
                player.endShootLeft();
            }
        }

    }
//
//    public void setStopShoot(boolean stop){ // setter for stop when there is collision
//        stopShoot = stop;
//    }

    /////////////////////////////////////METHODS FOR MOVE CONTROL//////////////////////////////////////
    public void updateMove(String[] moves){
        this.moves=moves;
    } // constantly update moves
    public float[] AmountToMove(String s) { // X,Y,increment,rotate
        int dir = player.getDir();
        if (s.contains("F")) {
            if (dir == 0) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 0f;
            } else if (dir == 90) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = -PLAYER_INCREMENT;
                movement[3] = 0f;
            } else if (dir == 180) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = -PLAYER_INCREMENT;
                movement[3] = 0f;
            } else if (dir == 270) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 0f;
            }
        } else if (s.contains("R")) {
            if (dir == 0) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = -90f;
            } else if (dir == 90) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = -90f;
            } else if (dir == 180) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = -PLAYER_INCREMENT;
                movement[3] = -90f;
            } else if (dir == 270) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = -PLAYER_INCREMENT;
                movement[3] = -90f;
            }
        } else if (s.contains("L")) {
            if (dir == 0) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = -PLAYER_INCREMENT;
                movement[3] = 90f;
            } else if (dir == 90) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = -PLAYER_INCREMENT;
                movement[3] = 90f;
            } else if (dir == 180) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 90f;
            } else if (dir == 270) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 90f;
            }
        } else if (s.contains("D")) {
            if (dir == 0) {
                movement[0] = 0f;
                movement[1] = PLAYER_DISTANCE;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 0f;
            } else if (dir == 90) {
                movement[0] = PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 0f;
            } else if (dir == 180) {
                movement[0] = 0f;
                movement[1] = -PLAYER_DISTANCE;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 0f;
            } else if (dir == 270) {
                movement[0] = -PLAYER_DISTANCE;
                movement[1] = 0f;
                movement[2] = PLAYER_INCREMENT;
                movement[3] = 0f;
            }
        } else if (s.contains("B")) {
            movement[0] = 0f;
            movement[1] = 0f;
            movement[2] = 0f;
            movement[3] = 0f;
        }
        Gdx.app.log("Move Info", String.valueOf(movement[0]) +" "+ String.valueOf(movement[1]) +" "+ String.valueOf(movement[2]) +" "+ String.valueOf(movement[3]));
        return movement;
    }
    public boolean checkPlayerHit(CircleMapObject mapObject1, CircleMapObject mapObject2) {

        if (Intersector.overlaps(mapObject1.getCircle(), mapObject2.getCircle())) {
            if (mapObject2 == player.getBoundingCircle()) {
                return false;
            }else {
                return true;
            }
        }
        else return false;
    }
    public boolean checkRock(CircleMapObject mapObject){
        Array<Rectangle> rocks = GameMap.getRocks();
        for(int i=0; i<rocks.size;i++){
            if (Intersector.overlaps(mapObject.getCircle(), rocks.get(i))){
                return true;

            }
        }
        return false;
    }

}