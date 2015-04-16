package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Loader to load all assets required by the game. Player assets are loaded in CactusLoader.
 */
public class MainLoader {
    private static String TAG = "MainLoader";

    public static BitmapFont white, green;
    public static TextureAtlas atlas;
    public static Skin skin;

    public static Drawable menuBG;
    public static Drawable exitmenuBG;
    public static Drawable homeButtonImg;


    public static Animation animation_faucet;

    //add the sounds here
    public static Music bgMusic;

    public static void load() {
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"),false); // white font
        green = new BitmapFont(Gdx.files.internal("fonts/green.fnt"),false); // green font
        atlas = new TextureAtlas(Gdx.files.internal("buttons/Gbuttons.pack")); // atlas for skin
        skin = new Skin(atlas); // skin containing drawables

       menuBG = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/menuBG.png"))));
       exitmenuBG = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/exitmenuBG.png"))));
       homeButtonImg = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/exitButton2.png"))));

        //Background music
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/bgMusic.mp3"));



//        TextureRegion[] faucets = new TextureRegion[11];
//        int faucet_count=0;
//        for (int i = 1; i < 12 ; i++) {
//            faucets[i-1] = new TextureRegion(skinCharacters.getRegion("Sprite.faucet"+String.valueOf(i)));
//            faucets[i-1].flip(false,true);
//        }
//        animation_faucet = new Animation(0.2f,faucets);


    }

    //Call when leaving game to unload all resources
    public static void dispose() {
        atlas.dispose();
        skin.dispose();
        bgMusic.dispose();
    }


}
