package gg.al.logic;

import com.artemis.ArchetypeBuilder;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import gg.al.exception.EntityException;
import gg.al.logic.component.*;
import gg.al.logic.map.Tile;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class EntityUtil {

    public static final ArchetypeBuilder TESTARCH = new ArchetypeBuilder()
            .add(Render.class)
            .add(Position.class)
            .add(Stats.class)
            .add(KinematicPhysic.class)
            .add(Input.class);

    public static final ArchetypeBuilder BULLETARCH = new ArchetypeBuilder()
            .add(Render.class)
            .add(Position.class)
            .add(DynamicPhysic.class);

    public static int spawnTest(ArcadeWorld aworld, int x, int y, float maxHealth, float maxAbilityPoints, AssetDescriptor<Texture> texture) {
        EntityWorld entityWorld = aworld.getEntityWorld();
        World physicWorld = aworld.getPhysicsWorld();

        Tile tile = aworld.getTile(x, y);
        if (tile.getEntities().size > 0)
            throw new EntityException("Can´t spawn test at position: other entity present");

        int entity = spawn(entityWorld, TESTARCH);

        Position position = entityWorld.getMapper(Position.class).get(entity);
        Stats stats = entityWorld.getMapper(Stats.class).get(entity);
        Render render = entityWorld.getMapper(Render.class).get(entity);
        KinematicPhysic kinematicPhysic = entityWorld.getMapper(KinematicPhysic.class).get(entity);

        setupPosition(position, entity, aworld, x, y);
        setupStats(stats, maxHealth, maxAbilityPoints);
        setupRender(render, 1, 1, true, texture);
        setupPhysic(kinematicPhysic, physicWorld, entity, x, y);

        return entity;
    }

    public static int spawnBullet(ArcadeWorld arcadeWorld, int x, int y, Vector2 move, AssetDescriptor<Texture> texture) {
        EntityWorld entityWorld = arcadeWorld.getEntityWorld();
        World physicWorld = arcadeWorld.getPhysicsWorld();

        int entity = spawn(entityWorld, BULLETARCH);
        DynamicPhysic dynamicPhysic = entityWorld.getMapper(DynamicPhysic.class).get(entity);
        Position position = entityWorld.getMapper(Position.class).get(entity);
        Render render = entityWorld.getMapper(Render.class).get(entity);

        setupRender(render, 1, 1, true, texture);
        setupPosition(position, entity, arcadeWorld, x, y);
        setupPhysic(dynamicPhysic, physicWorld, entity, x, y);

        Body body = dynamicPhysic.getBody();
        body.applyLinearImpulse(move, body.getPosition(), true);
        return entity;
    }

    public static int spawn(EntityWorld world, ArchetypeBuilder archetype) {
        return world.create(world.getArchetype(archetype));
    }

    public static void setupPosition(Position position, int entityId, ArcadeWorld world, int x, int y) {
        Tile tile = world.getTile(x, y);
        position.set(x, y);
        tile.addEntity(entityId);
        position.set(tile);
    }

    public static void setupStats(Stats stats, float maxHealth, float maxActionPoints) {
        stats.set(maxHealth, maxActionPoints);
    }

    public static void setupRender(Render render, float width, float height, boolean transparent, AssetDescriptor<Texture> texture) {
        render.set(width, height, transparent, texture);
    }

    public static void setupPhysic(Physic physic, World physicWorld, int entityId, int x, int y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = physic.getBodyType();
        bodyDef.position.set(x, y);
        Body body = physicWorld.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(.5f);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();
        body.setUserData(entityId);
        physic.setBody(body);
    }
}
