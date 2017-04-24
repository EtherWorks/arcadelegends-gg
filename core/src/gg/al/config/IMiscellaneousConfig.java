package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Config interface housing methods for accessing the current configuration
 * for miscellaneous options.
 */
public interface IMiscellaneousConfig {
    String PREFIX = "miscellaneous";

    boolean logConfigEvents();

    enum MiscellaneousKeys implements IConfigKey {
        logConfigEvents("true");

        private final String defaultValue;

        MiscellaneousKeys(String defaultValue) {
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
