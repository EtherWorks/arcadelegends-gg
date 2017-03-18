package gg.al.config;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public interface IMiscellaneousConfig {
    String PREFIX = "miscellaneous";

    boolean logConfigEvents();

    enum MiscellaneousKeys implements IConfigKey {
        LOGCONFIGEV("logConfigEvents");

        private final String key;

        MiscellaneousKeys(String key) {
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
