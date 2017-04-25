package gg.al.logic.entity;

import com.artemis.Component;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.al.exception.EntityException;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.*;
import gg.al.logic.map.Tile;

import java.io.*;
import java.util.*;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class EntityUtil {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static int spawn(Entity entity, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        EntityWorld entityWorld = arcadeWorld.getEntityWorld();
        World physicWorld = arcadeWorld.getPhysicsWorld();
        int id;

        switch (entity) {
            case Test:
                Map<String, Object> pos = arguments.get("Position", Map.class);
                Tile tile = arcadeWorld.getTile((int)(double)pos.get("x"),(int)(double)pos.get("y"));
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
            case Bullet:

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
        stats.set(arguments);
    }

    private static void setup(int entityId, Render render, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        render.set(arguments);
        render.texture = arguments.get("texture", AssetDescriptor.class);
    }

    private static void setup(int entityId, Input input, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        Map<String, Object> pos = arguments.get("Position", Map.class);
        input.move.set((int)(double)pos.get("x"), (int)(double)pos.get("y"));
    }

    private static void setup(int entityId, IPhysic physic, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = physic.getBodyType();
        bodyDef.position.set((int)(double)arguments.get("Position", Map.class).get("x"), (int)(double)arguments.get("Position", Map.class).get("y"));
        Body body = arcadeWorld.getPhysicsWorld().createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(.47f);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);

        shape.dispose();
        body.setUserData(entityId);
        physic.setBody(body);
    }

    private static void setup(int entityId, Position position, ArcadeWorld arcadeWorld, EntityArguments arguments) {
        Tile tile = arcadeWorld.getTile((int)(double)arguments.get("Position", Map.class).get("x"), (int)(double)arguments.get("Position", Map.class).get("y"));
        position.set((int)(double)arguments.get("Position", Map.class).get("x"), (int)(double)arguments.get("Position", Map.class).get("y"));
        tile.addEntity(entityId);
        position.set(tile);
    }

    public static String getJsonTemplate(Entity entity) {
        Map<String, IComponentDef> defs = new HashMap<>();
        for (Class<? extends Component> component :
                entity.getComponents()) {
            if (!IDefComponent.class.isAssignableFrom(component))
                continue;
            try {
                IDefComponent instance = (IDefComponent) component.newInstance();
                IComponentDef def = instance.getDefaultDef();
                defs.put(instance.getClass().getSimpleName(), def);
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            }
        }
        return GSON.toJson(defs);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Load template for: ");
        for (Entity entity :
                Entity.values()) {
            System.out.println("\t" + entity.getEntityId() + ": " + entity.name());
        }
        int id = scanner.nextInt();
        Entity entity = Entity.fromId(id);
        File out = new File(System.getProperty("user.dir") + File.separator + entity.name().toLowerCase() + ".json");
        String json = getJsonTemplate(entity);
        try {
            FileWriter writer = new FileWriter(out);
            writer.write(json);
            writer.close();
            System.out.println(out.getName() + " written successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
