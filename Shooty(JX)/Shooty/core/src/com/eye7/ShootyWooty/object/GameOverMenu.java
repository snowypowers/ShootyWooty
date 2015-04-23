package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.helper.MainLoader;
import com.eye7.ShootyWooty.model.GameConstants;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * GameOverMenu displayed when the game ends, replacing InputButtons
 *
 * Hierarchy
 * ActionMenu -> GameOverMenu
 */

public class GameOverMenu extends Table {
    private final ActionResolver actionResolver;
    private Player player;
    private HashMap<String, Integer> achievements;
    private LinkedList<String> pointer;

    private Label header;
    private Label label;
    private Button exitButton;
    private Game game;

    public GameOverMenu(ActionResolver a, final Game game) {
        actionResolver = a;
        this.game = game;
        //Setup Player
        a.setEndGame();
        player = GameConstants.PLAYERS.get(GameConstants.myID+1);
        achievements = player.getAchievments();

        Label.LabelStyle style = new Label.LabelStyle(MainLoader.green, Color.valueOf("452f04"));



        MainLoader.bgMusic.stop();

        //Setup Menu

        label = new Label(null, style);
        label.setAlignment(Align.center, Align.center);

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

        this.row();
        this.add(label).center().padBottom(10);

        for (String s: achievements.keySet()) {
            Label ac = new Label(null, style);
            ac.setText(s + ":" + String.valueOf(achievements.get(s)));
            ac.setFontScale(0.5f);
            this.row();
            this.add(ac).left().padBottom(5);

        }

        Drawable buttonImage = MainLoader.homeButtonImg;
        exitButton = new Button(buttonImage);
        exitButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x,float y) {
                try {
                    Thread exitGame = new Thread(){
                        public void run(){

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Gdx.app.log("GameOVERMENU", "DISPOSE SIGNAL");
                            game.dispose();
                        }
                    };
                    actionResolver.displayAchievements(achievements);
                    if(GameConstants.gameStateFlag.equals("W")){
                        actionResolver.gameDecided("win");
                    }
                    else if(GameConstants.gameStateFlag.equals("D"))
                        actionResolver.gameDecided("draw");
                    else
                        actionResolver.gameDecided("lose");
                    exitGame.start();
                    MainLoader.bgEndGame.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        //Debug
        if (GameConstants.DEBUG) {
            this.debug();
        }
        //Setup Table
        this.setHeight(536);
        this.setWidth(300);
        this.setBackground(MainLoader.exitmenuBG);

        this.row();
        this.add(exitButton).center().padBottom(25);
    }

}
