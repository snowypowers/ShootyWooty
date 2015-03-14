package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author PT
 * thread that counts in 30 seconds interval
 * with 5 seconds execution time after every
 * 30 seconds round
 */
public class Timer extends Thread {
    private int time;
    private String timeStatus = "Time: ";

    public Timer(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
                if (time >= 0) {
                    timeStatus = "Time: ";
                } else {
                    timeStatus = "Executing Moves...";
                }
                try {
                    Thread.sleep(1000);
                    time += 1;
                    //Gdx.app.log("GameWorld", time + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }
    public String getTimeStatus() {
        return timeStatus;
    }
    public int getTime() {
        return time;
    }
    public void reset() {
        time = -8;
    }
}



