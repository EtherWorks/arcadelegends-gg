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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import gg.al.game.ui.KeyInputDialog;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Created by Patrick Windegger on 24.04.2017.
 * Class responsible for managing Input Settings with UI
 */
@Slf4j
public class InputSettingsScreen implements IAssetScreen, InputProcessor {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private OrthographicCamera cam;
    private SpriteBatch spriteBatch;
    private Texture mainbackground;

    private ScrollPane scrollPane;
    private Table inputTable;
    private TextButton btBack;

    private KeyInputDialog dialog;

    private IInputConfig.InputKeys[] keys;
    private HashMap<String, Integer> keyMap;

    private Assets.SettingsAssets settingsAssets;


    /**
     * Method responsible for initializing the main menu
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
        BitmapFont font = settingsAssets.bocklin_fnt;
        TextureRegion backgroundTexture = new TextureRegion(settingsAssets.background_textbutton);

        inputTable = new Table();
        keys = IInputConfig.InputKeys.values();
        keyMap = new HashMap<>();

        for (int i = 0; i < keys.length; i++) {
            Label lbKey = new Label(keys[i].getKeyName().substring(0, 1).toUpperCase() + keys[i].getKeyName().substring(1), skin);
            lbKey.setAlignment(Align.center);
            keyMap.put(keys[i].getKeyName(), i);


            TextButton btKey = new TextButton(Input.Keys.toString(IInputConfig.InputKeys.getFromKey(keys[i], AL.getInputConfig())), skin);
            btKey.setText(btKey.getText().toString().toUpperCase());
            btKey.center();
            btKey.setName(keys[i].getKeyName());

            btKey.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showDialog(font, backgroundTexture, btKey.getName(), btKey);


                }
            });
            inputTable.add(lbKey).pad(10).fill();
            inputTable.add(btKey).pad(10).fill();
            inputTable.row();
        }


        scrollPane = new ScrollPane(inputTable, skin);
        scrollPane.setSize(500, 900);
        scrollPane.setPosition(x / 2 - 250, y / 2 - 400);

        stage.addActor(scrollPane);

        btBack = new TextButton("Back", skin);
        btBack.setSize(300, 50);
        btBack.setPosition(AL.graphics.getWidth() / 2 - 150, AL.graphics.getWidth() / 22);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.getGame().setScreen(AL.getScreenManager().get(SettingsScreen.class));
            }
        });
        stage.addActor(btBack);


        AL.input.setInputProcessor(new InputMultiplexer(stage, this));

    }

    /**
     * Method responsible for making the dialog visible
     *
     * @param font          {@link BitmapFont} font of the dialog
     * @param textureRegion {@link TextureRegion} background texture for the dialog
     * @param name          {@link String} name of the key
     * @param button        {@link TextButton} button of the input key from the {@link InputSettingsScreen}
     */
    private void showDialog(BitmapFont font, TextureRegion textureRegion, String name, TextButton button) {
        Drawable dlgBackground = new TextureRegionDrawable(new TextureRegion(settingsAssets.dlgbackground));
        dialog = new KeyInputDialog("", new Window.WindowStyle(font, Color.BLACK, new TextureRegionDrawable(new TextureRegion(textureRegion))), skin, stage, font,
                Input.Keys.toString(IInputConfig.InputKeys.getFromKey(keys[keyMap.get(name)], AL.getInputConfig())), keys[keyMap.get(name)].getKeyName(), button);
        dialog.initDialog(dlgBackground);
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
        viewport.update(width, height);
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
     * @param keycode of the key
     * @return java.lang.Boolean
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
            AL.getGame().setScreen(AL.getScreenManager().get(SettingsScreen.class));
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
     * Method responsible for returning {@link gg.al.util.Assets.MenuAssets}
     *
     * @return Object
     */
    @Override
    public Object assets() {
        return settingsAssets = new Assets.SettingsAssets();
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
}
