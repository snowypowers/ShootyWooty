package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.eye7.ShootyWooty.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PT on 3/4/2015.
 */


public class CactusLoader {

    public static HashMap<String, Animation> cactus1_animations = new HashMap<String, Animation>();
    public static HashMap<String, Animation> cactus2_animations = new HashMap<String, Animation>();
    public static HashMap<String, Animation> cactus3_animations = new HashMap<String, Animation>();
    public static HashMap<String, Animation> cactus4_animations = new HashMap<String, Animation>();

    public static ArrayList<HashMap<String, Animation>> cactus_animations = null;


    public CactusLoader(int NUM_PLAYERS){

        if(NUM_PLAYERS==2){
            loadC1();
            loadC2();
//            cactus_animations.add(cactus1_animations);
//            cactus_animations.add(cactus2_animations);
        }
        else{
            loadC1();
            loadC2();
            loadC3();
            loadC4();
//            cactus_animations.add(cactus1_animations);
//            cactus_animations.add(cactus2_animations);
//            cactus_animations.add(cactus3_animations);
//            cactus_animations.add(cactus4_animations);
        }
    }


    public void loader(String name, int frameNumber, float speed, boolean flipX, HashMap<String,Animation> Cactus){

        TextureRegion[] frames = new TextureRegion[frameNumber];

        for (int i = 1; i < frameNumber+1 ; i++) {
            frames[i-1] =  new TextureRegion(MainLoader.skinCharacters.getRegion(name+String.valueOf(i)));
            frames[i-1].flip(flipX,true);
        }
        if(flipX) {
            name = name.replace("R","L");
            Gdx.app.log("null", name);
        }
        Cactus.put(name.substring(8, name.length()), new Animation(speed, frames));


    }
    public void loadC1(){
        loader("Cactus1.idle",14,0.2f,false,cactus1_animations);
        loader("Cactus1.RS",4,0.4f,false, cactus1_animations);
        loader("Cactus1.RS.idle",4,0.4f,false, cactus1_animations);
        loader("Cactus1.RS",4,0.4f,true, cactus1_animations);
        loader("Cactus1.RS.idle",4,0.4f,true, cactus1_animations);
        loader("Cactus1.back",4,0.3f,false,cactus1_animations);
        loader("Cactus1.back.idle",2,0.4f,false,cactus1_animations);
        loader("Cactus1.front",6,0.4f,false,cactus1_animations);
        loader("Cactus1.score",8,0.2f,false,cactus1_animations);
        loader("Cactus1.shot",2,0.2f,false,cactus1_animations);
        loader("Cactus1.lose",2,0.2f,false,cactus1_animations);

    }
    public void loadC2(){
        loader("Cactus2.idle",9,0.2f,false,cactus2_animations);
        loader("Cactus2.RS",4,0.3f,false,cactus2_animations);
        loader("Cactus2.RS.idle",4,0.4f,false,cactus2_animations);
        loader("Cactus2.RS",4,0.3f,true,cactus2_animations);
        loader("Cactus2.RS.idle",4,0.4f,true,cactus2_animations);
        loader("Cactus2.back",4,0.3f,false,cactus2_animations);
        loader("Cactus2.back.idle",2,0.4f,false,cactus2_animations);
        loader("Cactus2.front",2,0.3f,false,cactus2_animations);
        loader("Cactus2.score",6,0.4f,false,cactus2_animations);
        loader("Cactus2.shot",2,0.2f,false,cactus2_animations);
        loader("Cactus2.lose",2,0.2f,false,cactus2_animations);


    }
    public void loadC3(){
        loader("Cactus3.idle",11,0.3f,false,cactus3_animations);
        loader("Cactus3.RS",4,0.3f,false,cactus3_animations);
        loader("Cactus3.RS.idle",4,0.4f,false,cactus3_animations);
        loader("Cactus3.RS",4,0.3f,true,cactus3_animations);
        loader("Cactus3.RS.idle",4,0.4f,true,cactus3_animations);
        loader("Cactus3.back",2,0.4f,false,cactus3_animations);
        loader("Cactus3.back.idle",4,0.3f,false,cactus3_animations);
        loader("Cactus3.front",2,0.3f,false,cactus3_animations);
        loader("Cactus3.score",9,0.4f,false,cactus3_animations);
        loader("Cactus3.shot",2,0.2f,false,cactus3_animations);
        loader("Cactus3.lose",4,0.2f,false,cactus3_animations);

    }
    public void loadC4(){
        loader("Cactus4.idle",9,0.3f,false,cactus4_animations);
        loader("Cactus4.RS",4,0.3f,false,cactus4_animations);
        loader("Cactus4.RS.idle",2,0.4f,false,cactus4_animations);
        loader("Cactus4.RS",4,0.3f,true,cactus4_animations);
        loader("Cactus4.RS.idle",2,0.4f,true,cactus4_animations);
        loader("Cactus4.back",2,0.4f,false,cactus4_animations);
        loader("Cactus4.back.idle",4,0.3f,false,cactus4_animations);
        loader("Cactus4.front",2,0.3f,false,cactus4_animations);
        loader("Cactus4.score",7,0.4f,false,cactus4_animations);
        loader("Cactus4.shot",2,0.2f,false,cactus4_animations);
        loader("Cactus4.lose",3,0.2f,false,cactus4_animations);
    }

}