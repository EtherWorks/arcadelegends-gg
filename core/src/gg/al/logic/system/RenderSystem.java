package gg.al.logic.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.logic.component.DynamicPhysic;
import gg.al.logic.component.Position;
import gg.al.logic.component.Render;
import gg.al.logic.component.Stats;

/**
 * Created by Thomas Neumann on 30.03.2017.<br />
 */
public class RenderSystem extends IteratingSystem {

    private FrameBuffer buffer;
    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Camera uiCamera;

    private final ObjectMap<Integer, Decal> decalMap;
    private final ObjectMap<Integer, Decal> uiMap;
    private final DecalBatch decalBatch;
    private final AssetManager assetManager;
    private ComponentMapper<Render> mapperRender;
    private ComponentMapper<Position> mapperPosition;
    private ComponentMapper<DynamicPhysic> mapperDynamicPhysic;
    private ComponentMapper<Stats> mapperStats;

    public RenderSystem(DecalBatch decalBatch, AssetManager assetManager) {
        super(Aspect.all(Render.class, Position.class));
        decalMap = new ObjectMap<>();
        uiMap = new ObjectMap<>();
        this.decalBatch = decalBatch;
        this.assetManager = assetManager;
        this.buffer = new FrameBuffer(Pixmap.Format.RGBA8888, 128 * 3, 128 * 2, false);
        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.spriteBatch = new SpriteBatch();
        this.font = new BitmapFont();
        font.getData().setScale(2.5f);
        this.uiCamera = new OrthographicCamera();
        Viewport viewportUi = new FitViewport(buffer.getWidth(), buffer.getHeight(), uiCamera);
        viewportUi.update(viewportUi.getScreenWidth(), viewportUi.getScreenHeight(), true);
    }

    @Override
    protected void process(int entityId) {
        DynamicPhysic dynamicPhysic = mapperDynamicPhysic.get(entityId);
        Position position = mapperPosition.get(entityId);
        Stats stats = mapperStats.get(entityId);
        Decal decal = decalMap.get(entityId);
        if (dynamicPhysic != null)
            decal.setPosition(dynamicPhysic.getBody().getPosition().x, dynamicPhysic.getBody().getPosition().y, 0);
        else
            decal.setPosition(position.position.x, position.position.y, 0);
        decalBatch.add(decal);

        if (stats != null) {
            buffer.begin();
            AL.graphics.getGL20().glClearColor(0, 0, 0, 0);
            AL.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
            spriteBatch.setProjectionMatrix(uiCamera.combined);
            spriteBatch.begin();

            font.draw(spriteBatch, String.format("%1.1fap", stats.abilityPower), 10, 30);
            font.draw(spriteBatch, String.format("%1.1fad", stats.attackDamage), 10, 60);
            font.draw(spriteBatch, String.format("%1.1fms", stats.moveSpeed), 10, 90);

            font.draw(spriteBatch, String.format("%1.1fcd", stats.cooldownReduction), 130, 30);
            font.draw(spriteBatch, String.format("%1.1fh/s", stats.healthRegen), 130, 60);
            font.draw(spriteBatch, String.format("%1.1fr/s", stats.resourceRegen), 130, 90);

            font.draw(spriteBatch, String.format("%d/%d", (int) stats.actionPoints, stats.maxActionPoints), 10, 170);
            font.draw(spriteBatch, String.format("%dRP/%dRP", (int) stats.resource, stats.maxResource), 110, 130);
            font.draw(spriteBatch, String.format("%dHP/%dHP", (int) stats.health, stats.maxHealth), 110, 170);
            spriteBatch.end();
            buffer.end();

            Decal uiDecal = uiMap.get(entityId);
            uiDecal.getTextureRegion().setTexture(buffer.getColorBufferTexture());
            if (dynamicPhysic != null)
                uiDecal.setPosition(dynamicPhysic.getBody().getPosition().x, dynamicPhysic.getBody().getPosition().y + 1, 0.1f);
            else
                uiDecal.setPosition(position.position.x, position.position.y + 1, 0.1f);
            decalBatch.add(uiDecal);
        }
    }

    @Override
    protected void inserted(int entityId) {
        Render render = mapperRender.get(entityId);
        Position position = mapperPosition.get(entityId);
        Decal decal = Decal.newDecal(render.width, render.height, new TextureRegion(assetManager.get(render.texture)), render.transparent);
        decal.setPosition(position.position.x, position.position.y, 0);
        decalMap.put(entityId, decal);

        TextureRegion uiTextureRegion = new TextureRegion(buffer.getColorBufferTexture());
        uiTextureRegion.flip(false, true);
        Decal uiDecal = Decal.newDecal(3, 2, uiTextureRegion, true);
        uiDecal.setPosition(position.position.x, position.position.y, 0);
        uiMap.put(entityId, uiDecal);
    }

    @Override
    protected void removed(int entityId) {
        decalMap.remove(entityId);
        uiMap.remove(entityId);
    }

    @Override
    public void dispose() {
        buffer.dispose();
        spriteBatch.dispose();
        font.dispose();
    }
}
