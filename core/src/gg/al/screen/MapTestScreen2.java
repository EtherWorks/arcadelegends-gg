package gg.al.screen;


import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.render.CameraLayerGroupStrategy;
import gg.al.render.OrderedDecal;

/**
 * Created by Thomas on 02.03.2017.
 */
public class MapTestScreen2 implements Screen, InputProcessor {

    private static final int[] UNDERLAYERS = new int[]{0};
    private static final int[] UPPERLAYERS = new int[]{1, 2};
    private FrameBuffer buffer;
    private Texture reticle;
    private FrameBuffer buffer2;
    private SpriteBatch batch;
    private SpriteBatch bufferBatch;
    private SpriteBatch screenBatch;
    private BitmapFont font;
    private Texture texture;
    private Texture towerbase;
    private Texture towertop;
    private Texture towerfull;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TextureRegion region;
    private OrthographicCamera cam;
    private Viewport viewport;
    private Viewport viewportPers;
    private PerspectiveCamera perspectiveCamera;
    private PerspectiveCamera testCamera;
    private Vector3 pos;
    private Decal ez;
    private Decal test;
    private DecalBatch decbatch;
    private Decal mapDec;
    private Decal mapDecUpper;
    private float rot;
    private Vector3 ezPos;
    private float rel;
    private Decal towerDecal;
    private Decal towerbaseDecal;
    private Decal towertopDecal;
    private Vector3 testpos;
    private Plane plane;

    @Override
    public void show() {


        batch = new SpriteBatch();
        bufferBatch = new SpriteBatch();
        texture = new Texture("assets/sprites/ezreal.png");
        towerbase = new Texture("assets/sprites/tower.png");
        towerfull = new Texture("assets/sprites/towerfull.png");
        towertop = new Texture("assets/sprites/towertop.png");
        reticle = new Texture("assets/sprites/reticle.png");
        map = new TmxMapLoader(new InternalFileHandleResolver()).load("assets/map/test.tmx");
        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, false);
        buffer2 = new FrameBuffer(Pixmap.Format.RGBA8888, map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, false);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        region = new TextureRegion();
        cam = new OrthographicCamera(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32);
        viewport = new FitViewport(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32);
        viewport.setCamera(cam);
        viewport.update(map.getProperties().get("width", Integer.class) * 32, map.getProperties().get("height", Integer.class) * 32, true);
        System.out.println(viewport.getWorldWidth() + " " + viewport.getScreenWidth());
        screenBatch = new SpriteBatch();
        font = new BitmapFont();
        perspectiveCamera = new PerspectiveCamera();
        perspectiveCamera.position.set(-40, -40, 39);
        perspectiveCamera.viewportWidth = 960;
        perspectiveCamera.viewportHeight = 540;
        perspectiveCamera.fieldOfView = 25;
        perspectiveCamera.far = 100;
        perspectiveCamera.near = 1;
        //perspectiveCamera.rotate(Vector3.X, 15);
        testCamera = new PerspectiveCamera();
        testCamera.position.set(-45, -45, 40);
        testCamera.viewportWidth = 960;
        testCamera.viewportHeight = 540;
        testCamera.fieldOfView = 25;
        testCamera.far = 40;
        testCamera.near = 1;
        testCamera.update();
        perspectiveCamera.rotateAround(Vector3.Zero, Vector3.X, 5);
        perspectiveCamera.update();
        pos = new Vector3(-50f, -50f, 0);
        ezPos = new Vector3(-50f, -50f, 0);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, new CameraInputController(perspectiveCamera)));
        decbatch = new DecalBatch(new CameraLayerGroupStrategy(perspectiveCamera));

        ez = OrderedDecal.newOrderedDecal(2, 1, 1, new TextureRegion(texture), true);


        test = OrderedDecal.newOrderedDecal(2, 1, 1, new TextureRegion(texture), true);
        towerDecal = OrderedDecal.newOrderedDecal(3, 1, 2, new TextureRegion(towerfull), true);
        towerDecal.setPosition(new Vector3(-47.5f, -45f, 0));
        towerbaseDecal = OrderedDecal.newOrderedDecal(2, 1, 1, new TextureRegion(towerbase), true);
        towertopDecal = OrderedDecal.newOrderedDecal(3, 1, 1, new TextureRegion(towertop), true);
        towerbaseDecal.setPosition(-45.5f, -45.5f, 0);
        towerbaseDecal.rotateX(15);
        towertopDecal.setPosition(towerbaseDecal.getPosition().x, towerbaseDecal.getPosition().y + 1, 0);

        testpos = new Vector3(-50, -50, 0);
        towerDecal.rotateX(15);
        rot = 15;
        Vector3 p1 = new Vector3(50, 50, 39);
        Vector3 p2 = new Vector3(-50, -50, 39);
        plane = new Plane(p1, p2, new Vector3(0, 0, 39));
        plane = new Plane(Vector3.Z, Vector3.Zero);
        rel = 10;
        mapDec = OrderedDecal.newOrderedDecal(1, map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), new TextureRegion(), false);
        mapDecUpper = OrderedDecal.newOrderedDecal(3, map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), new TextureRegion(), true);
        mapDec.setPosition(/*mapDec.getWidth() / 2, mapDec.getHeight() / 2,*/0, 0, 0);
        //mapDec.setRotationX(-rot);
        mapDecUpper.setPosition(0, 0, 0);
        //mapDecUpper.setRotationX(-rot);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, .5f, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        buffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.setView(cam);
        //mapRenderer.getBatch().setBlendFunction(-1, -1);
        mapRenderer.render(UNDERLAYERS);
        bufferBatch.setProjectionMatrix(cam.combined);
        bufferBatch.begin();
        bufferBatch.draw(reticle, (ezPos.x + 50) * 32, (ezPos.y + 50) * 32);
        bufferBatch.end();
        buffer.end();


        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        region = new TextureRegion(buffer.getColorBufferTexture());
        region.flip(false, true);
        mapDec.setTextureRegion(region);

        //System.out.println(ve);
        //System.out.println(ve);
        Vector3 x = new Vector3(0, -0.5f, 0);
        Matrix4 matrix4 = new Matrix4().rotate(Vector3.X, -15);
        x.mul(matrix4);
        Vector3 dsf = new Vector3(ezPos);
        dsf.x += 0.5f;
        dsf.y += 1f;
        dsf.z += x.z;
        dsf.y += x.y;
        dsf.x += x.x;
        ez.setRotation(0, 0, 0);
        ez.setPosition(dsf);
        ez.rotateX(15);
        decbatch.add(mapDec);
        decbatch.add(towerDecal);
        decbatch.add(towertopDecal);
        decbatch.add(towerbaseDecal);
        test.setPosition(testpos);
        //decbatch.add(test);
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

        screenBatch.begin();
        font.draw(screenBatch, "" + Gdx.graphics.getFramesPerSecond(), 0, 15);
        screenBatch.end();
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
                perspectiveCamera.fieldOfView++;

                System.out.println(perspectiveCamera.fieldOfView);
                perspectiveCamera.update();
                break;
            case Input.Keys.S:
                perspectiveCamera.fieldOfView--;
                System.out.println(perspectiveCamera.fieldOfView);
                perspectiveCamera.update();
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
        if (button != Input.Buttons.MIDDLE)
            return false;
