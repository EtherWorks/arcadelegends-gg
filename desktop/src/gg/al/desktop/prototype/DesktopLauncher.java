package gg.al.desktop.prototype;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gg.al.prototype.ArcadeLegends;

import java.util.HashMap;
import java.util.Map;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("assets/prototype/sprites/logo32.png", Files.FileType.Local);
        config.addIcon("assets/prototype/sprites/logo16.png", Files.FileType.Local);
        config.preferencesFileType = Files.FileType.Local;
        config.preferencesDirectory = "config";
        config.width = ScreenSizes.DEFAULT.getWidth();
        config.height = ScreenSizes.DEFAULT.getHeight();
        config.resizable = true;
        LwjglApplication application = new LwjglApplication(new ArcadeLegends(), config);

        Preferences pref = application.getPreferences("video");
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("foregroundFPS", 140);
        defaults.put("backgroundFPS", 15);
        defaults.put("vsync", false);
        defaults.put("height", ScreenSizes.DEFAULT.getHeight());
        defaults.put("width", ScreenSizes.DEFAULT.getWidth());
        defaults.put("isFullscreen", true);
        config.foregroundFPS = pref.getInteger("foregroundFPS", (int) defaults.get("foregroundFPS"));
        config.backgroundFPS = pref.getInteger("backgroundFPS", (int) defaults.get("backgroundFPS"));
        application.postRunnable(() -> {
            if (pref.getBoolean("isFullscreen", (boolean) defaults.get("isFullscreen")))
                application.getGraphics().setFullscreenMode(application.getGraphics().getDisplayMode());
            else
                application.getGraphics().setWindowedMode(
                        pref.getInteger("width", (int) defaults.get("width")),
                        pref.getInteger("height", (int) defaults.get("height")));
            application.getGraphics().setVSync(pref.getBoolean("vsync", (boolean) defaults.get("vsync")));
        });
        if (!pref.contains("vsync")) {
            pref.put(defaults);
            pref.flush();
        }

    }
}
