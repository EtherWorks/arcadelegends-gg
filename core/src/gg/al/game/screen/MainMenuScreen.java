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


/**
 * Created by Patrick Windegger on 16.03.2017.
 */
@Slf4j
public class MainMenuScreen extends ArcadeScreen {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    public MainMenuScreen(ArcadeLegendsGame game) {
        super(game);
    }

    @Override
    public void show() {
        // Inits:
        OrthographicCamera cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage = new Stage(viewport);
        stage.setViewport(viewport);
        skin = new Skin(Gdx.files.internal("assets/prototype/styles/buttonfont/textbuttonstyles.json"));
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
        btSettings.setPosition(x/2-100, y/5 + y/3);
        btSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });


        TextButton btExit = new TextButton("Exit Game", skin, "default");
        btExit.setWidth(200);
        btExit.setHeight(50);
        btExit.setPosition(x/2-100, y/5 + y/6);

        stage.addActor(btPlay);
        stage.addActor(btSettings);
        stage.addActor(btExit);





        Gdx.input.setInputProcessor(stage);
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
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
       // game.config.editor.setValue(IVideoConfig.VideoKeyNames.HEIGHT, 500);
        //game.config.editor.setValue(IVideoConfig.VideoKeyNames.WIDTH, 500);
        //game.config.editor.flush();
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void dispose() {

    }

    private void changeSize() {
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.HEIGHT, game.config.video.height() + 10);
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.WIDTH, game.config.video.width() + 10);
        game.config.editor.flush();
    }


}
