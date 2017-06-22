package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.util.Assets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 18.03.2017.<br>
 * {@link ILoadingScreen} used as the default LoadingScreen in {@link gg.al.game.ArcadeLegendsGame}.
 */
@Slf4j
public class DefaultLoadingScreen implements ILoadingScreen {

    private boolean init;
    private BitmapFont font;
    private SpriteBatch batch;

    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private OrthographicCamera cam;
    private Assets.LoadingScreenAssets loadingScreenAssets;

    private ProgressBar loadingScreenBar;

    @Getter
    @Setter
    private Screen next;

    public DefaultLoadingScreen() {

    }

    public DefaultLoadingScreen(Screen next) {
        this();
        this.next = next;
    }

    @Override
    public Screen withAssetScreen(IAssetScreen screen) {
        AL.getAssetManager().loadAssetFields(screen.assets());
        next = screen;
        return this;
    }

    public void show() {
        if (!init) {
            loadingScreenAssets = new Assets.LoadingScreenAssets();
            AL.getAssetManager().loadAssetFields(loadingScreenAssets);
            AL.getAssetManager().finishLoading();
            font = new BitmapFont();
            batch = new SpriteBatch();

            cam = new OrthographicCamera();
            viewport = new FitViewport(1920, 1080);
            viewport.setCamera(cam);
            stage = new Stage(viewport);
            stage.setViewport(viewport);
            skin = loadingScreenAssets.styles_json;

            loadingScreenBar = new ProgressBar(0, 100, 1, false, skin);
            loadingScreenBar.setPosition(25, -10);
            loadingScreenBar.setSize(1890, 50);

            stage.addActor(loadingScreenBar);
        }
        init = true;
    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0, 0, 0, 1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (AL.getAssetManager().update() && next != null)
            AL.getGame().setScreen(next);

        batch.begin();
        font.draw(batch, String.format("%1.0f%%", AL.getAssetManager().getProgress() * 100), 0, 15);
        loadingScreenBar.setValue(AL.getAssetManager().getProgress() * 100);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        AL.getAssetManager().unloadAssetFields(loadingScreenAssets);
        stage.dispose();
        batch.dispose();
    }

}
