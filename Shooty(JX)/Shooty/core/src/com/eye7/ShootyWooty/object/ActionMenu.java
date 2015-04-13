package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.model.GameConstants;

/**
 * Created by Yak Jun Xiang on 8/4/2015.
 */
public class ActionMenu extends Table {
    private final String TAG = "ActionMenu";

    private ActionResolver actionResolver;

    private static float leftEdge = 800;
    private static float rightEdge = 1050;

    private Button handle;
    private Drawable handle_img;
    private InputButtons inputButtons;
    private GameOverMenu gameOverMenu;

    private boolean deathFlag = false;


    private boolean beingDragged;
    private float drawerSpeed = 10f;

    public ActionMenu(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
        //Setup Name
        this.setName("ActionMenu");
        if (GameConstants.DEBUG) {
            this.debug();
        }



        //Setup buttons
        inputButtons = new InputButtons();
        handle_img = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/handle.png"))));
        handle = new Button(handle_img, handle_img);
        beingDragged = false;


        handle.addListener( new DragListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "Got it!");
                beingDragged = true;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event,float x, float y, int pointer) {
                float newX = getX() + x;
                if (newX < leftEdge) {
                    newX = leftEdge;
                } else if (newX > rightEdge) {
                    newX = rightEdge;
                }
                setX(newX);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                beingDragged = false;
            }
        });



        //Add buttons to table
        this.add(handle).center();
        this.add(inputButtons);
        this.setPosition(1050, 270); //Right edge of screen


    }

    public String[] getMoves() {
        return inputButtons.getMoves();
    }


    public void act(float delta) {
        if (!beingDragged) {
            float midpoint = (rightEdge - leftEdge) / 2;
            if (getX() < (leftEdge + midpoint) && getX() > leftEdge) {
                setX(getX() - drawerSpeed);
            } else if(getX() > (rightEdge - midpoint) && getX() < rightEdge) {
                setX(getX() + drawerSpeed);
            }
        }
        if (deathFlag == false) {
            if (GameConstants.PLAYERS.get(GameConstants.myID + 1).isDead()) {
                gameOver();
                deathFlag = true;
            }
        }
    }

    //Helper method for HourGlass to get the alpha value
    public float getAlpha() {
        float output = (rightEdge - getX()) / (rightEdge - leftEdge);
        if (output < 0.2f) {
            return 0.2f;
        } else if (output > 1) {
            return 1;
        } else {
            return output;
        }
    }

    public void gameOver() {
        this.removeActor(inputButtons);
        this.add(gameOverMenu = new GameOverMenu(actionResolver));
    }


}
