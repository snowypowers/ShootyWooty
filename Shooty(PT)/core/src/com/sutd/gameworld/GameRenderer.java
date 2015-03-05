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
//		 render time and moves
		 AssetLoader.white.draw(batcher, myWorld.getTimeStatus()+Integer.toString(myWorld.getTime()), 110, 0);
		 AssetLoader.white.draw(batcher, myWorld.getOut(), 0, 0);

		 batcher.end();
		 
		 
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.circle(myWorld.getPlayer1().getX(), myWorld.getPlayer1().getY(), 5);
		 shapeRenderer.setColor(0,0,0,1);
//         Gdx.app.log("GameRenderer","Y:"+myWorld.getBulletl().getY()+" X:"+myWorld.getBulletl().getX());
		 shapeRenderer.circle(myWorld.getBulletl().getX(), myWorld.getBulletl().getY(), 2);
		 shapeRenderer.setColor(0,0,0,1);
		 shapeRenderer.circle(myWorld.getBulletr().getX(), myWorld.getBulletr().getY(), 2);
		 shapeRenderer.setColor(0,0,0,1);
		 shapeRenderer.end();
		 
	}
	public void drawVLine(int x) {
		shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
		 shapeRenderer.line(x, 0, x, 160);
		 shapeRenderer.end();
		
	}
	public void drawHLine(int y){
		shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
		 shapeRenderer.line(0, y, 136, y);
		 shapeRenderer.end();
	}
}
