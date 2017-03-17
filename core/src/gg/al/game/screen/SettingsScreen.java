package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
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
import gg.al.game.ArcadeLegendsGame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * Created by Patrick Windegger on 16.03.2017.
 */
public class SettingsScreen extends ArcadeScreen {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    private int x;
    private int y;

    private TextButton btVsync;
    private TextButton btFullScreen;


    public SettingsScreen(ArcadeLegendsGame game) {
        super(game);
    }

    @Override
    public void show() {
        OrthographicCamera cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = new Skin(Gdx.files.internal("assets/prototype/styles/buttonfont/textbuttonstyles.json"));
        Gdx.input.setInputProcessor(stage);
        x = Gdx.graphics.getWidth();
        y = Gdx.graphics.getHeight();

        String vsyncText = game.config.video.vsyncEnabled() == true ? "Vsync on":"Vsync off";
        btVsync = new TextButton(vsyncText, skin, "default");
        btVsync.setWidth(200);
        btVsync.setHeight(50);
        btVsync.setPosition(x/2-100, y/2-25);
        btVsync.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                vsyncOnOff();
            }
        });

        String fullscreenText = game.config.video.fullscreen() == true ? "FULLSCREEN on" : "FULLSCREEN off";
        log.debug(fullscreenText);
        btFullScreen = new TextButton(fullscreenText, skin, "default");
        btFullScreen.setWidth(300);
        btFullScreen.setHeight(50);
        btFullScreen.setPosition(x/2-100, y/3-25);
        btFullScreen.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                fullscreenOnOff();
            }
        });

        stage.addActor(btVsync);
        stage.addActor(btFullScreen);




    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        x = Gdx.graphics.getWidth();
        y = Gdx.graphics.getHeight();
        btVsync.setPosition(x/2-100, y/2-25);
        btFullScreen.setPosition(x/2-100, y/3-25);

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
    }

    @Override
    public void dispose() {

    }

    private void vsyncOnOff()
    {
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.VSYNC, !game.config.video.vsyncEnabled());
        game.config.editor.flush();
        btVsync.setText(game.config.video.vsyncEnabled() == true ? "Vsync on":"Vsync off");
        log.debug(game.config.video.vsyncEnabled()+"");
    }

    private void fullscreenOnOff()
    {
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.FULLSCREEN, !game.config.video.fullscreen());
        game.config.editor.flush();
        btFullScreen.setText(game.config.video.fullscreen() == true ? "FULLSCREEN on" : "FULLSCREEN off");
        log.debug(game.config.video.fullscreen()+"");
    }
}
