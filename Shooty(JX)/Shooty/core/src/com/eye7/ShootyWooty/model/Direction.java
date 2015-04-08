package com.eye7.ShootyWooty.model;

/**
 * Created by JunXiang on 5/3/2015.
 */
public enum Direction {
    NORTH (0),
    WEST (90),
    SOUTH (180),
    EAST (270);

    private final int deg;

    Direction (int d) {
        this.deg = d;
    }

    Direction (Direction d) {
        this.deg = d.getDeg();
    }

    public int getDeg() {
        return this.deg;
    }
}