//        testCamera.far = testCamera.position.z - 12.937864f;
//        testCamera.update();
//        Vector3 vec = testCamera.unproject(new Vector3(screenX, screenY, 1f));
//        //-45.358875
//        System.out.println(vec);
//        System.out.println(vec.y * Math.tan(Math.toRadians(rot)));
//        testpos.x = vec.x;
//        testpos.y = vec.y;
//        Matrix3 ma = new Matrix3();
//        ma.setToRotation(Vector3.X, -rot);
//        Vector3 c = new Vector3(vec).mul(ma);
//        ezPos.x = c.x - 0.5f;
//        ezPos.y = c.y - 0.5f;
//        System.out.println(vec);
//        Vector3 pos = new Vector3(vec.x, vec.y,(float) (vec.y / Math.tan(Math.toRadians(rot))));
//        Matrix3 ma = new Matrix3();
//        ma.setToRotation(Vector3.X, -rot);
//        pos.mul(ma);
//        System.out.println(pos);
//        ezPos.x = pos.x;
//        ezPos.y = pos.y;


//        float x = (2.0f * screenX) / 960 - 1.0f;
//        float y = 1.0f - (2.0f * screenY) / 540;
//        float z = 1.0f;
//        Vector3 ray_nds = new Vector3(x, y, z);
//        Vector3 ray_clip = new Vector3(ray_nds.x,ray_nds.y, -1.0f);
//        Vector3 ray_eye = ray_clip.mul(perspectiveCamera.invProjectionView);
//        ray_eye.z = -1;
//        Vector3 ray_wor = (inverse(view_matrix)cam * ray_eye).xyz;
//        ray_wor = normalise(ray_wor);
        System.out.println(screenX + " " + screenY);
        Ray ray = perspectiveCamera.getPickRay(screenX, screenY);
        Vector3 worldcoor = new Vector3();
        Intersector.intersectRayPlane(ray, plane, worldcoor);
        System.out.println(worldcoor);
        ezPos.x = Math.round(worldcoor.x - 0.5f);
        ezPos.y = Math.round(worldcoor.y - 0.5f);
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
        rel += amount;
        return false;
    }
}
