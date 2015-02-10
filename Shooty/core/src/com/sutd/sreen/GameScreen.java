package com.sutd.sreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.sutd.gameworld.GameRenderer;
import com.sutd.gameworld.GameWorld;
import com.sutd.object.Button;
import com.sutd.object.Player;

public class GameScreen implements Screen {
	private GameWorld world;
	private GameRenderer renderer;
	
	private Button buttonF;
	private Button buttonFR;
	private Button buttonFL;
	private Button buttonB;
	private Button buttonReset;
	
	private Stage stage;
	
	private float startTime;
	private float endTime;
	private Player playerTurn;
	public GameScreen() {
        Gdx.app.log("GameScreen", "Attached");
		world = new GameWorld();
		renderer = new GameRenderer(world);
		stage = new Stage();
		buttonF = new Button(45,35, world,"F",20,0, stage);
		buttonFR = new Button(85, 35, world, "F&R", 20,20, stage);
		buttonFL = new Button(125, 35, world, "F&L", -20,20, stage);
		buttonB = new Button(165,35,world, "B", -20,0,stage);
		buttonReset = new Button(210, 35, world, "RESET", 0, 0, stage);
		
	}

	@Override
	public void render(float delta) {		
//		Gdx.app.log("GameScreen", "rendering");

		world.update(delta);
		renderer.render();

		stage.act(delta);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		Gdx.app.log("GameScreen", "resizing");
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		buttonF.show();
		buttonFR.show();
		buttonFL.show();
		buttonB.show();
		buttonReset.show();

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
