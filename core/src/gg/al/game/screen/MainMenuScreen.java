package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IVideoConfig;
import gg.al.game.AL;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Patrick Windegger on 16.03.2017.
 * Class responsible for the Main-Menu in the game
 */
@Slf4j
public class MainMenuScreen implements IAssetScreen {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private Texture mainbackground;
    private OrthographicCamera cam;

    private TextButton btPlay;
    private TextButton btSettings;
    private TextButton btExit;


    @Override
    public void show() {
        AL.getAudioManager().registerMusics(Assets.PT_BITRUSH);
        AL.getAudioManager().playMusic(Assets.PT_BITRUSH);
        // Inits:
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080);
        viewport.setCamera(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = AL.getAssetManager().get(Assets.PT_STYLES_JSON);
        skin.getFont("bocklin").getData().setScale(0.8f, 0.8f);
        int x = 1920;
        int y = 1080;
        spriteBatch = new SpriteBatch();
        mainbackground = AL.getAssetManager().get(Assets.PT_TESTMAINSCREEN);


        // Buttons:
        btPlay = new TextButton("Play", skin, "default");
        btPlay.setWidth(400);
        btPlay.setHeight(100);
        btPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!AL.getScreenManager().isRegistered(LevelScreen.class))
                    AL.getScreenManager().register(new LevelScreen(Assets.PT_TEST), LevelScreen.class);
                AL.getGame().setScreen(AL.getScreenManager().get(LevelScreen.class));
            }
        });

        btSettings = new TextButton("Settings", skin, "default");
        btSettings.setWidth(400);
        btSettings.setHeight(100);

        btSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
               SettingsScreen s = AL.getScreenManager().get(SettingsScreen.class, true);
                s.setPreviousScreen(MainMenuScreen.this);
                AL.getGame().setScreen(s);

            }
        });

        btExit = new TextButton("Exit Game", skin, "default");
        btExit.setWidth(400);
        btExit.setHeight(100);
        btExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.app.exit();
            }
        });

        Table table = new Table();
        table.add(btPlay).width(400).pad(10);
        table.row();
        table.add(btSettings).width(400).pad(10);
        table.row();
        table.add(btExit).width(400).pad(10);
        table.setPosition(x / 2, y / 2 + 50);

        stage.addActor(table);

        AL.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0, 0, 0, 1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        spriteBatch.draw(mainbackground, 0, 0);
        spriteBatch.end();


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
        AL.input.setInputProcessor(null);
        AL.getAudioManager().stopMusic(Assets.PT_BITRUSH);
        AL.getAudioManager().unregisterMusics(Assets.PT_BITRUSH);
        stage.dispose();
        spriteBatch.dispose();
    }

    @Override
    public void dispose() {
        AL.getAssetManager().unload(Assets.PT_STYLES_JSON.fileName);
        AL.getAssetManager().unload(Assets.PT_BITRUSH.fileName);
        AL.getAssetManager().unload(Assets.PT_TESTMAINSCREEN.fileName);
    }

    private void changeSize() {
        AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.height, AL.getVideoConfig().height() + 10);
        AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.width, AL.getVideoConfig().width() + 10);
        AL.getConfigEditor().flush();
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(Assets.PT_STYLES_JSON, Assets.PT_TESTMAINSCREEN, Assets.PT_BITRUSH);
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    // Sets the position of all TextButtons on the Screen
    private void setButtonPosition(int x, int y) {
        btPlay.setPosition(x / 2 - 200, y / 5 + y / 2);
        btSettings.setPosition(x / 2 - 200, y / 5 + y / 2.6f);
        btExit.setPosition(x / 2 - 200, y / 5 + y / 5);
    }
}
