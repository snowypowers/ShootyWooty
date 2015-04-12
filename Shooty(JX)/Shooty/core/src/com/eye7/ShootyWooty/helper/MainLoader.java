package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.main;
import com.eye7.ShootyWooty.model.GameConstants;

/**
 * Created by JunXiang on 3/3/2015.
 * Loader to load all assets required in game.
 */
public class MainLoader {
    private static String TAG = "MainLoader";

    public static BitmapFont white, green;
    public static TextureAtlas atlas;
    public static Skin skin, skinCharacters;


    public static Animation hourglass_timer;
    public static Animation hourglass_turn;

    public static Animation animation_faucet;

    //add the sounds here
    public static Music waterFountain;

    public static void load() {
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"),true); // white font
        green = new BitmapFont(Gdx.files.internal("fonts/green.fnt"),true); // green font
        white.setScale(GameConstants.SCALE_X, GameConstants.SCALE_Y); // scale it to size
        green.setScale(GameConstants.SCALE_X, GameConstants.SCALE_Y);
        atlas = new TextureAtlas(Gdx.files.internal("buttons/Gbuttons.pack")); // atlas for skin
        skin = new Skin(atlas); // skin containing drawables

        //add the background sound here
        waterFountain = Gdx.audio.newMusic(Gdx.files.internal("sounds/waterFountain.mp3"));
        waterFountain.setLooping(true);
        waterFountain.setVolume(0.4f);
        waterFountain.play();


//        TextureRegion[] faucets = new TextureRegion[11];
//        int faucet_count=0;
//        for (int i = 1; i < 12 ; i++) {
//            faucets[i-1] = new TextureRegion(skinCharacters.getRegion("Sprite.faucet"+String.valueOf(i)));
//            faucets[i-1].flip(false,true);
//        }
//        animation_faucet = new Animation(0.2f,faucets);


    }



}
