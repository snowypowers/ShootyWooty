package com.sutd.sreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sutd.gameworld.GameRenderer;
import com.sutd.gameworld.GameWorld;


/**
 * @author PT
 * receive update from GameWorld and GameRenderer
 * and display accordingly
 */

public class GameScreen implements Screen {
	private GameWorld world;
	private GameRenderer renderer;
	private Stage stage;


	public GameScreen() {
		Gdx.app.log("GameScreen", "Attached");
	    stage = new Stage(); // contain buttons and display
		world = new GameWorld(stage); // contains game objects
		renderer = new GameRenderer(world);	// animate game objects
		
	}

	@Override
	public void render(float delta) {
//		Gdx.app.log("GameScreen",1/Gdx.graphics.getDeltaTime()+"");
		world.update(delta); // GameWorld method that calls for any updates
		renderer.render(); // GameRenderer method that tells the screen what to display
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.log("GameScreen", "resizing");
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
			
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
