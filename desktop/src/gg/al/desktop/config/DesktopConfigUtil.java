package gg.al.desktop.config;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gg.al.config.Config;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public class DesktopConfigUtil {

    public static final String CONFIGFILELOCATION = "config/config.properties";

    public static String getCurrentConfigPath() {
        return System.getProperty("user.dir") + File.separator + CONFIGFILELOCATION;
    }

    public static ConfigurationProvider buildConfigProvider() {
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
    }
}
