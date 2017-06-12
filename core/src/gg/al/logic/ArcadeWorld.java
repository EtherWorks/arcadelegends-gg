package gg.al.logic;

import com.artemis.Component;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.character.Ezreal;
import gg.al.character.Ghost;
import gg.al.character.Kevin;
import gg.al.character.SuperGhost;
import gg.al.game.AL;
import gg.al.graphics.CameraLayerGroupStrategy;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Item;
import gg.al.logic.component.data.Template;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.entity.EntityWorld;
import gg.al.logic.map.LogicMap;
import gg.al.logic.map.Tile;
import gg.al.logic.system.*;
import gg.al.util.Assets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
@Slf4j
public class ArcadeWorld implements Disposable {
    @Getter
    private final Map<String, EntityArguments> loadedEntityArguments;
    @Getter
    private World physicsWorld;
    private Box2DDebugRenderer debugPhysicrender;
    @Getter
    private EntityWorld entityWorld;
    @Getter
    private TiledMap tiledMap;
    @Getter
    private int mapWidth, mapHeight, mapTileHeight, mapTileWidth;
    @Getter
    private float worldViewRotation;
    @Getter
    @Setter
    private float delta;
    private float deltaAccumulator;
    private FrameBuffer mapBuffer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Decal mapDecal;
    private DecalBatch decalBatch;
    private RenderSystem renderSystem;
    @Getter
    private Plane mapHitbox;
    @Getter
    @Setter
    private Camera cam;
    @Getter
    private float step = 1.0f / 60;
    @Getter
    private LogicMap logicMap;
    @Getter
    @Setter
    private boolean debug = false;

    @Getter
    private Assets.LevelAssets levelAssets;

    public ArcadeWorld(TiledMap map, float worldViewRotation, Camera cam, Assets.LevelAssets assets) {
        this.levelAssets = assets;
        this.tiledMap = map;
        this.worldViewRotation = worldViewRotation;
        this.cam = cam;
        this.loadedEntityArguments = new HashMap<>();
        this.mapWidth = map.getProperties().get("width", Integer.class);
        this.mapHeight = map.getProperties().get("height", Integer.class);
        this.mapTileWidth = map.getProperties().get("tilewidth", Integer.class);
        this.mapTileHeight = map.getProperties().get("tileheight", Integer.class);
        logicMap = new LogicMap(map);
        OrthographicCamera mapCam = new OrthographicCamera();
        Viewport viewportMap = new FitViewport(mapWidth * mapTileWidth, mapHeight * mapTileHeight, mapCam);
        viewportMap.update(viewportMap.getScreenWidth(), viewportMap.getScreenHeight(), true);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(mapCam);

        mapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, mapWidth * mapTileWidth, mapHeight * mapTileHeight, false);
        mapBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        TextureRegion mapTemp = new TextureRegion(mapBuffer.getColorBufferTexture());
        mapTemp.flip(false, true);

        mapDecal = Decal.newDecal(mapWidth, mapHeight, mapTemp);
        mapDecal.setPosition(-.5f + mapWidth / 2, -.5f + mapHeight / 2, 0);

        decalBatch = new DecalBatch(new CameraLayerGroupStrategy(cam));

        mapHitbox = new Plane(Vector3.Z, Vector3.Zero);

        physicsWorld = new World(Vector2.Zero, true);

        PolygonShape shapeSide = new PolygonShape();
        shapeSide.set(new Vector2[]{
                new Vector2(0, mapHeight + 2),
                new Vector2(0, 0),
                new Vector2(1, 1),
                new Vector2(1, mapHeight + 1)
        });
        PolygonShape shapeBot = new PolygonShape();
        shapeBot.set(new Vector2[]{
                new Vector2(mapWidth + 2, 0),
                new Vector2(0, 0),
                new Vector2(1, 1),
                new Vector2(mapWidth + 1, 1)
        });


