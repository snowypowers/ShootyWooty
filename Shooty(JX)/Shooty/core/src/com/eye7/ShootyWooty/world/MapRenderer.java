package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JunXiang on 2/3/2015.
 */
public class MapRenderer extends OrthogonalTiledMapRenderer {

    private Sprite sprite;
    private List<Sprite> sprites;
    private Sprite player1;
    private Sprite player2;
    private int drawSpritesAfterLayer = 1;

    public MapRenderer (TiledMap map) {
        super(map);
        sprites = new ArrayList<Sprite>();
    }

    public void addPlayer1(Sprite s) {
        player1 = s;
    }

    public void addPlayer2(Sprite s) {
        player2 = s;
    }

    public void render() {
        beginRender();
        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer)layer);
                    currentLayer++;
                    if(currentLayer == drawSpritesAfterLayer){
                    }
                } else {
                    for (MapObject object : layer.getObjects()) {
                        renderObject(object);
                    }
                }
            }
        }
        endRender();
    }
}


