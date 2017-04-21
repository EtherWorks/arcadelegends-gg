package gg.al.prototype.logic;

import com.artemis.World;
import com.artemis.WorldConfiguration;

/**
 * Created by Thomas Neumann on 21.03.2017.<br />
 * Helper class to avoid using the same class name
 * for {@link World} from Artemis and {@link com.badlogic.gdx.physics.box2d.World}
 * from Box2d
 */
public class EntityWorld extends World {
    public EntityWorld() {
    }

    public EntityWorld(WorldConfiguration configuration) {
        super(configuration);
    }
}
