package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
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
        VSYNC("vsyncEnabled", "true"),
        BACKGROUNDFPS("backgroundFPS", "15"),
        FOREGROUNDFPS("foregroundFPS", "144"),
        SCREENMODE("screenmode", "Fullscreen"),
        WIDTH("width", "960"),
        HEIGHT("height", "540");

        private final String key;
        private final String defaultValue;

        VideoKeys(String key, String defaultValue) {
            this.key = key;
            this.defaultValue = defaultValue;
        }

        @Override
        public String getKeyName() {
            return key;
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
