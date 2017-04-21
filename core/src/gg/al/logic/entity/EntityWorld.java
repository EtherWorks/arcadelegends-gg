package gg.al.logic.entity;

import com.artemis.*;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Thomas Neumann on 21.03.2017.<br />
 * Helper class to avoid using the same class name
 * for {@link World} from Artemis and {@link com.badlogic.gdx.physics.box2d.World}
 * from Box2d.<br />
 * Also contains predefined methods for handling {@link Archetype} objects.
 */
public class EntityWorld extends World {


    private final ObjectMap<ArchetypeBuilder, Archetype> archetypeMap;

    public EntityWorld() {
        archetypeMap = new ObjectMap<>();
    }

    public EntityWorld(WorldConfiguration configuration) {
        super(configuration);
        archetypeMap = new ObjectMap<>();
    }

    public Archetype getArchetype(ArchetypeBuilder builder)
    {
        if(!archetypeMap.containsKey(builder))
            archetypeMap.put(builder, builder.build(this));
        return archetypeMap.get(builder);
    }

    public <T extends Component> T getComponentOf(int id, Class<T> type) {
        return super.getMapper(type).get(id);
    }
}
