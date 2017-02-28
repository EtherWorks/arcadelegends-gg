package gg.al;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import gg.al.manager.SceneManager;
import gg.al.screen.LoadingScreen;
import gg.al.screen.Splash;
import gg.al.screen.TestScreen;

public class ArcadeLegends extends Game {

    private AssetManager assetManager;
    private SceneManager sceneManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        sceneManager = new SceneManager();
        setScreen(new LoadingScreen(this, Splash.ASSETS, new Splash(this, new TestScreen(this))));
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
