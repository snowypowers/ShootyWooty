package com.sutd.object;

import java.util.ArrayList;

import sun.net.www.content.text.plain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MoveDisplay {
	private Label label;
	private Stage stage;
	private LabelStyle labelStyle;
	private String text;
	private Table table;
	private boolean execute;
	private TextureAtlas atlas;
	private Skin skin;
	
	
	private ArrayList<String> moves;
	// in charge of displaying moves
	public MoveDisplay(Stage stage) {
		this.stage = stage;
		text = "Player Input:";
		atlas = new TextureAtlas(Gdx.files.internal("ui/button.pack"));
		skin = new Skin(atlas);
		table = new Table(skin);
		moves = new ArrayList<String>();
		execute=false;
	}
	// add move to a string for display and arraylist for calculation
	public void addMove(String moveInput) {
		text += " " + moveInput;
		Gdx.app.log("MoveDisplay", text);
		moves.add(moveInput);
	}
	// displaying label containing player inputs
	// this can be done in the renderer as well
	public void show() {
		BitmapFont green = new BitmapFont(Gdx.files.internal("font.fnt"), false);
		green.setScale(0.5f, 0.5f);
		labelStyle = new LabelStyle(green, null);
		table.setBounds(120, 70, 20, 20);
		label = new Label(text, labelStyle);
		table.add(label);
		stage.addActor(table);
	}
	// method to constantly update the string containing the moves
	public void render(float delta){
		label.setText(text);
		if(moves.size()==4 && !execute){
			moves=new ArrayList<String>();
			text = "Player Input:";
		}
	}

	public ArrayList<String> getMoves() {
		return moves;
	}
	public void reset(){
		execute=false;
	}
	public boolean isExecute() {
		return execute;
	}
	public void setExecute() {
		execute=true;
	}

}