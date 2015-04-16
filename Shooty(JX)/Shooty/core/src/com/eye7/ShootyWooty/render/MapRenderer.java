package com.eye7.ShootyWooty.render;

/**
 * MapRenderer. Renders the Map.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.eye7.ShootyWooty.model.GameConstants;

import java.util.ArrayList;
import java.util.List;

public class MapRenderer extends OrthogonalTiledMapRenderer {
    private final String TAG = "MapRenderer";

    private Sprite sprite;
    private SpriteBatch spriteBatch;
    private List<Sprite> sprites;
    private int drawSpritesAfterLayer = 2;
    private ShapeRenderer colliderRender;

    public MapRenderer (TiledMap map, SpriteBatch sb) {
        super(map, sb);
        spriteBatch = sb;
        sprites = new ArrayList<Sprite>();
        colliderRender = new ShapeRenderer();

    }

    public void addSprite(Sprite sprite){
        sprites.add(sprite);
    }

    @Override
    public void render() {
        beginRender();

        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                    currentLayer++;
                    if (currentLayer == drawSpritesAfterLayer) {
                        //render the rest here
                    }
                } else if (layer.getName().contains("Rocks")) { // Renders all rock squares
                    if (GameConstants.DEBUG) {
                        endRender();
                        colliderRender.setColor(Color.BLACK);
                        colliderRender.setProjectionMatrix(spriteBatch.getProjectionMatrix());
                        colliderRender.begin(ShapeRenderer.ShapeType.Line);
                        for (Rectangle r : GameConstants.ROCKS) {
                            colliderRender.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                            //renderObject(object);

                        }
                        colliderRender.end();
                        beginRender();
                    }
                } else if (layer.getName().contains("Water")) { // Renders all water squares
                    if (GameConstants.DEBUG) {
                        endRender();
                        colliderRender.setColor(Color.BLUE);
                        colliderRender.setProjectionMatrix(spriteBatch.getProjectionMatrix());
                        colliderRender.begin(ShapeRenderer.ShapeType.Line);
                        for (Rectangle r: GameConstants.WATER) {
                            colliderRender.rect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
                            //renderObject(object);

                        }
                        colliderRender.end();
                        beginRender();
                    }
                }
            }
        }

        endRender();
    }
    public void dispose(){
        colliderRender.dispose();
    }
}

