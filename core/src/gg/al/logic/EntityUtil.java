package gg.al.logic;

import com.artemis.ArchetypeBuilder;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import gg.al.logic.component.*;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class EntityUtil {

    public static final ArchetypeBuilder TESTARCH = new ArchetypeBuilder()
            .add(Render.class)
            .add(Position.class)
            .add(Stats.class)
            .add(KinetmaticPhysic.class)
            .add(Input.class);

    public static int spawnTest(ArcadeWorld aworld, int x, int y, float maxHealth, float maxAbilityPoints, AssetDescriptor<Texture> texture)
    {
        EntityWorld world = aworld.getEntityWorld();
        World physicWorld = aworld.getPhysicsWorld();

        int entity = world.create(world.getArchetype(TESTARCH));
        Position position = world.getMapper(Position.class).get(entity);
        Stats stats = world.getMapper(Stats.class).get(entity);
        Render render = world.getMapper(Render.class).get(entity);
        KinetmaticPhysic kinetmaticPhysic = world.getMapper(KinetmaticPhysic.class).get(entity);

        position.set(aworld.getTile(x,y));
        stats.set(maxHealth, maxAbilityPoints);
        render.set(1,1,true, texture);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position.position);
        Body body = physicWorld.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(.5f);
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();
        body.setUserData(entity);
        kinetmaticPhysic.body = body;
        return entity;
    }
}
