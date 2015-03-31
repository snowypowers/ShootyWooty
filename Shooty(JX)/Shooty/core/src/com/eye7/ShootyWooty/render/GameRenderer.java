package com.eye7.ShootyWooty.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.eye7.ShootyWooty.helper.MainLoader;
import com.eye7.ShootyWooty.main;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Player;
import com.eye7.ShootyWooty.world.GameWorld;

/**
 * @author PT
 * render all the game objects in the gameworld
 */
public class GameRenderer {

    //UI Render
    private ShapeRenderer shapeRenderer;
    private GameWorld myWorld;
    private OrthographicCamera cam;
    private SpriteBatch batcher;

    int w = Gdx.graphics.getWidth();
    int h = Gdx.graphics.getHeight();

    boolean stop;

    public GameRenderer(GameWorld world){
        myWorld = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 960, 540);
        cam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        stop=false;
    }

    public void render(float runTime){


        Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.app.log("GR:", "Width: " + String.valueOf(Gdx.graphics.getWidth()));
        //Gdx.app.log("GR:", "Height: " + String.valueOf(Gdx.graphics.getHeight()));
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        myWorld.getStage().act();
        myWorld.getStage().draw();

        batcher.begin();
        // render time and moves
        MainLoader.white.draw(batcher, myWorld.getOut() + "     " + myWorld.getTimeStatus()+Integer.toString(myWorld.getTime()), 0, 0);
        MainLoader.green.draw(batcher, "Player1 blood: "+Integer.toString(GameConstants.PLAYERS.get(1).getHealth()), 0,30);
        MainLoader.green.draw(batcher, "Player2 blood: "+Integer.toString(GameConstants.PLAYERS.get(2).getHealth()), 0,60);

//        if(!stop) {
//            batcher.draw(MainLoader.Animation_cactus1_idle.getKeyFrame(runTime,true), 100, 20, 64, 64);
//            batcher.draw(MainLoader.Animation_cactus1_RS.getKeyFrame(runTime,true), 164,84,64,64);
//            batcher.draw(MainLoader.Animation_cactus1_LS.getKeyFrame(runTime,true), 164,20,64,64);
//            batcher.draw(MainLoader.Animation_cactus1_back.getKeyFrame(runTime,true), 100,84,64,64);
//            batcher.draw(MainLoader.Animation_cactus1_front.getKeyFrame(runTime,true), 228,84,64,64);
//            batcher.draw(MainLoader.Animation_cactus1_score.getKeyFrame(runTime,true), 228,20,64,64);
//            batcher.draw(MainLoader.Animation_faucet.getKeyFrame(runTime,true), 292,20,64+64,64+64);
//
//        }
        //MainLoader.white.setScale(main.scaleX, main.scaleY);
        batcher.end();




    }

}

