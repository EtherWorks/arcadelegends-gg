package gg.al.config;

/**
 * Created by Thomas Neumann on 18.03.2017.<br />
 * Interface denoting a unique configuration key, separated
 * into prefix and name.
 */
public interface IConfigKey {
    String getKeyName();

    String getPrefix();

    String getDefaultValue();

    default String getKey() {
        return getPrefix() + "." + getKeyName();
    }
}
