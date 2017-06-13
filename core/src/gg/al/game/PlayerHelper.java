package gg.al.game;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import gg.al.character.Character;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.CharacterComponent;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.StatComponent;
import gg.al.util.Assets;
import gg.al.util.Shaders;

/**
 * Created by Thomas Neumann on 07.06.2017.<br>
 * Helper class for exposing entity related variables to the UI-system.<br>
 * Also contains {@link TextureRegion]s for current health, resource and cooldowns.
 */
public class PlayerHelper implements Disposable {

    private final int entityId;
    private final ArcadeWorld arcadeWorld;
    private final float[] abilityPercents = new float[5];
    private final FrameBuffer[] abilityBuffers = new FrameBuffer[5];
    private final TextureRegion[] cooldownTextures = new TextureRegion[5];
    float resourcePerc = 0;
    float healthPerc = 0;
    float xpPerc = 0;

    private Sprite[] abilityOverlaySprites = new Sprite[5];
    private Texture[] icons = new Texture[5];

    private FrameBuffer resourceBuffer;
    private TextureRegion resourceTexture;
    private FrameBuffer healthBuffer;
    private TextureRegion healthTexture;
    private TextureRegion xpTexture;
    private FrameBuffer xpBuffer;

    private ShaderProgram shader;
    private SpriteBatch shaderBatch;

    private TextureRegion healthGradient;
    private TextureRegion cooldownGradient;
    private TextureRegion passiveGradient;
    private TextureRegion xpGradient;

    private Camera healthCam;
    private Camera xpCam;
    private Camera cooldownCam;

    private Color healthColor = new Color(0.7f, 0, 0, 1);
    private Color resourceColor = new Color(0, 0, 0.7f, 1);
    private Color cooldownColor = new Color(0.5f, 0.5f, 0.5f, 0.8f);
    private Color xpColor = new Color(0, 1, 1, 1);

    public PlayerHelper(int entityId, ArcadeWorld arcadeWorld, Assets.LevelAssets assets) {
        String[] iNames = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(entityId).character.getIconNames();
        for (int i = 0; i < icons.length; i++) {
            icons[i] = arcadeWorld.getLevelAssets().get(iNames[i]);
        }
        this.entityId = entityId;
        this.arcadeWorld = arcadeWorld;
        shader = new ShaderProgram(Shaders.GradientShader.vertexShader, Shaders.GradientShader.fragmentShader);
        if (shader.isCompiled() == false)
            throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
        shaderBatch = new SpriteBatch(1000, shader);

        xpGradient = new TextureRegion(assets.xp_gradient);
        xpCam = new OrthographicCamera(xpGradient.getRegionWidth(), xpGradient.getRegionHeight());
        this.xpBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, xpGradient.getRegionWidth(), xpGradient.getRegionHeight(), false);
        this.xpTexture = new TextureRegion(xpBuffer.getColorBufferTexture());
        xpTexture.flip(false, true);

