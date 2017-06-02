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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.character.Kevin;
import gg.al.game.AL;
import gg.al.graphics.CameraLayerGroupStrategy;
import gg.al.graphics.renderer.CharacterRenderer;
import gg.al.logic.component.*;
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


        debugPhysicrender = new Box2DDebugRenderer();
        WorldConfiguration worldConfiguration = new WorldConfigurationBuilder()
                .with(1,
                        new StatSystem(this),
                        new CharacterSystem(),
                        new RegenSystem(.5f)
                )
                .with(0,
                        new PhysicPositionSystem(),
                        new PositionTileSystem(logicMap),
                        new CharacterControlSystem(physicsWorld),
                        new BulletSystem(this),
                        renderSystem = new RenderSystem(decalBatch, AL.getAssetManager(), worldViewRotation, levelAssets)
                )
                .build();
        entityWorld = new EntityWorld(worldConfiguration);

        physicsWorld.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                int entityIdA = (int) contact.getFixtureA().getBody().getUserData();
                int entityIdB = (int) contact.getFixtureB().getBody().getUserData();
                CharacterControlComponent inputA = entityWorld.getMapper(CharacterControlComponent.class).get(entityIdA);
                CharacterControlComponent inputB = entityWorld.getMapper(CharacterControlComponent.class).get(entityIdB);

                if (inputA != null && inputB != null) {
                    PositionComponent positionA = entityWorld.getMapper(PositionComponent.class).get(entityIdA);
                    PositionComponent positionB = entityWorld.getMapper(PositionComponent.class).get(entityIdB);
                    contact.getFixtureA().getBody().setLinearVelocity(Vector2.Zero);
                    contact.getFixtureB().getBody().setLinearVelocity(Vector2.Zero);
                    positionA.resetPos = true;
                    positionB.resetPos = true;
                    inputA.move.set(positionA.position);
                    inputB.move.set(positionB.position);
                    RenderComponent renderComponentA = entityWorld.getMapper(RenderComponent.class).get(entityIdA);
                    RenderComponent renderComponentB = entityWorld.getMapper(RenderComponent.class).get(entityIdB);
                    renderComponentA.setRenderState(CharacterRenderer.PlayerRenderState.IDLE);
                    renderComponentB.setRenderState(CharacterRenderer.PlayerRenderState.IDLE);
                }


            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                int entityIdA = (int) contact.getFixtureA().getBody().getUserData();
                int entityIdB = (int) contact.getFixtureB().getBody().getUserData();
                BulletComponent bulletA = entityWorld.getMapper(BulletComponent.class).get(entityIdA);
                BulletComponent bulletB = entityWorld.getMapper(BulletComponent.class).get(entityIdB);

                if (bulletA != null && bulletA.callback != null)
                    bulletA.callback.onCollision(entityIdA, entityIdB, contact.getFixtureA(), contact.getFixtureB(), contact);
                if (bulletB != null && bulletB.callback != null)
                    bulletB.callback.onCollision(entityIdB, entityIdA, contact.getFixtureB(), contact.getFixtureA(), contact);
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
            } else if (CharacterControlComponent.class.isAssignableFrom(componentType)) {
                CharacterControlComponent controlComponent = entityWorld.getMapper(CharacterControlComponent.class).get(entityID);
                PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                controlComponent.move.set(pos.x, pos.y);
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
            } else if (CharacterComponent.class.isAssignableFrom(componentType)) {
                //set character
                CharacterComponent characterComponent = entityWorld.getMapper(CharacterComponent.class).get(entityID);
                switch (arguments.get(CharacterComponent.class.getSimpleName(), CharacterComponent.CharacterTemplate.class).characterName) {
                    case "Kevin":
                        characterComponent.character = new Kevin();
                        break;
                }
                characterComponent.character.setEntityID(entityID);
                characterComponent.character.setArcadeWorld(this);
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
