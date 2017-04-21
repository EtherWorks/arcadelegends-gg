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
        LOGCONFIGEV("logConfigEvents", "true");

        private final String key;
        private final String defaultValue;

        MiscellaneousKeys(String key, String defaultValue) {
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
