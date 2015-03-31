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
    public static BitmapFont white, green;
    public static TextureAtlas atlas;
    public static Skin skin;


    public static Animation Animation_cactus1_idle;
    public static Animation Animation_cactus1_RS;
    public static Animation Animation_cactus1_LS;
    public static Animation Animation_cactus1_back;
    public static Animation Animation_cactus1_front;
    public static Animation Animation_cactus1_score;

    public static Animation Animation_faucet;

    public static void load() {
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"),true); // white font
        green = new BitmapFont(Gdx.files.internal("fonts/green.fnt"),true); // green font
        white.setScale(GameConstants.SCALE_X, GameConstants.SCALE_Y); // scale it to size
        green.setScale(GameConstants.SCALE_X, GameConstants.SCALE_Y);
        atlas = new TextureAtlas(Gdx.files.internal("buttons/Gbuttons.pack")); // atlas for skin
        skin = new Skin(atlas); // skin containing drawables

        Texture cactus1_idle = new Texture(Gdx.files.internal("Character1/Cactus1_idle.png"));
        cactus1_idle.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion[] cactus = new TextureRegion[14];
        int cactus_idle1= 0;
        for (int i = 0; i < 14; i++) {

            cactus[i] = new TextureRegion(cactus1_idle,cactus_idle1,0,64,64);
            cactus[i].flip(false,true);
            cactus_idle1+=64;
        }

        Animation_cactus1_idle = new Animation(0.2f,cactus);

        Texture cactus1_RS = new Texture(Gdx.files.internal("Character1/Cactus1_R.png"));
        cactus1_RS.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);

        TextureRegion[] cactus_RS = new TextureRegion[4];
        int cactus_rs1=0;
        TextureRegion[] cactus_LS = new TextureRegion[4];
        int cactus_ls1=0;
        for (int i = 0; i < 4 ; i++) {
            cactus_RS[i] = new TextureRegion(cactus1_RS,cactus_rs1,0,64,64);
            cactus_LS[i] = new TextureRegion(cactus1_RS,cactus_ls1,0,64,64);
            cactus_RS[i].flip(false,true);
            cactus_LS[i].flip(true,true);
            cactus_ls1+=64;
            cactus_rs1+=64;
        }

        Animation_cactus1_RS = new Animation(0.4f,cactus_RS);
        Animation_cactus1_LS = new Animation(0.4f,cactus_LS);

        Texture cactus1_back = new Texture(Gdx.files.internal("Character1/Cactus1_back.png"));
        cactus1_RS.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);

        TextureRegion[] cactus_back = new TextureRegion[6];
        int cactus_back1=0;
        for (int i = 0; i < 6 ; i++) {
            cactus_back[i] = new TextureRegion(cactus1_back,cactus_back1,0,64,64);
            cactus_back[i].flip(false,true);
            cactus_back1+=64;
        }
        Animation_cactus1_back = new Animation(0.4f,cactus_back);

        Texture cactus1_front = new Texture(Gdx.files.internal("Character1/Cactus1_front.png"));
        cactus1_RS.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);

        TextureRegion[] cactus_front = new TextureRegion[6];
        int cactus_front1=0;
        for (int i = 0; i < 6 ; i++) {
            cactus_front[i] = new TextureRegion(cactus1_front,cactus_front1,0,64,64);
            cactus_front[i].flip(false,true);
            cactus_front1+=64;
        }
        Animation_cactus1_front = new Animation(0.4f,cactus_front);


        Texture score = new Texture(Gdx.files.internal("Character1/Cactus1_score.png"));

        TextureRegion[] scores = new TextureRegion[13];
        int cactus1_score=0;
        for (int i = 0; i < 13 ; i++) {
            scores[i] = new TextureRegion(score,cactus1_score,0,64,64);
            scores[i].flip(false,true);
            cactus1_score+=64;
        }
        Animation_cactus1_score = new Animation(0.1f,scores);

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
