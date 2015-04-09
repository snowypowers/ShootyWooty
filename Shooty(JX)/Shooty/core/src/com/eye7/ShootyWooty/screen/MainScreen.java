package com.eye7.ShootyWooty.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.render.DisplayMap;
import com.eye7.ShootyWooty.render.GameRenderer;
import com.eye7.ShootyWooty.world.GameWorld;


public class MainScreen implements Screen {
    private GameWorld world;
    private GameRenderer renderer;
    private Stage stage;
    private DisplayMap map;
    private InputMultiplexer input;
    private ActionResolver actionResolver;
    private Game game;

    private float runTime;




    public MainScreen(Game game, ActionResolver actionResolver) {

        this.game = game;
        this.actionResolver = actionResolver;
        Gdx.app.log("GameScreen", "Attached");
        Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if(actionResolver.getMultiplayer()) {
            GameConstants.NUM_PLAYERS = actionResolver.getNumPlayers();
            GameConstants.myID = actionResolver.getMyID();
        }
        else{
            GameConstants.NUM_PLAYERS =2;
            GameConstants.myID = 0;
        }
        stage = new Stage(new StretchViewport(960, 540)); // contain buttons and display
        world = new GameWorld(stage, actionResolver); // contains game objects
        renderer = new GameRenderer(world);    // animate game objects

        map = new DisplayMap();
    }

    @Override
    public void render(float delta) {
        //Background Color
        Gdx.gl.glClearColor(229/255.0f, 214/255.0f, 136/255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render the map
        map.render(delta);

        runTime+=delta;
        world.update(delta); // GameWorld method that calls for any updates

        //Render the stage
        renderer.render(runTime); // GameRenderer method that tells the screen what to display



    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resizing to width " + String.valueOf(width) + " and height " + String.valueOf(height));
        stage.getViewport().update(width,height, true);
        map.resize(width, height);
    }

    @Override
    public void show() {
        input = new InputMultiplexer();
        input.addProcessor(stage);
        input.addProcessor(map);
        Gdx.input.setInputProcessor(input);
/*
        world.getInputButton0().show(); // buttons need to be display all the time
        world.getInputButton1().show(); // they are in the stage and not GameRenderer
        world.getInputButton2().show();
        world.getInputButton3().show();
*/

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



