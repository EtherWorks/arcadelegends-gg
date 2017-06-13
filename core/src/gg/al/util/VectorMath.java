package gg.al.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Thomas Neumann on 21.04.2017.<br />
 * Simple math functions for {@link Vector2}.
 */
public class VectorMath {
    public static double mag(Vector2 vec) {
        return Math.sqrt(sqrMag(vec));
    }

    public static double sqrMag(Vector2 vec)
    {
        return Math.pow(vec.x, 2) + Math.pow(vec.y, 2);
    }
}
