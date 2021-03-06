package com.eye7.ShootyWooty.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.model.GameConstants;

import java.util.HashMap;

/**
 * Loader for player sprites and sounds. Will only load 2 or 4 player sprites depending on context.
 *
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
    private static final String TAG = "CactusLoader";

    public static Skin cactus1Skin;
    public static Skin cactus2Skin;
    public static Skin cactus3Skin;
    public static Skin cactus4Skin;
    public static Skin deathSkin;

    public static HashMap<String, Animation> cactus1_animations;
    public static HashMap<String, Animation> cactus2_animations;
    public static HashMap<String, Animation> cactus3_animations;
    public static HashMap<String, Animation> cactus4_animations;
    public static Animation death_animation;

    public static Sound sound_walking;
    public static Sound sound_damaged;

    //add these
    public static Sound sound_shoot;
    public static Sound sound_water_collected;
    public static Sound sound_point_scored;
    public static Sound sound_dead;




    public static void load(int NUM_PLAYERS){

        //Sound effects
        sound_walking = Gdx.audio.newSound(Gdx.files.internal("sounds/Cacti/cactiWalking.mp3"));
        sound_damaged = Gdx.audio.newSound(Gdx.files.internal("sounds/Cacti/cactiDamaged.mp3"));
        sound_shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/Cacti/cactiShoot.mp3"));
        sound_water_collected = Gdx.audio.newSound(Gdx.files.internal("sounds/Cacti/cactiWaterCollected.mp3"));
        sound_point_scored = Gdx.audio.newSound(Gdx.files.internal("sounds/Cacti/cactiPointScored.mp3"));
        sound_dead = Gdx.audio.newSound(Gdx.files.internal("sounds/Cacti/cactiDead.mp3"));

        //Animations
        cactus1Skin = new Skin(new TextureAtlas(Gdx.files.internal("players/Cactus1.pack")));
        cactus2Skin = new Skin(new TextureAtlas(Gdx.files.internal("players/Cactus2.pack")));
        deathSkin = new Skin(new TextureAtlas(Gdx.files.internal("players/death_animation.pack")));

        cactus1_animations = new HashMap<String, Animation>();
        cactus2_animations = new HashMap<String, Animation>();


        //Construct death animation
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 11; i++) {
            frames.add(new TextureRegion(deathSkin.getRegion("death_animation" + String.valueOf(i))));
        }

        death_animation = new Animation(0.2f,frames, Animation.PlayMode.NORMAL);

        if(NUM_PLAYERS==2){
            loadC1();
            loadC2();
        }
        else{
            cactus3Skin = new Skin(new TextureAtlas(Gdx.files.internal("players/Cactus3.pack")));
            cactus4Skin = new Skin(new TextureAtlas(Gdx.files.internal("players/Cactus4.pack")));

            cactus3_animations = new HashMap<String, Animation>();
            cactus4_animations = new HashMap<String, Animation>();

            loadC1();
            loadC2();
            loadC3();
            loadC4();
        }

        Gdx.app.log(TAG, "CactiLoader Loaded! " + String.valueOf(NUM_PLAYERS) + " players Loaded!");
    }


    public static void loader(String name, int frameNumber, float speed, HashMap<String,Animation> Cactus){
        String[] output = name.split("\\.",2);
        Skin cactusSkin = null;
        if (output[0].contains("Cactus1")) {
            cactusSkin = cactus1Skin;
        } else if (output[0].contains("Cactus2")) {
            cactusSkin = cactus2Skin;
        } else if (output[0].contains("Cactus3")) {
            cactusSkin = cactus3Skin;
        } else if (output[0].contains("Cactus4")) {
            cactusSkin = cactus4Skin;
        }

        Array<TextureRegion> frames = new Array<TextureRegion>();
            for (int i = 1; i < frameNumber + 1; i++) {
                frames.add(new TextureRegion(cactusSkin.getRegion(name + String.valueOf(i))));
            }

        Cactus.put(output[1], new Animation(speed, frames, Animation.PlayMode.LOOP));
    }
    public static void loadC1(){
        loader("Cactus1.north.moving",4,0.15f,cactus1_animations);
        loader("Cactus1.north.idle",4,0.3f,cactus1_animations);

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

    public static void loadC2(){
        loader("Cactus2.north.moving",4,0.15f,cactus2_animations);
        loader("Cactus2.north.idle",4,0.3f,cactus2_animations);

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
    public static void loadC3(){
        loader("Cactus3.north.moving",4,0.15f,cactus3_animations);
        loader("Cactus3.north.idle",4,0.3f,cactus3_animations);

        loader("Cactus3.south.moving",4,0.15f,cactus3_animations);
        loader("Cactus3.south.idle",4,0.3f,cactus3_animations);

        loader("Cactus3.east.moving",4,0.15f,cactus3_animations);
        loader("Cactus3.east.idle",4,0.3f,cactus3_animations);

        loader("Cactus3.west.moving",4,0.15f,cactus3_animations);
        loader("Cactus3.west.idle",4,0.3f,cactus3_animations);

        loader("Cactus3.score",9,0.3f,cactus3_animations);
        loader("Cactus3.shot",2,0.2f,cactus3_animations);
        loader("Cactus3.lose",4,0.2f,cactus3_animations);
        loader("Cactus3.emote",7,0.4f,cactus3_animations);

    }
    public static void loadC4(){
        loader("Cactus4.north.moving",4,0.15f,cactus4_animations);
        loader("Cactus4.north.idle",4,0.3f,cactus4_animations);

        loader("Cactus4.south.moving",4,0.15f,cactus4_animations);
        loader("Cactus4.south.idle",4,0.3f,cactus4_animations);

        loader("Cactus4.east.moving",4,0.15f,cactus4_animations);
        loader("Cactus4.east.idle",4,0.3f,cactus4_animations);

        loader("Cactus4.west.moving",4,0.15f,cactus4_animations);
        loader("Cactus4.west.idle",4,0.3f,cactus4_animations);

        loader("Cactus4.score",7,0.3f,cactus4_animations);
        loader("Cactus4.shot",2,0.2f,cactus4_animations);
        loader("Cactus4.lose",3,0.2f,cactus4_animations);
        loader("Cactus4.emote",5,0.4f,cactus4_animations);
    }
    public static void dispose(){
            cactus1Skin.dispose();
            cactus2Skin.dispose();

            sound_walking.dispose();
            sound_damaged.dispose();
            sound_shoot.dispose();
            sound_water_collected.dispose();
            sound_point_scored.dispose();
            sound_dead.dispose();
        if(GameConstants.NUM_PLAYERS>2){
            cactus3Skin.dispose();
            cactus4Skin.dispose();
        }

    }
}