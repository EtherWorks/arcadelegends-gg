package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config interface housing methods for accessing the current audio configuration.
 */
public interface IAudioConfig {
    String PREFIX = "audio";

    float masterVolume();

    float musicVolume();

    float effectVolume();

    enum AudioKeys implements IConfigKey {
        MASTERVOLUME("masterVolume", "100"),
        MUSICVOLUME("musicVolume", "100"),
        EFFECTVOLUME("effectVolume", "100")
        ;
        private final String key;
        private final String defaultValue;

        AudioKeys(String key, String defaultValue) {
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
}
