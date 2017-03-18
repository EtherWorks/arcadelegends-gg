package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public interface IGameplayConfig {
    String PREFIX = "gameplay";

    enum GameplayKeys implements IConfigKey {
        ;
        private final String key;

        GameplayKeys(java.lang.String key) {
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
