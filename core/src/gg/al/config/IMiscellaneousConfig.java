package gg.al.config;

/**
 * Created by Dr. Gavrel on 15.03.2017.
 */
public interface IMiscellaneousConfig {

    String PREFIX = "miscellaneous";

    boolean logConfigEvents();

    interface MiscellaneousKeyNames {
        String LOGCFGEEVENT = PREFIX + ".logConfigEvents";
    }
}
