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

    boolean fullscreen();

    int width();

    int height();


    enum VideoKeys implements IConfigKey {
        VSYNC("vsyncEnabled"),
        BACKGROUNDFPS("backgroundFPS"),
        FOREGROUNDFPS("foregroundFPS"),
        FULLSCREEN("fullscreen"),
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
}
