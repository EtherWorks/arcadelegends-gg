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
        masterVolume("100"),
        musicVolume("100"),
        effectVolume("100")
        ;

        private final String defaultValue;

        AudioKeys(String defaultValue) {
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
}
