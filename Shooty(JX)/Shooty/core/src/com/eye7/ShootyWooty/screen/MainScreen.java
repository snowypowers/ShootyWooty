package com.eye7.ShootyWooty.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.eye7.ShootyWooty.world.DisplayMap;
import com.eye7.ShootyWooty.world.GameRenderer;
import com.eye7.ShootyWooty.world.GameWorld;


/**
 * @author PT
 * receive update from GameWorld and GameRenderer
 * and display accordingly
 */
public class MainScreen implements Screen {
    private GameWorld world;
    private GameRenderer renderer;
    private Stage stage;
    private DisplayMap map;
    private InputMultiplexer input;


    public MainScreen() {
        Gdx.app.log("GameScreen", "Attached");
        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new StretchViewport(960, 540)); // contain buttons and display
        world = new GameWorld(stage); // contains game objects
        renderer = new GameRenderer(world);    // animate game objects

        map = new DisplayMap();
    }

    @Override
    public void render(float delta) {

        world.update(delta); // GameWorld method that calls for any updates

        renderer.render(); // GameRenderer method that tells the screen what to display

        map.render();

    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resizing");
        stage.getViewport().update(width,height, true);
    }

    @Override
    public void show() {
        input = new InputMultiplexer();
        input.addProcessor(stage);
        input.addProcessor(map);
        Gdx.input.setInputProcessor(input);

        world.getButton0().show(); // buttons need to be display all the time
        world.getButton1().show(); // they are in the stage and not GameRenderer
        world.getButton2().show();
        world.getButton3().show();


    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause called");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide called");
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}

