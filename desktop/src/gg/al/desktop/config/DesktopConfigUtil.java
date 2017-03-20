package gg.al.desktop.config;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.drapostolos.typeparser.TypeParser;
import com.google.common.io.Files;
import gg.al.config.Config;
import gg.al.config.IVideoConfig;
import lombok.extern.slf4j.Slf4j;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Utility class holding useful methods for building
 * the {@link Config} class in a desktop environment.
 */
@Slf4j
public class DesktopConfigUtil {

    public static final String CONFIGFILELOCATION = "config/config.properties";

    public static String getCurrentConfigPath() {
        return System.getProperty("user.dir") + File.separator + CONFIGFILELOCATION;
    }

    public static ConfigurationProvider buildConfigProvider() {
        File file = new File(getCurrentConfigPath());
        if (!file.exists()) {
            try {
                File defaultConfig = new File(DesktopConfigUtil.class.getResource("/config.properties").toURI());
                Files.copy(defaultConfig, file);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConfigurationSource s = new FilesConfigurationSource(() -> Arrays.asList(Paths.get(getCurrentConfigPath())));
        ConfigurationProvider p = new ConfigurationProviderBuilder().withConfigurationSource(s).build();
        return p;
    }

    public static Config buildConfig() {
        return buildConfig(new DesktopConfigEditor());
    }

    public static Config buildConfig(DesktopConfigEditor editor) {
        return new Config(buildConfigProvider(), editor);
    }

    public static void setupLwjglConfig(LwjglApplicationConfiguration config, Config cfg) {
        config.foregroundFPS = cfg.video.foregroundFPS();
        config.backgroundFPS = cfg.video.backgroundFPS();
        config.fullscreen = cfg.video.fullscreen();
        config.vSyncEnabled = cfg.video.vsyncEnabled();
        config.width = cfg.video.width();
        config.height = cfg.video.height();
    }

    public static void registerStandardListeners(DesktopConfigEditor editor, Config cfg, LwjglApplicationConfiguration config, LwjglApplication application) {
        TypeParser parser = TypeParser.newBuilder().build();
        if (cfg.miscellaneous.logConfigEvents())
            editor.addConfigValueChangedListener((key, value) -> log.debug("Config value changed: {}={}", key, value));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.FULLSCREEN, (key, value) -> {
            boolean fullscreen = parser.parse(value, Boolean.class);
            if (fullscreen)
                application.postRunnable(() -> application.getGraphics().setFullscreenMode(application.getGraphics().getDisplayMode()));
            else
                application.postRunnable(() -> application.getGraphics().setWindowedMode(cfg.video.width(), cfg.video.height()));
        });
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.BACKGROUNDFPS, (key, value) -> config.backgroundFPS = parser.parse(value, Integer.class));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.FOREGROUNDFPS, (key, value) -> config.foregroundFPS = parser.parse(value, Integer.class));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.VSYNC, (key, value) -> application.postRunnable(() -> application.getGraphics().setVSync(parser.parse(value, Boolean.class))));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.WIDTH, (key, value) -> application.postRunnable(() -> application.getGraphics().setWindowedMode(cfg.video.width(), cfg.video.height())), true);
    }
}