        FixtureDef fixtureDef = new FixtureDef();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        fixtureDef.shape = shapeBot;

        Body body = physicsWorld.createBody(bodyDef);
        body.setTransform(-1.5f, -1.5f, body.getAngle());
        Fixture fixture = body.createFixture(fixtureDef);

        body = physicsWorld.createBody(bodyDef);
        body.setTransform(-1.5f, -1.5f, body.getAngle());
        fixtureDef.shape = shapeSide;
        fixture = body.createFixture(fixtureDef);

        bodyDef.angle = (float) Math.toRadians(180);
        body = physicsWorld.createBody(bodyDef);
        body.setTransform(mapWidth + 0.5f, mapHeight + 0.5f, body.getAngle());
        fixtureDef.shape = shapeSide;
        fixture = body.createFixture(fixtureDef);

        body = physicsWorld.createBody(bodyDef);
        body.setTransform(mapWidth + 0.5f, mapHeight + 0.5f, body.getAngle());
        fixtureDef.shape = shapeBot;
        fixture = body.createFixture(fixtureDef);

//        body = physicsWorld.createBody(bodyDef);
//        body.setTransform(-1.5f, -1.5f, body.getAngle());
//        fixtureDef.shape = shapeSide;
//        fixture = body.createFixture(fixtureDef);

        shapeBot.dispose();
        shapeSide.dispose();

        debugPhysicrender = new Box2DDebugRenderer();
        WorldConfiguration worldConfiguration = new WorldConfigurationBuilder()
                .with(1,
                        new StatSystem(this),
                        new AISystem(),
                        new CharacterSystem(physicsWorld),
                        new RegenSystem(.5f)
                )
                .with(0,
                        new PhysicPositionSystem(),
                        new PositionTileSystem(logicMap),
                        new BulletSystem(this),
                        renderSystem = new RenderSystem(decalBatch, AL.getAssetManager(), worldViewRotation, levelAssets)
                )
                .build();
        entityWorld = new EntityWorld(worldConfiguration);

