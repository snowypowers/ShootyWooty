package com.eye7.ShootyWooty.object;

        import sun.net.www.content.text.plain;

        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.math.Circle;
        import com.badlogic.gdx.math.Vector2;

public class Player {

    private int x;
    private int y;
    private boolean isMoveX = false;
    private boolean isMoveY = false;
    private int moveX;
    private int moveY;
    private Vector2 position;
    private Vector2 velocity;
    // takes in x,y as origin
    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
    }
    // constantly update player position
    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

    }
    // this method is called when button is pressed
    // button passes the amount the player should move
    // and set movement to true
    public void isMovedX(int moveX) {
        this.moveX = moveX;
        if (moveX != 0)isMoveX = true;
    }

    public void isMovedY(int moveY) {
        this.moveY = moveY;
        if (moveY != 0) {
            isMoveY = true;
        }
    }
    // setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // getters
    public Vector2 getPosition() {
        return position;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getIsMoveX() {
//		if(isMoveX)	velocity.set(1, 0);
        return isMoveX;
    }

    public boolean getIsMoveY() {
//		if(isMoveY) velocity.set(0, 1);
        return isMoveY;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    // reset methods
    public void resetX(){
        isMoveX = false;
        velocity.set(0, 0);
    }
    public void resetY() {
        isMoveY = false;
    }
    public void reset() {
        moveX = 0;
        moveY = 0;
        velocity.set(0, 0);
    }




}

