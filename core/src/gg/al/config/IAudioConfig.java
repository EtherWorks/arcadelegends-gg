package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public interface IAudioConfig {
    String PREFIX = "audio";


    enum AudioKeys implements IConfigKey {
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
