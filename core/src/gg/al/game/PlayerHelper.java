package gg.al.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
    private FrameBuffer resourceBuffer;
    private TextureRegion resourceTexture;
    private FrameBuffer healthBuffer;
    private TextureRegion healthTexture;

    private ShaderProgram shader;
    private SpriteBatch shaderBatch;

    private TextureRegion healthGradient;
    private TextureRegion cooldownGradient;

    private Color healthColor = new Color(255, 0, 0, 1);
    private Color resourceColor = new Color (0,0,255,1);
    private Color cooldownColor = new Color(1, 1, 1, 0.8f);

    public PlayerHelper(int entityId, ArcadeWorld arcadeWorld, Assets.LevelAssets assets) {
        this.entityId = entityId;
        this.arcadeWorld = arcadeWorld;
        shader = new ShaderProgram(Shaders.GradientShader.vertexShader, Shaders.GradientShader.fragmentShader);
        if (shader.isCompiled() == false)
            throw new IllegalArgumentException("couldn't compile shader: " + shader.getLog());
        shaderBatch = new SpriteBatch(1000, shader);

        this.healthGradient = new TextureRegion(assets.health_gradient);
        this.healthBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, healthGradient.getRegionWidth(), healthGradient.getRegionHeight(), false);
        this.healthTexture = new TextureRegion(healthBuffer.getColorBufferTexture());
        healthTexture.flip(false, true);
        this.resourceBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, healthGradient.getRegionWidth(), healthGradient.getRegionHeight(), false);
        this.resourceTexture = new TextureRegion(resourceBuffer.getColorBufferTexture());
        resourceTexture.flip(false, true);
        this.cooldownGradient = new TextureRegion(assets.cooldown_gradient);
        for (int i = 0; i < abilityBuffers.length; i++) {
            abilityBuffers[i] = new FrameBuffer(Pixmap.Format.RGBA8888, cooldownGradient.getRegionWidth(), cooldownGradient.getRegionHeight(), false);
            cooldownTextures[i] = new TextureRegion(abilityBuffers[i].getColorBufferTexture());
            cooldownTextures[i].flip(false, true);
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

        drawToBuffer(healthBuffer, healthColor, healthGradient, healthPerc);
        drawToBuffer(resourceBuffer, resourceColor, healthGradient, resourcePerc);
        for (int i = 0; i < abilityBuffers.length; i++) {
            drawToBuffer(abilityBuffers[i], cooldownColor, cooldownGradient, abilityPercents[i]);
        }
    }

    private void drawToBuffer(FrameBuffer buffer, Color color, TextureRegion gradient, float perc) {
        buffer.begin();
        AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
        AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);

        shaderBatch.begin();
        shaderBatch.setColor(color);
        shader.setUniformf("u_gradient", perc);
        shaderBatch.draw(gradient, 0, 0);
        shaderBatch.end();
        buffer.end();
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

    public int getEntityId() {
        return entityId;
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
    }
}
