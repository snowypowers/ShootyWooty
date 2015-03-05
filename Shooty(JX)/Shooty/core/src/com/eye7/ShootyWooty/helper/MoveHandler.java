package com.eye7.ShootyWooty.helper;


public class MoveHandler {

    float[] movement = {0f, 0f, 0f};
    public float[] AmountToMove(String s) {

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