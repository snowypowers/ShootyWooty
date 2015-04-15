package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.MainLoader;
import com.eye7.ShootyWooty.model.GameConstants;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Yak Jun Xiang on 11/4/2015.
 */
public class GameOverMenu extends Table {
    private final ActionResolver actionResolver;
    private Player player;
    private HashMap<String, Integer> achievements;
    private LinkedList<String> pointer;
    private Label label;
    private Button exitButton;

    public GameOverMenu(ActionResolver a) {
        actionResolver = a;
        //Setup Player
        player = GameConstants.PLAYERS.get(GameConstants.myID+1);
        achievements = player.getAchievments();
        pointer = new LinkedList<String>();
        for (String s: achievements.keySet()) {
            pointer.add(s);
        }

        //Setup Menu
        Label.LabelStyle style = new Label.LabelStyle(MainLoader.white, Color.WHITE);
        Label header = new Label("Game Over!", style);
        label = new Label(null, style);

        if (GameConstants.gameStateFlag.contains("W")) {
            label.setText("You Win!");
        }
        if (GameConstants.gameStateFlag.contains("L")) {
            label.setText("You Lose!");
        }
        if (GameConstants.gameStateFlag.contains("D")) {
            label.setText("Draw!!!");
        }
        if (GameConstants.gameStateFlag.contains("dead")) {
            label.setText("You died!");
        }
        Drawable buttonImage = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/exitButton.png"))));
        Button.ButtonStyle bstyle = new Button.ButtonStyle(buttonImage, buttonImage, buttonImage);
        exitButton = new Button(bstyle);

        //Setup Table
        if (GameConstants.DEBUG) {
            this.debug();
        }
        this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/menuBG.png")))));
        this.add(header).center();
        this.row();
        this.add(label).center().expand();
        this.row();
        this.add(exitButton).center();

        label.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x,float y) {
                changeText();
            }
        });

        exitButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x,float y) {
                try {
//                    String gameState = GameConstants.gameStateFlag;
                    if(GameConstants.gameStateFlag.equals("W")){
                        actionResolver.gameDecided("win", achievements);
                    }
                    else if(GameConstants.gameStateFlag.equals("D"))
                        actionResolver.gameDecided("draw", achievements);
                    else
                        actionResolver.gameDecided("lose", achievements);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        changeText();
    }

    public void changeText() {
        String output = pointer.poll();
        label.setText(output + "/n/n" + String.valueOf(achievements.get(output)));
        pointer.add(output);
    }
}
