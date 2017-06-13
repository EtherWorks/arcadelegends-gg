package gg.al.desktop.config;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.drapostolos.typeparser.TypeParser;
import gg.al.config.Config;
import gg.al.config.IVideoConfig;
import lombok.extern.slf4j.Slf4j;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.NoSuchElementException;

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

    /**
     * Builds the CFG4J {@link ConfigurationProvider} for the properties file.
     *
     * @return the {@link ConfigurationProvider}
     */
    public static ConfigurationProvider buildConfigProvider() {
        File file = new File(getCurrentConfigPath());
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                Config.defaultConfig().store(fos, "Default config for ArcadeLegends");
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConfigurationSource s = new FilesConfigurationSource(() -> Arrays.asList(Paths.get(getCurrentConfigPath())));
        ConfigurationProvider p = new ConfigurationProviderBuilder().withConfigurationSource(s).build();
        return p;
    }

    /**
     * Builds the {@link Config} object with links to CFG4J
     * @return the linked {@link Config}
     */
    public static Config buildConfig() {
        return buildConfig(new DesktopConfigEditor());
    }

    /**
     * @see #buildConfig()
     */
    public static Config buildConfig(DesktopConfigEditor editor) {
        Config cfg = null;
        try {
            cfg = new Config(buildConfigProvider(), editor);
        } catch (NoSuchElementException ex) {
            File file = new File(getCurrentConfigPath());
            File errFile = new File(getCurrentConfigPath() + ".err");
            if (errFile.exists())
                errFile.delete();
            file.renameTo(errFile);
            cfg = new Config(buildConfigProvider(), editor);
        }
        return cfg;
    }

    /**
     * Sets up the given {@link LwjglApplicationConfiguration} object.
     * @param config the {@link LwjglApplicationConfiguration} to alter
     * @param cfg the {@link Config} as presets
     */
    public static void setupLwjglConfig(LwjglApplicationConfiguration config, Config cfg) {
        config.foregroundFPS = cfg.video.foregroundFPS();
        config.backgroundFPS = cfg.video.backgroundFPS();
        config.fullscreen = cfg.video.screenmode().isFullscreen();
        config.vSyncEnabled = cfg.video.vsyncEnabled();
        config.width = cfg.video.width();
        config.height = cfg.video.height();
        System.setProperty("org.lwjgl.opengl.Window.undecorated", cfg.video.screenmode().isBorderless() + "");
    }

    /**
     * Registers the standard listeners to {@link gg.al.config.IConfigKey} changed events, which are native only to desktop.
     * @param editor the {@link gg.al.config.ConfigEditor} which is linked to the {@link Config} object
     * @param cfg the {@link Config} object
     * @param config the {@link LwjglApplicationConfiguration} object
     * @param application the {@link LwjglApplication} to alter
     */
    public static void registerStandardListeners(DesktopConfigEditor editor, Config cfg, LwjglApplicationConfiguration config, LwjglApplication application) {
        TypeParser parser = TypeParser.newBuilder().build();
        if (cfg.miscellaneous.logConfigEvents())
            editor.addConfigValueChangedListener((key, value) -> log.debug("Config value changed: {}={}", key, value));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.screenmode, (key, value) -> {
            IVideoConfig.ScreenMode mode = parser.parse(value, IVideoConfig.ScreenMode.class);
            switch (mode) {
                case Fullscreen:
                    application.postRunnable(() -> {
                        application.getGraphics().setFullscreenMode(application.getGraphics().getDisplayMode());
                        editor.setValue(IVideoConfig.VideoKeys.width, application.getGraphics().getWidth());
                        editor.setValue(IVideoConfig.VideoKeys.width, application.getGraphics().getWidth());
                        editor.flush();
                    });
                    break;
                case Borderless:
                    application.postRunnable(() -> {
                        application.getGraphics().setFullscreenMode(application.getGraphics().getDisplayMode());
                        application.getGraphics().setUndecorated(true);
                        application.getGraphics().setWindowedMode(cfg.video.width(), cfg.video.height());
                        editor.setValue(IVideoConfig.VideoKeys.width, application.getGraphics().getWidth());
                        editor.setValue(IVideoConfig.VideoKeys.width, application.getGraphics().getWidth());
                        editor.flush();
                    });
                    break;
                case Windowed:
                    application.postRunnable(() -> {
                        application.getGraphics().setFullscreenMode(application.getGraphics().getDisplayMode());
                        application.getGraphics().setUndecorated(false);
                        application.getGraphics().setWindowedMode(cfg.video.width(), cfg.video.height());
                    });
                    break;
            }
        }, true);
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.backgroundFPS, (key, value) -> config.backgroundFPS = parser.parse(value, Integer.class));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.foregroundFPS, (key, value) -> config.foregroundFPS = parser.parse(value, Integer.class));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.vsyncEnabled, (key, value) -> application.postRunnable(() -> application.getGraphics().setVSync(parser.parse(value, Boolean.class))));
        editor.addConfigValueChangedListener(IVideoConfig.VideoKeys.width, ((key, value) -> application.postRunnable(() -> {
            if (cfg.video.screenmode().isWindowed()) {
                application.getGraphics().setFullscreenMode(application.getGraphics().getDisplayMode());
                application.getGraphics().setUndecorated(false);
                application.getGraphics().setWindowedMode(cfg.video.width(), cfg.video.height());
            }
        })), true);
    }
}
