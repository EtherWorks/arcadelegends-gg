package gg.al.logic.entity;

import com.artemis.ArchetypeBuilder;
import gg.al.logic.component.*;

/**
 * Created by Thomas Neumann on 07.04.2017.<br />
 */
public enum Entity {

    TEST(0, new ArchetypeBuilder()
            .add(Render.class)
            .add(Position.class)
            .add(Stats.class)
            .add(DynamicPhysic.class)
            .add(Input.class)),
    BULLET(1, new ArchetypeBuilder()
            .add(Render.class)
            .add(Position.class)
            .add(DynamicPhysic.class));

    private final int entityId;
    private final ArchetypeBuilder archetype;

    Entity(int entityId, ArchetypeBuilder archetype) {
        this.entityId = entityId;
        this.archetype = archetype;
    }

    public int getEntityId() {
        return entityId;
    }

    public ArchetypeBuilder getArchetype() {
        return archetype;
    }
}
