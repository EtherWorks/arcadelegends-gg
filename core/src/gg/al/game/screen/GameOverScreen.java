package gg.al.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.util.Assets;

/**
 * Created by Patrick Windegger on 12.06.2017.
 */
public class GameOverScreen implements IAssetScreen {

    private Assets.MenuAssets menuAssets;

    private Stage stage;
    private Skin skin;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private Texture mainbackground;
    private OrthographicCamera cam;
    private String endgameText;

    private Label endgameLabel;
    private TextButton btBackToMainMenu;




    @Override
    public Object assets() {
        return menuAssets = new Assets.MenuAssets();
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    @Override
    public void show() {
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


        btBackToMainMenu = new TextButton("Back to Main Menu", skin);
        btBackToMainMenu.center();
        btBackToMainMenu.setWidth(600);
        btBackToMainMenu.setHeight(80);
        btBackToMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AL.getGame().setScreen(AL.getScreenManager().get(MainMenuScreen.class));
            }
        });
        btBackToMainMenu.setPosition(AL.graphics.getWidth() / 2 - 300, AL.graphics.getHeight() / 2 +40);
        stage.addActor(btBackToMainMenu);

        endgameLabel = new Label(endgameText, skin);
        endgameLabel.setWidth(600);
        endgameLabel.setHeight(80);
        endgameLabel.setAlignment(Align.center);
        endgameLabel.setPosition(AL.graphics.getWidth() / 2 - 300, AL.graphics.getHeight() / 1.5f);
        stage.addActor(endgameLabel);

        AL.input.setInputProcessor(stage);
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
        AL.input.setInputProcessor(null);
        stage.dispose();
        spriteBatch.dispose();
    }

    @Override
    public void dispose() {
        AL.getAssetManager().unloadAssetFields(menuAssets);
    }

    public void setEndgameText(String endgameText) {
        this.endgameText = endgameText;
    }
}
