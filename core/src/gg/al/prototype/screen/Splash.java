package gg.al.prototype.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.prototype.ArcadeLegends;
import gg.al.prototype.anim.ScaleAnimation;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Dr. Gavrel on 16.02.2017.
 */
public class Splash implements Screen, InputProcessor {

    public static final Map<String, Class> ASSETS;

    static {
        ASSETS = new HashMap<>();
        ASSETS.put("assets/sprites/splash/splash.png", Texture.class);
        ASSETS.put("assets/sprites/splash/background.jpg", Texture.class);
        ASSETS.put("assets/audio/bitrush.mp3", Music.class);
    }

    private final ArcadeLegends game;
    private final AssetManager manager;
    private Screen nextScreen;
    private Texture splash;
    private Texture background;
    private SpriteBatch batch;
    private Camera camera;
    private Viewport viewport;
    private ScaleAnimation animation;
    private Music music;
    private float time;

    public Splash(final ArcadeLegends game, Screen nextScreen) {
        this.game = game;
        this.manager = game.getAssetManager();
        this.nextScreen = nextScreen;
        time = 0;
    }

    public Splash(final ArcadeLegends game) {
        this.game = game;
        this.manager = game.getAssetManager();
        time = 0;
    }

    public void setNextScreen(Screen nextScreen) {
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {
        if (splash == null)
            splash = manager.get("assets/prototype/sprites/splash/splash.png", Texture.class);
        if (background == null)
            background = manager.get("assets/prototype/sprites/splash/background.jpg", Texture.class);
        if (batch == null)
            batch = new SpriteBatch();
        if (music == null)
            music = manager.get("assets/prototype/audio/bitrush.mp3", Music.class);
        music.setLooping(true);
        music.play();
        animation = new ScaleAnimation(splash, .5f, ScaleAnimation.ScaleMode.SMOOTH, 1, 1.5f, 1.6f, 1.5f);
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.update(1920, 1080, true);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        time += Gdx.graphics.getDeltaTime();
        if (time == 100) time = 0;
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        float ratio = splash.getHeight() / (float) splash.getWidth();
        int width = (int) camera.viewportWidth / 2;
        int height = (int) (camera.viewportWidth / 2 * ratio);
        animation.drawKeyFrame(batch, time,
                (int) camera.viewportWidth / 2 - animation.getKeyFrameWidth(time, width) / 2,
                (int) camera.viewportHeight / 2 - animation.getKeyFrameHeight(time, height) / 2,
                width,
                height);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.stop();
    }

    @Override
    public void dispose() {
        splash.dispose();
        batch.dispose();
        music.dispose();
    }

    public void nextScreen() {
        if (nextScreen != null)
            game.setScreen(nextScreen);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        nextScreen();
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        nextScreen();
        return true;
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