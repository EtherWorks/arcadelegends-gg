package gg.al.logic.map;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.IntSet;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Thomas Neumann on 30.03.2017.<br>
 * Class describing exatly one tile of a {@link LogicMap}.
 */
public class Tile {

    @Getter
    private final int x;
    @Getter
    private final int y;

    @Getter
    @Setter
    private boolean traversable;

    @Getter
    private IntSet entities;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        traversable = true;
        entities = new IntSet();
    }

    /**
     * Creates a {@link Tile} from a {@link com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell}.
     *
     * @param cell the cell from which to read from
     * @param x    coordinate
     * @param y    coordinate
     * @return the created Tile
     */
    public static Tile fromCell(TiledMapTileLayer.Cell cell, int x, int y) {
        return fromTileMapTile(cell.getTile(), x, y);
    }

    /**
     * Creates a {@link Tile} from a {@link TiledMapTile}.
     *
     * @param tiledMapTile the tiledMapTile from which to read from
     * @param x            coordinate
     * @param y            coordinate
     * @return the created Tile
     */
    public static Tile fromTileMapTile(TiledMapTile tiledMapTile, int x, int y) {
        MapProperties mapProperties = tiledMapTile.getProperties();

        Tile tile = new Tile(x, y);

        if (mapProperties.get("untraversable") != null)
            tile.setTraversable(mapProperties.get("untraversable", Boolean.class));

        return tile;
    }

    public void addEntity(int entityId) {
        entities.add(entityId);
    }

    public void removeEntity(int entityId) {
        entities.remove(entityId);
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", e=" + entities +
                '}';
    }
}
