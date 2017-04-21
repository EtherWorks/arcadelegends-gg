package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config interface housing methods for accessing the current gameplay configuration.
 */
public interface IGameplayConfig {
    String PREFIX = "gameplay";

    enum GameplayKeys implements IConfigKey {
        ;
        private final String key;
        private final String defaultValue;

        GameplayKeys(String key, String defaultValue) {
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
