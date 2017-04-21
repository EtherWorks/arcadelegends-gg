package gg.al.game.screen;

import com.artemis.Aspect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.Position;
import gg.al.logic.entity.Entity;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.entity.EntityUtil;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas Neumann on 20.03.2017.<br />
 * Main {@link IAssetScreen} for loading and playing levels.
 */
@Slf4j
public class LevelScreen implements IAssetScreen, InputProcessor {

    private final AssetDescriptor<TiledMap> mapDesc;
    private final float rot;
    private int playerEnt = -1;
    private TiledMap map;
    private PerspectiveCamera camera;
    private Viewport viewport;
    private ArcadeWorld arcadeWorld;
    private SpriteBatch fpsBatch;
    private BitmapFont font;

    public LevelScreen(AssetDescriptor<TiledMap> mapDesc) {
        this(mapDesc, 15);
    }

    public LevelScreen(AssetDescriptor<TiledMap> mapDesc, float rot) {
        this.rot = rot;
        this.mapDesc = mapDesc;
    }

    @Override
    public List<AssetDescriptor> assets() {
        return Arrays.asList(mapDesc, Assets.PT_EZREAL);
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    @Override
    public void show() {
        fpsBatch = new SpriteBatch();
        font = new BitmapFont();

        camera = new PerspectiveCamera();
        camera.far = 1000;
        camera.position.set(new Vector3(-.5f, -.5f, 50));
        camera.rotateAround(new Vector3(-.5f, -.5f, 0), Vector3.X, rot);
        camera.fieldOfView = 15;
        camera.update();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        map = AL.asset.get(mapDesc);

        arcadeWorld = new ArcadeWorld(map, rot, camera);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            arcadeWorld.setDelta(AL.graphics.getDeltaTime());
            arcadeWorld.step();
            arcadeWorld.render();


        fpsBatch.begin();
        font.draw(fpsBatch, String.format("%d FPS %d", Gdx.graphics.getFramesPerSecond(), arcadeWorld.getEntityWorld().getAspectSubscriptionManager().get(Aspect.all()).getEntities().size()), 0, 15);
        fpsBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        fpsBatch.dispose();
        AL.asset.unload(mapDesc.fileName);
        AL.asset.unload(Assets.PT_EZREAL.fileName);
        arcadeWorld.dispose();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                camera.translate(-1, 0, 0);
                break;
            case Input.Keys.D:
                camera.translate(1, 0, 0);
                break;
            case Input.Keys.S:
                camera.translate(0, -1, 0);
                break;
            case Input.Keys.W:
                camera.translate(0, 1, 0);
                break;
            case Input.Keys.UP:
                gg.al.logic.component.Input input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, gg.al.logic.component.Input.class);

                Position position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, Position.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x, position.position.y + 1);
                break;
            case Input.Keys.DOWN:
                input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, gg.al.logic.component.Input.class);

                position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, Position.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x, position.position.y - 1);
                break;
            case Input.Keys.LEFT:
                input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, gg.al.logic.component.Input.class);

                position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, Position.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x - 1, position.position.y);
                break;
            case Input.Keys.RIGHT:
                input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, gg.al.logic.component.Input.class);
                position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, Position.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x + 1, position.position.y);
                break;
        }
        camera.update();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Ray ray = camera.getPickRay(screenX, screenY);
        Vector3 worldcoor = new Vector3();
        Intersector.intersectRayPlane(ray, arcadeWorld.getMapHitbox(), worldcoor);
        log.debug("Clicked: " + worldcoor.toString());
        Vector2 mapCoord = new Vector2(Math.round(worldcoor.x), Math.round(worldcoor.y));
        switch (button) {
            case Input.Buttons.LEFT:
                if (playerEnt == -1) {
                    EntityArguments arguments = new EntityArguments();
                    arguments.put("texture", Assets.PT_EZREAL);
                    arguments.put("x", (int) mapCoord.x);
                    arguments.put("y", (int) mapCoord.y);
                    arguments.put("maxHealth", 100);
                    arguments.put("maxAP", 10);
                    playerEnt = EntityUtil.spawn(Entity.TEST, arcadeWorld, arguments);
                } else {
                    gg.al.logic.component.Input input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, gg.al.logic.component.Input.class);
                    input.move.set((int) mapCoord.x, (int) mapCoord.y);
                }
                break;
            case Input.Buttons.RIGHT:
                EntityArguments arguments = new EntityArguments();
                arguments.put("texture", Assets.PT_EZREAL);
                arguments.put("x", (int) mapCoord.x);
                arguments.put("y", (int) mapCoord.y);
                arguments.put("move", new Vector2(1, 0));
                EntityUtil.spawn(Entity.BULLET, arcadeWorld, arguments);
                break;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.translate(0, 0, amount);
        camera.update();
        return false;
    }
}
