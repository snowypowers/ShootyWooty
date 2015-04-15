package com.eye7.ShootyWooty.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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



        //Gdx.app.log("GR:", "Width: " + String.valueOf(Gdx.graphics.getWidth()));
        //Gdx.app.log("GR:", "Height: " + String.valueOf(Gdx.graphics.getHeight()));
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        myWorld.getStage().act();
        myWorld.getStage().draw();

        batcher.begin();
        // render time and moves
        //MainLoader.white.draw(batcher, myWorld.getOut() + "     " + myWorld.getTimeStatus()+Integer.toString(myWorld.getTime()), 0, 0);
        //MainLoader.green.draw(batcher, "Player1 blood: "+Integer.toString(GameConstants.PLAYERS.get(1).getHealth()), 0,60);
        //MainLoader.green.draw(batcher, "Player2 blood: "+Integer.toString(GameConstants.PLAYERS.get(2).getHealth()), 0,90);

        if(!stop) {
//             batcher.draw(MainLoader.animation_faucet.getKeyFrame(runTime,true), 292+64,20,64+64,64+64);

//            batcher.draw(CactusLoader.cactus1_animations.get("idle").getKeyFrame(runTime,true), 100, 20, 64, 64);
//            batcher.draw(CactusLoader.cactus1_animations.get("RS").getKeyFrame(runTime,true), 356,20,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("RS.idle").getKeyFrame(runTime,true), 164,84,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("LS").getKeyFrame(runTime,true), 356,84,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("LS.idle").getKeyFrame(runTime,true), 164,20,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("back").getKeyFrame(runTime,true), 100,84,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("back.idle").getKeyFrame(runTime,true), 420,20,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("front").getKeyFrame(runTime,true), 228,84,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("score").getKeyFrame(runTime,true), 228,20,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("shot").getKeyFrame(runTime,true), 292,20,64,64);
//            batcher.draw(CactusLoader.cactus1_animations.get("lose").getKeyFrame(runTime,true), 292,84,64,64);

//            batcher.draw(CactusLoader.cactus2_animations.get("idle").getKeyFrame(runTime,true), 100, 20, 64, 64);
//            batcher.draw(CactusLoader.cactus2_animations.get("RS").getKeyFrame(runTime,true), 356,20,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("RS.idle").getKeyFrame(runTime,true), 164,84,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("LS").getKeyFrame(runTime,true), 356,84,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("LS.idle").getKeyFrame(runTime,true), 164,20,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("back").getKeyFrame(runTime,true), 100,84,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("back.idle").getKeyFrame(runTime,true), 420,20,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("front").getKeyFrame(runTime,true), 228,84,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("score").getKeyFrame(runTime,true), 228,20,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("shot").getKeyFrame(runTime,true), 292,20,64,64);
//            batcher.draw(CactusLoader.cactus2_animations.get("lose").getKeyFrame(runTime,true), 292,84,64,64);

//            batcher.draw(CactusLoader.cactus3_animations.get("idle").getKeyFrame(runTime,true), 100, 20, 64, 64);
//            batcher.draw(CactusLoader.cactus3_animations.get("RS").getKeyFrame(runTime,true), 356,20,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("RS.idle").getKeyFrame(runTime,true), 164,84,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("LS").getKeyFrame(runTime,true), 356,84,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("LS.idle").getKeyFrame(runTime,true), 164,20,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("back").getKeyFrame(runTime,true), 100,84,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("back.idle").getKeyFrame(runTime,true), 420,20,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("front").getKeyFrame(runTime,true), 228,84,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("score").getKeyFrame(runTime,true), 228,20,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("shot").getKeyFrame(runTime,true), 292,20,64,64);
//            batcher.draw(CactusLoader.cactus3_animations.get("lose").getKeyFrame(runTime,true), 292,84,64,64);

//            batcher.draw(CactusLoader.cactus4_animations.get("idle").getKeyFrame(runTime,true), 100, 20, 64, 64);
//            batcher.draw(CactusLoader.cactus4_animations.get("RS").getKeyFrame(runTime,true), 356,20,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("RS.idle").getKeyFrame(runTime,true), 164,84,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("LS").getKeyFrame(runTime,true), 356,84,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("LS.idle").getKeyFrame(runTime,true), 164,20,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("back").getKeyFrame(runTime,true), 100,84,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("back.idle").getKeyFrame(runTime,true), 420,20,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("front").getKeyFrame(runTime,true), 228,84,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("score").getKeyFrame(runTime,true), 228,20,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("shot").getKeyFrame(runTime,true), 292,20,64,64);
//            batcher.draw(CactusLoader.cactus4_animations.get("lose").getKeyFrame(runTime,true), 292,84,64,64);


        }
        //MainLoader.white.setScale(main.scaleX, main.scaleY);
        batcher.end();




    }
    public void dispose(){
        shapeRenderer.dispose();
        batcher.dispose();
        shapeRenderer.dispose();
    }

}

