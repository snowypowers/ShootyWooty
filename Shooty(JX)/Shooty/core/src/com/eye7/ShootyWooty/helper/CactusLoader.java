package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.eye7.ShootyWooty.main;
import com.eye7.ShootyWooty.model.GameConstants;

/**
 * Created by PT on 3/4/2015.
 */


public class CactusLoader {
    public static Animation animation_cactus1_idle;
    public static Animation animation_cactus1_RS;
    public static Animation animation_cactus1_RS_idle;
    public static Animation animation_cactus1_LS;
    public static Animation animation_cactus1_LS_idle;
    public static Animation animation_cactus1_back;
    public static Animation animation_cactus1_back_idle;
    public static Animation animation_cactus1_front;
    public static Animation animation_cactus1_score;
    public static Animation animation_cactus1_shot;
    public static Animation animation_cactus1_lose;

    public static Animation animation_cactus2_idle;
    public static Animation animation_cactus2_RS;
    public static Animation animation_cactus2_RS_idle;
    public static Animation animation_cactus2_LS;
    public static Animation animation_cactus2_LS_idle;
    public static Animation animation_cactus2_back;
    public static Animation animation_cactus2_back_idle;
    public static Animation animation_cactus2_front;
    public static Animation animation_cactus2_score;
    public static Animation animation_cactus2_shot;
    public static Animation animation_cactus2_lose;

    public static Animation animation_cactus3_idle;
    public static Animation animation_cactus3_RS;
    public static Animation animation_cactus3_RS_idle;
    public static Animation animation_cactus3_LS;
    public static Animation animation_cactus3_LS_idle;
    public static Animation animation_cactus3_back;
    public static Animation animation_cactus3_back_idle;
    public static Animation animation_cactus3_front;
    public static Animation animation_cactus3_score;
    public static Animation animation_cactus3_shot;
    public static Animation animation_cactus3_lose;

    public static Animation animation_cactus4_idle;
    public static Animation animation_cactus4_RS;
    public static Animation animation_cactus4_RS_idle;
    public static Animation animation_cactus4_LS;
    public static Animation animation_cactus4_LS_idle;
    public static Animation animation_cactus4_back;
    public static Animation animation_cactus4_back_idle;
    public static Animation animation_cactus4_front;
    public static Animation animation_cactus4_score;
    public static Animation animation_cactus4_shot;
    public static Animation animation_cactus4_lose;

    public CactusLoader(int NUM_PLAYERS){
        if(NUM_PLAYERS==2){
            loadC1();
            loadC2();
        }
        else{
            loadC1();
            loadC2();
            loadC3();
            loadC4();
        }
    }


    public Animation loader(String file, int frameNumber, float speed, boolean flipX){
        Texture sprite = new Texture(Gdx.files.internal(file));
        sprite.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);

