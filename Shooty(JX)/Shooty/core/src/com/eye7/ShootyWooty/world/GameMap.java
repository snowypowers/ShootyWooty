package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.model.GameConstants;
import com.eye7.ShootyWooty.object.Player;

public class GameMap {
    private TiledMap map;
    private static Array<Rectangle> rocks=  new Array<Rectangle>();
    private MapObjects spawns;
    public static int tileWidth = 64;
    public static int tileHeight =64;
    public GameMap(TiledMap tmap) {
        GameConstants.PLAYERS.clear(); // Resets the PlayerList
        this.map = tmap;
        MapObjects rockslist = map.getLayers().get("Rocks").getObjects();
        for (int i = 0; i < rockslist.getCount(); i++) {
            RectangleMapObject obj = (RectangleMapObject) rockslist.get(i);
            Rectangle rect= obj.getRectangle();
            rocks.add(new Rectangle(rect.x, rect.y, rect.width, rect.height));
        }
        this.spawns = map.getLayers().get("Spawns").getObjects();

    }
    public static Array<Rectangle> getRocks(){
        return rocks;
    }
    public void setUpPlayers(int num) {
        for (int i = 0; i < num; i++) {
            RectangleMapObject spawn = (RectangleMapObject) spawns.get("Spawn" + String.valueOf(i+1));
            Player p = new Player(this, (int) spawn.getRectangle().x,(int) spawn.getRectangle().y, Integer.parseInt(spawn.getProperties().get("direction").toString()));
            GameConstants.PLAYERS.put(p.getPlayerID(), p);
            Gdx.app.log("Player Creation", String.valueOf(p.getPlayerID()));
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
