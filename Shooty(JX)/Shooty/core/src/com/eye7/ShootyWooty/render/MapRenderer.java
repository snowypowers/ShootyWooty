package com.eye7.ShootyWooty.render;

/**
 * Created by JunXiang on 5/3/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

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
        super(map);
        this.spriteBatch = sb;
        sprites = new ArrayList<Sprite>();
        colliderRender = new ShapeRenderer();
        colliderRender.setColor(Color.BLACK);
    }

    public void addSprite(Sprite sprite){
        sprites.add(sprite);
    }

    @Override
    public void render() {
        beginRender();
        colliderRender.begin(ShapeRenderer.ShapeType.Line);

        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer)layer);
                    currentLayer++;
                    if(currentLayer == drawSpritesAfterLayer){
                        //render the rest here
                    }
                } else {
                    for (MapObject object : layer.getObjects()) {
                        colliderRender.rect(object.getProperties().get("x", Float.class),object.getProperties().get("y", Float.class), object.getProperties().get("width", Float.class),object.getProperties().get("height", Float.class));
                        renderObject(object);

                    }
                }
            }
        }
        colliderRender.end();
        endRender();
    }
}
