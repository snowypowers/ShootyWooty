package com.eye7.ShootyWooty.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.eye7.ShootyWooty.model.Direction;
import com.eye7.ShootyWooty.object.Player;

/**
 * Created by JunXiang on 5/3/2015.
 */
public class GameMap {
    private TiledMap map;
    private Array<Ellipse> rocks=  new Array<Ellipse>();
    private MapObjects spawns;

    private Array<Player> players;

    public static int tileWidth = 32;
    public static int tileHeight =32;

    public GameMap(TiledMap tmap) {
        this.map = tmap;
        MapObjects rockslist = map.getLayers().get("Rocks").getObjects();
        for (int i = 0; i < rockslist.getCount(); i++) {
            EllipseMapObject obj = (EllipseMapObject) rockslist.get(i);
            Ellipse ell = obj.getEllipse();
            rocks.add(new Ellipse(ell.x, ell.y, ell.width, ell.height));
        }
        this.spawns = map.getLayers().get("Spawns").getObjects();

    }

    public void setUpPlayers(int num) {
        players = new Array<Player>();
        for (int i = 0; i < num; i++) {
            RectangleMapObject spawn = (RectangleMapObject) spawns.get("Spawn" + String.valueOf(i+1));
            players.add(new Player((int) spawn.getRectangle().x,(int) spawn.getRectangle().y, Integer.parseInt(spawn.getProperties().get("direction").toString())));
            Gdx.app.log("Player Creation", players.get(i).getPosition().toString());
        }
    }

    public void render(SpriteBatch sb) {
        sb.begin();

        for (Player p: players) {

            p.draw(sb);
        }
        sb.end();
    }


}
