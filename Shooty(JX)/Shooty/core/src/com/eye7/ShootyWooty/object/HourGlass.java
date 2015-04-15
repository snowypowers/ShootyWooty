package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.eye7.ShootyWooty.model.GameConstants;

/**
 * Created by Yak Jun Xiang on 13/4/2015.
 */

public class HourGlass extends Actor implements Observer {
    private final String TAG = "HourGlass";

    public Skin hourglassSkin;
    private Animation hourglassTimerAnimation;
    private Animation hourglassTurnAnimation;

    private ActionMenu actionMenu;
    private Stage stage;
    private Timer timer;
    private boolean turn;

    private float turnDelta = 0;

    public HourGlass(Stage stage) {
        //Listen to Turn End
        GameConstants.subscribeTurnEnd(this);
        GameConstants.subscribeGameEnd(this);

        this.stage = stage;
        this.timer = new Timer(this);
        turn = false;
        stage.addActor(this);

        //Import and build animation
        hourglassSkin = new Skin(new TextureAtlas(Gdx.files.internal("ShootyUI/hourglass1.pack")));
        hourglassSkin.addRegions(new TextureAtlas(Gdx.files.internal("ShootyUI/hourglass2.pack")));
        TextureRegion[] hourglassTimer = new TextureRegion[30];
        TextureRegion[] hourglassTurn = new TextureRegion[6];

        for (int i = 0; i < 30; i++) {
            hourglassTimer[i] = new TextureRegion (hourglassSkin.getRegion("hourglass.timer." + String.valueOf(i+1)));
        }
        for (int i = 0; i < 6; i++) {
            hourglassTurn[i] = new TextureRegion (hourglassSkin.getRegion("hourglass.turn." + String.valueOf(i+1)));
        }

        hourglassTimerAnimation = new Animation(GameConstants.TIME_LIMIT/30f, hourglassTimer);
        hourglassTurnAnimation = new Animation(0.2f, hourglassTurn);

        attachActionMenu();

    }

    public void start() {
        timer.start();
    }

    public void turnStart() {
        turn();
        GameConstants.TurnStart();
    }

    @Override
    public void draw(Batch b, float parentAlpha) {
        b.enableBlending();
        float a = actionMenu.getAlpha();

        //Set the alpha to the alpha given by ActionMenu
        Color c = b.getColor();
        b.setColor(c.r, c.g, c.b, a);
        if (!turn) {
            b.draw(hourglassTimerAnimation.getKeyFrame(timer.getTime(), true), 0,0,192,192);
        } else {
            b.draw(hourglassTurnAnimation.getKeyFrame(turnDelta), 0, 0, 192, 192);
            turnDelta += Gdx.graphics.getDeltaTime();

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

    public void turn() {
        Gdx.app.log(TAG, "TURN");
        turnDelta = 0;
        if (turn) {
            turn = false;
        } else {
            turn = true;
        }
    }

    public float getTime() {
        return timer.getTime();
    }

    public void observerUpdate(int i) {
        if (i == 1) { // Turn End
            synchronized (timer) {
                timer.notify();
                turn();
            }
        }
        if (i == 2) { // Game End
            synchronized (timer) {
                timer.gameEnd();
                timer.notify();
            }
        }
    }

    public int observerType() {
        return 0;
    }

}

class Timer extends Thread {
    private HourGlass hourGlass;
    private float time;
    private String timeStatus = "Time: ";
    private boolean running;

    public Timer(HourGlass hg) {
        running = true;
        time = 0f;
        this.hourGlass = hg;
    }

    @Override
    public void run() {
        while (running) {
            if (time >= GameConstants.TIME_LIMIT) {
                hourGlass.turnStart();
                this.interrupt();
            }
            if (time >= 0) {
                timeStatus = "Time: ";
            } else {
                timeStatus = "Executing Moves...";
            }
            try {
                Thread.sleep(100);
                time += 0.1f;
            } catch (InterruptedException e) {
                try {
                    synchronized(this) {
                        this.wait();
                    }
                    time = 0f;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

    public String getTimeStatus() {
        return timeStatus;
    }
    public float getTime() {
        return time;
    }
    public void gameEnd() {
        running = false;
    }


}





