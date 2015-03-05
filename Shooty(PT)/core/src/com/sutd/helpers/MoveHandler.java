package com.sutd.helpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sutd.gameworld.GameWorld;

//import com.sutd.object.MoveDisplay;

public class MoveHandler {

    float[] movement = {0f, 0f, 0f};
    public float[] AmountToMove(String s) {
        // for(String s:moves){

        if (s.contains("F")) {
            movement[0] = 0f;
            movement[1] = -20f;
            movement[2] = 1f;
        } else if (s.contains("R")) {
            movement[0] = 20f;
            movement[1] = 0f;
            movement[2] = -1f;
        } else if (s.contains("L")) {
            movement[0] = -20f;
            movement[1] = 0f;
            movement[2] = 1f;
        } else if (s.contains("D")) {
            movement[0] = 0f;
            movement[1] = 20f;
            movement[2] = -1f;
        } else if (s.contains("B")) {
            movement[0] = 0f;
            movement[1] = 0f;
        }
        return movement;
    }
    public void updateList(float[] update){
        movement = update;
    }

}