        passiveGradient = new TextureRegion(assets.passive_cooldown_gradient);
        this.healthGradient = new TextureRegion(assets.health_gradient);
        healthCam = new OrthographicCamera(healthGradient.getRegionWidth(), healthGradient.getRegionHeight());
        this.healthBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, assets.health_gradient.getWidth(), assets.health_gradient.getHeight(), false);
        this.healthTexture = new TextureRegion(healthBuffer.getColorBufferTexture());
        healthTexture.flip(false, true);
        this.resourceBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, assets.health_gradient.getWidth(), assets.health_gradient.getHeight(), false);
        this.resourceTexture = new TextureRegion(resourceBuffer.getColorBufferTexture());
        resourceTexture.flip(false, true);
        this.cooldownGradient = new TextureRegion(assets.cooldown_gradient, assets.cooldown_gradient.getWidth(), assets.cooldown_gradient.getHeight());
        cooldownCam = new OrthographicCamera(cooldownGradient.getRegionWidth(), cooldownGradient.getRegionHeight());

        for (int i = 0; i < abilityBuffers.length; i++) {
            abilityBuffers[i] = new FrameBuffer(Pixmap.Format.RGBA8888,
                    i == 0 ? passiveGradient.getRegionWidth() : cooldownGradient.getRegionWidth(),
                    i == 0 ? passiveGradient.getRegionHeight() : cooldownGradient.getRegionHeight(), false);
            cooldownTextures[i] = new TextureRegion(abilityBuffers[i].getColorBufferTexture());
            cooldownTextures[i].flip(false, true);
        }

        for (int i = 0; i < abilityOverlaySprites.length; i++) {
            if (i == 0) {
                Pixmap pixmap = new Pixmap(passiveGradient.getRegionWidth(), passiveGradient.getRegionHeight(), Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.WHITE);
                pixmap.fillCircle(passiveGradient.getRegionWidth() / 2, passiveGradient.getRegionHeight() / 2, passiveGradient.getRegionHeight() / 2);
                abilityOverlaySprites[i] = new Sprite(new Texture(pixmap));
                pixmap.dispose();
            } else {
                Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.WHITE);
                pixmap.fill();
                abilityOverlaySprites[i] = new Sprite(new Texture(pixmap));
                pixmap.dispose();
            }
        }
    }

    /**
     * @return the position of the player
     */
    public Vector2 getPosition() {
        return arcadeWorld.getEntityWorld().getMapper(PhysicComponent.class).get(entityId).body.getPosition();
    }

    /**
     * Called every frame. Updates cooldown, health and resource textures.
     *
     * @param delta
     */
    public void step(float delta) {
        StatComponent stat = arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(entityId);
        CharacterComponent chara = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(entityId);
        for (int i = 0; i < abilityPercents.length; i++) {
            if (chara.character.getCooldownTimer(i) != 0)
                abilityPercents[i] = chara.character.getCooldownTimer(i) / Character.getCooldown(i, stat);
            else
                abilityPercents[i] = 0;
        }
        healthPerc = stat.getRuntimeStat(StatComponent.RuntimeStat.health) / stat.getCurrentStat(StatComponent.BaseStat.maxHealth);
        resourcePerc = stat.getRuntimeStat(StatComponent.RuntimeStat.resource) / stat.getCurrentStat(StatComponent.BaseStat.maxResource);
        xpPerc = stat.getRuntimeStat(StatComponent.RuntimeStat.experience) / stat.getNextLevelExperience();

        drawToBuffer(xpBuffer, xpColor, xpGradient, xpPerc, xpCam.combined);
        drawToBuffer(healthBuffer, healthColor, healthGradient, healthPerc, healthCam.combined);
        drawToBuffer(resourceBuffer, resourceColor, healthGradient, resourcePerc, healthCam.combined);
        for (int i = 0; i < abilityBuffers.length; i++) {
            drawToBuffer(abilityBuffers[i], cooldownColor, i == 0 ? passiveGradient : cooldownGradient, abilityPercents[i], cooldownCam.combined);
        }
    }

    /**
     * Draws the given gradient {@link Texture} to the given {@link FrameBuffer}, using the given {@link Color} with the given percent.
     * <p>
     * This is achieved via a gradient shader.
     *
     * @param buffer     the {@link FrameBuffer} to be drawn to
     * @param color      the {@link Color} which should be drawn
     * @param gradient   the gradient texture which should be drawn
     * @param perc       the percent the gradient texture should be drawn
     * @param projection projection matrix for pixel perfect rendering
     */
    private void drawToBuffer(FrameBuffer buffer, Color color, TextureRegion gradient, float perc, Matrix4 projection) {
        buffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderBatch.setProjectionMatrix(projection);
        shaderBatch.begin();
        shaderBatch.setColor(color);
        shader.setUniformf("u_gradient", perc);
        shaderBatch.draw(gradient, -buffer.getWidth() / 2, -buffer.getHeight() / 2);
        shaderBatch.end();
        buffer.end();
    }

    /**
     * @return whether the player is dead.
     */
    public boolean isDead() {
        return arcadeWorld.getEntityWorld().getMapper(StatComponent.class).get(entityId).getFlag(StatComponent.FlagStat.dead);
    }

    public TextureRegion getHealthTexture() {
        return healthTexture;
    }

    public TextureRegion getResourceTexture() {
        return resourceTexture;
    }

    public TextureRegion[] getCooldownTextures() {
        return cooldownTextures;
    }

    public TextureRegion getExperienceTexture() {
        return xpTexture;
    }

    /**
     * Returns a shaded sprite for coloring the ability icons. Controlled by the {@link Character} class.
     *
     * @param ability for the overlay sprite
     * @return the overlay sprite
     */
    public Sprite getAbilityOverlaySprite(int ability) {
        CharacterComponent characterComponent = arcadeWorld.getEntityWorld().getMapper(CharacterComponent.class).get(entityId);
        Color color;
        if ((color = characterComponent.character.getAbilityOverlay(ability)) != null)
            abilityOverlaySprites[ability].setColor(color);
        else
            abilityOverlaySprites[ability].setColor(0, 0, 0, 0);
        return abilityOverlaySprites[ability];
    }

    public int getEntityId() {
        return entityId;
    }

    public Texture getIcon(int ability) {
        return icons[ability];
    }

    @Override
    public void dispose() {
        for (FrameBuffer buffer : abilityBuffers) {
            buffer.dispose();
        }
        healthBuffer.dispose();
        xpBuffer.dispose();
        resourceBuffer.dispose();
        shaderBatch.dispose();
        shader.dispose();
        for (Sprite s : abilityOverlaySprites)
            s.getTexture().dispose();
    }
}
