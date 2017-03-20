package gg.al.desktop.launcher;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gg.al.config.Config;
import gg.al.desktop.config.DesktopConfigEditor;
import gg.al.desktop.config.DesktopConfigUtil;
import gg.al.game.ArcadeLegendsGame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;

/**
 * Created by Thomas Neumann on 11.03.2017.<br />
 * Main class responsible for launching the game in a
 * desktop environment.
 */
@Slf4j
public class DesktopLauncher {
    public static void main(String[] arg) {

        try {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.resizable = false;
            DesktopConfigEditor editor = new DesktopConfigEditor();
            Config cfg = DesktopConfigUtil.buildConfig(editor);
            DesktopConfigUtil.setupLwjglConfig(config, cfg);
            LwjglApplication application = new LwjglApplication(new ArcadeLegendsGame(cfg), config);
            DesktopConfigUtil.registerStandardListeners(editor, cfg, config, application);
        } catch (Exception ex) {
            log.error(MarkerFactory.getMarker("ERROR"), "Error loading config", ex);
            System.exit(0);
        }

    }
}
