package com.sutd.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.sutd.object.Bullet;
import com.sutd.object.Player;


public class MoveHandler {

    private int pointer;
    private Boolean check;
    private boolean stop;
    private String command;
 
    private Player player1;
    private Bullet bulletl;
    private Bullet bulletr;

    float[] movement = {0f, 0f, 0f}; // amount to move
    private String[] moves; // movement input
    
    private float PLAYER_DISTANCE = 10; // control player amount to move
    private float PLAYER_INCREMENT = 0.5f; // control player speed
    
    private float BULLET_DISTANCE= 30; // control bullet amount to move
    private float BULLET_INCREMENT = 0.5f; // control bullet speed

    private float bullet_distance_R;
    private float bullet_distance_L;

    private boolean stopShoot;

    public MoveHandler(Player player1, Bullet bulletl, Bullet bulletr,String[] moves){
        check = false;
        pointer = -1;
        stop = false;

        bullet_distance_R = BULLET_DISTANCE;
        bullet_distance_L = BULLET_DISTANCE;
        
        command = moves[0];
        this.player1 = player1;
        this.bulletl = bulletl;
        this.bulletr = bulletr;
        this.moves=moves;

        stopShoot = false;
    }
    /////////////////////////////////////////EXECUTE MOVEMENT///////////////////////////////////////
    public void execute(){
        if(!stop) {
            if (check) {
                if (Math.abs(movement[0]) > 0) { // move along x axis
                    movement[0] += movement[2];
                    player1.incrementX(movement[2] * -1);
                    bulletl.incrementX(movement[2] * -1); // move bullet with player
                    bulletr.incrementX(movement[2] * -1);
                    bulletl.setReturn(player1.getX(),player1.getY());
                    bulletr.setReturn(player1.getX(),player1.getY());

                } else if (Math.abs(movement[1]) > 0) { // move along Y axis
                    movement[1] += movement[2];
                    player1.incrementY(movement[2] * -1);
                    bulletl.incrementY(movement[2] * -1);
                    bulletr.incrementY(movement[2] * -1);
                    bulletl.setReturn(player1.getX(),player1.getY());
                    bulletr.setReturn(player1.getX(),player1.getY());

                } else { // shoot after every move
                    bulletl.setReturn(player1.getX(),player1.getY());
                    bulletr.setReturn(player1.getX(),player1.getY());
                    shootBullets();
                    if(!stopShoot) shootBullets(); // continue to shoot if not hit
                    else { // stop shooting if hit
                        bullet_distance_L = 0;
                        bullet_distance_R = 0;
                        stopShoot = false;
                    }
                }
            } else { // fetching and changing command
                pointer += 1;
                if (pointer == 4) {
                    pointer = -1;
                    stop=true;
                }
                else{
                    command = moves[pointer];
                    movement = AmountToMove(command);
                    check = true;
                }
            }
        }
    }
    /////////////////////////////////////METHODS FOR BULLET CONTROL///////////////////////////////////

    // shooting method
    public void shootBullets(){
        // shoot right
        if(command.substring(2).equals("1")&&command.substring(0,1).equals("0")){
            if(bullet_distance_R>0){
                bullet_distance_R-=BULLET_INCREMENT;
                bulletr.incrementX(BULLET_INCREMENT);
            }
            else{
                bullet_distance_R = BULLET_DISTANCE;
                bulletr.getReturn();
                check = false;
            }
        }
        // shoot left
        else if(command.substring(0,1).equals("1")&&command.substring(2).equals("0")){
            if(bullet_distance_L>0){
                bullet_distance_L-=BULLET_INCREMENT;
                bulletl.incrementX(BULLET_INCREMENT*-1);
            }
            else{
                bullet_distance_L=BULLET_DISTANCE;
                bulletl.getReturn();
                check = false;
            }
        }
        // shoot both sides
        else if(command.substring(0,1).equals("1")&&command.substring(2).equals("1")){
            if(bullet_distance_L>0 && bullet_distance_R>0){
                bullet_distance_L-=BULLET_INCREMENT;
                bulletl.incrementX(BULLET_INCREMENT*-1);
                bullet_distance_R-=BULLET_INCREMENT;
                bulletr.incrementX(BULLET_INCREMENT);
            }
            else{
                bullet_distance_R = BULLET_DISTANCE;
                bulletr.getReturn();
                bullet_distance_L = BULLET_DISTANCE;
                bulletl.getReturn();
                check = false;
            }
        }
        else check = false;
    }

    public void setStopCheck(){
        check=false;
        stop=false;
    }
    public void setStopShoot(boolean stop){ // setter for stop when there is collision
        stopShoot = stop;
    }

 /////////////////////////////////////METHODS FOR MOVE CONTROL//////////////////////////////////////
    public void updateMove(String[] moves){
        this.moves=moves;
    } // constantly update moves
    public float[] AmountToMove(String s) { // output how much to move, direction and speed
        if (s.contains("F")) {
            movement[0] = 0f;
            movement[1] = PLAYER_DISTANCE*-1;
            movement[2] = PLAYER_INCREMENT;
        } else if (s.contains("R")) {
            movement[0] = PLAYER_DISTANCE;
            movement[1] = 0f;
            movement[2] = PLAYER_INCREMENT*-1;
        } else if (s.contains("L")) {
            movement[0] = PLAYER_DISTANCE*-1;
            movement[1] = 0f;
            movement[2] = PLAYER_INCREMENT;
        } else if (s.contains("D")) {
            movement[0] = 0f;
            movement[1] = PLAYER_DISTANCE;
            movement[2] = PLAYER_INCREMENT*-1;
        } else if (s.contains("B")) {
            movement[0] = 0f;
            movement[1] = 0f;
        }
        return movement;
    }

}