package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.logic.component.*;
import gg.al.util.Assets;
import gg.al.util.Shaders;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Thomas Neumann on 30.03.2017.<br>
 * {@link BaseEntitySystem} containing all logic responsible for updating the render data for each entity.
 * Most render work is delegated to the {@link RenderComponent.RenderDelegate}, but this class holds the references on most of the used resources.
 * {@link RenderComponent.RenderDelegate} don´t own the resources, and only the {@link RenderSystem} can release them.
 */
@Slf4j
public class RenderSystem extends BaseEntitySystem {

    /**
     * Map containing the {@link Decal} used for rendering of the entity sprites.
     */
    @Getter
    private final ObjectMap<Integer, Decal> decalMap;
    @Getter
    /**
     * Map containing the {@link Decal} used for rendering of the entity UI.
     */
    private final ObjectMap<Integer, Decal> uiMap;
    @Getter
    private final DecalBatch decalBatch;
    @Getter
    private final AssetManager assetManager;
    @Getter
    private ObjectMap<Integer, FrameBuffer> buffers;
    @Getter
    private SpriteBatch spriteBatch;
    @Getter
    private BitmapFont font;
    @Getter
    private Camera uiCamera;
    /**
     * The current state time of the simulation.
     */
    @Getter
    private float stateTime;
    /**
     * The tilt angle of the complete level.
     */
    @Getter
    private float worldDegree;
    @Getter
    private ComponentMapper<RenderComponent> mapperRender;
    @Getter
    private ComponentMapper<PositionComponent> mapperPosition;
    @Getter
    private ComponentMapper<PhysicComponent> mapperPhysic;
    @Getter
    private ComponentMapper<StatComponent> mapperStats;
    @Getter
    private ComponentMapper<CharacterComponent> mapperCharacterComponent;
    @Getter
    private Assets.LevelAssets levelAssets;
    @Getter
    private ShaderProgram gradientShader;
    @Getter
    private SpriteBatch shaderBatch;

    public RenderSystem(DecalBatch decalBatch, AssetManager assetManager, float worldDegree, Assets.LevelAssets assets) {
        super(Aspect.all(RenderComponent.class, PositionComponent.class));
        this.levelAssets = assets;
        decalMap = new ObjectMap<>();
        uiMap = new ObjectMap<>();
        this.decalBatch = decalBatch;
        this.assetManager = assetManager;
        buffers = new ObjectMap<>();

        this.spriteBatch = new SpriteBatch();
        this.font = assets.uifont;
        font.setColor(Color.BLACK);
        this.uiCamera = new OrthographicCamera();

        Viewport viewportUi = new FitViewport(levelAssets.health_bar_gradient.getWidth(), levelAssets.health_bar_gradient.getHeight(), uiCamera);
        viewportUi.update(viewportUi.getScreenWidth(), viewportUi.getScreenHeight(), true);

        stateTime = 0;
        this.worldDegree = worldDegree;

        gradientShader = new ShaderProgram(Shaders.GradientShader.vertexShader, Shaders.GradientShader.fragmentShader);
        if (gradientShader.isCompiled() == false)
            throw new IllegalArgumentException("couldn't compile shader: " + gradientShader.getLog());
        shaderBatch = new SpriteBatch(10, gradientShader);
    }


    protected void process(int entityId) {
        RenderComponent renderComponent = mapperRender.get(entityId);
        renderComponent.renderDelegate.process(entityId, this);
    }

    @Override
    protected void inserted(int entityId) {
        mapperRender.get(entityId).renderDelegate.inserted(entityId, this);
    }

    @Override
    protected void removed(int entityId) {
        mapperRender.get(entityId).renderDelegate.removed(entityId, this);
    }

    /**
     * Updates the {@link #stateTime}.
     */
    @Override
    protected void processSystem() {
        if (stateTime == Float.MAX_VALUE)
            stateTime = 0;
        stateTime += getWorld().getDelta();
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            process(ids[i]);
        }
    }

    @Override
    public void dispose() {
        ObjectMap.Values<FrameBuffer> buffs = buffers.values();
        while (buffs.hasNext())
            buffs.next().dispose();
        spriteBatch.dispose();
        font.dispose();
        shaderBatch.dispose();
        gradientShader.dispose();
    }

    public float getDelta() {
        return getWorld().getDelta();
    }
}
