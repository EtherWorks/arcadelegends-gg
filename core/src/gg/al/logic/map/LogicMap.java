package gg.al.logic.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import lombok.Getter;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class LogicMap {

    @Getter
    private Tile[][] map;
    @Getter
    private int width;
    @Getter
    private int height;

    public LogicMap(TiledMap map)
    {
        this.width = map.getProperties().get("width", Integer.class);
        this.height = map.getProperties().get("height", Integer.class);
        this.map = new Tile[width][height];
        for (int x = 1;  x <= width; x++)
            for (int y = 1; y <= height; y++)
                this.map[x-1][y-1] = new Tile(x,y);
    }

    public Tile getTile(int x, int y)
    {
        return map[x-1][y-1];
    }
}
