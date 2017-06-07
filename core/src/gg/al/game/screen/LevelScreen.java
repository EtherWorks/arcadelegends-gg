package gg.al.game.screen;

import com.artemis.Aspect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cyphercove.gdx.covetools.assets.AssignmentAssetManager;
import gg.al.character.Character;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import gg.al.game.ui.ProgressCircle;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.CharacterComponent;
import gg.al.logic.component.InventoryComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.Item;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.map.Tile;
import gg.al.util.Assets;
import gg.al.util.InputMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 20.03.2017.<br />
 * Main {@link IAssetScreen} for loading and playing levels.
 */
@Slf4j
public class LevelScreen implements IAssetScreen, InputProcessor {

    private final String mapName;
    private final float rot;
    private final InputMapper inputMapper;
    private int playerEnt = -1;
    private TiledMap map;
    private PerspectiveCamera camera;
    private Viewport viewport;
    private ArcadeWorld arcadeWorld;
    private SpriteBatch fpsBatch;
    private BitmapFont font;
    private boolean reInit;
    private Assets.LevelAssets levelAssets;
    private ShaderProgram shaderProgram;
    private SpriteBatch shaderBatch;

    private Stage uiStage;
    private Viewport uiViewport;


    public LevelScreen(String mapName) {
        this(mapName, 15);
    }

