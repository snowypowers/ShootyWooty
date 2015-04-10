package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * @author PT
 * thread that counts in 30 seconds interval
 * with 5 seconds execution time after every
 * 30 seconds round
 */
public class Timer extends Thread {
    private Object timerReset;
    private int time;
    private String timeStatus = "Time: ";

    public Timer(Stage stage, Object timerReset) {
        time = 0;
        this.timerReset = timerReset;
        HourGlass hg = new HourGlass(stage, this);
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
                    try {
                        synchronized(timerReset) {
                            timerReset.wait();
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
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
        timeStatus = "Executing Moves...";
        time = -1;
        interrupt();
    }
}

class HourGlass extends Actor {
    public Skin hourglassSkin;
    private Animation hourglassTimerAnimation;
    private Animation hourglassTurnAnimation;

    private ActionMenu actionMenu;
    private Stage stage;
    private Timer timer;

    public HourGlass(Stage stage, Timer t) {
        this.stage = stage;
        this.timer = t;
        stage.addActor(this);

        //Import and build animation
        hourglassSkin = new Skin(new TextureAtlas(Gdx.files.internal("ShootyUI/hourglass1.pack")));
        hourglassSkin.addRegions(new TextureAtlas(Gdx.files.internal("ShootyUI/hourglass2.pack")));
        TextureRegion[] hourglassTimer = new TextureRegion[27];
        TextureRegion[] hourglassTurn = new TextureRegion[6];

        for (int i = 0; i < 27; i++) {
            hourglassTimer[i] = new TextureRegion (hourglassSkin.getRegion("hourglass.timer." + String.valueOf(i+1)));
        }
        for (int i = 0; i < 6; i++) {
            hourglassTurn[i] = new TextureRegion (hourglassSkin.getRegion("hourglass.turn." + String.valueOf(i+1)));
        }

        hourglassTimerAnimation = new Animation((float)30.0/27, hourglassTimer);
        hourglassTurnAnimation = new Animation(0.2f, hourglassTurn);

        attachActionMenu();

    }

    @Override
    public void draw(Batch b, float parentAlpha) {
        b.enableBlending();
        float a = actionMenu.getAlpha();

        //Set the alpha to the alpha given by ActionMenu
        Color c = b.getColor();
        b.setColor(c.r, c.g, c.b, a);
        if (timer.getTime() < 30) {
            b.draw(hourglassTimerAnimation.getKeyFrame((float) timer.getTime(), true), 0,0,192,192);
        }
    }

    public boolean attachActionMenu() {
        for (Actor a: stage.getActors()) {
            if (a.getName().contains("ActionMenu")) {
                actionMenu = (ActionMenu) a;
                return true;
            }
        }
        return false;
    }

}




