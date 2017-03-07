package gg.al.prototype.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Thomas on 02.03.2017.
 */
public class BillboardTestScreen implements Screen {

    private Texture texture;
    private TextureRegion region;
    private Viewport viewport;
    private Viewport viewportPers;
    private PerspectiveCamera perspectiveCamera;
    private Vector2 pos;
    private Decal ez;
    private DecalBatch decbatch;
    private CameraGroupStrategy strat;

    @Override
    public void show() {

        texture = new Texture("assets/prototype/sprites/ezreal.png");
        region = new TextureRegion();
        perspectiveCamera = new PerspectiveCamera(45, 960, 540);
        perspectiveCamera.position.set(0, 0, 5);
        perspectiveCamera.lookAt(0, 0, 0);
        perspectiveCamera.far = 100;
        perspectiveCamera.near = 1;
        //perspectiveCamera.rotateAround(Vector3.Zero, Vector3.X, 5);
        perspectiveCamera.update();
        pos = new Vector2();
        Gdx.input.setInputProcessor(new CameraInputController(perspectiveCamera));
        decbatch = new DecalBatch(strat = new CameraGroupStrategy(perspectiveCamera));
        ez = Decal.newDecal(new TextureRegion(texture));
        ez.setPosition(0, 0, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ez.lookAt(perspectiveCamera.position, perspectiveCamera.up);
        decbatch.add(ez);
        decbatch.flush();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
