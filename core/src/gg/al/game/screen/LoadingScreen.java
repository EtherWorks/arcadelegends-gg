package gg.al.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gg.al.game.AL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Neumann on 18.03.2017.
 */
public class LoadingScreen implements Screen {

    private final List<AssetDescriptor> toLoad;
    private boolean init;
    private BitmapFont font;
    private SpriteBatch batch;
    private Screen next;

    public LoadingScreen() {
        this.toLoad = new ArrayList<>();
    }

    public LoadingScreen(Screen next) {
        this();
        this.next = next;
    }

    @Override
    public void show() {
        if (!init) {
            font = new BitmapFont();
            batch = new SpriteBatch();
        }
        init = true;
        for (AssetDescriptor desc : toLoad) {
            AL.asset.load(desc);
        }
    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0, 0, 0, 1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (AL.asset.update() && next != null)
            AL.game.setScreen(next);

        batch.begin();
        font.draw(batch, String.format("%1.0f%%", AL.asset.getProgress() * 100), 0, 15);
        batch.end();
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
        toLoad.clear();
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
