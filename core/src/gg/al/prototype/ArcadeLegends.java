package gg.al.prototype;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import gg.al.prototype.manager.SceneManager;
import gg.al.prototype.screen.MapTestScreen2;

public class ArcadeLegends extends Game {

    private AssetManager assetManager;
    private SceneManager sceneManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        sceneManager = new SceneManager();
        setScreen(new MapTestScreen2());
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
