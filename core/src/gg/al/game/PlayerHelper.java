package gg.al.game;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import gg.al.character.Character;
import gg.al.logic.ArcadeWorld;
import gg.al.logic.component.CharacterComponent;
import gg.al.logic.component.StatComponent;
import gg.al.util.Assets;
import gg.al.util.Shaders;

/**
 * Created by Thomas Neumann on 07.06.2017.
 */
public class PlayerHelper implements Disposable {

    private final int entityId;
    private final ArcadeWorld arcadeWorld;
    private final float[] abilityPercents = new float[5];
    private final FrameBuffer[] abilityBuffers = new FrameBuffer[5];
    private final TextureRegion[] cooldownTextures = new TextureRegion[5];
    float resourcePerc = 0;
    float healthPerc = 0;
    private Sprite[] abilityOverlaySprites = new Sprite[5];
    private Texture[] icons = new Texture[5];
    private FrameBuffer resourceBuffer;
    private TextureRegion resourceTexture;
    private FrameBuffer healthBuffer;
    private TextureRegion healthTexture;

    private ShaderProgram shader;
    private SpriteBatch shaderBatch;

    private TextureRegion healthGradient;
    private TextureRegion cooldownGradient;
    private TextureRegion passiveGradient;

    private Camera healthCam;
    private Camera cooldownCam;

    private Color healthColor = new Color(0.7f, 0, 0, 1);
    private Color resourceColor = new Color(0, 0, 0.7f, 1);
    private Color cooldownColor = new Color(0.7f, 0.7f, 0.7f, 0.8f);

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

        drawToBuffer(healthBuffer, healthColor, healthGradient, healthPerc, healthCam.combined);
        drawToBuffer(resourceBuffer, resourceColor, healthGradient, resourcePerc, healthCam.combined);
        for (int i = 0; i < abilityBuffers.length; i++) {
            drawToBuffer(abilityBuffers[i], cooldownColor, i == 0 ? passiveGradient : cooldownGradient, abilityPercents[i], cooldownCam.combined);
        }
    }

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

    public boolean isDead()
    {
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
        resourceBuffer.dispose();
        shaderBatch.dispose();
        shader.dispose();
        for (Sprite s : abilityOverlaySprites)
            s.getTexture().dispose();
    }
}
