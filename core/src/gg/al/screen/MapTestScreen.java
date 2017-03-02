package gg.al.screen;


import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Thomas on 02.03.2017.
 */
public class MapTestScreen implements Screen, InputProcessor {

    private FrameBuffer buffer;
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
    private Vector2 pos;
    private Decal ez;
    private DecalBatch decbatch;
    private Decal mapDec;

    @Override
    public void show() {
        batch = new SpriteBatch();
        bufferBatch = new SpriteBatch();
        texture = new Texture("assets/sprites/ezreal.png");
        map = new TmxMapLoader(new InternalFileHandleResolver()).load("assets/map/test.tmx");
        buffer = new FrameBuffer(Pixmap.Format.RGB888, map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, false);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        region = new TextureRegion();
        cam = new OrthographicCamera(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32);
        viewport = new FitViewport(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32);
        viewport.setCamera(cam);
        viewport.update(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, true);
        System.out.println(viewport.getWorldWidth() + " " + viewport.getScreenWidth());
        perspectiveCamera = new PerspectiveCamera();
        perspectiveCamera.position.set(0, 0, 50);
        perspectiveCamera.viewportWidth = 1920;
        perspectiveCamera.viewportHeight = 1080;
        perspectiveCamera.fieldOfView = 45;
        perspectiveCamera.far = 1000;
        perspectiveCamera.near = 1;
        perspectiveCamera.lookAt(0, 0, 0);
        //perspectiveCamera.rotateAround(Vector3.Zero, Vector3.X, 5);
        perspectiveCamera.update();
        pos = new Vector2(0, 0);
        Gdx.input.setInputProcessor(new InputMultiplexer(new CameraInputController(perspectiveCamera), this));
        decbatch = new DecalBatch(new CameraGroupStrategy(perspectiveCamera));
        ez = Decal.newDecal(new TextureRegion(texture), true);
        mapDec = Decal.newDecal(map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), new TextureRegion());
        mapDec.setPosition(mapDec.getWidth() / 2, mapDec.getHeight() / 2, 0);
        mapDec.setRotationX(-5);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        buffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(cam);
        mapRenderer.getBatch().setBlendFunction(-1, -1);
        mapRenderer.render();
        bufferBatch.setProjectionMatrix(cam.combined);
        bufferBatch.begin();
        bufferBatch.draw(texture, pos.x, pos.y);
        bufferBatch.end();
        buffer.end();


        region = new TextureRegion(buffer.getColorBufferTexture());
        region.flip(false, true);
        mapDec.setTextureRegion(region);
        ez.setPosition(pos.x, pos.y, 0);
        decbatch.add(mapDec);
        decbatch.add(ez);
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
                pos.x -= 32;
                break;
            case Input.Keys.D:
                pos.x += 32;
                break;
            case Input.Keys.W:
                pos.y += 32;
                break;
            case Input.Keys.S:
                pos.y -= 32;
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
        return false;
    }
}
