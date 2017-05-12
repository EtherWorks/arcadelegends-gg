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
import lombok.extern.slf4j.Slf4j;

import java.nio.IntBuffer;
import java.util.Map;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
@Slf4j
public class RenderSystem extends BaseEntitySystem {

    private ObjectMap<Integer, FrameBuffer> buffers;
    private int buffHeight = 256 * 5;
    private int buffWidth = 256 * 3;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Camera uiCamera;

    private float stateTime;
    private float worldDegree;

    private final ObjectMap<Integer, Decal> decalMap;
    private final ObjectMap<Integer, Decal> uiMap;
    private final DecalBatch decalBatch;
    private final AssetManager assetManager;
    private ComponentMapper<RenderComponent> mapperRender;
    private ComponentMapper<PositionComponent> mapperPosition;
    private ComponentMapper<PhysicComponent> mapperPhysic;
    private ComponentMapper<StatComponent> mapperStats;

    public RenderSystem(DecalBatch decalBatch, AssetManager assetManager, float worldDegree) {
        super(Aspect.all(RenderComponent.class, PositionComponent.class));
        decalMap = new ObjectMap<>();
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
        PhysicComponent physic = mapperPhysic.get(entityId);
        PositionComponent position = mapperPosition.get(entityId);
        RenderComponent render = mapperRender.get(entityId);

        StatComponent stats = mapperStats.get(entityId);
        Decal decal = decalMap.get(entityId);
        TextureRegion region = render.animations.get(render.renderState).getKeyFrame(stateTime);
        region.flip(region.isFlipX() ^ render.flipX, region.isFlipY() ^ render.flipY);
        decal.setTextureRegion(region);

        if (physic != null)
            decal.setPosition(physic.body.getPosition().x, physic.body.getPosition().y, decal.getZ());
        else
            decal.setPosition(position.position.x, position.position.y, decal.getZ());
        decalBatch.add(decal);

        if (stats != null) {
            FrameBuffer buffer = buffers.get(entityId);
            buffer.begin();
            AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
            AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
            spriteBatch.setProjectionMatrix(uiCamera.combined);
            spriteBatch.begin();
            font.draw(spriteBatch, stats.toString(), 0, buffHeight);
            spriteBatch.end();
            buffer.end();

            Decal uiDecal = uiMap.get(entityId);
            if (physic != null)
                uiDecal.setPosition(physic.body.getPosition().x + 2, physic.body.getPosition().y - 3, uiDecal.getZ());
            else
                uiDecal.setPosition(position.position.x + 2, position.position.y - 3, uiDecal.getZ());
            decalBatch.add(uiDecal);
        }
    }

    @Override
    protected void inserted(int entityId) {
        RenderComponent render = mapperRender.get(entityId);
        PositionComponent position = mapperPosition.get(entityId);
        if (render.textures.size == 0) {
            for (Map.Entry<RenderComponent.RenderState, RenderComponent.RenderTemplate.AnimationTemplate> entry : render.renderTemplate.animationTemplates.entrySet()) {
                Texture texture = assetManager.get((AssetDescriptor<Texture>) Assets.get(entry.getValue().texture));
                render.textures.put(entry.getKey(), Assets.get(entry.getValue().texture));

                TextureRegion[][] tmp = TextureRegion.split(texture,
                        +texture.getWidth() / entry.getValue().frameColumns,
                        +texture.getHeight() / entry.getValue().frameRows);
                TextureRegion[] frames = new TextureRegion[entry.getValue().frameColumns * entry.getValue().frameRows];
                int index = 0;
                for (int i = 0; i < entry.getValue().frameRows; i++) {
                    for (int j = 0; j < entry.getValue().frameColumns; j++) {
                        frames[index++] = tmp[i][j];
                    }
                }
                render.animations.put(entry.getKey(), new Animation<TextureRegion>(entry.getValue().frameDuration, new Array<>(frames), Animation.PlayMode.LOOP));
            }
        }
        Decal decal = Decal.newDecal(render.width, render.height, render.animations.get(render.renderState).getKeyFrame(stateTime), render.transparent);
        decal.rotateX(worldDegree);
        decal.setPosition(position.position.x, position.position.y, 0);
        decalMap.put(entityId, decal);

        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, buffWidth, buffHeight, false);
        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        buffers.put(entityId, buffer);

        TextureRegion uiTextureRegion = new TextureRegion(buffer.getColorBufferTexture());
        uiTextureRegion.flip(false, true);
        Decal uiDecal = Decal.newDecal(3, 5, uiTextureRegion, true);
        uiDecal.setPosition(position.position.x, position.position.y, 1);
        uiMap.put(entityId, uiDecal);
    }

    @Override
    protected void removed(int entityId) {
        buffers.get(entityId).dispose();
        buffers.remove(entityId);
        decalMap.remove(entityId);
        uiMap.remove(entityId);
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
}
