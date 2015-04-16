package com.eye7.ShootyWooty.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.eye7.ShootyWooty.world.GameWorld;

/**
 * GameRenderer. Renders the UI.
 *
 * Hierarchy
 * MainScreen -> GameRenderer
 */
public class GameRenderer {

    //UI Render
    private ShapeRenderer shapeRenderer;
    private GameWorld myWorld;
    private OrthographicCamera cam;

    public GameRenderer(GameWorld world){
        myWorld = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 960, 540);
        cam.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
    }

    public void render(float runTime){
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        myWorld.getStage().act();
        myWorld.getStage().draw();

    }
    public void dispose(){
        shapeRenderer.dispose();
    }

}

