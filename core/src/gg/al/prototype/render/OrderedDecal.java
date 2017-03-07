package gg.al.prototype.render;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;

/**
 * Created by Dr. Gavrel on 03.03.2017.
 */
public class OrderedDecal extends Decal {
    private int layer;

    public OrderedDecal(int layer) {
        this.layer = layer;
    }

    public OrderedDecal(DecalMaterial material, int layer) {
        super(material);
        this.layer = layer;
    }

    public static OrderedDecal newOrdererdDecal(int layer, float width, float height, TextureRegion textureRegion, int srcBlendFactor, int dstBlendFactor) {
        OrderedDecal decal = new OrderedDecal(layer);
        decal.setTextureRegion(textureRegion);
        decal.setBlending(srcBlendFactor, dstBlendFactor);
        decal.dimensions.x = width;
        decal.dimensions.y = height;
        decal.setColor(1, 1, 1, 1);
        return decal;
    }

    public static OrderedDecal newOrderedDecal(int layer, float width, float height, TextureRegion textureRegion, boolean hasTransparency) {
        return newOrdererdDecal(layer, width, height, textureRegion, hasTransparency ? GL20.GL_SRC_ALPHA : DecalMaterial.NO_BLEND,
                hasTransparency ? GL20.GL_ONE_MINUS_SRC_ALPHA : DecalMaterial.NO_BLEND);
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
