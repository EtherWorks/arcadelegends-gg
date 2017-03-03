package gg.al.screen;


import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.render.CameraLayerGroupStrategy;

/**
 * Created by Thomas on 02.03.2017.
 */
public class MapTestScreen implements Screen, InputProcessor {

    private static final int[] UNDERLAYERS = new int[]{0};
    private static final int[] UPPERLAYERS = new int[]{1, 2};
    private FrameBuffer buffer;
    private FrameBuffer buffer2;
    private SpriteBatch batch;
    private SpriteBatch bufferBatch;
    private Texture texture;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TextureRegion region;
    private OrthographicCamera cam;
    private Viewport viewport;
    private Viewport viewportPers;
    private PerspectiveCamera perspectiveCamera;
    private Vector3 pos;
    private Decal ez;
    private DecalBatch decbatch;
    private Decal mapDec;
    private Decal mapDecUpper;
    private float rot;
    private Vector3 ezPos;
    private float rel;

    @Override
    public void show() {
        batch = new SpriteBatch();
        bufferBatch = new SpriteBatch();
        texture = new Texture("assets/sprites/ezreal.png");
        map = new TmxMapLoader(new InternalFileHandleResolver()).load("assets/map/test.tmx");
        buffer = new FrameBuffer(Pixmap.Format.RGB888, map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, false);
        buffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, false);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        region = new TextureRegion();
        cam = new OrthographicCamera(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32);
        viewport = new FitViewport(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32);
        viewport.setCamera(cam);
        viewport.update(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, true);
        System.out.println(viewport.getWorldWidth() + " " + viewport.getScreenWidth());
        perspectiveCamera = new PerspectiveCamera();
        perspectiveCamera.position.set(-50, -50, 50);
        perspectiveCamera.viewportWidth = 1920;
        perspectiveCamera.viewportHeight = 1080;
        perspectiveCamera.fieldOfView = 45;
        perspectiveCamera.far = 1000;
        perspectiveCamera.near = 1;
        perspectiveCamera.lookAt(-50, -50, 0);
        //perspectiveCamera.rotateAround(Vector3.Zero, Vector3.X, 5);
        perspectiveCamera.update();
        pos = new Vector3(-50f, -50f, 0);
        ezPos = new Vector3(-50f, -50f, 0);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, new CameraInputController(perspectiveCamera)));
        decbatch = new DecalBatch(new CameraLayerGroupStrategy(perspectiveCamera));

        ez = Decal.newDecal(1, 1, new TextureRegion(texture), true);
        rot = 15;
        rel = 10;
        mapDec = Decal.newDecal(map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), new TextureRegion(), false);
        mapDecUpper = Decal.newDecal(map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), new TextureRegion(), true);
        mapDec.setPosition(/*mapDec.getWidth() / 2, mapDec.getHeight() / 2,*/0, 0, 0);
        mapDec.setRotationX(-rot);
        mapDecUpper.setPosition(0, 0, 0);
        mapDecUpper.setRotationX(-rot);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, .5f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        buffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(cam);
        mapRenderer.getBatch().setBlendFunction(-1, -1);
        mapRenderer.render(UNDERLAYERS);
        buffer.end();


        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        region = new TextureRegion(buffer.getColorBufferTexture());
        region.flip(false, true);
        mapDec.setTextureRegion(region);

        Matrix3 ma = new Matrix3();
        ma.setToRotation(Vector3.X, rot);
        Vector3 ve = new Vector3(ezPos).mul(ma);
        ez.setPosition(ve.x + 0.5f, ve.y + 0.5f, ve.z);
        ve = new Vector3(pos).mul(ma);
//        perspectiveCamera.position.set(ve.x + 0.5f, ve.y + 0.5f, ve.z + rel);
//        perspectiveCamera.update();
        decbatch.add(mapDec);
        decbatch.add(ez);

        buffer2.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(cam);
        mapRenderer.render(UPPERLAYERS);
        buffer2.end();
        buffer2.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion reg = new TextureRegion(buffer2.getColorBufferTexture());
        reg.flip(false, true);
        mapDecUpper.setTextureRegion(reg);
        decbatch.add(mapDecUpper);
        decbatch.flush();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                pos.x -= 1;
                break;
            case Input.Keys.D:
                pos.x += 1;
                break;
            case Input.Keys.W:
                pos.y += 1;
                break;
            case Input.Keys.S:
                pos.y -= 1;
                break;
            case Input.Keys.LEFT:
                ezPos.x -= 1;
                break;
            case Input.Keys.RIGHT:
                ezPos.x += 1;
                break;
            case Input.Keys.UP:
                ezPos.y += 1;
                break;
            case Input.Keys.DOWN:
                ezPos.y -= 1;
                break;
        }
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
        rel += amount * 0.1f;
        return false;
    }
}