    public LevelScreen(String mapName, float rot) {
        this.rot = rot;
        this.mapName = mapName;
        this.inputMapper = new InputMapper(AL.getInputConfig());

        inputMapper.registerInputHanlder(IInputConfig.InputKeys.up, new MoveEventHandler(new Vector2(0, 1)));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.down, new MoveEventHandler(new Vector2(0, -1)));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.right, new MoveEventHandler(new Vector2(1, 0)));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.left, new MoveEventHandler(new Vector2(-1, 0)));

        inputMapper.registerInputHanlder(IInputConfig.InputKeys.ability1, new AbillityEventHandler(Character.ABILITY_1));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.ability2, new AbillityEventHandler(Character.ABILITY_2));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.ability3, new AbillityEventHandler(Character.ABILITY_3));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.ability4, new AbillityEventHandler(Character.ABILITY_4));
        inputMapper.registerInputHanlder(IInputConfig.InputKeys.trait, new AbillityEventHandler(Character.TRAIT));

        shaderProgram = new ShaderProgram("attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "uniform float u_gradient;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "varying float gradient;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "   v_color = a_color;\n" +
                "   v_color.a = v_color.a * (255.0/254.0);\n" +
                "   v_texCoords = a_texCoord0;\n" +
                "   gradient = u_gradient;\n" +
                "   gl_Position =  u_projTrans * a_position;\n" +
                "}",
                "#ifdef GL_ES\n" +
                        "precision mediump float;\n" +
                        "#endif\n" +
                        "varying vec4 v_color;\n" +
                        "varying vec2 v_texCoords;\n" +
                        "varying float gradient;\n" +
                        "uniform sampler2D u_texture;\n" +
                        "void main()\n" +
                        "{\n" +
                        "  vec4 color = texture2D(u_texture, v_texCoords);\n" +
                        "  if(color.a == 0 || color.r <= 1.0f -gradient) discard;\n" +
                        "  gl_FragColor = v_color * vec4(1,1,1,1);\n" +
                        "}");
        if (shaderProgram.isCompiled() == false)
            throw new IllegalArgumentException("couldn't compile shader: " + shaderProgram.getLog());
        shaderBatch = new SpriteBatch(1000, shaderProgram);
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
            case Input.Keys.K:
                StatComponent statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
                statComponent.damages.add(new Damage(Damage.DamageType.Normal, 10, 10));
                break;
            case Input.Keys.J:
                statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
                statComponent.addRuntimeStat(StatComponent.RuntimeStat.experience, 10);
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
                arguments = arcadeWorld.getArguments("player.json");
                PositionComponent.PositionTemplate pos = arguments.get(PositionComponent.class.getSimpleName(), PositionComponent.PositionTemplate.class);
                pos.x = 5;
                pos.y = 5;
                int id = arcadeWorld.spawn(Entities.Player, arguments);
                statComponent = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(id);
                statComponent.setFlag(StatComponent.FlagStat.deleteOnDeath, true);
                CharacterComponent characterControlComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(id);
                characterControlComponent.targetId = 0;
                break;
            case Input.Keys.ESCAPE:
                if (!AL.getScreenManager().isRegistered(PauseMenuScreen.class))
                    AL.getScreenManager().register(new PauseMenuScreen(), PauseMenuScreen.class);
                AL.getGame().setScreen(AL.getScreenManager().get(PauseMenuScreen.class));
                break;
            default:
                inputMapper.handleInput(keycode);
                break;
        }
        camera.update();
        return false;
    }

    @Override
    public Object assets() {
        return levelAssets = new Assets.LevelAssets();
    }

    @Override
    public ILoadingScreen customLoadingScreen() {
        return null;
    }

    public void setReInit(boolean reInit) {
        this.reInit = reInit;
    }

    @Override
    public void show() {
        if (arcadeWorld == null || reInit) {
            AL.getAudioManager().registerSound("sword_1", levelAssets.sword_1);
            AL.getAudioManager().registerSound("sword_2", levelAssets.sword_2);
            AL.getAudioManager().registerSound("sword_3", levelAssets.sword_3);
            AL.getAudioManager().registerSound("sword_4", levelAssets.sword_4);
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
            uiViewport = new ExtendViewport(1920, 1080);
            uiStage = new Stage(uiViewport);

            map = levelAssets.get(mapName);

            arcadeWorld = new ArcadeWorld(map, rot, camera, levelAssets);

            reInit = false;
            playerEnt = -1;

            for (int i = 0; i < buffers.length; i++) {
                buffers[i] = new FrameBuffer(Pixmap.Format.RGBA8888, levelAssets.gradient.getWidth(), levelAssets.gradient.getHeight(), false);
            }
        }

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        AL.graphics.getGL20().glClearColor(0, 0, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        arcadeWorld.setDelta(AL.graphics.getDeltaTime());
        arcadeWorld.step();
        arcadeWorld.render();

        if (playerEnt != -1) {
            StatComponent stat = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
            CharacterComponent chara = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
            for (int i = 0; i < abilityPerc.length; i++) {
                if (chara.character.getCooldownTimer(i) != 0)
                    abilityPerc[i] = chara.character.getCooldownTimer(i) / Character.getCooldown(i, stat);
            }
            healthPerc = stat.getRuntimeStat(StatComponent.RuntimeStat.health) / stat.getCurrentStat(StatComponent.BaseStat.maxHealth);
            rescPerc = stat.getRuntimeStat(StatComponent.RuntimeStat.resource) / stat.getCurrentStat(StatComponent.BaseStat.maxResource);
        }

        buffers[5].begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderBatch.begin();
        shaderBatch.setColor(Color.RED);
        shaderProgram.setUniformf("u_gradient", healthPerc);
        shaderBatch.draw(levelAssets.gradient, 0, 0);
        shaderBatch.end();
        buffers[5].end();

        buffers[6].begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderBatch.begin();
        shaderBatch.setColor(Color.BLUE);
        shaderProgram.setUniformf("u_gradient", rescPerc);
        shaderBatch.draw(levelAssets.gradient, 0, 0);
        shaderBatch.end();
        buffers[6].end();
        for (int i = 0; i < abilityPerc.length; i++) {
            buffers[i].begin();
            AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
            AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
            shaderBatch.begin();
            shaderBatch.setColor(Color.GREEN);
            shaderProgram.setUniformf("u_gradient", abilityPerc[i]);
            shaderBatch.draw(levelAssets.gradient, 0, 0);
            shaderBatch.end();
            buffers[i].end();
        }

        fpsBatch.begin();
        fpsBatch.draw(levelAssets.gradient, 500, 500);
        fpsBatch.draw(levelAssets.uioverlay, 30, AL.graphics.getHeight() / 10 - 150, 640, 360);
        font.draw(fpsBatch, String.format("%d FPS %d Entities", Gdx.graphics.getFramesPerSecond(), arcadeWorld.getEntityWorld().getAspectSubscriptionManager().get(Aspect.all()).getEntities().size()), 0, 15);

        for (int i = 0; i < buffers.length; i++) {
            fpsBatch.draw(buffers[i].getColorBufferTexture(), 100 + 100 * i, 100, 600, 300);
        }
        fpsBatch.end();


    }

    float[] abilityPerc = new float[5];
    float rescPerc = 0;
    float healthPerc = 0;
    FrameBuffer[] buffers = new FrameBuffer[7];

    @Override
    public void resize(int width, int height) {
        uiViewport.update(width, height);
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
        if (reInit) {
            fpsBatch.dispose();
            arcadeWorld.dispose();
            AL.getAssetManager().unloadAssetFields(levelAssets);
            AL.getAudioManager().unregisterSound("sword_1");
            AL.getAudioManager().unregisterSound("sword_2");
            AL.getAudioManager().unregisterSound("sword_3");
            AL.getAudioManager().unregisterSound("sword_4");
        }
    }

    @Override
    public void dispose() {

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
                    arguments = arcadeWorld.getArguments("ezreal.json");
                    PositionComponent.PositionTemplate positionDef = arguments.get("PositionComponent", PositionComponent.PositionTemplate.class);
                    float posX = positionDef.x;
                    float posY = positionDef.y;
                    positionDef.x = (int) mapCoord.x;
                    positionDef.y = (int) mapCoord.y;
                    playerEnt = arcadeWorld.spawn(Entities.Player, arguments);
                    positionDef.x = posX;
                    positionDef.y = posY;
                    InventoryComponent inventoryComponent = arcadeWorld.getEntityWorld().getMapper(InventoryComponent.class).get(playerEnt);
                    inventoryComponent.items[0] = Item.builder().name("Armor")
                            .flatStat(StatComponent.BaseStat.armor, 50f)
                            .flatStat(StatComponent.BaseStat.cooldownReduction, 0.1f)
                            .build();
                    inventoryComponent.items[1] = Item.builder().name("Staff")
                            .flatStat(StatComponent.BaseStat.spellPower, 20f)
                            .build();
                } else {
                    CharacterComponent input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, CharacterComponent.class);
                    input.move.set((int) mapCoord.x, (int) mapCoord.y);
                }
                break;
            case Input.Buttons.RIGHT:
                Tile t = arcadeWorld.getTile(mapCoord);
                try {
                    int id = t.getEntities().first();
                    CharacterComponent input = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
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


    private class MoveEventHandler implements InputMapper.InputEvent {

        private final Vector2 move;

        public MoveEventHandler(Vector2 move) {
            this.move = move;
        }

        @Override
        public void onInput() {
            CharacterComponent input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, CharacterComponent.class);

            PositionComponent position = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, PositionComponent.class);
            if (!input.move.equals(position.position))
                return;
            input.move.set(position.position.x + move.x, position.position.y + move.y);
        }
    }

    private class AbillityEventHandler implements InputMapper.InputEvent {
        private final int ability;

        public AbillityEventHandler(int ability) {
            this.ability = ability;
        }

        @Override
        public void onInput() {
            arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt).character.cast(ability);
        }
    }
}
