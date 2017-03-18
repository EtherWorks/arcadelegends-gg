package gg.al.config;

/**
 * Created by Dr. Gavrel on 18.03.2017.
 */
public interface IConfigKey {
    String getKeyName();

    String getPrefix();

    default String getKey() {
        return getPrefix() + "." + getKeyName();
    }
}
