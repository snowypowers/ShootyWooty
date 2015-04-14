package com.eye7.ShootyWooty.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.helper.MainLoader;
import com.eye7.ShootyWooty.model.GameConstants;

public class InputButtons extends Table implements Observer{
    private final String TAG = "InputButtons";

    private ButtonRow row1;
    private ButtonRow row2;
    private ButtonRow row3;
    private ButtonRow row4;

    private ButtonGroup shooters;

    private Table buttons_compact;


    public InputButtons() {
        //Listen in to TurnEnd & TurnStart
        GameConstants.subscribeTurnStart(this);
        GameConstants.subscribeTurnEnd(this);

        //Load in all sprites
        ButtonRow.load();
        buttons_compact = new Table();
        //Create ButtonGroup
        shooters = new ButtonGroup();
        shooters.setMinCheckCount(0);
        shooters.setMaxCheckCount(4);

        //Create buttons
        row1 = new ButtonRow(this);
        row2 = new ButtonRow(this);
        row3 = new ButtonRow(this);
        row4 = new ButtonRow(this);

        //Table properties
//        this.defaults().height(100);
        this.defaults().height(536);
        this.defaults().width(300);
        buttons_compact.defaults().height(100);

        this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("buttons/menuBG.png")))));
            //Add to table

            buttons_compact.add(row1);
            buttons_compact.row();
            buttons_compact.add(row2);
            buttons_compact.row();
            buttons_compact.add(row3);
            buttons_compact.row();
            buttons_compact.add(row4);
            buttons_compact.row();

            this.add(buttons_compact).center();

    }

    private void setLock (boolean lock) {
        row1.setLock(lock);
        row2.setLock(lock);
        row3.setLock(lock);
        row4.setLock(lock);
    }

    private void resetButtons() {
        row1.reset();
        row2.reset();
        row3.reset();
        row4.reset();
    }

    //Observer Methods
    public void observerUpdate(int i) {
        //Turn Start
        if (i == 0) {
            setLock(true);
        }
        //Turn End
        if (i == 1) {
            shooters.setMaxCheckCount(GameConstants.PLAYERS.get(GameConstants.myID + 1).getBulletCount());
            setLock(false);
            resetButtons();
        }
    }

    public int observerType() {
        return 0;
    }


    public String[] getMoves() { // Gets moves from all 4
        return new String[]{row1.getMoves(), row2.getMoves(), row3.getMoves(), row4.getMoves()};
    }

    public void addShooter(Button b) {
        shooters.add(b);
    }

}

class ButtonRow extends Table {
    private static Array<Button.ButtonStyle> mBStyles;
    private static Button.ButtonStyle lBStyle;
    private static Button.ButtonStyle rBStyle;

    private Button mB, lB, rB;


    private char movements[] = { 'B', 'F', 'R', 'D', 'L' };
    private int mBPointer;
    private boolean lock;

    ButtonRow(InputButtons i) {
        if (GameConstants.DEBUG) {
            this.debug();
        }
        lock = false;
        mBPointer = 0;

        lB = new Button(lBStyle);
        rB = new Button(rBStyle);

        i.addShooter(lB);
        i.addShooter(rB);

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
            style.pressedOffsetY = -20;
            style.disabled = skin.getDrawable("buttonBlank.up");
            mBStyles.add(style);
        }

        rBStyle = new Button.ButtonStyle(skin.getDrawable("bulletR"), skin.getDrawable("bulletR"), skin.getDrawable("bulletChosenR"));
        rBStyle.disabled = skin.getDrawable("bulletR");
        rBStyle.pressedOffsetY = 20;
        lBStyle = new Button.ButtonStyle(skin.getDrawable("bullet"), skin.getDrawable("bullet"), skin.getDrawable("bulletChosen"));
        lBStyle.disabled = skin.getDrawable("bullet");
        lBStyle.pressedOffsetY = 20;

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