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
import com.sutd.helpers.MoveHandler;

public class Button {
	
	private int x;
	private int y;

	private boolean isclick;
	
	private GameWorld world;
	
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private Table table;
	private ImageButton buttonRelease;
	private BitmapFont white, green;
	private Label heading;
	
	private String label;
	
	private MoveDisplay moveDisplay;
	
	public Button(int x, int y, GameWorld world, 
			String label,Stage stage, MoveDisplay moveDisplay) {
		this.x = x;
		this.y = y;
		this.world=world;
		this.label = label;
		this.stage = stage;
		this.moveDisplay = moveDisplay;
		
		isclick=false;
		
		atlas = new TextureAtlas(Gdx.files.internal("ui/button.pack"));
		skin = new Skin(atlas);
		table = new Table(skin);	
		
	}

	// display the button
	public void show(){
		Gdx.app.log("GameScreen", "show called");
		// set fonts
		white = new BitmapFont(Gdx.files.internal("white.fnt"),false);
		green = new BitmapFont(Gdx.files.internal("font.fnt"),false);
		white.setScale(.3f,.3f);
		green.setScale(.5f, .5f);		
		// table that holds the button
		table.setBounds(x, y, 20, 20);
		// set button style: image when pressed up and down
		ImageButtonStyle imageButtonSytle = new ImageButtonStyle();
		imageButtonSytle.up = skin.getDrawable("button.up");
		imageButtonSytle.down = skin.getDrawable("button.down");
		// create a button
		buttonRelease = new ImageButton(imageButtonSytle);
		buttonRelease.pad(15);
		// add button listener
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
					if(moveDisplay.getMoves().size()<4) {
						if(moveDisplay.getMoves().size()==3) moveDisplay.setExecute();
						moveDisplay.addMove(label);
					}
				
				}
				
			}
		});
		// create button label
		LabelStyle labelStyle = new LabelStyle(green, null);
		heading = new Label(label, labelStyle);

		// add heading and button to table
		table.add(heading);
		table.row();
		table.add(buttonRelease);
		// add table to stage
		stage.addActor(table);

		
		
	}
	// supporting listener methods
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
	// getters
	public int getX() {
		return x;
	}
	public Table getTable(){
		return table;
	}
	public Player getPlayer() {
		return world.getPlayer1();
	}

  
}

