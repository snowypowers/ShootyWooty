package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by JunXiang on 3/3/2015.
 */
public class MainLoader {
    public static BitmapFont white, green;
    public static TextureAtlas atlas;
    public static Skin skin;

    public static void load() {
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"),true); // white font
        green = new BitmapFont(Gdx.files.internal("fonts/green.fnt"),true); // green font
        white.setScale(.3f,.3f); // scale it to size
        green.setScale(.5f, .5f);
        atlas = new TextureAtlas(Gdx.files.internal("Gbuttons/Gbuttons.pack")); // atlas for skin
        skin = new Skin(atlas); // skin containing drawables
    }
}
