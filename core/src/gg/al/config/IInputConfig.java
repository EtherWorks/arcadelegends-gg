package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config interface housing methods for accessing the current input / control configuration.
 */
public interface IInputConfig {
    String PREFIX = "input";

    int up();
    int down();
    int left();
    int right();

    enum InputKeys implements IConfigKey {
        up("0"),
        down("0"),
        left("0"),
        right("0")

        ;

        private final String defaultValue;

        InputKeys(String defaultValue) {
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
