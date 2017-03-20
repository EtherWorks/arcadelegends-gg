package gg.al.game.screen;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;

import javax.swing.*;
import javax.swing.text.View;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas Neumann on 20.03.2017.<br />
 * Main {@link IAssetScreen} for loading and playing levels.
 */
public class LevelScreen implements IAssetScreen {

    private final AssetDescriptor<TiledMap> mapDesc;
    private TiledMap map;
    private OrthographicCamera cam;
    private Viewport viewport;

    private OrthogonalTiledMapRenderer mapRenderer;

    public LevelScreen(AssetDescriptor<TiledMap> mapDesc) {
        this.mapDesc = mapDesc;
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(mapDesc);
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    @Override
    public void show() {
        map = AL.asset.get(mapDesc);
        cam = new OrthographicCamera(1920,1080);
        //viewport = new FitViewport(1920, 1080, cam);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0,0,0,1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
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

    }

    @Override
    public void dispose() {
        AL.asset.unload(mapDesc.fileName);
    }
}
