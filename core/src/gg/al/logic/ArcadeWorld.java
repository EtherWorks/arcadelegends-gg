package gg.al.logic;

import com.artemis.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.logic.map.LogicMap;
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


    private FrameBuffer mapBuffer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Decal mapDecal;
    private TextureRegion mapTemp;

    private DecalBatch decalBatch;

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
        log.debug(logicMap.getTile(1,1).toString());

        OrthographicCamera mapCam = new OrthographicCamera();
        Viewport viewportMap = new FitViewport(mapWidth * mapTileWidth, mapHeight * mapTileHeight, mapCam);
        viewportMap.update(viewportMap.getScreenWidth(), viewportMap.getScreenHeight(), true);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(mapCam);

        mapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, mapWidth * mapTileWidth, mapHeight * mapTileHeight, false);
        mapBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        mapTemp = new TextureRegion(mapBuffer.getColorBufferTexture());
        mapTemp.flip(false, true);

        mapDecal = Decal.newDecal(mapWidth, mapHeight, mapTemp);
        mapDecal.setPosition(-.5f, -.5f, 0);

        decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

        mapHitbox = new Plane(Vector3.Z, Vector3.Zero);

        physicsWorld = new World(Vector2.Zero, true);
        debugPhysicrender = new Box2DDebugRenderer();

        WorldConfiguration worldConfiguration = new WorldConfigurationBuilder()
//                .with(
//                )
                .build();
        entityWorld = new EntityWorld(worldConfiguration);
    }

    public void step() {
        float accumulator = delta;
        do {
            physicsWorld.step(step, 6, 2);
            accumulator -= step;
        } while (accumulator >= step);
        entityWorld.setDelta(delta);
        entityWorld.process();
    }

    public void render() {
        mapBuffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mapBuffer.end();

        mapTemp.setTexture(mapBuffer.getColorBufferTexture());
        mapDecal.setTextureRegion(mapTemp);
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
}
