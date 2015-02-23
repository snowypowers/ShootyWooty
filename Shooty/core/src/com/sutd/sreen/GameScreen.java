package com.sutd.sreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sutd.gameworld.GameRenderer;
import com.sutd.gameworld.GameWorld;
import com.sutd.object.Button;
import com.sutd.object.MoveDisplay;
import com.sutd.object.Player;

public class GameScreen implements Screen {
	private GameWorld world;
	private GameRenderer renderer;
	private Stage stage;

	private float startTime;
	private float endTime;
	private Player playerTurn;

	private MoveDisplay moveDisplay;
	
	private Viewport viewport;
	private OrthographicCamera camera;

	public GameScreen() {
		Gdx.app.log("GameScreen", "Attached");

	    stage = new Stage(); // contain buttons and display
		world = new GameWorld(stage); // contains game objects
		renderer = new GameRenderer(world);	// animate game objects

	}

	@Override
	public void render(float delta) {
		// Gdx.app.log("GameScreen", "rendering");
		Gdx.gl.glClearColor(0, 0, 0, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		world.update(delta);
		renderer.render();

		stage.act(delta);
		stage.draw();


	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "resizing");
		stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		world.getButtonF().show();
		world.getButtonFR().show();
		world.getButtonFL().show();
		world.getButtonB().show();
		world.getButtonReset().show();
		world.getMoveDisplay().show();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "pause called");
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "resume called");
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "hide called");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}

}
