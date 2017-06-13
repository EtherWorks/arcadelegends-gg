package gg.al.config;

/**
 * Created by Patrick Windegger on 15.03.2017.<br />
 * Config interface housing methods for accessing the current gameplay configuration.
 */
public interface IGameplayConfig {
    String PREFIX = "gameplay";

    enum GameplayKeys implements IConfigKey {
        ;
        private final String defaultValue;

        GameplayKeys(String defaultValue) {
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
