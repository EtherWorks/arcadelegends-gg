package gg.al.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Dr. Gavrel on 16.02.2017.
 */
public class TestScreen implements Screen, InputProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestScreen.class);

    private final Game game;
    private SpriteBatch batch;
    private Texture ezidle;
    private Texture lawbringeridle;
    private Texture lawbringer32;
    private Stage stage;
    private Skin skin;
    private BitmapFont bfont;
    private BitmapFont font;
    private OrthographicCamera camera;
    private OrthographicCamera mapCamera;
    private Viewport viewport;
    private Animation<TextureRegion> ez;
    private Animation<TextureRegion> lawbringer;
    private TiledMap map;
    private float time;
    private float unitScale = 1 / 32f;
    private OrthogonalTiledMapRenderer renderer;
    private Vector2 pos;
    private Vector2 lawbringerpos;
    private Vector2 lastTouch = new Vector2();
    private boolean drag = false;
    private FrameBuffer buffer;

    public TestScreen(Game game) {
        this.game = game;
        pos = new Vector2();
        lawbringerpos = new Vector2();
        time = 0;
    }

    @Override
    public void show() {
        LOGGER.debug("showing TestScreen");
        if (batch == null)
            batch = new SpriteBatch();
        if (stage == null)
            stage = new Stage();
        if (skin == null)
            skin = new Skin();
        if (font == null)
            font = new BitmapFont();
        if (ezidle == null)
            ezidle = new Texture("assets/sprites/ez_idle.png");
        if (lawbringer32 == null)
            lawbringer32 = new Texture("assets/sprites/lawbringer32.png");
        if (lawbringeridle == null)
            lawbringeridle = new Texture("assets/sprites/lawbringer.png");
        if (map == null)
            map = new TmxMapLoader(new InternalFileHandleResolver()).load("assets/map/test.tmx");
        if (buffer == null)
            buffer = new FrameBuffer(Pixmap.Format.RGBA8888, map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class), true);
        if (renderer == null)
            renderer = new OrthogonalTiledMapRenderer(map);
        if (ez == null) {
            TextureRegion[][] tmp = TextureRegion.split(ezidle, ezidle.getWidth() / 4, ezidle.getHeight());
            TextureRegion[] frames = new TextureRegion[4 * 1];
            int ind = 0;
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    frames[ind++] = tmp[i][j];
                }
            }
            ez = new Animation<>(.3f, frames);
            ez.setPlayMode(Animation.PlayMode.LOOP);
        }
        if (lawbringer == null) {
            TextureRegion[][] tmp = TextureRegion.split(lawbringeridle, lawbringeridle.getWidth() / 3, lawbringeridle.getHeight());
            TextureRegion[] frames = new TextureRegion[3 * 1];
            int ind = 0;
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 3; j++) {
                    frames[ind++] = tmp[i][j];
                }
            }
            lawbringer = new Animation<>(.5f, frames);
            lawbringer.setPlayMode(Animation.PlayMode.LOOP);
        }
        camera = new OrthographicCamera();
        mapCamera = new OrthographicCamera();
        mapCamera.position.set(0, 0, 100);
        mapCamera.near = 0;
        mapCamera.far = 1000;
        mapCamera.lookAt(0, 0, 0);
        mapCamera.update();
        viewport = new ScreenViewport(camera);
        mapCamera.setToOrtho(false, 1920 / 2, 1080 / 2);
        //Gdx.input.setInputProcessor(/*new InputMultiplexer(stage, this)*/this);
        Gdx.input.setInputProcessor(new CameraInputController(mapCamera));

        if (bfont == null)
            bfont = new BitmapFont();
        bfont.getData().setScale(2);

        skin.add("default", bfont);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");

        skin.add("default", textButtonStyle);

        final TextButton textButton = new TextButton("PLAY", skin);
        textButton.setPosition(0, 0);
        textButton.setWidth(200);
        textButton.setHeight(200);
        stage.addActor(textButton);
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                textButton.setChecked(false);
                Gdx.app.postRunnable(() -> {
                    if (!Gdx.graphics.isFullscreen())
                        Gdx.app.getGraphics().setFullscreenMode(Gdx.app.getGraphics().getDisplayMode());
                    else
                        Gdx.app.getGraphics().setWindowedMode(960, 540);
                });
            }
        });
        stage.setViewport(viewport);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        time += Gdx.graphics.getDeltaTime();
        mapCamera.update();
        camera.update();
        renderer.setView(mapCamera);
        renderer.render();
        //stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        //stage.draw();

        batch.setProjectionMatrix(mapCamera.combined);
        batch.begin();
        //batch.setProjectionMatrix(mapCamera.combined);
        batch.draw(buffer.getColorBufferTexture(), 0, 0);
        batch.draw(ez.getKeyFrame(time), pos.x, pos.y, 32, 32);
        batch.draw(lawbringer.getKeyFrame(time), lawbringerpos.x, lawbringerpos.y, 32, 32);
        batch.draw(lawbringer32, 128, 128, 32, 32);
        batch.setProjectionMatrix(camera.combined);
        font.draw(batch, Gdx.graphics.getFramesPerSecond() + "", 0, viewport.getWorldHeight());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        batch.dispose();
        stage.dispose();
        skin.dispose();
        bfont.dispose();
        font.dispose();
        map.dispose();
        buffer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                lawbringerpos.x -= 32;
                break;
            case Input.Keys.LEFT:
                mapCamera.translate(-32, 0);
                break;
            case Input.Keys.D:
                lawbringerpos.x += 32;
                break;
            case Input.Keys.RIGHT:
                mapCamera.translate(32, 0);
                break;
            case Input.Keys.W:
                lawbringerpos.y += 32;
                break;
            case Input.Keys.UP:
                mapCamera.translate(0, 32);
                break;
            case Input.Keys.S:
                lawbringerpos.y -= 32;
                break;
            case Input.Keys.DOWN:
                mapCamera.translate(0, -32);
                break;
            case Input.Keys.G:
                mapCamera.zoom += 1;
                break;
            case Input.Keys.H:
                mapCamera.zoom -= 1;
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
        switch (character) {
            case 'x':
                camera.translate(10, 0);
                break;
            case 'y':
                camera.translate(0, 10);
                break;
            case 'X':
                camera.translate(-10, 0);
                break;
            case 'Y':
                camera.translate(0, -10);
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
            Vector3 position = mapCamera.unproject(clickCoordinates);
            pos.set(position.x - position.x % 32, position.y - position.y % 32);
        } else {
            drag = true;
            lastTouch.set(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        mapCamera.position.set(mapCamera.position.x - mapCamera.position.x % 32, mapCamera.position.y - mapCamera.position.y % 32, mapCamera.position.z);
        drag = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!drag) return false;
        Vector2 newTouch = new Vector2(screenX, screenY);
        // delta will now hold the difference between the last and the current touch positions
        // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
        Vector2 delta = newTouch.cpy().sub(lastTouch);
        lastTouch = newTouch;
        mapCamera.translate(-delta.x, delta.y, 0);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        mapCamera.zoom += amount;
        return false;
    }
}
