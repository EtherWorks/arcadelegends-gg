package gg.al;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import gg.al.manager.SceneManager;
import gg.al.screen.MapTestScreen;

public class ArcadeLegends extends Game {

    private AssetManager assetManager;
    private SceneManager sceneManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        sceneManager = new SceneManager();
        setScreen(new MapTestScreen());
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        sceneManager.dispose();
    }
}
