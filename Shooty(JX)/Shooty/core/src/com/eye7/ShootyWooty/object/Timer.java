package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;

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
        while (true) {

            if (time < 30) {
                try {
                    Thread.sleep(1000);
                    time += 1;
                    Gdx.app.log("GameWorld", time + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                timeStatus = "Executing Moves...";
                try {
                    // allowing 5 seconds for execution
                    Thread.sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                time=0;
            }

            timeStatus = "Time: ";
        }

    }
    public String getTimeStatus() {
        return timeStatus;
    }
    public int getTime() {
        return time;
    }
}