        physicsWorld.setContactListener(new ContactListener() {

            private void resetPos(int entity, Fixture fixture) {
                CharacterComponent input = entityWorld.getMapper(CharacterComponent.class).get(entity);
                PositionComponent position = entityWorld.getMapper(PositionComponent.class).get(entity);
                fixture.getBody().setLinearVelocity(Vector2.Zero);
                position.resetPos = true;
                input.move.set(position.position);
                RenderComponent renderComponent = entityWorld.getMapper(RenderComponent.class).get(entity);
                renderComponent.setRenderState(CharacterRenderer.PlayerRenderState.IDLE);
            }

            private void handleOne(Object one, Object other, Fixture fixOne, Fixture fixOther, Contact contact) {
                if (one == null)
                    return;
                int entity = (int) one;

                CharacterComponent input = entityWorld.getMapper(CharacterComponent.class).get(entity);
                if (input != null) {
                    if (other == null) {
                        resetPos(entity, fixOne);
                    } else {
                        CharacterComponent inputOther = entityWorld.getMapper(CharacterComponent.class).get((int) other);
                        if (inputOther != null) {
                            resetPos(entity, fixOne);
                            resetPos((int) other, fixOther);
                        }
                    }
                }

                BulletComponent bullet = entityWorld.getMapper(BulletComponent.class).get(entity);
                if (bullet != null && other != null)
                    bullet.collisionCallback.onCollision(entity, (int) other, fixOne, fixOther, contact);
                else if (bullet != null && other == null)
                    bullet.delete = true;
            }

            @Override
            public void beginContact(Contact contact) {
                Object uA = contact.getFixtureA().getBody().getUserData();
                Object uB = contact.getFixtureB().getBody().getUserData();
                handleOne(uA, uB, contact.getFixtureA(), contact.getFixtureB(), contact);
                handleOne(uB, uA, contact.getFixtureB(), contact.getFixtureA(), contact);
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void step() {
        float accumulator = delta;

        if (deltaAccumulator >= step) {
            physicsWorld.step(Math.min(delta, step), 6, 2);
            entityWorld.setDelta(Math.min(delta, step));
            entityWorld.process();
            deltaAccumulator -= step;
        }
        do {
            physicsWorld.step(Math.min(delta, step), 6, 2);
            entityWorld.setDelta(Math.min(delta, step));
            entityWorld.process();
            accumulator -= step;
        } while (accumulator >= step);

        deltaAccumulator += accumulator;
    }

    public void render() {
        mapBuffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mapBuffer.end();

        for (Decal decal : renderSystem.getDecals())
            decalBatch.add(decal);
        decalBatch.add(mapDecal);
        decalBatch.flush();

        if (debug)
            debugPhysicrender.render(physicsWorld, cam.combined);
    }

    @Override
    public void dispose() {
        decalBatch.dispose();
        mapBuffer.dispose();
        physicsWorld.dispose();
        entityWorld.dispose();
    }

    public Tile getTile(int x, int y) {
        return logicMap.getTile(x, y);
    }

    public Tile getTile(Vector2 pos) {
        return logicMap.getTile(pos);
    }

    public int spawn(Entities entity, EntityArguments arguments) {
        int entityID = entityWorld.create(entityWorld.getArchetype(entity.getArchetype()));
        for (Class<? extends Component> componentType : entity.getComponents()) {
            if (StatComponent.class.isAssignableFrom(componentType)) {
                StatComponent statComponent = entityWorld.getMapper(StatComponent.class).get(entityID);
                statComponent.fromTemplate((Template) arguments.get(StatComponent.class.getSimpleName()));
            } else if (PositionComponent.class.isAssignableFrom(componentType)) {
                PositionComponent positionComponent = entityWorld.getMapper(PositionComponent.class).get(entityID);
                positionComponent.fromTemplate((Template) arguments.get(PositionComponent.class.getSimpleName()));
                positionComponent.tile = logicMap.getTile(positionComponent.position);
                positionComponent.tile.addEntity(entityID);
            } else if (RenderComponent.class.isAssignableFrom(componentType)) {
                RenderComponent renderComponent = entityWorld.getMapper(RenderComponent.class).get(entityID);
                renderComponent.fromTemplate((Template) arguments.get(RenderComponent.class.getSimpleName()));
            } else if (CharacterComponent.class.isAssignableFrom(componentType)) {
                CharacterComponent controlComponent = entityWorld.getMapper(CharacterComponent.class).get(entityID);
                PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                controlComponent.move.set(pos.x, pos.y);
                switch (arguments.get(CharacterComponent.class.getSimpleName(), CharacterComponent.CharacterTemplate.class).characterName) {
                    case "Kevin":
                        controlComponent.character = new Kevin();
                        break;
                    case "Ezreal":
                        controlComponent.character = new Ezreal();
                        break;
                    case "Ghost":
                        controlComponent.character = new Ghost();
                        break;
                    case "SuperGhost":
                        controlComponent.character = new SuperGhost();
                        break;
                }
                controlComponent.character.setEntityID(entityID);
                controlComponent.character.setArcadeWorld(this);
            } else if (PhysicComponent.class.isAssignableFrom(componentType)) {
                PhysicComponent.PhysicTemplate physicTemplate = arguments.get(PhysicComponent.class.getSimpleName(), PhysicComponent.PhysicTemplate.class);
                PhysicComponent physicComponent = entityWorld.getMapper(PhysicComponent.class).get(entityID);
                BodyDef bodyDef = new BodyDef();
                bodyDef.bullet = physicTemplate.isBullet;
                bodyDef.type = physicTemplate.bodyType;
                PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                bodyDef.position.set(pos.x, pos.y);
                Body body = physicsWorld.createBody(bodyDef);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.isSensor = physicTemplate.isSensor;
                fixtureDef.filter.categoryBits = physicTemplate.collisionCategory.getCategory();
                fixtureDef.filter.maskBits = physicTemplate.collisionCategory.getMask();
                CircleShape shape = new CircleShape();
                shape.setRadius(physicTemplate.radius);

                fixtureDef.shape = shape;
                body.createFixture(fixtureDef);

                shape.dispose();
                body.setUserData(entityID);
                physicComponent.body = body;
            }
        }
        return entityID;
    }

    public void delete(int id) {
        PhysicComponent physicComponent = entityWorld.getMapper(PhysicComponent.class).get(id);

        physicsWorld.destroyBody(physicComponent.body);

        PositionComponent position = entityWorld.getMapper(PositionComponent.class).get(id);
        position.tile.removeEntity(id);

        entityWorld.delete(id);
    }

    public EntityArguments loadArguments(String fileName) throws IOException {
        EntityArguments arguments = EntityArguments.fromFile(fileName);
        loadedEntityArguments.put(fileName, arguments);
        return arguments;
    }

    public Vector2 getSpawnPosition() {
        return new Vector2(
                tiledMap.getProperties().get("spawnX", Integer.class),
                tiledMap.getProperties().get("spawnY", Integer.class)
        );
    }

    public IntArray spawnEnemies(int playerId) {
        String enemies = (String) tiledMap.getProperties().get("enemies");
        IntArray array = new IntArray();
        for (String enemy : enemies.split("-")) {
            String[] parts = enemy.split(";");

            Vector2 pos = new Vector2(Integer.parseInt(parts[0]),
                    Integer.parseInt(parts[1]));
            EntityArguments arguments = getArguments(parts[2]);
            PositionComponent.PositionTemplate positionDef = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
            positionDef.x = pos.x;
            positionDef.y = pos.y;
            int id = spawn(Entities.Player, arguments);
            StatComponent statComponent = entityWorld.getMapper(StatComponent.class).get(id);
            final float xp = Integer.parseInt(parts[3]);
            statComponent.statEventHandler = (statComponent1, entityId) ->
            {
                StatComponent player = entityWorld.getMapper(StatComponent.class).get(playerId);
                player.addRuntimeStat(StatComponent.RuntimeStat.experience, xp);
                array.removeValue(id);
            };
            statComponent.setFlag(StatComponent.FlagStat.deleteOnDeath, true);
            AIComponent aiComponent = entityWorld.getMapper(AIComponent.class).create(id);
            aiComponent.target = playerId;
            aiComponent.aggroRange = Integer.parseInt(parts[4]);
            array.add(id);
        }
        return array;
    }

    public int spawnPlayer() {
        Vector2 mapCoord = getSpawnPosition();

        EntityArguments arguments;
        arguments = getArguments("kevin.json");
        PositionComponent.PositionTemplate positionDef = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
        float posX = positionDef.x;
        float posY = positionDef.y;
        positionDef.x = (int) mapCoord.x;
        positionDef.y = (int) mapCoord.y;
        int playerEnt = spawn(Entities.Player, arguments);
        positionDef.x = posX;
        positionDef.y = posY;
        InventoryComponent inventoryComponent = entityWorld.getMapper(InventoryComponent.class).get(playerEnt);
        inventoryComponent.items[0] = Item.builder().name("Armor")
                .flatStat(StatComponent.BaseStat.armor, 50f)
                .flatStat(StatComponent.BaseStat.cooldownReduction, 0.1f)
                .build();
        inventoryComponent.items[1] = Item.builder().name("Staff")
                .flatStat(StatComponent.BaseStat.spellPower, 20f)
                .build();
        return playerEnt;
    }

    public EntityArguments getArguments(String fileName) {
        if (!loadedEntityArguments.containsKey(fileName))
            try {
                return loadArguments(fileName);
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        return loadedEntityArguments.get(fileName);
    }
}