        TextureRegion[] frames = new TextureRegion[frameNumber];
        int count=0;
        for (int i = 0; i < frameNumber ; i++) {
            frames[i] = new TextureRegion(sprite,count,0,64,64);
            frames[i].flip(flipX,true);
            count+=64;
        }
        return new Animation(speed,frames);
    }
    public void loadC1(){
        animation_cactus1_idle = loader("Character1/Cactus1_idle.png",14,0.2f,false);
        animation_cactus1_RS = loader("Character1/Cactus1_RS.png",4,0.4f,false);
        animation_cactus1_RS_idle = loader("Character1/Cactus1_RS_idle.png",4,0.4f,false);
        animation_cactus1_LS = loader("Character1/Cactus1_RS.png",4,0.4f,true);
        animation_cactus1_LS_idle = loader("Character1/Cactus1_RS_idle.png",4,0.4f,true);
        animation_cactus1_back = loader("Character1/Cactus1_back.png",4,0.3f,false);
        animation_cactus1_back_idle = loader("Character1/Cactus1_back_idle.png",2,0.4f,false);
        animation_cactus1_front = loader("Character1/Cactus1_front.png",6,0.4f,false);
        animation_cactus1_score = loader("Character1/Cactus1_score.png",8,0.2f,false);
        animation_cactus1_shot = loader("Character1/Cactus1_shot.png",2,0.2f,false);
        animation_cactus1_lose = loader("Character1/Cactus1_lose.png",2,0.2f,false);

    }
    public void loadC2(){
        animation_cactus2_idle = loader("Character2/Cactus2_idle.png",9,0.2f,false);
        animation_cactus2_RS = loader("Character2/Cactus2_RS.png",4,0.3f,false);
        animation_cactus2_RS_idle = loader("Character2/Cactus2_RS_idle.png",4,0.4f,false);
        animation_cactus2_LS = loader("Character2/Cactus2_RS.png",4,0.3f,true);
        animation_cactus2_LS_idle = loader("Character2/Cactus2_RS_idle.png",4,0.4f,true);
        animation_cactus2_back = loader("Character2/Cactus2_back.png",4,0.3f,false);
        animation_cactus2_back_idle = loader("Character2/Cactus2_back_idle.png",2,0.4f,false);
        animation_cactus2_front = loader("Character2/Cactus2_front.png",2,0.3f,false);
        animation_cactus2_score = loader("Character2/Cactus2_score.png",6,0.4f,false);
        animation_cactus2_shot = loader("Character2/Cactus2_shot.png",2,0.2f,false);
        animation_cactus2_lose = loader("Character2/Cactus2_lose.png",2,0.2f,false);


    }
    public void loadC3(){
        animation_cactus3_idle = loader("Character3/Cactus3_idle.png",11,0.3f,false);
        animation_cactus3_RS = loader("Character3/Cactus3_RS.png",4,0.3f,false);
        animation_cactus3_RS_idle = loader("Character3/Cactus3_RS_idle.png",4,0.4f,false);
        animation_cactus3_LS = loader("Character3/Cactus3_RS.png",4,0.3f,true);
        animation_cactus3_LS_idle = loader("Character3/Cactus3_RS_idle.png",4,0.4f,true);
        animation_cactus3_back = loader("Character3/Cactus3_back.png",2,0.4f,false);
        animation_cactus3_back_idle = loader("Character3/Cactus3_back_idle.png",4,0.3f,false);
        animation_cactus3_front = loader("Character3/Cactus3_front.png",2,0.3f,false);
        animation_cactus3_score = loader("Character3/Cactus3_score.png",9,0.4f,false);
        animation_cactus3_shot = loader("Character3/Cactus3_shot.png",2,0.2f,false);
        animation_cactus3_lose = loader("Character3/Cactus3_lose.png",4,0.2f,false);

    }
    public void loadC4(){
        animation_cactus4_idle = loader("Character4/Cactus4_idle.png",9,0.3f,false);
        animation_cactus4_RS = loader("Character4/Cactus4_RS.png",4,0.3f,false);
        animation_cactus4_RS_idle = loader("Character4/Cactus4_RS_idle.png",2,0.4f,false);
        animation_cactus4_LS = loader("Character4/Cactus4_RS.png",4,0.3f,true);
        animation_cactus4_LS_idle = loader("Character4/Cactus4_RS_idle.png",2,0.4f,true);
        animation_cactus4_back = loader("Character4/Cactus4_back.png",2,0.4f,false);
        animation_cactus4_back_idle = loader("Character4/Cactus4_back_idle.png",4,0.3f,false);
        animation_cactus4_front = loader("Character4/Cactus4_front.png",2,0.3f,false);
        animation_cactus4_score = loader("Character4/Cactus4_score.png",7,0.4f,false);
        animation_cactus4_shot = loader("Character4/Cactus4_shot.png",2,0.2f,false);
        animation_cactus4_lose = loader("Character4/Cactus4_lose.png",3,0.2f,false);
    }

}