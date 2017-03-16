package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.config.IVideoConfig;
import gg.al.game.ArcadeLegendsGame;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * Created by Patrick Windegger on 16.03.2017.
 */
@Slf4j
public class MainMenuScreen extends ArcadeScreen {

    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    private int x;
    private int y;

    public MainMenuScreen(ArcadeLegendsGame game) {
        super(game);
    }

    @Override
    public void show() {
        // Inits:
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("assets/prototype/styles/buttonfont/textbuttonstyles.json"));
        x = Gdx.graphics.getWidth();
        y = Gdx.graphics.getHeight();

        // Buttons:
        TextButton btPlay = new TextButton("Play", skin, "default");
        btPlay.setWidth(200);
        btPlay.setHeight(50);
        btPlay.setPosition(x / 2 - 100, y / 5 + y / 2);

        /*
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeSize();

            }
        });
        */

        TextButton btSettings = new TextButton("Settings", skin, "default");
        btSettings.setWidth(200);
        btSettings.setHeight(50);
        btSettings.setPosition(x/2-100, y/5 + y/3);
        btSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });


        TextButton btExit = new TextButton("Exit Game", skin, "default");
        btExit.setWidth(200);
        btExit.setHeight(50);
        btExit.setPosition(x/2-100, y/5 + y/6);

        stage.addActor(btPlay);
        stage.addActor(btSettings);
        stage.addActor(btExit);



        OrthographicCamera cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
        stage.setViewport(viewport);

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
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.HEIGHT, 500);
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.WIDTH, 500);
        game.config.editor.flush();
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
