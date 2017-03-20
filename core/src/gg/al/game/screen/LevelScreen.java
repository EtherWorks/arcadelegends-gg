package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas Neumann on 20.03.2017.<br />
 * Main {@link IAssetScreen} for loading and playing levels.
 */
@Slf4j
public class LevelScreen implements IAssetScreen {

    private final AssetDescriptor<TiledMap> mapDesc;
    private final float rot;
    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    private FrameBuffer mapBuffer;

    private DecalBatch batch;
    private PerspectiveCamera camera;
    private Viewport viewport;

    private Decal mapDecal;
    private TextureRegion mapTemp;

    public LevelScreen(AssetDescriptor<TiledMap> mapDesc) {
        this(mapDesc, 15);
    }

    public LevelScreen(AssetDescriptor mapDesc, float rot) {
        this.rot = rot;
        this.mapDesc = mapDesc;
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(mapDesc);
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    @Override
    public void show() {
        camera = new PerspectiveCamera();

        //camera.rotate(Vector3.X, rot);
        camera.position.set(0, 0, 10);
        camera.fieldOfView = 15;
        viewport = new FitViewport(1920, 1080, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new DecalBatch(new CameraGroupStrategy(camera));
        map = AL.asset.get(mapDesc);
        OrthographicCamera mapCam = new OrthographicCamera();
        Viewport viewportMap = new FitViewport(map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class),
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class), mapCam);
        viewportMap.update(viewportMap.getScreenWidth(), viewportMap.getScreenHeight(), true);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(mapCam);

        mapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class),
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class),
                false);
        mapDecal = Decal.newDecal(map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), new TextureRegion());
        mapDecal.setPosition(0, 0, 0);

        Gdx.input.setInputProcessor(new CameraInputController(camera));
    }

    @Override
    public void render(float delta) {
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mapBuffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        mapBuffer.end();
        mapBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        if (mapTemp == null) {
            mapTemp = new TextureRegion(mapBuffer.getColorBufferTexture());
            mapTemp.flip(false, true);
        } else mapTemp.setTexture(mapBuffer.getColorBufferTexture());

        mapDecal.setTextureRegion(mapTemp);
        batch.add(mapDecal);
        batch.flush();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        mapBuffer.dispose();
        batch.dispose();
        AL.asset.unload(mapDesc.fileName);
    }

    @Override
    public void dispose() {

    }
}
