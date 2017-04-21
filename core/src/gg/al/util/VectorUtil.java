package gg.al.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas Neumann on 21.04.2017.<br />
 */
public class VectorUtil {
    public static double magni(Vector2 vec) {
        return Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2));
    }
}
