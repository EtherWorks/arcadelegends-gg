package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IVideoConfig;
import gg.al.game.AL;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
/**
 * Created by Patrick Windegger on 16.03.2017.
 * Class responsible for the different Settings (Video, Audio, Input) in the game
 */
public class SettingsScreen implements IAssetScreen, InputProcessor {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    private TextButton btVsync;
    private TextButton btFullScreen;
    private TextButton btTest;


    @Override
    public void show() {
        OrthographicCamera cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = AL.asset.get(Assets.PT_TEXTBUTTON_JSON);
        Gdx.input.setInputProcessor(new InputMultiplexer(this, stage));

        String vsyncText = AL.cvideo.vsyncEnabled() == true ? "Vsync on" : "Vsync off";
        btVsync = new TextButton(vsyncText, skin, "default");
        btVsync.setWidth(250);
        btVsync.setHeight(50);
        btVsync.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 25);
        btVsync.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                vsyncOnOff();
            }
        });

        String fullscreenText = AL.cvideo.fullscreen() == true ? "Fullscreen on" : "Fullscreen off";
        log.debug(fullscreenText);
        btFullScreen = new TextButton(fullscreenText, skin, "default");
        btFullScreen.setWidth(250);
        btFullScreen.setHeight(50);
        btFullScreen.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 3 - 25);
        btFullScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fullscreenOnOff();
            }
        });

        btTest = new TextButton("Test", skin, "default");
        btTest.setWidth(250);
        btTest.setHeight(50);
        btTest.setPosition(0,0);
        btTest.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.cedit.setValue(IVideoConfig.VideoKeys.WIDTH, 1920);
                AL.cedit.setValue(IVideoConfig.VideoKeys.HEIGHT, 1080);
                AL.cedit.flush();
            }
        });

        stage.addActor(btVsync);
        stage.addActor(btFullScreen);
        stage.addActor(btTest);


    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0, 0, 0, 1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        btVsync.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 25);
        btFullScreen.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 3 - 25);

        log.debug("in resize");
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        stage.dispose();
    }

    @Override
    public void dispose() {
        AL.asset.unload(Assets.PT_TEXTBUTTON_JSON.fileName);
    }

    private void vsyncOnOff() {
        AL.cedit.setValue(IVideoConfig.VideoKeys.VSYNC, !AL.cvideo.vsyncEnabled());
        AL.cedit.flush();
        btVsync.setText(AL.cvideo.vsyncEnabled() == true ? "Vsync on" : "Vsync off");
        log.debug(AL.cvideo.vsyncEnabled() + "");
    }

    private void fullscreenOnOff() {
        AL.cedit.setValue(IVideoConfig.VideoKeys.FULLSCREEN, !AL.cvideo.fullscreen());
        AL.cedit.flush();
        btFullScreen.setText(AL.cvideo.fullscreen() == true ? "Fullscreen on" : "Fullscreen off");
        log.debug(AL.cvideo.fullscreen() + "");
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(Assets.PT_TEXTBUTTON_JSON);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
            AL.game.setScreen(AL.screen.get(MainMenuScreen.class));
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

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }
}
