package gg.al.logic.entity;

import com.artemis.ArchetypeBuilder;
import com.artemis.Component;
import gg.al.logic.component.*;

/**
 * Created by Thomas Neumann on 07.04.2017.<br />
 * Enum containing all entity types, their default components, and a id for identifying proposes.
 */
public enum Entities {

    Player(0, RenderComponent.class,
            PositionComponent.class,
            StatComponent.class,
            PhysicComponent.class,
            CharacterComponent.class,
            InventoryComponent.class),
    Bullet(1,
            RenderComponent.class,
            PositionComponent.class,
            PhysicComponent.class,
            BulletComponent.class);

    private final int entityId;
    private final Class<? extends Component>[] components;

    Entities(int entityId, Class<? extends Component>... components) {
        this.entityId = entityId;
        this.components = components;
    }

    public static Entities fromId(int id) {
        for (Entities entity :
                values()) {
            if (entity.getEntityId() == id)
                return entity;
        }
        throw new IllegalArgumentException("No entity with id " + id);
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
