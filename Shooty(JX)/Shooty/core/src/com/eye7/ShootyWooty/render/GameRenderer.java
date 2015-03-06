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

    public GameRenderer(GameWorld world){
        myWorld = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 960, 540);
        cam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
    }

    public void render(){


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
        //MainLoader.white.setScale(main.scaleX, main.scaleY);
        batcher.end();




    }
    public void drawVLine(int x) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.line(x, 0, x, 160);
        shapeRenderer.end();

    }
    public void drawHLine(int y){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.line(0, y, 136, y);
        shapeRenderer.end();
    }
}

