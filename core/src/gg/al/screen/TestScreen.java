package gg.al.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private Stage stage;
    private Skin skin;
    private BitmapFont bfont;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Animation<TextureRegion> ez;
    private float time;

    public TestScreen(Game game) {
        this.game = game;
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
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        Gdx.input.setInputProcessor(new InputMultiplexer(stage, this));

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
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, Gdx.graphics.getFramesPerSecond() + "", 0, viewport.getWorldHeight());
        batch.draw(ez.getKeyFrame(time), 100, 100, 128, 128);
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
