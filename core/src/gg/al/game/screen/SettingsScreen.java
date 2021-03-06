package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IAudioConfig;
import gg.al.config.IVideoConfig;
import gg.al.game.AL;
import gg.al.game.ui.SettingsTabbedPane;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;


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
    private SelectBox sbResolution;
    private SelectBox sbFps;
    private Slider volumeSlider;
    private Slider musicSlider;
    private Slider effectSlider;
    private TextButton btBack;

    private TextButton btTabVideo;
    private TextButton btTabAudio;
    private TextButton btTabInput;

    private Table tableVideo;
    private Table tableAudio;

    private SettingsTabbedPane tabbedPane;

    private HashMap<TextButton, Table> componentMap;
    private SpriteDrawable selection;
    private Texture selectionTexture;

    private Assets.SettingsAssets settingsAssets;

    private IAssetScreen previousScreen;

    /**
     * Method responsible for initializing the settings menu
     * Called when the screen becomes the current screen of the game
     */
    @Override
    public void show() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080);
        viewport.setCamera(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = settingsAssets.styles_json;
        int x = 1920;
        int y = 1080;
        spriteBatch = new SpriteBatch();
        mainbackground = settingsAssets.testmainscreen;
        componentMap = new HashMap<>();
        selectionTexture = settingsAssets.background_textbutton;
        Sprite sprite = new Sprite(selectionTexture);
        selection = new SpriteDrawable(sprite);

        tabbedPane = new SettingsTabbedPane(skin, x, y, componentMap);

        initTabs();

        createVideoTable();
        createAudioTable();

        componentMap.put(btTabVideo, tableVideo);
        componentMap.put(btTabAudio, tableAudio);
        tabbedPane.setCurrentTab(btTabVideo);
        stage.addActor(tabbedPane);

        createBackButton();

        AL.input.setInputProcessor(new InputMultiplexer(stage, this));

    }

    /**
     * Method responsible for creating the Video Settings Table with the components
     */
    private void createVideoTable() {
        String vsyncText = AL.getVideoConfig().vsyncEnabled() ? "Vsync on" : "Vsync off";
        btVsync = new TextButton(vsyncText, skin, "default");
        btVsync.setWidth(250);
        btVsync.setHeight(50);
        btVsync.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                vsyncOnOff();
            }
        });

        TextureRegion backgroundTexture = new TextureRegion(settingsAssets.background_textbutton);
        BitmapFont font = settingsAssets.bocklin_fnt;
        font.getData().setScale(0.5f, 0.5f);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle listStyle = new com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle(font, Color.BLACK, new Color(255, 244, 0, 255), selection);
        listStyle.background = new TextureRegionDrawable(backgroundTexture);
        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle(font, new Color(255, 244, 0, 255), new TextureRegionDrawable(backgroundTexture), scrollPaneStyle, listStyle);

        String[] resolutions = {"Fullscreen", "Borderless", "1920x1080", "1680x1050",
                "1600x900", "1400x1050", "1280x1024", "1280x768",
                "1280x720", "1024x768", "1024x600", "800x600"};
        sbResolution = new SelectBox(selectBoxStyle);
        sbResolution.setItems(resolutions);
        sbResolution.setSelected(getResolution());
        sbResolution.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setResolution();
            }
        });

        Label lbResoultion = new Label("Resolution", skin);
        lbResoultion.setAlignment(Align.center);

        sbFps = new SelectBox(selectBoxStyle);
        sbFps.setItems(30, 60, 144);
        sbFps.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setFps();
            }
        });
        sbFps.setSelected(getCurrentFPS());

        Label lbFps = new Label("FPS", skin);
        lbFps.setAlignment(Align.center);


        tableVideo = new Table();
        tableVideo.add(btVsync).pad(10).fill().colspan(2);
        tableVideo.row();
        tableVideo.add(lbResoultion).pad(10).fill();
        tableVideo.add(sbResolution).pad(10).fill();
        tableVideo.row();
        tableVideo.add(lbFps).pad(10).fill();
        tableVideo.add(sbFps).pad(10).fill();

    }

    /**
     * Method responsible for creating the Audio Settings Table with the components
     */
    private void createAudioTable() {
        Label lbMasterVolume = new Label("Master Volume", skin);
        lbMasterVolume.setAlignment(Align.center);
        volumeSlider = new Slider(0, 100, 1, false, skin);
        volumeSlider.setValue(AL.getAudioConfig().masterVolume());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AL.getConfigEditor().setValue(IAudioConfig.AudioKeys.masterVolume, volumeSlider.getValue());
                AL.getConfigEditor().flush();
            }
        });

        TextButton musicOnOff = new TextButton("Music on", skin);
        musicOnOff.center();
        musicSlider = new Slider(0, 100, 1, false, skin);
        musicSlider.setValue(AL.getAudioConfig().musicVolume());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AL.getConfigEditor().setValue(IAudioConfig.AudioKeys.musicVolume, musicSlider.getValue());
                if (musicSlider.getValue() > 0) {
                    musicOnOff.setText("Music on");
                } else {
                    musicOnOff.setText("Music off");
                }
                AL.getConfigEditor().flush();
            }
        });
        musicOnOff.setText(AL.getAudioConfig().musicVolume() == 0 ? "Music off" : "Music on");

        musicOnOff.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch (musicOnOff.getText().toString()) {
                    case "Music on":
                        musicOnOff.setText("Music off");
                        AL.getConfigEditor().setValue(IAudioConfig.AudioKeys.musicVolume, 0);
                        musicSlider.setValue(0);
                        AL.getConfigEditor().flush();
                        break;
                    case "Music off":
                        musicOnOff.setText("Music on");
                        AL.getConfigEditor().setValue(IAudioConfig.AudioKeys.musicVolume, 100);
                        musicSlider.setValue(100);
                        AL.getConfigEditor().flush();
                        break;
                }
            }
        });

        Label lbEffects = new Label("Effects", skin);
        lbEffects.setAlignment(Align.center);
        effectSlider = new Slider(0, 100, 1, false, skin);
        effectSlider.setValue(AL.getAudioConfig().effectVolume());
        effectSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AL.getConfigEditor().setValue(IAudioConfig.AudioKeys.effectVolume, effectSlider.getValue());
                AL.getConfigEditor().flush();
            }
        });
        tableAudio = new Table();
        tableAudio.add(lbMasterVolume).pad(10).fill();
        tableAudio.add(volumeSlider).pad(10).fill();
        tableAudio.row();
        tableAudio.add(musicOnOff).pad(10).fill();
        tableAudio.add(musicSlider).pad(10).fill();
        tableAudio.row();
        tableAudio.add(lbEffects).pad(10).fill();
        tableAudio.add(effectSlider).pad(10).fill();


    }


    private void initTabs()
    {
        // Init Tabs:
        btTabVideo = new TextButton("Video", skin);
        btTabVideo.setSize(300, 50);
        tabbedPane.addTab(btTabVideo);

        btTabAudio = new TextButton("Audio", skin);
        btTabAudio.setSize(300, 50);
        tabbedPane.addTab(btTabAudio);

        btTabInput = new TextButton("Input", skin);
        btTabInput.setSize(300, 50);
        btTabInput.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!AL.getScreenManager().isRegistered(InputSettingsScreen.class))
                    AL.getScreenManager().register(new InputSettingsScreen(), InputSettingsScreen.class);
                AL.getGame().setScreen(AL.getScreenManager().get(InputSettingsScreen.class));
            }
        });
        tabbedPane.addTab(btTabInput);
    }

    private void createBackButton()
    {
        btBack = new TextButton("Back", skin);
        btBack.setSize(300, 50);
        btBack.setPosition(1920 / 2 - 150, 1080 / 22);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (previousScreen != null) {
                    AL.getGame().setScreen(previousScreen);
                }

            }
        });
        stage.addActor(btBack);
    }


    /**
     * Method responsible for rendering components
     * Called everytime when rendering need to be done
     *
     * @param delta
     */
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

    /**
     * Method responsible for resizing the application
     * Called everytime when the screen is re-sized
     *
     * @param width  - width of the window
     * @param height - height of the window
     */
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

    /**
     * Method responsible for hiding the screen
     */
    @Override
    public void hide() {
        AL.input.setInputProcessor(null);
        stage.dispose();
        spriteBatch.dispose();
    }

    /**
     * Method responsible for disposing the Assets
     * Called when the screen releases all resources
     */
    @Override
    public void dispose() {
        AL.getAssetManager().unloadAssetFields(settingsAssets);
    }

    /**
     * Mehthod responsible for turing VSync on or off
     */
    private void vsyncOnOff() {
        AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.vsyncEnabled, !AL.getVideoConfig().vsyncEnabled());
        AL.getConfigEditor().flush();
        btVsync.setText(AL.getVideoConfig().vsyncEnabled() == true ? "Vsync on" : "Vsync off");
    }

    /**
     * Method responsible for setting the resolution of the game
     */
    private void setResolution() {
        String resolution = (String) sbResolution.getSelected();
        log.debug(resolution);
        switch (resolution) {
            case "Fullscreen":
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.screenmode, IVideoConfig.ScreenMode.Fullscreen);
                AL.getConfigEditor().flush();
                break;
            case "Borderless":
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.width, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth());
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.height, (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.screenmode, IVideoConfig.ScreenMode.Borderless);
                AL.getConfigEditor().flush();
                break;
            default:
                int xSize = Integer.parseInt(resolution.split("x")[0]);
                int ySize = Integer.parseInt(resolution.split("x")[1]);
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.width, xSize);
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.height, ySize);
                AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.screenmode, IVideoConfig.ScreenMode.Windowed);
                AL.getConfigEditor().flush();
                break;
        }
    }

    private String getResolution() {
        switch (AL.getVideoConfig().screenmode()) {
            case Fullscreen:
                return "Fullscreen";
            case Borderless:
                return "Borderless";
            default:
                return AL.getVideoConfig().width() + "x" + AL.getVideoConfig().height();
        }
    }

    /**
     * Method responsible for setting the FPS (Frames per second) of the game
     */
    private void setFps() {
        int fps = (int) sbFps.getSelected();
        AL.getConfigEditor().setValue(IVideoConfig.VideoKeys.foregroundFPS, fps);
        AL.getConfigEditor().flush();
    }

    private int getCurrentFPS() {
        return AL.getVideoConfig().foregroundFPS();
    }


    /**
     * Method responsible for returning {@link gg.al.util.Assets.MenuAssets}
     *
     * @return Object
     */
    @Override
    public Object assets() {
        return settingsAssets = new Assets.SettingsAssets();
    }

    /**
     * @param keycode of the key
     * @return true when key was pressed
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE && previousScreen != null)
            AL.getGame().setScreen(previousScreen);
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


    /**
     * Method responsible for setting a custom loading screen
     *
     * @return the {@link ILoadingScreen}
     */
    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    public IAssetScreen getPreviousScreen() {
        return previousScreen;
    }

    public void setPreviousScreen(IAssetScreen previousScreen) {
        this.previousScreen = previousScreen;
    }
}
