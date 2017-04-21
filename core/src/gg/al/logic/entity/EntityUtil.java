package gg.al.logic.entity;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import gg.al.exception.EntityException;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.*;
import gg.al.logic.map.Tile;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class EntityUtil {

    public static int spawn(Entity entity, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        EntityWorld entityWorld = arcadeWorld.getEntityWorld();
        World physicWorld = arcadeWorld.getPhysicsWorld();
        int id;

        switch (entity) {
            case TEST:

                Tile tile = arcadeWorld.getTile(arguments.get("x", Integer.class), arguments.get("y", Integer.class));
                if (tile.getEntities().size > 0)
                    throw new EntityException("Can´t spawn test at position: other entity present");

                id = entityWorld.create(entityWorld.getArchetype(entity.getArchetype()));

                Position position = entityWorld.getComponentOf(id, Position.class);
                Stats stats = entityWorld.getComponentOf(id, Stats.class);
                Render render = entityWorld.getComponentOf(id, Render.class);
                DynamicPhysic dynamicPhysic = entityWorld.getComponentOf(id, DynamicPhysic.class);
                Input input = entityWorld.getComponentOf(id, Input.class);

                setup(id, stats, arcadeWorld, arguments);
                setup(id, position, arcadeWorld, arguments);
                setup(id, render, arcadeWorld, arguments);
                setup(id, dynamicPhysic, arcadeWorld, arguments);
                setup(id, input, arcadeWorld, arguments);
                return id;
            case BULLET:

                id = entityWorld.create(entityWorld.getArchetype(entity.getArchetype()));

                dynamicPhysic = entityWorld.getComponentOf(id, DynamicPhysic.class);
                position = entityWorld.getComponentOf(id, Position.class);
                render = entityWorld.getComponentOf(id, Render.class);

                setup(id, position, arcadeWorld, arguments);
                setup(id, dynamicPhysic, arcadeWorld, arguments);
                setup(id, render, arcadeWorld, arguments);

                Body body = dynamicPhysic.getBody();
                body.applyLinearImpulse(arguments.get("move", Vector2.class), body.getPosition(), true);
                return id;
            default:
                throw new EntityException("Unknown entity: " + entity);
        }
    }

    private static void setup(int entityId, Stats stats, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        stats.set(arguments.get("maxHealth", Integer.class), arguments.get("maxAP", Integer.class));
    }

    private static void setup(int entityId, Render render, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        render.set(arguments.get("renderWidth", 1f, Float.class),
                arguments.get("renderHeight", 1f, Float.class),
                arguments.get("transparent", true, Boolean.class),
                arguments.get("texture", AssetDescriptor.class));
    }

    private static void setup(int entityId, Input input, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        input.move.set(arguments.get("x", Integer.class), arguments.get("y", Integer.class));
    }

    private static void setup(int entityId, IPhysic physic, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = physic.getBodyType();
        bodyDef.position.set(arguments.get("x", Integer.class), arguments.get("y", Integer.class));
        Body body = arcadeWorld.getPhysicsWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(.5f);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();
        body.setUserData(entityId);
        physic.setBody(body);


    }


    private static void setup(int entityId, Position position, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        Tile tile = arcadeWorld.getTile(arguments.get("x", Integer.class), arguments.get("y", Integer.class));
        position.set(arguments.get("x", Integer.class), arguments.get("y", Integer.class));
        tile.addEntity(entityId);
        position.set(tile);
    }


}
