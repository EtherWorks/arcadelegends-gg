package gg.al.util;

import com.artemis.Component;
import gg.al.logic.component.data.ITemplateable;
import gg.al.logic.component.data.Template;
import gg.al.logic.entity.Entities;
import gg.al.util.GsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Thomas Neumann on 24.03.2017.<br />
 */
public class MainUtil {

//    public static void delete(int id, World physicWorld, com.artemis.World entityWorld) {
//        DynamicPhysic dynamicPhysic = entityWorld.getMapper(DynamicPhysic.class).get(id);
//        KinematicPhysic kinematicPhysic = entityWorld.getMapper(KinematicPhysic.class).get(id);
//        IPhysic physic = dynamicPhysic == null ? kinematicPhysic : dynamicPhysic;
//
//        physicWorld.destroyBody(physic.getBody());
//
//        PositionComponent position = entityWorld.getMapper(PositionComponent.class).get(id);
//        position.tile.removeEntity(id);
//
//        entityWorld.delete(id);
//    }
//
//    public static int spawn(Entities entity, ArcadeWorld arcadeWorld, EntityArguments arguments) {
//        EntityWorld entityWorld = arcadeWorld.getEntityWorld();
//        World physicWorld = arcadeWorld.getPhysicsWorld();
//        int id;
//
//        switch (entity) {
//            case Player:
//                PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
//                Tile tile = arcadeWorld.getTile(pos.x, pos.y);
//                if (tile.getEntities().size > 0)
//                    throw new EntityException("Can´t spawn test at position: other entity present");
//
//                id = entityWorld.create(entityWorld.getArchetype(entity.getArchetype()));
//
//                PositionComponent position = entityWorld.getComponentOf(id, PositionComponent.class);
//                Stats stats = entityWorld.getComponentOf(id, Stats.class);
//                RenderComponent render = entityWorld.getComponentOf(id, RenderComponent.class);
//                DynamicPhysic dynamicPhysic = entityWorld.getComponentOf(id, DynamicPhysic.class);
//                CharacterControlComponent input = entityWorld.getComponentOf(id, CharacterControlComponent.class);
//                Abilities abilities = entityWorld.getComponentOf(id, Abilities.class);
//
//                setup(abilities, arguments);
//                setup(id, stats, arcadeWorld, arguments);
//                setup(id, position, arcadeWorld, arguments);
//                setup(id, render, arcadeWorld, arguments);
//                setup(id, dynamicPhysic, arcadeWorld, arguments);
//                setup(id, input, arcadeWorld, arguments);
//                return id;
//            case Bullet:
//
//                id = entityWorld.create(entityWorld.getArchetype(entity.getArchetype()));
//
//                dynamicPhysic = entityWorld.getComponentOf(id, DynamicPhysic.class);
//                position = entityWorld.getComponentOf(id, PositionComponent.class);
//                render = entityWorld.getComponentOf(id, RenderComponent.class);
//
//                setup(id, position, arcadeWorld, arguments);
//                setup(id, dynamicPhysic, arcadeWorld, arguments);
//                setup(id, render, arcadeWorld, arguments);
//
//                Body body = dynamicPhysic.getBody();
//                body.applyLinearImpulse(arguments.get("move", Vector2.class), body.getPosition(), true);
//                return id;
//            default:
//                throw new EntityException("Unknown entity: " + entity);
//        }
//    }
//
//    private static void setup(int entityId, Stats stats, ArcadeWorld arcadeWorld, EntityArguments arguments) {
//        stats.fromDef(arguments.get("RuntimeStat", Stats.StatsTemplate.class));
//    }
//
//    private static void setup(int entityId, RenderComponent render, ArcadeWorld arcadeWorld, EntityArguments arguments) {
//        render.fromDef(arguments.get("RenderComponent", RenderComponent.RenderTemplate.class));
//    }
//
//    private static void setup(Abilities abilities, EntityArguments arguments) {
//        abilities.fromDef(arguments.get("Abilities", Abilities.AbilitiesTemplate.class));
//    }
//
//    private static void setup(int entityId, CharacterControlComponent input, ArcadeWorld arcadeWorld, EntityArguments arguments) {
//        PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
//        input.move.set(pos.x, pos.y);
//    }
//
//    private static void setup(int entityId, IPhysic physic, ArcadeWorld arcadeWorld, EntityArguments arguments) {
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = physic.getBodyType();
//        PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
//        bodyDef.position.set(pos.x, pos.y);
//        Body body = arcadeWorld.getPhysicsWorld().createBody(bodyDef);
//
//        FixtureDef fixtureDef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(.47f);
//
//        fixtureDef.shape = shape;
//        body.createFixture(fixtureDef);
//
//        shape.dispose();
//        body.setUserData(entityId);
//        physic.setBody(body);
//    }
//
//    private static void setup(int entityId, PositionComponent position, ArcadeWorld arcadeWorld, EntityArguments arguments) {
//        PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
//        Tile tile = arcadeWorld.getTile(pos.x, pos.y);
//        position.fromDef(pos);
//        tile.addEntity(entityId);
//        position.set(tile);
//    }

    public static String getJsonTemplate(Entities entity) {
        Map<String, Template> defs = new HashMap<>();
        for (Class<? extends Component> component :
                entity.getComponents()) {
            if (!ITemplateable.class.isAssignableFrom(component))
                continue;
            try {
                ITemplateable instance = (ITemplateable) component.newInstance();
                Template def = instance.getTemplate();
                defs.put(instance.getClass().getSimpleName(), def);
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            }
        }
        return GsonUtil.getGSON().toJson(defs);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Load template for: ");
        for (Entities entity :
                Entities.values()) {
            System.out.println("\t" + entity.getEntityId() + ": " + entity.name());
        }
        int id = scanner.nextInt();
        Entities entity = Entities.fromId(id);
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
