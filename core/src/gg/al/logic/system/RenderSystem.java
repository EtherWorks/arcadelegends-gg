package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.logic.component.PhysicComponent;
import gg.al.logic.component.PositionComponent;
import gg.al.logic.component.RenderComponent;
import gg.al.logic.component.StatComponent;
import gg.al.util.Assets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.IntBuffer;
import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
@Slf4j
public class RenderSystem extends BaseEntitySystem {

    @Getter
    private ObjectMap<Integer, FrameBuffer> buffers;
    @Getter
    private int buffHeight = 256 * 6;
    @Getter
    private int buffWidth = 256 * 3;
    @Getter
    private SpriteBatch spriteBatch;
    @Getter
    private BitmapFont font;
    @Getter
    private Camera uiCamera;
    @Getter
    private float stateTime;
    @Getter
    private float worldDegree;
    @Getter
    private final ObjectMap<Integer, Decal> decalMap;
    @Getter
    private final ObjectMap<Integer, Decal> uiMap;
    private final Array<Decal> tempDecals;
    @Getter
    private final DecalBatch decalBatch;
    @Getter
    private final AssetManager assetManager;
    @Getter
    private ComponentMapper<RenderComponent> mapperRender;
    @Getter
    private ComponentMapper<PositionComponent> mapperPosition;
    @Getter
    private ComponentMapper<PhysicComponent> mapperPhysic;
    @Getter
    private ComponentMapper<StatComponent> mapperStats;
    @Getter
    private Assets.LevelAssets levelAssets;

    public RenderSystem(DecalBatch decalBatch, AssetManager assetManager, float worldDegree, Assets.LevelAssets assets) {
        super(Aspect.all(RenderComponent.class, PositionComponent.class));
        this.levelAssets = assets;
        decalMap = new ObjectMap<>();
        tempDecals = new Array<>(100);
        uiMap = new ObjectMap<>();
        this.decalBatch = decalBatch;
        this.assetManager = assetManager;
        buffers = new ObjectMap<>();

        this.spriteBatch = new SpriteBatch();
        this.font = new BitmapFont();
        font.getData().setScale(2);
        font.setColor(Color.BLACK);
        this.uiCamera = new OrthographicCamera();

        Viewport viewportUi = new FitViewport(buffWidth, buffHeight, uiCamera);
        viewportUi.update(viewportUi.getScreenWidth(), viewportUi.getScreenHeight(), true);

        stateTime = 0;
        this.worldDegree = worldDegree;
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
    }

    public Array<Decal> getDecals() {
        tempDecals.clear();
        for (Decal decal : uiMap.values())
            tempDecals.add(decal);
        for (Decal decal : decalMap.values())
            tempDecals.add(decal);
        return tempDecals;
    }
}
