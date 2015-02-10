package com.sutd.object;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.sutd.gameworld.GameRenderer;
import com.sutd.gameworld.GameWorld;

public class Button {
	
	private int x;
	private int y;

	private boolean isclick;
	
	private GameWorld world;
	private GameRenderer renderer;
	
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private TextButton buttonClick;
	private ImageButton buttonRelease;
	private BitmapFont white, green;
	private Label heading;
	
	private String label;
	private int moveX;
	private int moveY;
	
	public Button(int x, int y, GameWorld world, String label, int moveX, int moveY, Stage stage) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		this.world=world;
		this.label = label;
		this.moveX = moveX;
		this.moveY = moveY;
		this.stage = stage;
		
		isclick=false;
		
//		stage = new Stage();
		atlas = new TextureAtlas(Gdx.files.internal("ui/button.pack"));
		skin = new Skin(atlas);
		table = new Table(skin);	
		
	}
	public int getX() {
		return x;
	}
	public Player getPlayer() {
		return world.getPlayer1();
	}
	public void show(){
		Gdx.app.log("GameScreen", "show called");
	
		white = new BitmapFont(Gdx.files.internal("white.fnt"),false);
		green = new BitmapFont(Gdx.files.internal("font.fnt"),false);
		white.setScale(.3f,.3f);
		green.setScale(.5f, .5f);
		
		
		table.setBounds(x, y, 20, 20);
			
		ImageButtonStyle imageButtonSytle = new ImageButtonStyle();
		imageButtonSytle.up = skin.getDrawable("button.up");
		imageButtonSytle.down = skin.getDrawable("button.down");
		buttonRelease = new ImageButton(imageButtonSytle);
		buttonRelease.pad(15);
		buttonRelease.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.app.log("GameScreen", "button click");
				if(label.contentEquals("RESET")){
					world.getPlayer1().reset();
					world.getPlayer1().getPosition().x=10;
					world.getPlayer1().getPosition().y=10;
					world.getBullet().getPosition().x=10;
					world.getBullet().getPosition().y=10;
					Gdx.app.log("Button", "----");
				}
				else{
				world.getPlayer1().isMovedX(moveX);
				world.getPlayer1().isMovedY(moveY);
				}
			}
		});
		LabelStyle labelStyle = new LabelStyle(green, null);
		heading = new Label(label, labelStyle);
		
//		table.add(buttonClick);
		
		table.add(heading);
		table.row();
		table.add(buttonRelease);		
		stage.addActor(table);
//		table.debug();
		
		
	}
	public Table getTable(){
		return table;
	}
	public void onClick(){
		Gdx.app.log("Button", "click");
		isclick=true;
	}
	public boolean isIsclick() {
		return isclick;
	}
	public void reset() {
		this.isclick = false;
	}

  
}

