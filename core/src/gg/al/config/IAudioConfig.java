package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config interface housing methods for accessing the current audio configuration.
 */
public interface IAudioConfig {
    String PREFIX = "audio";

    float masterVolume();

    enum AudioKeys implements IConfigKey {
        MASTERVOLUME("masterVolume")
        ;
        private final String key;

        AudioKeys(String key) {
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
