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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.*;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.map.Tile;
import gg.al.util.Assets;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
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
        List<AssetDescriptor> assets = new ArrayList<>(6);
        assets.add(mapDesc);
        try {
            EntityArguments arguments = EntityArguments.fromFile("player.json");
            for (RenderComponent.RenderTemplate.AnimationTemplate template :
                    arguments.get(RenderComponent.class.getSimpleName(), RenderComponent.RenderTemplate.class)
                            .animationTemplates.values())
                assets.add(Assets.get(template.texture));
        } catch (IOException e) {
            log.error("Couldn´t load player", e);
        }
        return assets;
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
        viewport = new ExtendViewport(1920, 1080, camera);
        viewport.apply();

        map = AL.getAssetManager().get(mapDesc);

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
        font.draw(fpsBatch, String.format("%d FPS %d Entities", Gdx.graphics.getFramesPerSecond(), arcadeWorld.getEntityWorld().getAspectSubscriptionManager().get(Aspect.all()).getEntities().size()), 0, 15);
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
        AL.getAssetManager().unload(mapDesc.fileName);
        AL.getAssetManager().unload(Assets.PT_SIDEVIEWSHEET.fileName);
        arcadeWorld.dispose();
    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                camera.translate(-1, 0, 0);
                break;
            case Input.Keys.RIGHT:
                camera.translate(1, 0, 0);
                break;
            case Input.Keys.DOWN:
                camera.translate(0, -1, 0);
                break;
            case Input.Keys.UP:
                camera.translate(0, 1, 0);
                break;
            case Input.Keys.W:
                ControlComponent input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, ControlComponent.class);

                PositionComponent position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, PositionComponent.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x, position.position.y + 1);
                break;
            case Input.Keys.S:
                input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, ControlComponent.class);

                position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, PositionComponent.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x, position.position.y - 1);
                break;
            case Input.Keys.A:
                input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, ControlComponent.class);

                position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, PositionComponent.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x - 1, position.position.y);
                break;
            case Input.Keys.D:
                input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, ControlComponent.class);
                position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, PositionComponent.class);
                if (!input.move.equals(position.position))
                    break;
                input.move.set(position.position.x + 1, position.position.y);
                break;
            case Input.Keys.K:
                StatComponent statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
                statComponent.damages.add(new Damage(Damage.DamageType.Normal, 10, 10));
                break;
            case Input.Keys.J:
                statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
                statComponent.addRuntimeStat(StatComponent.RuntimeStat.level, 1);
                break;

            case Input.Keys.Z:
                statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
                statComponent.statusEffects.put("ARP1", StatusEffect.builder()
                        .effectTime(10)
                        .flatStat(StatComponent.BaseStat.healthRegen, 10f)
                        .build());
                break;

            case Input.Keys.O:
                EntityArguments arguments = null;
                try {
                    arguments = arcadeWorld.getArguments("player.json");
                    int id = arcadeWorld.spawn(Entities.Player, arguments);
                    statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(id);
                    statComponent.setFlag(StatComponent.FlagStat.deleteOnDeath, true);
                } catch (IOException e) {
                    log.error("Couldn´t load player", e);
                }
                break;
            case Input.Keys.Q:
                CharacterComponent characterComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
                characterComponent.character.cast(1);
                break;
            case Input.Keys.E:
                characterComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
                characterComponent.character.cast(2);
                break;
            case Input.Keys.R:
                characterComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
                characterComponent.character.cast(3);
                break;
            case Input.Keys.F:
                characterComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
                characterComponent.character.cast(4);
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
                    EntityArguments arguments;
                    try {
                        arguments = arcadeWorld.getArguments("player.json");
                        PositionComponent.PositionTemplate positionDef = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                        int posX = positionDef.x;
                        int posY = positionDef.y;
                        positionDef.x = (int) mapCoord.x;
                        positionDef.y = (int) mapCoord.y;
                        playerEnt = arcadeWorld.spawn(Entities.Player, arguments);
                        positionDef.x = posX;
                        positionDef.y = posY;
                    } catch (IOException e) {
                        log.error("Couldn´t load player", e);
                    }
                } else {
                    ControlComponent input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, ControlComponent.class);
                    input.move.set((int) mapCoord.x, (int) mapCoord.y);
                }
                break;
            case Input.Buttons.RIGHT:
                Tile t = arcadeWorld.getTile(mapCoord);
                try {
                    int id = t.getEntities().first();
                    ControlComponent input = arcadeWorld.getEntityWorld().getMapper(ControlComponent.class).get(playerEnt);
                    input.targetId = id;
                    log.debug("{}", id);
                } catch (IllegalStateException ex) {

                }
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
