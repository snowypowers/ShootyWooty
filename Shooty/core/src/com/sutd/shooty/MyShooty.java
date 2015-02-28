package com.sutd.shooty;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sutd.shootyHelper.AssetLoader;
import com.sutd.sreen.GameScreen;

public class MyShooty extends Game {

	@Override
	public void create() {
		Gdx.app.log("ShootyGame", "created");
		AssetLoader.load();
		setScreen(new GameScreen());


	}
	@Override
	public void setScreen(Screen screen) {
	    super.setScreen(screen);
	    this.getScreen().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 5 / 10);


	}
	@Override
	public void resize(int width, int height) {
	    if (getScreen() != null) getScreen().resize(width, height * 5 / 10);
	}

}