package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.eye7.ShootyWooty.main;

/**
 * Created by JunXiang on 3/3/2015.
 */
public class MainLoader {
    public static BitmapFont white, green;
    public static TextureAtlas atlas;
    public static Skin skin;

    public static void load() {
        white = new BitmapFont(Gdx.files.internal("android/assets/fonts/white.fnt"),true); // white font
        green = new BitmapFont(Gdx.files.internal("android/assets/fonts/green.fnt"),true); // green font
        white.setScale(main.scaleX, main.scaleY); // scale it to size
        green.setScale(main.scaleX, main.scaleY);
        atlas = new TextureAtlas(Gdx.files.internal("android/assets/Gbuttons/Gbuttons.pack")); // atlas for skin
        skin = new Skin(atlas); // skin containing drawables
    }
}
