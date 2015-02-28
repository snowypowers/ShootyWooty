package com.sutd.shootyHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author PT
 * stores all the assets used in the game
 * i.e. fonts, skin, image etc.
 */
public class AssetLoader {
	public static BitmapFont white, green;
	public static TextureAtlas atlas;
	public static Skin skin;
	
	public static void load() {
		white = new BitmapFont(Gdx.files.internal("white.fnt"),true); // white font
		green = new BitmapFont(Gdx.files.internal("font.fnt"),true); // green font
		white.setScale(.3f,.3f); // scale it to size
		green.setScale(.5f, .5f);	
		atlas = new TextureAtlas(Gdx.files.internal("Gbuttons/Gbuttons.pack")); // atlas for skin
		skin = new Skin(atlas); // skin containing drawables
	}

	


}
