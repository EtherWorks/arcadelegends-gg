package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.box2d.*;
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
        camera = new PerspectiveCamera();
        camera.position.set(new Vector3(0,0,50));
        camera.rotateAround(Vector3.Zero, Vector3.X, rot);
        camera.position.set(camera.position.x, camera.position.y, 50);
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
        mapDecal.setPosition(0, 0, 0);

        mapHitbox = new Plane(Vector3.Z, Vector3.Zero);

        physicWorld = new World(new Vector2(0,0), true);

        //Debug hitbox
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(0.5f, 0.5f);
        body = physicWorld.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(.5f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;
        fix = body.createFixture(fixtureDef);

        circle.dispose();



        Gdx.input.setInputProcessor(new InputMultiplexer(new CameraInputController(camera),this));
    }

    //Debug objects
    Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    Fixture fix;
    Body body;

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

        //Debug stepping and rendering
        debugRenderer.render(physicWorld, camera.combined);
        physicWorld.step(1/45f, 6, 2);
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

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode)
        {
            case Input.Keys.A:
                camera.translate(-1,0,0);
                break;
            case Input.Keys.D:
                camera.translate(1,0,0);
                break;
            case Input.Keys.S:
                camera.translate(0,-1,0);
                break;
            case Input.Keys.W:
                camera.translate(0,1,0);
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
        return false;
    }
}
