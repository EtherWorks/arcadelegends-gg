package gg.al.logic.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
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

    public LogicMap(TiledMap map) {
        this.width = map.getProperties().get("width", Integer.class);
        this.height = map.getProperties().get("height", Integer.class);
        this.map = new Tile[width][height];

        TiledMapTileLayer bottom = (TiledMapTileLayer) map.getLayers().get(0);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                this.map[x][y] = Tile.fromCell(bottom.getCell(x, y), x, y);
            }
    }

    public Tile getTile(int x, int y) {
        return map[x][y];
    }

    public Tile getTile(Vector2 pos) {
        return map[(int) pos.x][(int) pos.y];
    }
}
