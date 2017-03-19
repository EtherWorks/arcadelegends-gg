package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
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


/**
 * Created by Patrick Windegger on 16.03.2017.
 */
@Slf4j
public class MainMenuScreen implements AssetScreen {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    @Override
    public void show() {
        // Inits:
        OrthographicCamera cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        //skin = new Skin(Gdx.files.internal("assets/prototype/styles/buttonfont/textbuttonstyles.json"));
        skin = AL.asset.get(Assets.TEXTBUTTONSTYLES);
        int x = Gdx.graphics.getWidth();
        int y = Gdx.graphics.getHeight();

        // Buttons:
        TextButton btPlay = new TextButton("Play", skin, "default");
        btPlay.setWidth(200);
        btPlay.setHeight(50);
        btPlay.setPosition(x / 2 - 100, y / 5 + y / 2);

        TextButton btSettings = new TextButton("Settings", skin, "default");
        btSettings.setWidth(200);
        btSettings.setHeight(50);
        btSettings.setPosition(x / 2 - 100, y / 5 + y / 3);
        btSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!AL.screen.isRegistered(SettingsScreen.class))
                    AL.screen.register(new SettingsScreen(), SettingsScreen.class);
                AL.game.setScreen(AL.screen.get(SettingsScreen.class));
            }
        });


        TextButton btExit = new TextButton("Exit Game", skin, "default");
        btExit.setWidth(200);
        btExit.setHeight(50);
        btExit.setPosition(x / 2 - 100, y / 5 + y / 6);

        stage.addActor(btPlay);
        stage.addActor(btSettings);
        stage.addActor(btExit);


        AL.input.setInputProcessor(stage);
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
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //game.config.editor.setValue(IVideoConfig.VideoKeyNames.HEIGHT, 500);
        //game.config.editor.setValue(IVideoConfig.VideoKeyNames.WIDTH, 500);
        //game.config.editor.flush();
        AL.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        AL.asset.unload(Assets.TEXTBUTTONSTYLES.fileName);
        stage.dispose();
    }

    private void changeSize() {
        AL.cedit.setValue(IVideoConfig.VideoKeys.HEIGHT, AL.cvideo.height() + 10);
        AL.cedit.setValue(IVideoConfig.VideoKeys.WIDTH, AL.cvideo.width() + 10);
        AL.cedit.flush();
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(Assets.TEXTBUTTONSTYLES);
    }
}
