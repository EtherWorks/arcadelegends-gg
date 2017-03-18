package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public interface IInputConfig {
    String PREFIX = "input";

    enum InputKeys implements IConfigKey {
        ;
        private final String key;

        InputKeys(String key) {
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
