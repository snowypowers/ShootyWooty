package com.eye7.ShootyWooty.render;

        import com.badlogic.gdx.ApplicationAdapter;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.InputProcessor;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
        import com.badlogic.gdx.maps.MapLayer;
        import com.badlogic.gdx.maps.MapObjects;
        import com.badlogic.gdx.maps.MapProperties;
        import com.badlogic.gdx.maps.objects.RectangleMapObject;
        import com.badlogic.gdx.maps.tiled.TiledMap;
        import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
        import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
        import com.badlogic.gdx.maps.tiled.TmxMapLoader;
        import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
        import com.badlogic.gdx.math.Vector2;
        import com.badlogic.gdx.scenes.scene2d.Actor;
        import com.eye7.ShootyWooty.model.GameConstants;
        import com.eye7.ShootyWooty.object.Player;
        import com.eye7.ShootyWooty.world.GameMap;

/**
 * Created by JunXiang on 1/3/2015.
 */
public class DisplayMap implements InputProcessor {
    private static final String TAG = "DisplayMap";

    SpriteBatch sb;
    //Map
    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tiledMapRenderer;
    GameMap gameMap;
    private ShapeRenderer shapeRenderer;
    int zoom = 2;
    int posX;
    int posY;

    //Map position
    private double scaleWFactor = Gdx.graphics.getWidth() / 960.0;
    private double scaleHFactor = Gdx.graphics.getHeight() / 540.0;
    private int screenXStart = (int) (20 * scaleWFactor);
    private int screenYStart = (int) (20 * scaleHFactor);
    private int screenWidth = (int) (640 * scaleWFactor);
    private int screenHeight = (int) (360 * scaleHFactor);

    private MapProperties mp;
    private int mapPixelWidth;
    private int mapPixelHeight;

    Player temp;

    public DisplayMap () {
        sb = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 960 / zoom, 540 / zoom);
        camera.update();
        tiledMap = new TmxMapLoader().load("maps/size10map_ui.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, sb);

        gameMap = new GameMap(tiledMap);
        gameMap.setUpPlayers(GameConstants.NUM_PLAYERS);

        mp = tiledMap.getProperties();
        mapPixelWidth = mp.get("tilewidth", Integer.class) * mp.get("width", Integer.class);
        mapPixelHeight = mp.get("tileheight", Integer.class) * mp.get("height", Integer.class);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

    }

    public void render (float delta) {
    //Define the rectangle where the map will be rendered
        Gdx.gl.glViewport(screenXStart, screenYStart, screenWidth, screenHeight);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        gameMap.render(sb, delta);
        //sb.begin();
        //temp.draw(sb);
       // sb.end();

    }

    public void resize(int width, int height) {
        scaleWFactor = width / 960.0;
        scaleHFactor = height / 540.0;
        Gdx.app.log(TAG, "Scaling: " + String.valueOf(scaleWFactor) + " " + String.valueOf(scaleHFactor));
        screenXStart = (int) (20 * scaleWFactor);
        screenYStart = (int) (20 * scaleHFactor);
        screenWidth = (int) (640 * scaleWFactor);
        screenHeight = (int) (360 * scaleHFactor);
        Gdx.app.log(TAG, "Resized to " + String.valueOf(screenWidth) + " by " + String.valueOf(screenHeight));
        camera.setToOrtho(false,screenWidth / zoom, screenHeight / zoom);
        camera.update();
    }

    //Input Processor Methods
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        posX = screenX;
        posY = screenY;
        System.out.println("X:" + String.valueOf(posX) + " Y:" + String.valueOf(posY));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        float x = 0;
        float y = 0;
        if (camera.position.x < (screenWidth / (2*zoom))) {
            x = (screenWidth/(2*zoom)) - camera.position.x;
            Gdx.app.log(TAG, "Reached left edge. " + String.valueOf(camera.position.x) + " " + String.valueOf(x));
        } else if (camera.position.x > (mapPixelWidth - (screenWidth/(2*zoom)))) {
            x = (mapPixelWidth - (screenWidth/(2*zoom))) - camera.position.x;
            Gdx.app.log(TAG, "Reached right edge. " + String.valueOf(camera.position.x) + " " + String.valueOf(x));
        }
        if (camera.position.y < (screenHeight / (2*zoom))) {
            y = (screenHeight/(2*zoom)) - camera.position.y;
            Gdx.app.log(TAG, "Reached btm edge. " + String.valueOf(camera.position.y) + " " + String.valueOf(y));
        } else if (camera.position.y > (mapPixelHeight - (screenHeight/(2*zoom)))) {
            y = (mapPixelHeight - (screenHeight/(2*zoom))) - camera.position.y;
            Gdx.app.log(TAG, "Reached top edge. " + String.valueOf(camera.position.y) + " " + String.valueOf(y));
        }

        camera.translate(x,y);

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (withinScreenX(posX) && withinScreenY(posY)) {
            int newX = posX - screenX;
            int newY = screenY - posY;
            camera.translate(newX / zoom, newY / zoom);
            posX = screenX;
            posY = screenY;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean withinScreenX (int screenX) {
        if (screenX >= screenXStart && screenX <= (screenXStart + screenWidth)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean withinScreenY (int screenY) {
        int sHeight = Gdx.graphics.getHeight();
        if (screenY <= (sHeight - screenYStart) && screenY >= (sHeight - (screenYStart + screenHeight))) {
            return true;
        } else {
            return false;
        }
    }
}

