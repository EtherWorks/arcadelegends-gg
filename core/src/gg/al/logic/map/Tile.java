package gg.al.logic.map;

import lombok.Getter;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class Tile {

    @Getter
    private final int x;
    @Getter
    private final int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
