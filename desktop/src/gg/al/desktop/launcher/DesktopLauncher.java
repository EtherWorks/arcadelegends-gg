package gg.al.desktop.launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gg.al.config.Config;
import gg.al.config.IVideoConfig;
import gg.al.desktop.config.DesktopConfigEditor;
import gg.al.desktop.config.DesktopConfigUtil;
import gg.al.game.ArcadeLegendsGame;

/**
 * Created by Thomas Neumann on 11.03.2017.
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        DesktopConfigEditor editor = new DesktopConfigEditor();
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeyNames.BACKGRFPS, (key, value) -> config.backgroundFPS = (int) value);
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeyNames.FOREGRFPS, (key, value) -> config.foregroundFPS = (int) value);
        Config cfg = DesktopConfigUtil.buildConfig(editor);
        DesktopConfigUtil.setupLwjglConfig(config, cfg);

        LwjglApplication application = new LwjglApplication(new ArcadeLegendsGame(cfg), config);
    }
}
