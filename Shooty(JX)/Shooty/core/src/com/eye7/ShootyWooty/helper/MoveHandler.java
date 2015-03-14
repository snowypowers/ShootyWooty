package com.eye7.ShootyWooty.helper;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Bullet;
import com.eye7.ShootyWooty.object.Player;

import static com.badlogic.gdx.utils.Timer.Task;

public class MoveHandler extends Thread{

    private long startTime; // Time of thread creation
    private long delta;

    //Movement inputs
    float[] movement = {0f, 0f, 0f, 0f}; // X,Y,speed,Rotate
    private String[] moves; // movement input
    Timer moveTimer;
    private int pointer;
    private String command;

    //The Player object which this handler is controlling
    private Player player;
    private Bullet bulletl;
    private Bullet bulletr;



    private float PLAYER_DISTANCE = 32; // control player amount to move
    private float PLAYER_INCREMENT = 1f; // pixels per tick

    private float BULLET_DISTANCE= 96; // control bullet amount to move
    private float BULLET_INCREMENT = 5f; // pixels per tick

    private float bullet_distance_R;
    private float bullet_distance_L;

    private boolean stopShoot;

    public MoveHandler(int PlayerTag, String[] moves){
        startTime = TimeUtils.millis(); // Time of creation of MoveHandler. They should be roughly the same so we can base our synchronized movements around this time.
        Timer moveTimer = new Timer();

        // Each turn last 2 seconds
        moveTimer.schedule(new Task() {
            @Override
            public void run() {
                player.snapInGrid();
                startTime = TimeUtils.millis();
                nextMove();
            }
        }, 0f, 2f, 3);

        pointer = 0;


        //Player setup
        this.player = GameConstants.PLAYERS.get(PlayerTag - 1);
        this.bulletl = player.getBulletl();
        this.bulletr = player.getBulletr();

        bullet_distance_R = BULLET_DISTANCE;
        bullet_distance_L = BULLET_DISTANCE;

        command = moves[0];

        this.moves=moves;

        stopShoot = false;
    }

    public void nextMove() {
        command = moves[pointer];
        pointer += 1;
        movement = AmountToMove(command);
    }
    /////////////////////////////////////////EXECUTE MOVEMENT///////////////////////////////////////
    public void run() {
        Gdx.app.log("MoveHandler", "Execution!");

        //Starts the turn scheduler
        moveTimer.instance();
        for (int i = 0; i < 4;i++) {
            //1 seconds to complete movement
            while (TimeUtils.timeSinceMillis(startTime) <= 1000){
                // move along x axis
                if (movement[0] > 0f) {
                    player.incrementX(movement[2]);
                    movement[0] -= PLAYER_INCREMENT;
                    //bulletl.incrementX(movement[2] * delta); // move bullet with player
                   // bulletr.incrementX(movement[2] * delta);
                }

                // move along Y axis
                if (movement[1] > 0f) {
                    player.incrementY(movement[2]);
                    movement[1] -= PLAYER_INCREMENT;
                    //bulletl.incrementY(movement[2] * delta);
                    //bulletr.incrementY(movement[2] * delta);
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
            if(command.substring(2).equals("1")){
                player.startShootRight();
            }
            if(command.substring(0,1).equals("1")){
                player.startShootLeft();
            }
            while (TimeUtils.timeSinceMillis(startTime) > 1000 && TimeUtils.timeSinceMillis(startTime) <= 2000) {

                        // shoot after every move
                        bulletl.setReturn(player.getX(), player.getY());
                        bulletr.setReturn(player.getX(), player.getY());
                        if (!stopShoot) shootBullets(); // continue to shoot if not hit
                        else { // stop shooting if hit
                            bullet_distance_L = 0;
                            bullet_distance_R = 0;
                            stopShoot = false;
                        }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            player.endShootLeft();
            player.endShootRight();
        }
        Gdx.app.log("MoveHandler", "End!");
    }
    /////////////////////////////////////METHODS FOR BULLET CONTROL///////////////////////////////////

    // shooting method
    public void shootBullets(){
        // shoot right
        if(command.substring(2).equals("1")){
            if(bullet_distance_R>0){
                bullet_distance_R-=BULLET_INCREMENT;
                bulletr.incrementX(BULLET_INCREMENT);
            }
            else{
                bullet_distance_R = BULLET_DISTANCE;
                bulletr.getReturn();
                player.endShootRight();
            }
        }
        // shoot left
        if(command.substring(0,1).equals("1")){
            if(bullet_distance_L>0){
                bullet_distance_L-=BULLET_INCREMENT;
                bulletl.incrementX(BULLET_INCREMENT*-1);
            }
            else{
                bullet_distance_L=BULLET_DISTANCE;
                bulletl.getReturn();
                player.endShootLeft();
            }
        }

    }

    public void setStopShoot(boolean stop){ // setter for stop when there is collision
        stopShoot = stop;
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

}