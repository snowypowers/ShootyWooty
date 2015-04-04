package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Player;

public class GameMap {
    private final String TAG = "GameMap";
    private TiledMap map;
    private static Array<Rectangle> rocks=  new Array<Rectangle>();
    private static Array<Rectangle> water=  new Array<Rectangle>();
    private MapObjects spawns;

    public GameMap(TiledMap tmap) {
        GameConstants.PLAYERS.clear(); // Resets the PlayerList
        this.map = tmap;
        //Get rocks
        MapObjects rockslist = map.getLayers().get("Rocks").getObjects();
        for (int i = 0; i < rockslist.getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) rockslist.get(i);
            Rectangle rect= obj.getRectangle();
            rocks.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
        }
        //Add rocks to border of map
        for (int i = 0; i < GameConstants.MAP_WIDTH / 64;i++) {
            //Northern Edge
            rocks.add(new Rectangle(i*64, -GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE));
            //Southern Edge
            rocks.add(new Rectangle(i*64, GameConstants.MAP_HEIGHT, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE));
        }
        for (int i = 0; i < GameConstants.MAP_HEIGHT / 64;i++) {
            //Western Edge
            rocks.add(new Rectangle(-GameConstants.TILE_SIZE, i*64, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE));
            //Eastern Edge
            rocks.add(new Rectangle( GameConstants.MAP_WIDTH, i*64, GameConstants.TILE_SIZE, GameConstants.TILE_SIZE));
        }
        GameConstants.ROCKS = rocks;

        //Get water
        MapObjects waterlist = map.getLayers().get("Water").getObjects();
        for (int i = 0; i < waterlist.getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) rockslist.get(i);
            Rectangle rect= obj.getRectangle();
            water.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
        }
        GameConstants.WATER = water;

        //Get spawns
        this.spawns = map.getLayers().get("Spawns").getObjects();

    }
    public static Array<Rectangle> getRocks(){
        return rocks;
    }
    public void setUpPlayers(int num) {
        for (int i = 0; i < num; i++) {
            EllipseMapObject spawnpoint = (EllipseMapObject) spawns.get("Spawn" + String.valueOf(i+1));
            CircleMapObject spawn = new CircleMapObject(spawnpoint.getEllipse().x+32, spawnpoint.getEllipse().y+32, 20);
            Player p = new Player(this, spawn, Integer.parseInt(spawnpoint.getProperties().get("direction").toString()));
            GameConstants.PLAYERS.put(p.getPlayerID(), p);
            Gdx.app.log(TAG, "Created Player: " + String.valueOf(p.getPlayerID()));
        }

    }

    public void render(SpriteBatch sb, float delta) {
        sb.enableBlending();
        sb.begin();

        for (Player p: GameConstants.PLAYERS.values()) {
            p.draw(sb, delta);
        }
        sb.end();
    }


}
