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

/**
 * Created by Yak Jun Xiang on 8/4/2015.
 */
public class ActionMenu extends Table {
    private final String TAG = "ActionMenu";

    private Button handle;
    private Drawable handle_img;
    private InputButtons inputButtons;
    private DragListener d;


    public ActionMenu() {

        //Setup buttons
        inputButtons = new InputButtons();
        handle_img = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/handle.png"))));
        handle = new Button(handle_img, handle_img);

        handle.addListener( new DragListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "Got it!");
                return true;
            }

            @Override
            public void touchDragged(InputEvent event,float x, float y, int pointer) {
                float newX = getX() + x;
                if (newX < 800) {
                    newX = 800;
                } else if (newX > 1050) {
                    newX = 1050;
                }
                setX(newX);
            }
        });



        //Add buttons to table
        this.add(handle).center();
        this.add(inputButtons);
        this.setPosition(1050, 225); //Right edge of screen
    }

    public String[] getMoves() {
        return inputButtons.getMoves();
    }

    public void setLock(boolean lock) {
        inputButtons.setLock(lock);
    }

    public void reset() {
        inputButtons.reset();
    }


}
