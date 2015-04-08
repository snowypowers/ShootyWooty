package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.helper.MainLoader;

public class InputButtons extends Table {
    private ButtonRow row1;
    private ButtonRow row2;
    private ButtonRow row3;
    private ButtonRow row4;

    private static ButtonGroup shooters;


    public InputButtons() {

        //Load in all sprites
        ButtonRow.load();

        //Create ButtonGroup
        shooters = new ButtonGroup();
        shooters.setMinCheckCount(0);
        shooters.setMaxCheckCount(8);

        //Create buttons
        row1 = new ButtonRow();
        row2 = new ButtonRow();
        row3 = new ButtonRow();
        row4 = new ButtonRow();

        //Table properties
        this.defaults().height(90);
        //Add to table
        this.add(row1);
        this.row();
        this.add(row2);
        this.row();
        this.add(row3);
        this.row();
        this.add(row4);


    }

    public void setLock (boolean lock) {
        row1.setLock(lock);
        row2.setLock(lock);
        row3.setLock(lock);
        row4.setLock(lock);
    }

    public void reset() {
        row1.reset();
        row2.reset();
        row3.reset();
        row4.reset();
    }

    public String[] getMoves() { // Gets moves from all 4
        return new String[]{row1.getMoves(), row2.getMoves(), row3.getMoves(), row4.getMoves()};
    }

    public static void addShooter(Button b) {
        shooters.add(b);
    }

}

class ButtonRow extends Table {
    private static Array<com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle> mBStyles;
    private static Button.ButtonStyle lBStyle;
    private static Button.ButtonStyle rBStyle;

    private Button mB, lB, rB;


    private char movements[] = { 'B', 'F', 'R', 'D', 'L' };
    private int mBPointer;
    private boolean lock;

    ButtonRow() {
        this.debug();
        lock = false;
        mBPointer = 0;

        lB = new Button(lBStyle);
        rB = new Button(rBStyle);

        InputButtons.addShooter(lB);
        InputButtons.addShooter(rB);

        mB = new Button(mBStyles.get(0));


        mB.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (!lock) { // record input when not locked
                    mBPointer = (mBPointer + 1) % movements.length; // Get the next Style
                    mB.setStyle(mBStyles.get(mBPointer)); // Set the next Style
                }

            }
        });

        lB.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }

        });

        rB.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }

        });

        this.add(lB, mB, rB);
    }

    public static void load() {
        Skin skin = MainLoader.skin;
        mBStyles = new Array<Button.ButtonStyle>();
        String[] dir = new String[]{"Blank", "F", "R", "D", "L"};
        for (String s: dir) {
            Button.ButtonStyle style = new Button.ButtonStyle();
            style.up = skin.getDrawable("button" + s + ".up");
            style.down = skin.getDrawable("button" + s + ".down");
            style.pressedOffsetY = -8;
            style.disabled = skin.getDrawable("buttonBlank.up");
            mBStyles.add(style);
        }

        rBStyle = new Button.ButtonStyle(skin.getDrawable("bulletR"), skin.getDrawable("bulletR"), skin.getDrawable("bulletChosenR"));
        rBStyle.disabled = skin.getDrawable("bulletR");
        rBStyle.pressedOffsetY = -8;
        lBStyle = new Button.ButtonStyle(skin.getDrawable("bullet"), skin.getDrawable("bullet"), skin.getDrawable("bulletChosen"));
        lBStyle.disabled = skin.getDrawable("bullet");
        lBStyle.pressedOffsetY = -8;

    }

    public String getMoves() {
        return ((lB.isChecked())?"1":"0") + movements[mBPointer] + ((rB.isChecked())?"1":"0");
    }

    public void setLock(boolean lock) {
        this.lock = lock;
        lB.setDisabled(lock);
        rB.setDisabled(lock);
        mB.setDisabled(lock);
    }

    public void reset() {
        mBPointer = 0;
        mB.setStyle(mBStyles.get(mBPointer));
        lB.setChecked(false);
        rB.setChecked(false);
    }

}