package com.eye7.ShootyWooty;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.CactusLoader;
import com.eye7.ShootyWooty.helper.MainLoader;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.screen.MainScreen;


public class main extends Game {
    SpriteBatch batch;
    Texture img;

    private ActionResolver actionResolver;
    public main(ActionResolver actionResolver){

        this.actionResolver = actionResolver;
    }
    @Override

    public void create () {

        Gdx.app.log("ShootyGame", "created");
        GameConstants.SCALE_X = Gdx.graphics.getWidth() / 960.0f;
        GameConstants.SCALE_Y = Gdx.graphics.getHeight() / 540.0f;
        MainLoader.load();
        setScreen(new MainScreen(this, actionResolver));
        CactusLoader cactusLoader = new CactusLoader(GameConstants.NUM_PLAYERS);
        Gdx.app.log("Screen Reso:", "Width: " + String.valueOf(Gdx.graphics.getWidth()));
        Gdx.app.log("Screen Reso:", "Height: " + String.valueOf(Gdx.graphics.getHeight()));
        Gdx.app.log("ScaleX", String.valueOf(GameConstants.SCALE_X));
        Gdx.app.log("ScaleY", String.valueOf(GameConstants.SCALE_Y));
    }

    public void reCreate(){
        setScreen(new MainScreen(this, actionResolver));
    }
    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        //this.getScreen().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }
    @Override
    public void resize(int width, int height) {
        Gdx.app.log("Resizing: ", "Width: " + String.valueOf(width));
        Gdx.app.log("Resizing: ", "Height: " + String.valueOf(height));
        if (getScreen() != null) getScreen().resize(width, height);
    }

    

}
