package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.eye7.ShootyWooty.helper.ActionResolver;
import com.eye7.ShootyWooty.model.GameConstants;

/**
 * ActionMenu is the sliding menu that contains buttons for players to input their moves. This class controls the main frame which holds the buttons.
 * Buttons are found in InputButtons and GameOverMenu.
 * HourGlass refers to the state of ActionMenu for alpha adjustment.
 *
 * Hierarchy
 * GameWorld -> ActionMenu -> InputButtons
 *                         -> GameOverMenu
 */
public class ActionMenu extends Table implements Observer{
    private final String TAG = "ActionMenu";

    private ActionResolver actionResolver;

    private static float leftEdge = 580;
    private static float rightEdge = 880;

    private Button handle;
    private Drawable handle_img;
    private InputButtons inputButtons;
    private GameOverMenu gameOverMenu;

    private Game game;
    private boolean transitionFlag = false;



    private boolean beingDragged;
    private float drawerSpeed = 10f;

    public ActionMenu(ActionResolver actionResolver, Game game) {
        GameConstants.subscribeTurnEnd(this);
        GameConstants.subscribeGameEnd(this);
        this.actionResolver = actionResolver;
        this.game = game;
        //Setup Name
        this.setName("ActionMenu");
        if (GameConstants.DEBUG) {
            this.debug();
        }



        //Setup buttons
        inputButtons = new InputButtons();
        handle_img = new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/handle.png"))));
        handle = new Button(handle_img, handle_img);
        beingDragged = false;


        handle.addListener( new DragListener() {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log(TAG, "Got it!");
                beingDragged = true;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event,float x, float y, int pointer) {
                float newX = getX() + x;
                if (newX < leftEdge) {
                    newX = leftEdge;
                } else if (newX > rightEdge) {
                    newX = rightEdge;
                }
                setX(newX);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                beingDragged = false;
            }
        });

        //Table Properties
        this.setHeight(540);
        this.setWidth(385);

        //Add buttons to table
        this.add(handle).center();
        this.add(inputButtons);
        this.setPosition(leftEdge, 0); //Right edge of screen


    }

    public String[] getMoves() {
        return inputButtons.getMoves();
    }


    public void act(float delta) {
        if (!beingDragged) {
            float midpoint = (rightEdge - leftEdge) / 2;
            if (getX() < (leftEdge + midpoint) && getX() > leftEdge) {
                setX(getX() - drawerSpeed);
            } else if(getX() > (rightEdge - midpoint) && getX() < rightEdge) {
                setX(getX() + drawerSpeed);
            }
        }
    }

    //Helper method for HourGlass to get the alpha value
    public float getAlpha() {
        float output = (rightEdge - getX()) / (rightEdge - leftEdge);
        if (output < 0.2f) {
            return 0.2f;
        } else if (output > 1) {
            return 1;
        } else {
            return output;
        }
    }

    public void observerUpdate(int i) { // Turn End or Game End
        if (!transitionFlag) { // If menu has not been swapped yet
            if (!GameConstants.gameStateFlag.contains("U")) {
                //this.clearChildren();
                //this.add(handle).center();

                gameOverMenu = new GameOverMenu(actionResolver,game);
                //this.add(gameOverMenu);
                this.invalidate();
                Cell c = this.getCell(inputButtons);                            //Get the cell containing inputButtons
                c.clearActor();                                                 //Remove inputButtons


                c.setActor(gameOverMenu);    //Replace it with GameOverMenu
                transitionFlag = true;
            }
        }


    }

    public int observerType() {
        return 0;
    }



}
