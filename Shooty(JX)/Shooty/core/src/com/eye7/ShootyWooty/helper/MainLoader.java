package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.eye7.ShootyWooty.main;
import com.eye7.ShootyWooty.model.GameConstants;

/**
 * Created by JunXiang on 3/3/2015.
 */
public class MainLoader {
    private final String TAG = "MainLoader";
    public static BitmapFont white, green;
    public static TextureAtlas atlas;
    public static Skin skin;

    public static Animation Animation_faucet;

    public static void load() {
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"),true); // white font
        green = new BitmapFont(Gdx.files.internal("fonts/green.fnt"),true); // green font
        white.setScale(GameConstants.SCALE_X, GameConstants.SCALE_Y); // scale it to size
        green.setScale(GameConstants.SCALE_X, GameConstants.SCALE_Y);
        atlas = new TextureAtlas(Gdx.files.internal("buttons/Gbuttons.pack")); // atlas for skin
        skin = new Skin(atlas); // skin containing drawables

        Texture faucet = new Texture(Gdx.files.internal("ShootyUI/Sprite_faucet.png"));
        faucet.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);

        TextureRegion[] faucets = new TextureRegion[11];
        int faucet_count=0;
        for (int i = 0; i < 11 ; i++) {
            faucets[i] = new TextureRegion(faucet,faucet_count,0,64+64,64+64);
            faucets[i].flip(false,true);
            faucet_count+=64+64;
        }
        Animation_faucet = new Animation(0.2f,faucets);

    }

}
