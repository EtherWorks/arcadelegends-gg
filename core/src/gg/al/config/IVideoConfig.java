package gg.al.config;

/**
 * Created by Patrick Windegger on 15.03.2017.<br />
 * Config interface housing methods for accessing the current video configuration.
 */
public interface IVideoConfig {

    String PREFIX = "video";

    boolean vsyncEnabled();

    int foregroundFPS();

    int backgroundFPS();

    ScreenMode screenmode();

    int width();

    int height();


    enum VideoKeys implements IConfigKey {
        vsyncEnabled("true"),
        backgroundFPS("15"),
        foregroundFPS("144"),
        screenmode("Fullscreen"),
        width("960"),
        height("540");

        private final String defaultValue;

        VideoKeys(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKeyName() {
            return name();
        }

        @Override
        public String getPrefix() {
            return PREFIX;
        }

        @Override
        public String getDefaultValue() {
            return defaultValue;
        }
    }

    enum ScreenMode {
        Fullscreen, Borderless, Windowed;

        public boolean isFullscreen() {
            return this == Fullscreen;
        }

        public boolean isWindowed() {
            return this == Windowed;
        }

        public boolean isBorderless() {
            return this == Borderless;
        }
    }
}
