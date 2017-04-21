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
        VSYNC("vsyncEnabled"),
        BACKGROUNDFPS("backgroundFPS"),
        FOREGROUNDFPS("foregroundFPS"),
        SCREENMODE("screenmode"),
        WIDTH("width"),
        HEIGHT("height");

        private final String key;

        VideoKeys(String key) {
            this.key = key;
        }

        @Override
        public String getKeyName() {
            return key;
        }

        @Override
        public String getPrefix() {
            return PREFIX;
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
