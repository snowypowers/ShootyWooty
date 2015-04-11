package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PT on 3/4/2015.
 * Animation Strings:
 * character.north.moving (4 frames)
 * character.north.idle (4 frames)
 * character.south.moving (4 frames)
 * character.south.idle (4 frames)
 * character.east.moving (4 frames)
 * character.east.idle (4 frames)
 * character.west.moving (4 frames)
 * character.west.idle (4 frames)
 * character.score (varied frames)
 * character.shot (2 frames)
 * character.lose (2 frames)
 * character.emote (varied frames)
 */


public class CactusLoader {
    public static TextureAtlas cactus1Skin;
    public static TextureAtlas cactus2Skin;
    public static TextureAtlas cactus3Skin;
    public static TextureAtlas cactus4Skin;

    public static HashMap<String, Animation> cactus1_animations = new HashMap<String, Animation>();
    public static HashMap<String, Animation> cactus2_animations = new HashMap<String, Animation>();
    public static HashMap<String, Animation> cactus3_animations = new HashMap<String, Animation>();
    public static HashMap<String, Animation> cactus4_animations = new HashMap<String, Animation>();



    public CactusLoader(int NUM_PLAYERS){

        cactus1Skin = new TextureAtlas(Gdx.files.internal("players/Cactus1.pack"));
        cactus2Skin = new TextureAtlas(Gdx.files.internal("players/Cactus2.pack"));

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


    public void loader(String name, int frameNumber, float speed, HashMap<String,Animation> Cactus){
        String[] output = name.split("\\.",2);
        Skin cactusSkin = null;
        if (output[0].contains("Cactus1")) {
            cactusSkin = new Skin(cactus1Skin);
        } else if (output[0].contains("Cactus2")) {
            cactusSkin = new Skin(cactus2Skin);
        } else if (output[0].contains("Cactus3")) {
            cactusSkin = new Skin(cactus3Skin);
        } else if (output[0].contains("Cactus4")) {
            cactusSkin = new Skin(cactus4Skin);
        }

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < frameNumber+1 ; i++) {
            frames.add(new TextureRegion(cactusSkin.getRegion(name+String.valueOf(i))));
        }

        Cactus.put(output[1], new Animation(speed, frames, Animation.PlayMode.LOOP));


    }
    public void loadC1(){
        loader("Cactus1.north.moving",4,0.15f,cactus1_animations);
        loader("Cactus1.north.idle",4,0.2f,cactus1_animations);

        loader("Cactus1.south.moving",4,0.15f,cactus1_animations);
        loader("Cactus1.south.idle",4,0.3f,cactus1_animations);

        loader("Cactus1.east.moving",4,0.15f,cactus1_animations);
        loader("Cactus1.east.idle",4,0.3f,cactus1_animations);

        loader("Cactus1.west.moving",4,0.15f,cactus1_animations);
        loader("Cactus1.west.idle",4,0.3f,cactus1_animations);

        loader("Cactus1.score",8,0.3f,cactus1_animations);
        loader("Cactus1.shot",2,0.2f,cactus1_animations);
        loader("Cactus1.lose",2,0.2f,cactus1_animations);
        loader("Cactus1.emote",10,0.2f,cactus1_animations);
    }

    public void loadC2(){
        loader("Cactus2.north.moving",4,0.15f,cactus2_animations);
        loader("Cactus2.north.idle",4,0.2f,cactus2_animations);

        loader("Cactus2.south.moving",4,0.15f,cactus2_animations);
        loader("Cactus2.south.idle",4,0.3f,cactus2_animations);

        loader("Cactus2.east.moving",4,0.15f,cactus2_animations);
        loader("Cactus2.east.idle",4,0.3f,cactus2_animations);

        loader("Cactus2.west.moving",4,0.15f,cactus2_animations);
        loader("Cactus2.west.idle",4,0.3f,cactus2_animations);

        loader("Cactus2.score",8,0.3f,cactus2_animations);
        loader("Cactus2.shot",2,0.2f,cactus2_animations);
        loader("Cactus2.lose",2,0.2f,cactus2_animations);
        loader("Cactus2.emote",10,0.4f,cactus2_animations);
    }
    public void loadC3(){
        loader("Cactus3.north.moving",4,0.4f,cactus3_animations);
        loader("Cactus3.north.idle",4,0.4f,cactus3_animations);

        loader("Cactus3.south.moving",4,0.4f,cactus3_animations);
        loader("Cactus3.south.idle",4,0.4f,cactus3_animations);

        loader("Cactus3.east.moving",4,0.4f,cactus3_animations);
        loader("Cactus3.east.idle",4,0.4f,cactus3_animations);

        loader("Cactus3.west.moving",4,0.4f,cactus3_animations);
        loader("Cactus3.west.idle",4,0.4f,cactus3_animations);

        loader("Cactus3.score",8,0.4f,cactus3_animations);
        loader("Cactus3.shot",2,0.2f,cactus3_animations);
        loader("Cactus3.lose",2,0.2f,cactus3_animations);
        loader("Cactus3.emote",10,0.4f,cactus3_animations);

    }
    public void loadC4(){
        loader("Cactus4.north.moving",4,0.4f,cactus4_animations);
        loader("Cactus4.north.idle",4,0.4f,cactus4_animations);

        loader("Cactus4.south.moving",4,0.4f,cactus4_animations);
        loader("Cactus4.south.idle",4,0.4f,cactus4_animations);

        loader("Cactus4.east.moving",4,0.4f,cactus4_animations);
        loader("Cactus4.east.idle",4,0.4f,cactus4_animations);

        loader("Cactus4.west.moving",4,0.4f,cactus4_animations);
        loader("Cactus4.west.idle",4,0.4f,cactus4_animations);

        loader("Cactus4.score",8,0.4f,cactus4_animations);
        loader("Cactus4.shot",2,0.2f,cactus4_animations);
        loader("Cactus4.lose",2,0.2f,cactus4_animations);
        loader("Cactus4.emote",10,0.4f,cactus4_animations);
    }

}