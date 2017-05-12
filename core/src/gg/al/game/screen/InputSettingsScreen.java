package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import gg.al.game.ui.ALDialog;
import gg.al.util.Assets;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Patrick Windegger on 24.04.2017.
 * Class responsible for managing InputComponent Settings with UI
 */
public class InputSettingsScreen implements IAssetScreen, InputProcessor {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private OrthographicCamera cam;
    private SpriteBatch spriteBatch;
    private Texture mainbackground;

    private ScrollPane scrollPane;
    private Table inputTable;

    private ALDialog dialog;


    @Override
    public void show() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080);
        viewport.setCamera(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = AL.getAssetManager().get(Assets.PT_STYLES_JSON);
        int x = 1920;
        int y = 1080;
        spriteBatch = new SpriteBatch();
        mainbackground = AL.getAssetManager().get(Assets.PT_TESTMAINSCREEN);
        BitmapFont font = AL.getAssetManager().get(Assets.PT_BOCKLIN_FNT);
        TextureRegion backgroundTexture = new TextureRegion(AL.getAssetManager().get(Assets.PT_BACKGROUND_TEXTBUTTON));

        inputTable = new Table();
        IInputConfig.InputKeys[] keys = IInputConfig.InputKeys.values();
        for (int i = 0; i < keys.length; i++) {
            Label lbKey = new Label(keys[i].getKeyName().substring(0, 1).toUpperCase() + keys[i].getKeyName().substring(1), skin);
            lbKey.setAlignment(Align.center);

            TextButton btKey = new TextButton(IInputConfig.InputKeys.getFromKey(keys[i], AL.getConfig().input) + "", skin);
            btKey.center();
            btKey.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Setzen der Inputs. Warten auf InputManager

                    // Dialog:
                    showDialog(font, backgroundTexture);


                }
            });
            inputTable.add(lbKey).pad(10).fill();
            inputTable.add(btKey).pad(10).fill();
            inputTable.row();
        }

        scrollPane = new ScrollPane(inputTable, skin);
        scrollPane.setSize(450, 500);
        scrollPane.setPosition(x / 2 - 225, y / 2 - 250);
        stage.addActor(scrollPane);


        AL.input.setInputProcessor(new InputMultiplexer(stage, this));

    }

    private void showDialog(BitmapFont font, TextureRegion textureRegion)
    {
        Drawable dlgBackground = new TextureRegionDrawable(new TextureRegion(AL.asset.get(Assets.PT_DLGBACKGROUND)));
        dialog = new ALDialog("", new Window.WindowStyle(font, Color.BLACK, new TextureRegionDrawable(new TextureRegion(textureRegion))),skin, stage, font);
        dialog.initDefaultDialog(dlgBackground);



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
        AL.getAssetManager().unload(Assets.PT_DLGBACKGROUND.fileName);
        AL.getAssetManager().unload(Assets.PT_STYLES_JSON.fileName);
        AL.getAssetManager().unload(Assets.PT_TESTMAINSCREEN.fileName);
        AL.getAssetManager().unload(Assets.PT_BACKGROUND_TEXTBUTTON.fileName);
        AL.getAssetManager().unload(Assets.PT_BOCKLIN_FNT.fileName);
    }

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

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(Assets.PT_STYLES_JSON, Assets.PT_TESTMAINSCREEN, Assets.PT_BACKGROUND_TEXTBUTTON, Assets.PT_BOCKLIN_FNT,
                Assets.PT_DLGBACKGROUND);
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }
}
