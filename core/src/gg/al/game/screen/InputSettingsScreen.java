package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import gg.al.util.Assets;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Patrick Windegger on 24.04.2017.
 * Class responsible for managing Input Settings with UI
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



    @Override
    public void show() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080);
        viewport.setCamera(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = AL.asset.get(Assets.PT_STYLES_JSON);
        int x = 1920;
        int y = 1080;
        spriteBatch = new SpriteBatch();
        mainbackground = AL.asset.get(Assets.PT_TESTMAINSCREEN);

        inputTable = new Table();
        IInputConfig.InputKeys[] keys = IInputConfig.InputKeys.values();
        for (int i = 0; i < keys.length; i++) {
            Label lbKey = new Label(keys[i].getKeyName(), skin);
            lbKey.setAlignment(Align.center);

            TextButton btKey = new TextButton(IInputConfig.InputKeys.getFromKey(keys[i], AL.config.input) + "", skin);
            btKey.center();
            inputTable.add(lbKey).pad(10).fill();
            inputTable.add(btKey).pad(10).fill();
            inputTable.row();
        }

        scrollPane = new ScrollPane(inputTable, skin);
        scrollPane.setSize(300,500);
        scrollPane.setPosition(x / 2-150, y / 2-250);
        stage.addActor(scrollPane);




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
        AL.asset.unload(Assets.PT_STYLES_JSON.fileName);
        AL.asset.unload(Assets.PT_TESTMAINSCREEN.fileName);
        AL.asset.unload(Assets.PT_BACKGROUND_TEXTBUTTON.fileName);
        AL.asset.unload(Assets.PT_BOCKLIN.fileName);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE)
            AL.game.setScreen(AL.screen.get(SettingsScreen.class));
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
        return Arrays.asList(Assets.PT_STYLES_JSON, Assets.PT_TESTMAINSCREEN, Assets.PT_BACKGROUND_TEXTBUTTON, Assets.PT_BOCKLIN);
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }
}
