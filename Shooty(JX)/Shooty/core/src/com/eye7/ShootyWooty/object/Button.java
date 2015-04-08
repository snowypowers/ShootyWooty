package com.eye7.ShootyWooty.object;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.eye7.ShootyWooty.helper.MainLoader;

/**
 * @author PT
 * contains 3 buttons, bullet-move-bullet
 * listen to user input and change the command
 * string accordingly
 */
public class Button {
    private final String TAG = "Button";

    private int x;
    private int y;

    private Stage stage;
    private Skin skin;
    private Table table;
    private ImageButton moveButton, bulletButtonL,bulletButtonR;

    private ArrayList<Drawable> buttonUP,buttonDOWN,bulletR,bulletL;

    private ChangeListener moveListen, bulletLListen, bulletRListen;


    String moves;
    private char movements[] = { 'B', 'F', 'R', 'D', 'L' };

    ImageButtonStyle imageButtonSytle,imageButtonSytle2,imageButtonSytle3;
    private boolean lock;

    private int nMove;
    private int nbulletL;
    private int nbulletR;

    public Button(int x, int y, Stage stage) {
        this.x = x; // x coordinate to set table location
        this.y = y; // y coordinate to set table location
        this.stage = stage;
        this.moves = "0B0"; // default move
        lock = false; // lock for when moves are being execute

        skin = MainLoader.skin;
        table = new Table(skin);

        buttonUP = new ArrayList<Drawable>(); // array containing drawables for buttonMove when not pressed
        buttonDOWN = new ArrayList<Drawable>(); // array containing drawables for buttonMove when pressed
        bulletL = new ArrayList<Drawable>(); // array containing drawables for bulletButton
        bulletR = new ArrayList<Drawable>();
        imageButtonSytle = new ImageButtonStyle(); // imageButtonSytle for buttonMove
        imageButtonSytle2 = new ImageButtonStyle(); // imageButtonSytle for bulletButtonR
        imageButtonSytle3 = new ImageButtonStyle(); // imageButtonSytle for bulletButtonF

        nMove = 1; // buttonMove control
        nbulletL = 0; // bulletButtonL control
        nbulletR = 0; // bulletButtonR control
    }


    public void show() {
        Gdx.app.log(TAG, "show called");

        table.setBounds(x, y, 20, 20); // table that holds the button

        buttonInitializer(); // initialize button styles
        bulletInitializer();

        moveButton = new ImageButton(imageButtonSytle); // create a buttons
        moveButton.pad(0);
        bulletButtonL = new ImageButton(imageButtonSytle2);
        bulletButtonL.pad(0);
        bulletButtonR = new ImageButton(imageButtonSytle3);
        bulletButtonR.pad(0);

        setButtonstyle(); // add button styles to array
        setBulletStyleL();
        setBulletStyleR();


        // Buttons listener
        moveButton.addListener( moveListen = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!lock) { // record input when not locked
                    imageButtonSytle.up = buttonUP.get(nMove);
                    imageButtonSytle.down = buttonDOWN.get(nMove);
//                    Gdx.app.log(TAG, moves.substring(0, 1));
                    moves = moves.substring(0, 1) + movements[nMove] + moves.substring(2, 3);
//                    Gdx.app.log(TAG, moves + " after");
                    if (nMove == buttonUP.size() - 1) {
                        nMove = 0;
                    } else {
                        nMove += 1;
                    }
                }

            }
        });
        bulletButtonL.addListener(bulletLListen = new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!lock) {
                    imageButtonSytle2.up = bulletL.get(nbulletL);
                    imageButtonSytle2.down = bulletL.get(nbulletL);

//                    Gdx.app.log(TAG, moves);
                    if (nbulletL == 1) {
                        moves = "0" + moves.substring(1,3);
                        nbulletL = 0;
                    } else {
                        moves = "1" + moves.substring(1,3);
                        nbulletL += 1;
                    }
                }
            }

        });
        bulletButtonR.addListener(bulletRListen = new ChangeListener(){

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!lock) {
                    imageButtonSytle3.up = bulletR.get(nbulletR);
                    imageButtonSytle3.down = bulletR.get(nbulletR);
                    if (nbulletR == 1) {
                        moves = moves.substring(0, 2) + "0";
                        nbulletR = 0;
//                        Gdx.app.log(TAG, moves);
                    } else {
                        moves = moves.substring(0, 2) + "1";
                        nbulletR += 1;
//                        Gdx.app.log(TAG, moves);
                    }
                }
            }

        });

        table.row();
        table.pad(100);
        table.add(bulletButtonL);
        table.add(moveButton);
        table.add(bulletButtonR);
        stage.addActor(table);
    }

    public void remove() {
        stage.removeListener(moveListen);
        stage.removeListener(bulletLListen);
        stage.removeListener(bulletRListen);
        table.remove();
    }
    // buttons methods
    public void buttonInitializer(){
        imageButtonSytle.up = skin.getDrawable("buttonBlank.up");
        imageButtonSytle.down = skin.getDrawable("buttonBlank.down");
        imageButtonSytle.up.setMinWidth(20);
    }
    public void bulletInitializer() {
        imageButtonSytle2.up = MainLoader.skin.getDrawable("bullet");
        imageButtonSytle2.down = MainLoader.skin.getDrawable("bullet");
        imageButtonSytle3.up = MainLoader.skin.getDrawable("bulletR");
        imageButtonSytle3.down = MainLoader.skin.getDrawable("bulletR");
    }
    // bullets methods
    public void setBulletStyleL(){
        bulletL.add(skin.getDrawable("bulletChosen"));
        bulletL.add(skin.getDrawable("bullet"));

    }
    public void setBulletStyleR(){
        bulletR.add(skin.getDrawable("bulletChosenR"));
        bulletR.add(skin.getDrawable("bulletR"));

    }

    public void setButtonstyle(){
        buttonUP.add(skin.getDrawable("buttonBlank.up"));
        buttonDOWN.add(skin.getDrawable("buttonBlank.down"));
        buttonUP.add(skin.getDrawable("buttonF.up"));
        buttonDOWN.add(skin.getDrawable("buttonF.down"));
        buttonUP.add(skin.getDrawable("buttonR.up"));
        buttonDOWN.add(skin.getDrawable("buttonR.down"));
        buttonUP.add(skin.getDrawable("buttonD.up"));
        buttonDOWN.add(skin.getDrawable("buttonD.down"));
        buttonUP.add(skin.getDrawable("buttonL.up"));
        buttonDOWN.add(skin.getDrawable("buttonL.down"));

    }

    public void resetButton() {
        imageButtonSytle.up = skin.getDrawable("buttonBlank.up");
        imageButtonSytle.down = skin.getDrawable("buttonBlank.down");
        imageButtonSytle2.up = MainLoader.skin.getDrawable("bullet");
        imageButtonSytle2.down = MainLoader.skin.getDrawable("bullet");
        imageButtonSytle3.up = MainLoader.skin.getDrawable("bulletR");
        imageButtonSytle3.down = MainLoader.skin.getDrawable("bulletR");

    }

    public void resetMoves() {
        moves = "0B0";
        nbulletL =0;
        nbulletR =0;
        nMove =1;
    }

    // getters
    public int getX() {
        return x;
    }

    public Table getTable() {
        return table;
    }


    public String getMoves() {
        return moves;

    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void onClick() {
        // TODO Auto-generated method stub
        Gdx.app.log(TAG, "click");
    }


}
