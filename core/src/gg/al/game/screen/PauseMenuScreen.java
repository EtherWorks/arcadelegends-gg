package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
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
import gg.al.game.AL;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Patrick Windegger on 02.06.2017.
 * Class responsible for providing a screen when the game is paused.
 */
@Slf4j
public class PauseMenuScreen implements IAssetScreen, InputProcessor {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private Texture mainbackground;
    private OrthographicCamera cam;

    private TextButton btBackToGame;
    private TextButton btBackToMainMenu;
    private TextButton btSettings;
    private TextButton btExitGame;

    private Assets.MenuAssets menuAssets;


    /**
     * Method responsible for initializing the pause menu
     * Called when the screen becomes the current screen of the game
     */
    @Override
    public void show() {
        // Inits:
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080);
        viewport.setCamera(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = menuAssets.styles_json;
        skin.getFont("bocklin").getData().setScale(0.8f, 0.8f);
        int x = 1920;
        int y = 1080;
        spriteBatch = new SpriteBatch();
        mainbackground = menuAssets.testmainscreen;

        btBackToGame = new TextButton("Back to game", skin);
        btBackToGame.center();
        btBackToGame.setWidth(600);
        btBackToGame.setHeight(100);
        btBackToGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.getGame().setScreen(AL.getScreenManager().get(LevelScreen.class));
            }
        });

        btBackToMainMenu = new TextButton("Back to main menu", skin);
        btBackToMainMenu.center();
        btBackToMainMenu.setWidth(600);
        btBackToMainMenu.setHeight(100);
        btBackToMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.getScreenManager().get(LevelScreen.class).setReInit(true);
                AL.getGame().setScreen(AL.getScreenManager().get(MainMenuScreen.class));
            }
        });

        btSettings = new TextButton("Settings", skin);
        btSettings.center();
        btSettings.setWidth(600);
        btSettings.setHeight(100);
        btSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SettingsScreen s = AL.getScreenManager().get(SettingsScreen.class, true);
                s.setPreviousScreen(PauseMenuScreen.this);
                AL.getGame().setScreen(s);
            }
        });

        btExitGame = new TextButton("Exit game", skin);
        btExitGame.center();
        btExitGame.setWidth(600);
        btExitGame.setHeight(100);
        btExitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.app.exit();
            }
        });

        Table table = new Table();
        table.add(btBackToGame).width(600).pad(10);
        table.row();
        table.add(btBackToMainMenu).width(600).pad(10);
        table.row();
        table.add(btSettings).width(600).pad(10);
        table.row();
        table.add(btExitGame).width(600).pad(10);
        table.setPosition(1920 / 2, 1080 / 2 + 50);


        stage.addActor(table);

        AL.input.setInputProcessor(new InputMultiplexer(stage, this));
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
        AL.getAssetManager().unloadAssetFields(menuAssets);
    }

    /**
     * Method responsible for disposing the Assets
     * Called when the screen releases all resources
     */
    @Override
    public void dispose() {
        AL.getAssetManager().unloadAssetFields(menuAssets);
    }

    /**
     * Method responsible for returning {@link gg.al.util.Assets.MenuAssets}
     *
     * @return Object
     */
    @Override
    public Object assets() {
        return menuAssets = new Assets.MenuAssets();
    }

    /**
     * Method responsible for setting a custom loading screen
     *
     * @return ILoadingScreen
     */
    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }


    /**
     * @param keycode of the key
     * @return java.lang.Boolean
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            AL.getGame().setScreen(AL.getScreenManager().get(LevelScreen.class));
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
