package gg.al.game.screen;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.game.screen.IAssetScreen;
import gg.al.game.screen.ILoadingScreen;
import gg.al.prototype.logic.EntityWorld;
import gg.al.prototype.logic.entity.component.Physics;
import gg.al.prototype.logic.entity.component.PositionUpdate;
import gg.al.prototype.logic.entity.component.Render;
import gg.al.prototype.logic.system.MovementSystem;
import gg.al.prototype.logic.system.PositionUpdateSystem;
import gg.al.prototype.logic.system.RenderSystem;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas Neumann on 20.03.2017.<br />
 * Main {@link IAssetScreen} for loading and playing levels.
 */
@Slf4j
public class LevelScreen implements IAssetScreen, InputProcessor {

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
    private Plane mapHitbox;
    private World physicWorld;

    private SpriteBatch fpsBatch;
    private BitmapFont font;

    public LevelScreen(AssetDescriptor<TiledMap> mapDesc) {
        this(mapDesc, 15);
    }

    public LevelScreen(AssetDescriptor<TiledMap> mapDesc, float rot) {
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
        fpsBatch = new SpriteBatch();
        font = new BitmapFont();

        camera = new PerspectiveCamera();
        camera.position.set(new Vector3(-.5f, -.5f, 50));
        camera.rotateAround(new Vector3(-.5f, -.5f, 0), Vector3.X, rot);
        camera.fieldOfView = 15;
        camera.update();
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
        mapDecal.setPosition(-.5f, -.5f, 0);

        mapHitbox = new Plane(Vector3.Z, Vector3.Zero);

        physicWorld = new World(new Vector2(0, 0), true);

        Gdx.input.setInputProcessor(this);
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

        //Debug stepping and rendering

        physicWorld.step(1 / 45f, 6, 2);

        batch.flush();

        fpsBatch.begin();
        font.draw(fpsBatch, String.format("%d FPS", Gdx.graphics.getFramesPerSecond()), 0, 15);
        fpsBatch.end();
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
        batch.dispose();
        mapBuffer.dispose();
        fpsBatch.dispose();
        AL.asset.unload(mapDesc.fileName);
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                camera.translate(-1, 0, 0);
                break;
            case Input.Keys.D:
                camera.translate(1, 0, 0);
                break;
            case Input.Keys.S:
                camera.translate(0, -1, 0);
                break;
            case Input.Keys.W:
                camera.translate(0, 1, 0);
                break;
        }
        camera.update();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Ray ray = camera.getPickRay(screenX, screenY);
        Vector3 worldcoor = new Vector3();
        Intersector.intersectRayPlane(ray, mapHitbox, worldcoor);
        //worldcoor.sub(.5f,.5f,0);
        log.debug("Clicked: " + worldcoor.toString());
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.translate(0, 0, amount);
        camera.update();
        return false;
    }
}