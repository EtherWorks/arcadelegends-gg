package gg.al.prototype.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.prototype.ArcadeLegends;

import java.util.Map;


/**
 * Created by Thomas Neumann on 16.02.2017.
 */
public class LoadingScreen implements Screen {

    private final ArcadeLegends game;
    private final AssetManager manager;
    private final Map<String, Class> toLoad;
    private Screen nextScreen;
    private SpriteBatch batch;
    private Camera camera;
    private Viewport viewport;
    private float time;
    private Animation<TextureRegion> waitAnim;
    private BitmapFont percFont;


    public LoadingScreen(final ArcadeLegends game, Map<String, Class> toLoad, Screen nextScreen) {
        this.game = game;
        this.manager = game.getAssetManager();
        this.nextScreen = nextScreen;
        this.toLoad = toLoad;
        time = 0;
    }

    public LoadingScreen(final ArcadeLegends game, Map<String, Class> toLoad) {
        this.game = game;
        this.manager = game.getAssetManager();
        this.toLoad = toLoad;
        time = 0;
    }

    public void setNextScreen(Screen nextScreen) {
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {
        if (!manager.isLoaded("assets/prototype/sprites/ez_idle.png")) {
            manager.load("assets/prototype/sprites/ez_idle.png", Texture.class);
            manager.finishLoadingAsset("assets/prototype/sprites/ez_idle.png");
        }
        if (percFont == null) {
            percFont = new BitmapFont();
        }
        if (waitAnim == null) {
            Texture ezidle = manager.get("assets/prototype/sprites/ez_idle.png", Texture.class);
            TextureRegion[][] tmp = TextureRegion.split(ezidle, ezidle.getWidth() / 4, ezidle.getHeight());
            TextureRegion[] frames = new TextureRegion[4 * 1];
            int ind = 0;
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    frames[ind++] = tmp[i][j];
                }
            }
            waitAnim = new Animation<>(.3f, frames);
            waitAnim.setPlayMode(Animation.PlayMode.LOOP);
        }
        if (batch == null)
            batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        viewport.update(1920, 1080, true);
        for (Map.Entry<String, Class> entry : toLoad.entrySet()) {
            manager.load(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (manager.update())
            nextScreen();
        time += Gdx.graphics.getDeltaTime();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(waitAnim.getKeyFrame(time), 0, 0, 128, 128);
        percFont.draw(batch, (int) (manager.getProgress() * 100) + "%", viewport.getWorldWidth() - 40, 20);
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
        manager.unload("assets/sprites/ez_idle.png");
    }

    public void nextScreen() {
        if (nextScreen != null)
            game.setScreen(nextScreen);
    }
}
