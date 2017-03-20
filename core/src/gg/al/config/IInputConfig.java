package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config interface housing methods for accessing the current input / control configuration.
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
