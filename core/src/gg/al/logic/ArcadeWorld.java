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
import gg.al.game.AL;
import gg.al.graphics.CameraLayerGroupStrategy;
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

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
@Slf4j
public class ArcadeWorld implements Disposable {
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

    public ArcadeWorld(TiledMap map, float worldViewRotation, Camera cam) {
        this.tiledMap = map;
        this.worldViewRotation = worldViewRotation;
        this.cam = cam;

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
                        new RegenSystem(.5f)
                )
                .with(0,
                        new PhysicPositionSystem(),
                        new PositionTileSystem(logicMap),
                        new InputSystem(physicsWorld),
                        renderSystem = new RenderSystem(decalBatch, AL.getAssetManager(), worldViewRotation)
                )
                .build();
        entityWorld = new EntityWorld(worldConfiguration);

        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                int entityIdA = (int) contact.getFixtureA().getBody().getUserData();
                int entityIdB = (int) contact.getFixtureB().getBody().getUserData();
                InputComponent inputA = entityWorld.getMapper(InputComponent.class).get(entityIdA);
                InputComponent inputB = entityWorld.getMapper(InputComponent.class).get(entityIdB);

                if (inputA != null && inputB != null) {
                    PositionComponent positionA = entityWorld.getMapper(PositionComponent.class).get(entityIdA);
                    PositionComponent positionB = entityWorld.getMapper(PositionComponent.class).get(entityIdB);
                    contact.getFixtureA().getBody().setLinearVelocity(Vector2.Zero);
                    contact.getFixtureB().getBody().setLinearVelocity(Vector2.Zero);
                    positionA.resetPos = true;
                    positionB.resetPos = true;
                    inputA.move.set(positionA.position);
                    inputB.move.set(positionB.position);
                }
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
            deltaAccumulator -= step;
        }
        do {
            physicsWorld.step(Math.min(delta, step), 6, 2);
            accumulator -= step;
        } while (accumulator >= step);
        entityWorld.setDelta(Math.min(delta, step));
        entityWorld.process();
        deltaAccumulator += accumulator;
    }

    public void render() {
        mapBuffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mapBuffer.end();

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
            } else if (InputComponent.class.isAssignableFrom(componentType)) {
                InputComponent inputComponent = entityWorld.getMapper(InputComponent.class).get(entityID);
                PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                inputComponent.move.set(pos.x, pos.y);
            } else if (PhysicComponent.class.isAssignableFrom(componentType)) {
                PhysicComponent physicComponent = entityWorld.getMapper(PhysicComponent.class).get(entityID);
                BodyDef bodyDef = new BodyDef();
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                PositionComponent.PositionTemplate pos = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                bodyDef.position.set(pos.x, pos.y);
                Body body = physicsWorld.createBody(bodyDef);

                FixtureDef fixtureDef = new FixtureDef();
                CircleShape shape = new CircleShape();
                shape.setRadius(.47f);

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
}
