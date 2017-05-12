package gg.al.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import gg.al.game.AL;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Thomas Neumann on 18.03.2017.<br />
 * {@link ILoadingScreen} used as the default LoadingScreen in {@link gg.al.game.ArcadeLegendsGame}.
 */
@Slf4j
public class DefaultLoadingScreen implements ILoadingScreen {

    private final List<AssetDescriptor> toLoad;
    private boolean init;
    private BitmapFont font;
    private SpriteBatch batch;

    @Getter
    @Setter
    private Screen next;

    public DefaultLoadingScreen() {
        this.toLoad = new ArrayList<>();
    }

    public DefaultLoadingScreen(Screen next) {
        this();
        this.next = next;
    }

    @Override
    public Screen withAssetScreen(IAssetScreen screen) {
        boolean loaded = true;
        for (AssetDescriptor desc : screen.assets())
            if (!AL.getAssetManager().isLoaded(desc.fileName, desc.type)) {
                loaded = false;
                break;
            }
        if (loaded) {
            for (AssetDescriptor desc : screen.assets())
                AL.getAssetManager().setReferenceCount(desc.fileName, AL.getAssetManager().getReferenceCount(desc.fileName) + 1);
            return screen;
        }
        toLoad.clear();
        toLoad.addAll(screen.assets());
        next = screen;
        return this;
    }

    public boolean addToLoad(Collection<? extends AssetDescriptor> collection) {
        return toLoad.addAll(collection);
    }

    public boolean addToLoad(AssetDescriptor assetDescriptor) {
        return toLoad.add(assetDescriptor);
    }

    public void resetToLoad() {
        toLoad.clear();
    }

    @Override
    public void show() {
        if (!init) {
            font = new BitmapFont();
            batch = new SpriteBatch();
        }
        init = true;
        for (AssetDescriptor desc : toLoad) {
            AL.getAssetManager().load(desc);
        }
    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0, 0, 0, 1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (AL.getAssetManager().update() && next != null)
            AL.getGame().setScreen(next);

        batch.begin();
        font.draw(batch, String.format("%1.0f%%", AL.getAssetManager().getProgress() * 100), 0, 15);
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
