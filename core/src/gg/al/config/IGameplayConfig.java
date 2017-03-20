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

        GameplayKeys(String key) {
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
