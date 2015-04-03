package com.eye7.ShootyWooty.helper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Bullet;
import com.eye7.ShootyWooty.object.Player;
import com.eye7.ShootyWooty.world.GameMap;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

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



    private float PLAYER_DISTANCE = 64; // control player amount to move
    private float PLAYER_INCREMENT = 1f; // pixels per tick

    private float BULLET_DISTANCE= 128; // control bullet amount to move
    private float BULLET_INCREMENT = 5f; // pixels per tick

    private float bullet_distance_R;
    private float bullet_distance_L;

    private int PlayerTag;

    public MoveHandler(Player player, String[] moves, CyclicBarrier cb){
        TAG = "MoveHandler of Player "+String.valueOf(player.getPlayerID());
        this.moves = moves;
        this.cb = cb;
        this.pointer = -1;

        //Player setup
        this.player =  player;
        this.bulletl = player.getBulletl();
        this.bulletr = player.getBulletr();

        bullet_distance_R = BULLET_DISTANCE;
        bullet_distance_L = BULLET_DISTANCE;
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

            while (movement[0] > 0f || movement[1] > 0f || movement[3] != 0f){

                CircleMapObject collider = player.getCollider();
                float[] oldMove = Arrays.copyOf(movement, movement.length);

                // move along x axis
                if (movement[0] > 0f) {
                    collider.getCircle().setPosition((collider.getCircle().x+movement[2]),collider.getCircle().y);
                    if (!checkPlayerHit()){
                        player.incrementX(movement[2]);
                        movement[0] -= PLAYER_INCREMENT;
                        bulletl.incrementX(movement[2]); // move bullet with player
                        bulletr.incrementX(movement[2]);
                    }
                    else{
                        collider.getCircle().setPosition((collider.getCircle().x-movement[2]),collider.getCircle().y);
                        movement[0] = oldMove[0] - movement[0];
                        movement[2] = movement[2] * -1;
                    }
                }

                // move along Y axis
                if (movement[1] > 0f) {
                    collider.getCircle().setPosition(collider.getCircle().x,(collider.getCircle().y+movement[2]));
                    if (!checkPlayerHit()) {
                        player.incrementY(movement[2]);
                        movement[1] -= PLAYER_INCREMENT;
                        bulletl.incrementY(movement[2]);
                        bulletr.incrementY(movement[2]);
                    }
                    else{
                        collider.getCircle().setPosition(collider.getCircle().x,(collider.getCircle().y-movement[2]));
                        movement[1] = oldMove[0] - movement[1];
                        movement[2] = movement[2] * -1;
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
                bullet_distance_R=BULLET_DISTANCE;
                player.startShootRight();
            }
            if(moves[pointer].substring(0, 1).equals("1")){
                bullet_distance_L=BULLET_DISTANCE;
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

            if (pointer == 3) {
                continue;
            } else {
                try {
                    //Wait for all to finish executing
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

    //Method to check for bullets hitting stuff
    public boolean checkBulletHit(Bullet b) {
        //collision with rocks
        for (int i = 0; i < GameConstants.ROCKS.size;i++) {
            if (Intersector.overlaps(player.getCollider().getCircle(), GameConstants.ROCKS.get(i))) {
                Gdx.app.log(TAG, "Collision with rock at " + GameConstants.ROCKS.get(i).getX() + " " + GameConstants.ROCKS.get(i).getY());
                player.decreaseHealth();
                return true;
            }
        }

        //collision with players
        for (Player p: GameConstants.PLAYERS.values()) {
            if (Intersector.overlaps(b.getCollider().getCircle(), p.getCollider().getCircle())) {
                p.decreaseHealth();
                return true;
            }
        }

        //No Collision
        return false;
    }

    // Method to check for player hitting rock
    public boolean checkPlayerHit(){
        //Collision with rocks
        for (int i = 0; i < GameConstants.ROCKS.size;i++) {
            if (Intersector.overlaps(player.getCollider().getCircle(), GameConstants.ROCKS.get(i))) {
                Gdx.app.log(TAG, "Collision with rock at " + GameConstants.ROCKS.get(i).getX() + " " + GameConstants.ROCKS.get(i).getY());
                player.decreaseHealth();
                return true;
            }
        }

        for (Player p: GameConstants.PLAYERS.values()) {
            if (p == player) {
                continue; // pass if checking if hit himself
            }
            if (Intersector.overlaps(player.getCollider().getCircle(), p.getCollider().getCircle())) {
                Gdx.app.log(TAG, "Collision with player at " + String.valueOf(p.getCollider().getCircle().x) + " " + String.valueOf(p.getCollider().getCircle().y));
                p.decreaseHealth();
                return true;
            }
        }

        return false;
    }

}