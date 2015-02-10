package com.sutd.shooty;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.sutd.sreen.GameScreen;

public class MyShooty extends Game {

	@Override
	public void create() {
		Gdx.app.log("ShootyGame", "created");
		// AssetLoader.load();
		setScreen(new GameScreen());


	}

}