package gg.al.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

/**
 * Created by Thomas Neumann on 21.04.2017.<br />
 */
public class CameraLayerGroupStrategy extends CameraGroupStrategy {


    private static final int GROUP_OPAQUE = 0;
    private static final int GROUP_BLEND = 1;

    public CameraLayerGroupStrategy(Camera camera) {
        super(camera);
    }

    public CameraLayerGroupStrategy(Camera camera, Comparator<Decal> sorter) {
        super(camera, sorter);
    }

    @Override
    public void beforeGroup(int group, Array<Decal> contents) {
        if (group == GROUP_BLEND)
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        super.beforeGroup(group, contents);
    }

    @Override
    public void beforeGroups() {
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        super.beforeGroups();
    }

    @Override
    public void afterGroups() {
        super.afterGroups();
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
    }
}
