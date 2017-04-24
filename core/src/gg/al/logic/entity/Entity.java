package gg.al.logic.entity;

import com.artemis.ArchetypeBuilder;
import com.artemis.Component;
import gg.al.logic.component.*;

/**
 * Created by Thomas Neumann on 07.04.2017.<br />
 */
public enum Entity {

    TEST(0, Render.class,
            Position.class,
            Stats.class,
            DynamicPhysic.class,
            Input.class),
    BULLET(1,
            Render.class,
            Position.class,
            DynamicPhysic.class);

    private final int entityId;
    private final Class<? extends Component>[] components;

    Entity(int entityId, Class<? extends Component>... components) {
        this.entityId = entityId;
        this.components = components;
    }

    public int getEntityId() {
        return entityId;
    }

    public Class<? extends Component>[] getComponents() {
        return components;
    }

    public ArchetypeBuilder getArchetype() {
        ArchetypeBuilder builder = new ArchetypeBuilder();
        for (Class<? extends Component> component :
                components) {
            builder.add(component);
        }
        return builder;
    }
}
