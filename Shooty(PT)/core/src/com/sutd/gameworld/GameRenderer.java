package com.sutd.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sutd.shootyHelper.AssetLoader;

/**
 * @author PT
 * render all the game objects in the gameworld
 */
public class GameRenderer {
	
	private ShapeRenderer shapeRenderer;
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	
	public GameRenderer(GameWorld world){
		myWorld = world; 
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 140, 115);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        
	}
	
	public void render(){
		 Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 
		 myWorld.getStage().act();
		 myWorld.getStage().draw();
		 
		 batcher.begin();
        // render time and moves
		 AssetLoader.white.draw(batcher, myWorld.getTimeStatus()+Integer.toString(myWorld.getTime()), 110, 0);
		 AssetLoader.white.draw(batcher, myWorld.getOut(), 0, 0);
         AssetLoader.green.draw(batcher, myWorld.getPlayer1().getHealth()+"",0,20);
         AssetLoader.green.draw(batcher,myWorld.getPlayer2().getHealth()+"",0,40);
		 batcher.end();
		 
		 
		 shapeRenderer.begin(ShapeType.Filled);
        // Player1
         shapeRenderer.setColor(0,0,0,1);
		 shapeRenderer.circle(myWorld.getPlayer1().getX(), myWorld.getPlayer1().getY(), 5);
		 shapeRenderer.setColor(0,0,0,1);
		 shapeRenderer.circle(myWorld.getBulletl(myWorld.getPlayer1()).getX(), myWorld.getBulletl(myWorld.getPlayer1()).getY(), 2);
		 shapeRenderer.setColor(0,0,0,1);
		 shapeRenderer.circle(myWorld.getBulletr(myWorld.getPlayer1()).getX(), myWorld.getBulletr(myWorld.getPlayer1()).getY(), 2);
		 shapeRenderer.setColor(0,100,0,1);

        // Player2
        shapeRenderer.circle(myWorld.getPlayer2().getX(), myWorld.getPlayer2().getY(), 5);
        shapeRenderer.setColor(0,100,0,1);
        shapeRenderer.circle(myWorld.getBulletl(myWorld.getPlayer2()).getX(), myWorld.getBulletl(myWorld.getPlayer2()).getY(), 2);
        shapeRenderer.setColor(0,100,0,1);
        shapeRenderer.circle(myWorld.getBulletr(myWorld.getPlayer2()).getX(), myWorld.getBulletr(myWorld.getPlayer2()).getY(), 2);
		 shapeRenderer.end();
		 
	}

}
