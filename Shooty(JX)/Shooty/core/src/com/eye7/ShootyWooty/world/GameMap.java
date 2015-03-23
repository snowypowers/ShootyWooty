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
    private Array<Player> players;
    public static int tileWidth = 32;
    public static int tileHeight =32;
    public GameMap(TiledMap tmap) {
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
        players = new Array<Player>();
        for (int i = 0; i < num; i++) {
            RectangleMapObject spawn = (RectangleMapObject) spawns.get("Spawn" + String.valueOf(i+1));
            players.add(new Player(this, (int) spawn.getRectangle().x,(int) spawn.getRectangle().y, Integer.parseInt(spawn.getProperties().get("direction").toString())));
            Gdx.app.log("Player Creation", players.get(i).getPosition().toString());
        }
        GameConstants.PLAYERS = players;
    }

    public void render(SpriteBatch sb) {
        sb.enableBlending();
        sb.begin();

        for (Player p: players) {
            p.draw(sb);
        }
        sb.end();
    }


}
