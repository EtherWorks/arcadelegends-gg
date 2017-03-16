package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * Created by Patrick Windegger on 16.03.2017.
 */
@Slf4j
public class UITestScreen extends ArcadeScreen {

    private Skin skin;
    private Stage stage;

    private Skin skin2;
    private Viewport viewport;

    public UITestScreen(ArcadeLegendsGame game) {
        super(game);
    }

    @Override
    public void show() {
        BitmapFont bmfont = new BitmapFont();
        skin = new Skin();
        skin.add("default", bmfont);
        stage = new Stage(new ScreenViewport());

        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGB888);
        map.setColor(Color.WHITE);
        map.fill();
        skin.add("default", new Texture(map));


        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("default", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("default", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("default", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");

        skin2 = new Skin(Gdx.files.internal("assets/prototype/styles/buttonfont/textbuttonstyles.json"));

        TextButton button = new TextButton("Click me", skin2, "default");
        button.setWidth(200);
        button.setHeight(50);
        button.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeSize();

            }
        });
        stage.addActor(button);

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

    private void changeSize()
    {
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.HEIGHT, game.config.video.height()+10);
        game.config.editor.setValue(IVideoConfig.VideoKeyNames.WIDTH, game.config.video.width()+10);
        game.config.editor.flush();
    }
}
