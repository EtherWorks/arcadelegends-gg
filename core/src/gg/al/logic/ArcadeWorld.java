package gg.al.logic;

import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Thomas Neumann on 23.03.2017.<br />
 */
public class ArcadeWorld implements Disposable{
    @Getter
    private World physicsWorld;
    @Getter
    private EntityWorld entityWorld;

    @Getter
    private TiledMap map;
    @Getter
    private int mapWidth, mapHeight, mapTileHeight, mapTileWidth;
    @Getter
    private float worldViewRotation;

    @Getter @Setter
    private float delta;


    private FrameBuffer mapBuffer;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Decal mapDecal;
    private TextureRegion mapTemp;

    private DecalBatch decalBatch;

    @Getter
    private Plane mapHitbox;

    @Getter @Setter
    private Camera cam;

    public ArcadeWorld(TiledMap map, float worldViewRotation, Camera cam) {
        this.map = map;
        this.worldViewRotation = worldViewRotation;
        this.cam = cam;

        this.mapWidth = map.getProperties().get("width", Integer.class);
        this.mapHeight = map.getProperties().get("height", Integer.class);
        this.mapTileWidth = map.getProperties().get("tilewidth", Integer.class);
        this.mapTileHeight = map.getProperties().get("tileheight", Integer.class);

        OrthographicCamera mapCam = new OrthographicCamera();
        Viewport viewportMap = new FitViewport(mapWidth * mapTileWidth, mapHeight * mapTileHeight, mapCam);
        viewportMap.update(viewportMap.getScreenWidth(), viewportMap.getScreenHeight(), true);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(mapCam);

        mapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, mapWidth * mapTileWidth, mapHeight * mapTileHeight,false);
        mapTemp = new TextureRegion(new Texture(mapWidth * mapTileWidth, mapHeight * mapTileHeight, Pixmap.Format.RGBA4444));
        mapTemp.flip(false,true);

        mapDecal = Decal.newDecal(mapWidth, mapHeight, mapTemp);
        mapDecal.setPosition(-.5f, -.5f, 0);

        decalBatch = new DecalBatch(new CameraGroupStrategy(cam));

        mapHitbox = new Plane(Vector3.Z, Vector3.Zero);

        physicsWorld = new World(Vector2.Zero, true);

        WorldConfiguration worldConfiguration = new WorldConfigurationBuilder().build();
        entityWorld = new EntityWorld(worldConfiguration);
    }

    public void step()
    {
        physicsWorld.step(1 / 45f, 6, 2);
        entityWorld.setDelta(delta);
        entityWorld.process();
    }

    public void render()
    {
        mapBuffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mapBuffer.end();
        mapBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        mapTemp.setTexture(mapBuffer.getColorBufferTexture());
        mapDecal.setTextureRegion(mapTemp);
        decalBatch.add(mapDecal);
        decalBatch.flush();
    }


    @Override
    public void dispose() {

    }
}
