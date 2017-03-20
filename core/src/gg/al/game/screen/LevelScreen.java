package gg.al.game.screen;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.text.View;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thomas Neumann on 20.03.2017.<br />
 * Main {@link IAssetScreen} for loading and playing levels.
 */
@Slf4j
public class LevelScreen implements IAssetScreen {

    private final AssetDescriptor<TiledMap> mapDesc;
    private TiledMap map;
    private OrthographicCamera mapCam;
    private Viewport viewportMap;

    private OrthogonalTiledMapRenderer mapRenderer;

    private FrameBuffer mapBuffer;

    private SpriteBatch batch;

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
        batch = new SpriteBatch();
        map = AL.asset.get(mapDesc);
        mapCam = new OrthographicCamera();
        viewportMap = new FitViewport(map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class),
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class), mapCam);

        viewportMap.update(viewportMap.getScreenWidth(), viewportMap.getScreenHeight(), true);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView(mapCam);

        mapBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, map.getProperties().get("width", Integer.class) * map.getProperties().get("tilewidth", Integer.class),
                map.getProperties().get("height", Integer.class) * map.getProperties().get("tileheight", Integer.class),
                false);
    }

    @Override
    public void render(float delta) {
        AL.gl.glClearColor(0, 0, 0, 1);
        AL.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapBuffer.begin();
        mapRenderer.render();
        mapBuffer.end();
        batch.begin();
        TextureRegion region = new TextureRegion(mapBuffer.getColorBufferTexture());
        region.flip(false, true);
        batch.draw(region, 0, 0);
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
        mapBuffer.dispose();
        batch.dispose();
        AL.asset.unload(mapDesc.fileName);
    }

    @Override
    public void dispose() {

    }
}
