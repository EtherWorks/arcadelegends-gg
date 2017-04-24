package gg.al.graphics;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gg.al.game.AL;
import gg.al.game.screen.IAssetScreen;
import gg.al.game.screen.ILoadingScreen;
import gg.al.util.Assets;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tobia on 03.04.2017.
 */
public class Animator implements IAssetScreen
{
        private static final int FRAME_COLS = 5;
        private static final int FRAME_ROWS = 8;

        private Animation<TextureRegion> walkAnimation;
        private Texture walkSheet;
        private DecalBatch spriteBatch;
        private Decal character;
        private PerspectiveCamera camera;
        private Viewport viewport;

        // Variable to track animated time
        private float stateTime;

        private String path = "";

    @Override
    public void show()
    {
        camera = new PerspectiveCamera();
        camera.position.set(0,0,1);
        camera.near = 0.1f;
        viewport = new FitViewport(1920,1080,camera);
        walkSheet = AL.asset.get(Assets.PT_SIDEVIEWSHEET);
        viewport.apply();

        float xScale = walkSheet.getWidth()/FRAME_COLS;
        float yScale = walkSheet.getHeight()/FRAME_ROWS;

        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++)
        {
            for (int j = 0; j < FRAME_COLS; j++)
            {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);

        spriteBatch = new DecalBatch(new CameraGroupStrategy(camera));
        character = Decal.newDecal(1,1,tmp[1][1],true);
        stateTime = 0f;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT); // Clear screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        // Get current frame of animation for the current stateTime
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        character.setTextureRegion(currentFrame);
        spriteBatch.add(character);
        spriteBatch.flush();

    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width,height);
    }


    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {
        spriteBatch.dispose();
        AL.asset.unload(Assets.PT_SIDEVIEWSHEET.fileName);
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public List<AssetDescriptor> assets()
    {
        return Arrays.asList(Assets.PT_SIDEVIEWSHEET);
    }

    @Override
    public ILoadingScreen customLoadingScreen()
    {
        return null;
    }
}
