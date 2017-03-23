package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IVideoConfig;
import gg.al.game.AL;
import gg.al.game.ui.ALTabbedPane;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
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
    private OrthographicCamera cam;
    private SpriteBatch spriteBatch;
    private Texture mainbackground;

    private TextButton btVsync;
    private TextButton btFullScreen;
    private TextButton btTest;

    private TextButton btTabVideo;
    private TextButton btTabAudio;
    private TextButton btTabInput;

    private Table tableVideo;

    private ALTabbedPane tabbedPane;

    private HashMap<TextButton, Table> componentMap;




    @Override
    public void show() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080);
        viewport.setCamera(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = AL.asset.get(Assets.PT_TEXTBUTTON_JSON);
        int x = 1920;
        int y = 1080;
        spriteBatch = new SpriteBatch();
        mainbackground = AL.asset.get(Assets.PT_TESTMAINSCREEN);
        componentMap = new HashMap<>();



        tabbedPane = new ALTabbedPane(skin, x, y, componentMap);

        // Init Tabs:
        btTabVideo = new TextButton("Video", skin);
        btTabVideo.setSize(300,50);
        tabbedPane.addTab(btTabVideo);

        btTabAudio = new TextButton("Audio", skin);
        btTabAudio.setSize(300,50);
        tabbedPane.addTab(btTabAudio);

        btTabInput = new TextButton("Input", skin);
        btTabInput.setSize(300,50);
        tabbedPane.addTab(btTabInput);


        String vsyncText = AL.cvideo.vsyncEnabled() == true ? "Vsync on" : "Vsync off";
        btVsync = new TextButton(vsyncText, skin, "default");
        btVsync.setWidth(250);
        btVsync.setHeight(50);
        btVsync.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                vsyncOnOff();
            }
        });


        btFullScreen = new TextButton(AL.cvideo.fullscreen() == true ? "Fullscreen on":"Fullscreen off", skin);
        btFullScreen.setWidth(250);
        btFullScreen.setHeight(50);
        btFullScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fullscreenOnOff();
            }
        });

        tableVideo = new Table();
        tableVideo.add(btVsync).pad(10);
        tableVideo.row();
        tableVideo.add(btFullScreen).pad(10);
        tableVideo.row();

        componentMap.put(btTabVideo, tableVideo);


        stage.addActor(tabbedPane);

        AL.input.setInputProcessor(new InputMultiplexer(stage, this));

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
        stage.dispose();
        spriteBatch.dispose();
    }

    @Override
    public void dispose() {
        AL.asset.unload(Assets.PT_TEXTBUTTON_JSON.fileName);
        AL.asset.unload(Assets.PT_TESTMAINSCREEN.fileName);
    }

    private void vsyncOnOff() {
        AL.cedit.setValue(IVideoConfig.VideoKeys.VSYNC, !AL.cvideo.vsyncEnabled());
        AL.cedit.flush();
        btVsync.setText(AL.cvideo.vsyncEnabled() == true ? "Vsync on" : "Vsync off");
    }

    private void fullscreenOnOff() {
        AL.cedit.setValue(IVideoConfig.VideoKeys.FULLSCREEN, !AL.cvideo.fullscreen());
        AL.cedit.flush();
        btFullScreen.setText(AL.cvideo.fullscreen() == true ? "Fullscreen on" : "Fullscreen off");
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(Assets.PT_TEXTBUTTON_JSON, Assets.PT_TESTMAINSCREEN);
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
