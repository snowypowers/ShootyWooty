package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Yak Jun Xiang on 8/4/2015.
 */
public class ActionMenu extends Group {
    private Sprite img;


    ActionMenu(Stage s) {
    img = new Sprite(new Texture(Gdx.files.internal("buttons/test.png")));
    setPosition(900,0); //Right edge of screen
    }

    public void draw(SpriteBatch sb) {

    }

}
