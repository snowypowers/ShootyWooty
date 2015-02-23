package com.sutd.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameRenderer {
	
	private ShapeRenderer shapeRenderer;
	private GameWorld myWorld;
	private OrthographicCamera cam;
	private SpriteBatch batcher;
	
	public GameRenderer(GameWorld world){
		myWorld = world;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, 136, 204);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
	}
	
	public void render(){

		
		 Gdx.gl.glClearColor(0,0,0,1);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		 
		 // draw player 1
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
		 shapeRenderer.circle(myWorld.getPlayer1().getPosition().x, myWorld.getPlayer1().getPosition().y,5);
		 shapeRenderer.end();
		 
		 // draw player 2
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
		 shapeRenderer.circle(myWorld.getPlayer2().getPosition().x, myWorld.getPlayer2().getPosition().y,5);
		 shapeRenderer.end();
		 
		 // draw the grid
		 drawHLine(20); drawVLine(20);
		 drawHLine(40);	drawVLine(40);
		 drawHLine(60); drawVLine(60);
		 drawHLine(80); drawVLine(80);
		 drawHLine(100); drawVLine(100);
		 drawHLine(120); drawVLine(120);
		 drawHLine(140);
		 drawHLine(160); 
		 
		 //draw bullet
		 shapeRenderer.begin(ShapeType.Filled);
		 shapeRenderer.setColor(87 / 255.0f, 225 / 255.0f, 120 / 255.0f, 1);
		 shapeRenderer.circle(myWorld.getBullet().getPosition().x, myWorld.getBullet().getPosition().y, 3);
		 
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
