package gg.al.game.screen;

import com.artemis.Aspect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.character.Character;
import gg.al.config.IInputConfig;
import gg.al.game.AL;
import gg.al.game.PlayerHelper;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.CharacterComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.StatComponent;
import gg.al.logic.component.data.Damage;
import gg.al.logic.component.data.StatusEffect;
import gg.al.logic.entity.Entities;
import gg.al.logic.entity.EntityArguments;
import gg.al.logic.map.Tile;
import gg.al.util.Assets;
import gg.al.util.Constants;
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
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private boolean reInit;
    private Assets.LevelAssets levelAssets;
    private PlayerHelper playerHelper;
    private Stage uiStage;
    private Viewport uiViewport;
    private OrthographicCamera uiCamera;
    private BitmapFont uiFont;
    private BitmapFont uiFontSmall;
    private Label.LabelStyle uiLabelStyle;
    private Label actionPointsLabel;


    private Label levelLabel;
    private Label xpLabel;
    private Label skillPointLabel;

    private Label[] abilityPointsLabels;

    private Matrix4 rotationMatrix;
    private Matrix4 invRotationMatrix;
    private Vector3 tempVec;
    private Vector3 cameraVector;
    private Vector2 lastMousePos;

    private IntArray enemies;

    private Label attackDamageLabel;
    private Label movespeedLabel;
    private Label armorLabel;

    private Label spellPowerLabel;
    private Label attackspeedLabel;
    private Label magicResistLabel;

    private Label cooldownReductionLabel;
    private Label critchanceLabel;

    private Label actionPointRegenLabel;
    private Label healthRegenLabel;
    private Label resourceRegenLabel;

    private Label healthLabel;
    private Label resourceLabel;




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

        rotationMatrix = new Matrix4().rotate(Vector3.X, rot);
        cameraVector = new Vector3(0, 1, 1);
        cameraVector.mul(rotationMatrix);
        invRotationMatrix = rotationMatrix.cpy().inv();
        lastMousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        tempVec = new Vector3();
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
                statComponent.damages.add(new Damage(Damage.DamageType.Physical, 10, 10));
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
                arguments = arcadeWorld.getArguments("kevin.json");
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
            AL.getAudioManager().registerSound("rocketlauncher", levelAssets.rocketLauncher);
            AL.getAudioManager().registerSound("boom", levelAssets.boom);
            spriteBatch = new SpriteBatch();
            font = new BitmapFont();

            camera = new PerspectiveCamera();
            camera.far = 1000;
            camera.position.set(new Vector3(-.5f, -.5f, 50));
            camera.rotateAround(new Vector3(-.5f, -.5f, 0), Vector3.X, rot);
            camera.fieldOfView = 15;
            camera.update();
            viewport = new ExtendViewport(1920, 1080, camera);
            viewport.apply();
            uiCamera = new OrthographicCamera();
            uiViewport = new FitViewport(1920, 1080, uiCamera);
            uiStage = new Stage(uiViewport);

            map = levelAssets.get(mapName);

            arcadeWorld = new ArcadeWorld(map, rot, camera, levelAssets);

            uiFont = levelAssets.uifont;
            uiLabelStyle = new Label.LabelStyle(uiFont, Color.BLACK);
            actionPointsLabel = new Label("0", uiLabelStyle);
            actionPointsLabel.setPosition(115, 75);
            uiStage.addActor(actionPointsLabel);


            uiFontSmall = levelAssets.uifontsmaller;
            uiLabelStyle = new Label.LabelStyle(uiFontSmall, Color.WHITE);


            skillPointLabel = new Label("0 Skillpoints", uiLabelStyle);
            skillPointLabel.setPosition(130, 250);
            uiStage.addActor(skillPointLabel);

            abilityPointsLabels = new Label[5];
            for (int i = 0; i < 5; i++) {
                Label abilLabel = new Label(i + ": 0", uiLabelStyle);
                abilLabel.setPosition(200 + i * 50, 100);
                uiStage.addActor(abilLabel);
                abilityPointsLabels[i] = abilLabel;
            }
            int val = 45;
            int val2 = 5;
            abilityPointsLabels[0].setPosition(290 + val, 50 + val2);
            abilityPointsLabels[1].setPosition(290 + val, 115 + val2);
            abilityPointsLabels[2].setPosition(375 + val, 115 + val2);
            abilityPointsLabels[3].setPosition(460 + val, 115 + val2);
            abilityPointsLabels[4].setPosition(375 + val, 50 + val2);

            xpLabel = new Label("", uiLabelStyle);
            xpLabel.setPosition(90, 60);
            uiStage.addActor(xpLabel);

            levelLabel = new Label("Level 0", uiLabelStyle);
            levelLabel.setPosition(100, 70);
            uiStage.addActor(levelLabel);



            // First row
            attackDamageLabel = new Label("0 AD", uiLabelStyle);
            attackDamageLabel.setPosition(35, 1010);
            uiStage.addActor(attackDamageLabel);

            movespeedLabel = new Label("0 MS", uiLabelStyle);
            movespeedLabel.setPosition(150, 1010);
            uiStage.addActor(movespeedLabel);

            armorLabel = new Label("0 AR", uiLabelStyle);
            armorLabel.setPosition(250,1010);
            uiStage.addActor(armorLabel);

            // Second row
            spellPowerLabel = new Label("0 SP", uiLabelStyle);
            spellPowerLabel.setPosition(35, 980);
            uiStage.addActor(spellPowerLabel);

            attackspeedLabel = new Label("0 AS", uiLabelStyle);
            attackspeedLabel.setPosition(150, 980);
            uiStage.addActor(attackspeedLabel);

            magicResistLabel = new Label("0 MR", uiLabelStyle);
            magicResistLabel.setPosition(250, 980);
            uiStage.addActor(magicResistLabel);

            // Third row
            cooldownReductionLabel = new Label("0 CDR", uiLabelStyle);
            cooldownReductionLabel.setPosition(35, 950);
            uiStage.addActor(cooldownReductionLabel);

            critchanceLabel = new Label("0 CRIT", uiLabelStyle);
            critchanceLabel.setPosition(170, 950);
            uiStage.addActor(critchanceLabel);

            // Fourth row
            actionPointRegenLabel = new Label("0 AP/S", uiLabelStyle);
            actionPointRegenLabel.setPosition(35, 920);
            uiStage.addActor(actionPointRegenLabel);

            healthRegenLabel = new Label("0 HP/S", uiLabelStyle);
            healthRegenLabel.setPosition(150, 920);
            uiStage.addActor(healthRegenLabel);

            resourceRegenLabel = new Label("0 RS/S", uiLabelStyle);
            resourceRegenLabel.setPosition(250, 920);
            uiStage.addActor(resourceRegenLabel);


            healthLabel = new Label("0/0 HP", uiLabelStyle);
            healthLabel.setPosition(110, 840);
            healthLabel.setAlignment(Align.center);
            uiStage.addActor(healthLabel);

            resourceLabel = new Label("0/0 RS", uiLabelStyle);
            resourceLabel.setPosition(110, 770);
            resourceLabel.setAlignment(Align.center);
            uiStage.addActor(resourceLabel);

            reInit = false;
            playerEnt = -1;

            spawnPlayer();
            enemies = arcadeWorld.spawnEnemies(playerEnt);
            arcadeWorld.setDebug(AL.getMiscellaneousConfig().debug());
        }

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        AL.graphics.getGL20().glClearColor(0, 0.3f, 0, 1);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


        arcadeWorld.setDelta(AL.graphics.getDeltaTime());
        arcadeWorld.step();

        if(AL.input.isKeyPressed(Input.Keys.SPACE))
        {
            tempVec.set(camera.position).mul(invRotationMatrix);
            float z = tempVec.z;
            Vector2 pos = playerHelper.getPosition();
            tempVec.set(pos, z);
            tempVec.mul(rotationMatrix);
            camera.position.set(tempVec);
            camera.update();
        }

        if (enemies.size == 0) {
            GameOverScreen gos = AL.getScreenManager().get(GameOverScreen.class, true);
            gos.setEndgameText("You win!");
            AL.getGame().setScreen(gos);
            reInit = true;
        } else if (playerHelper.isDead()) {
            GameOverScreen gos = AL.getScreenManager().get(GameOverScreen.class, true);
            gos.setEndgameText("Game over! You lost!");
            AL.getGame().setScreen(gos);
            reInit = true;
        }

        arcadeWorld.render();

        if (playerHelper != null)
            playerHelper.step(delta);

        spriteBatch.setProjectionMatrix(uiCamera.combined);
        spriteBatch.begin();
        spriteBatch.draw(levelAssets.uioverlay, 10, 10, levelAssets.uioverlay.getWidth() / 2, levelAssets.uioverlay.getHeight() / 2);
        spriteBatch.draw(levelAssets.uistats, 1, 1080-levelAssets.uistats.getHeight() * 1.5f, levelAssets.uistats.getWidth() * 1.6f, levelAssets.uistats.getHeight() * 1.5f);
        spriteBatch.draw(levelAssets.uistats2, 1, 1080-levelAssets.uistats.getHeight()*1.5f-levelAssets.uistats2.getHeight()+20, levelAssets.uistats2.getWidth(), levelAssets.uistats2.getHeight());

        if (playerEnt != -1) {
            StatComponent stats = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
            actionPointsLabel.setText(String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.actionPoints)));
            attackDamageLabel.setText(String.format("%1.0f AD", stats.getCurrentStat(StatComponent.BaseStat.attackDamage)));
            movespeedLabel.setText(String.format("%1.1f MS", stats.getCurrentStat(StatComponent.BaseStat.moveSpeed)));
            armorLabel.setText(String.format("%1.0f AR", stats.getCurrentStat(StatComponent.BaseStat.armor)));
            spellPowerLabel.setText(String.format("%1.0f SP", stats.getCurrentStat(StatComponent.BaseStat.spellPower)));
            attackspeedLabel.setText(String.format("%1.2f AS", stats.getCurrentStat(StatComponent.BaseStat.attackSpeed)));
            magicResistLabel.setText(String.format("%1.0f MR", stats.getCurrentStat(StatComponent.BaseStat.magicResist)));
            cooldownReductionLabel.setText(String.format("%1.0f%% CDR", stats.getCurrentStat(StatComponent.BaseStat.cooldownReduction)*100));
            critchanceLabel.setText(String.format("%1.0f%% CRIT", stats.getCurrentStat(StatComponent.BaseStat.criticalStrikeChance)*100));
            actionPointRegenLabel.setText(String.format("%1.0f AP/S", stats.getCurrentStat(StatComponent.BaseStat.actionPointsRegen)));
            healthRegenLabel.setText(String.format("%1.0f HP/S", stats.getCurrentStat(StatComponent.BaseStat.healthRegen)));
            resourceRegenLabel.setText(String.format("%1.0f RS/S", stats.getCurrentStat(StatComponent.BaseStat.resourceRegen)));

            healthLabel.setText(String.format("%1.0f/%1.0f HP", stats.getRuntimeStat(StatComponent.RuntimeStat.health), stats.getCurrentStat(StatComponent.BaseStat.maxHealth)));
            resourceLabel.setText(String.format("%1.0f/%1.0f RS", stats.getRuntimeStat(StatComponent.RuntimeStat.resource), stats.getCurrentStat(StatComponent.BaseStat.maxResource)));

            levelLabel.setText(String.format("Level %1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.level)));
            abilityPointsLabels[0].setText(String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.trait_points)));
            abilityPointsLabels[1].setText(String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.ability_1_points)));
            abilityPointsLabels[2].setText(String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.ability_2_points)));
            abilityPointsLabels[3].setText(String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.ability_3_points)));
            abilityPointsLabels[4].setText(String.format("%1.0f", stats.getRuntimeStat(StatComponent.RuntimeStat.ability_4_points)));
            if (stats.getRuntimeStat(StatComponent.RuntimeStat.skillPoints) != 0)
                skillPointLabel.setText(String.format("%1.0fx", stats.getRuntimeStat(StatComponent.RuntimeStat.skillPoints)));
            else skillPointLabel.setText("");
            xpLabel.setText(String.format("%1.0f/%1.0f XP", stats.getRuntimeStat(StatComponent.RuntimeStat.experience), stats.getNextLevelExperience()));
        }


        if (playerHelper != null) {
            spriteBatch.draw(playerHelper.getHealthTexture(), 50, 142, 190, 95);
            spriteBatch.draw(playerHelper.getResourceTexture(), 68, 142, 150, 75);
            spriteBatch.draw(playerHelper.getExperienceTexture(), 10, 10, 200, 25);

            spriteBatch.draw(playerHelper.getIcon(Character.ABILITY_1), 275, 155, 50, 50);
            Sprite sprite = playerHelper.getAbilityOverlaySprite(Character.ABILITY_1);
            sprite.setBounds(275, 155, 50, 50);
            sprite.draw(spriteBatch);
            spriteBatch.draw(playerHelper.getCooldownTextures()[Character.ABILITY_1], 275, 155, 50, 50);

            spriteBatch.draw(playerHelper.getIcon(Character.ABILITY_2), 359, 155, 50, 50);
            sprite = playerHelper.getAbilityOverlaySprite(Character.ABILITY_2);
            sprite.setBounds(359, 155, 50, 50);
            sprite.draw(spriteBatch);
            spriteBatch.draw(playerHelper.getCooldownTextures()[Character.ABILITY_2], 359, 155, 50, 50);

            spriteBatch.draw(playerHelper.getIcon(Character.ABILITY_3), 448, 154, 50, 50);
            sprite = playerHelper.getAbilityOverlaySprite(Character.ABILITY_3);
            sprite.setBounds(448, 154, 50, 50);
            sprite.draw(spriteBatch);
            spriteBatch.draw(playerHelper.getCooldownTextures()[Character.ABILITY_3], 448, 154, 50, 50);

            spriteBatch.draw(playerHelper.getIcon(Character.ABILITY_4), 359, 77, 50, 50);
            sprite = playerHelper.getAbilityOverlaySprite(Character.ABILITY_4);
            sprite.setBounds(359, 77, 50, 50);
            sprite.draw(spriteBatch);
            spriteBatch.draw(playerHelper.getCooldownTextures()[Character.ABILITY_4], 359, 77, 50, 50);

            spriteBatch.draw(playerHelper.getIcon(Character.TRAIT), 276, 79, 48, 48);
            sprite = playerHelper.getAbilityOverlaySprite(Character.TRAIT);
            sprite.setBounds(276, 79, 48, 48);
            sprite.draw(spriteBatch);
            spriteBatch.draw(playerHelper.getCooldownTextures()[Character.TRAIT], 276, 79, 48, 48);


        }
        font.draw(spriteBatch, String.format("%d FPS %d Entities", Gdx.graphics.getFramesPerSecond(), arcadeWorld.getEntityWorld().getAspectSubscriptionManager().get(Aspect.all()).getEntities().size()), 0, 15);

        spriteBatch.end();
        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();
    }

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
            spriteBatch.dispose();
            arcadeWorld.dispose();
            AL.getAssetManager().unloadAssetFields(levelAssets);
            AL.getAudioManager().unregisterSound("sword_1");
            AL.getAudioManager().unregisterSound("sword_2");
            AL.getAudioManager().unregisterSound("sword_3");
            AL.getAudioManager().unregisterSound("sword_4");
            AL.getAudioManager().unregisterSound("rocketlauncher");
            AL.getAudioManager().unregisterSound("boom");
            if (playerHelper != null)
                playerHelper.dispose();
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
        lastMousePos.set(screenX, screenY);
        Ray ray = camera.getPickRay(screenX, screenY);
        Vector3 worldcoor = new Vector3();
        Intersector.intersectRayPlane(ray, arcadeWorld.getMapHitbox(), worldcoor);
        log.debug("Clicked: " + worldcoor.toString());
        Vector2 mapCoord = new Vector2(Math.round(worldcoor.x), Math.round(worldcoor.y));
        switch (button) {
            case Input.Buttons.LEFT:
                CharacterComponent input = arcadeWorld.getEntityWorld().getComponentOf(playerEnt, CharacterComponent.class);
                input.move.set((int) mapCoord.x, (int) mapCoord.y);
                break;
            case Input.Buttons.RIGHT:
                Tile t = arcadeWorld.getTile(mapCoord);
                try {
                    int id = t.getEntities().first();
                    input = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
                    input.targetId = id;
                    log.debug("{}", id);
                } catch (IllegalStateException ex) {

                }
                break;
        }

        return false;
    }

    private int spawnPlayer() {
        playerEnt = arcadeWorld.spawnPlayer();
        playerHelper = new PlayerHelper(playerEnt, arcadeWorld, levelAssets);
        Vector2 pos = arcadeWorld.getSpawnPosition();
        camera.position.set(pos.x, pos.y - 12, camera.position.z);
        return playerEnt;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            camera.translate((lastMousePos.x - screenX) / 10, (screenY - lastMousePos.y) / 10, 0);
            lastMousePos.set(screenX, screenY);
            camera.update();
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount > 0 && camera.position.z > Constants.MAX_ZOOM)
            return false;
        else if (amount < 0 && camera.position.z < Constants.MIN_ZOOM)
            return false;
        camera.translate(0, -amount * cameraVector.y, amount * cameraVector.z);
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
            CharacterComponent characterComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(playerEnt);
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                StatComponent stat = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(playerEnt);
                if (stat.getRuntimeStat(StatComponent.RuntimeStat.skillPoints) > 0) {
                    switch (ability) {
                        case Character.TRAIT:
                            stat.addRuntimeStat(StatComponent.RuntimeStat.trait_points, 1);
                            break;
                        case Character.ABILITY_1:
                            stat.addRuntimeStat(StatComponent.RuntimeStat.ability_1_points, 1);
                            break;
                        case Character.ABILITY_2:
                            stat.addRuntimeStat(StatComponent.RuntimeStat.ability_2_points, 1);
                            break;
                        case Character.ABILITY_3:
                            stat.addRuntimeStat(StatComponent.RuntimeStat.ability_3_points, 1);
                            break;
                        case Character.ABILITY_4:
                            stat.addRuntimeStat(StatComponent.RuntimeStat.ability_4_points, 1);
                            break;
                    }
                    stat.addRuntimeStat(StatComponent.RuntimeStat.skillPoints, -1);
                }
            } else
                characterComponent.character.cast(ability);
        }
    }
}