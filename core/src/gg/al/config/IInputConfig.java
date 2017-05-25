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

    int ability1();

    int ability2();

    int ability3();

    int ability4();

    int inventory();

    int trait();


    enum InputKeys implements IConfigKey {
        up("0"),
        down("0"),
        left("0"),
        right("0"),
        ability1("0"),
        ability2("0"),
        ability3("0"),
        ability4("0"),
        inventory("0"),
        trait("0");

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

        public static int getFromKey(InputKeys key, IInputConfig cfg) {
            switch (key) {
                case up:
                    return cfg.up();
                case down:
                    return cfg.down();
                case left:
                    return cfg.left();
                case right:
                    return cfg.right();
                case ability1:
                    return cfg.ability1();
                case ability2:
                    return cfg.ability2();
                case ability3:
                    return cfg.ability3();
                case ability4:
                    return cfg.ability4();
                case inventory:
                    return cfg.inventory();
                case trait:
                    return cfg.trait();
                default:
                    throw new IllegalArgumentException("Key does not exist");
            }
        }

    }
}
