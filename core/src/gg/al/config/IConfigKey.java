package gg.al.config;

/**
 * Created by Thomas Neumann on 18.03.2017.<br>
 * Interface denoting a unique configuration key, separated
 * into prefix and name.
 */
public interface IConfigKey {
    /**
     * @return the name of this key
     */
    String getKeyName();

    /**
     * @return the prefix of this key
     */
    String getPrefix();

    /**
     * @return the default value for this key
     */
    String getDefaultValue();

    /**
     * @return the complete key
     */
    default String getKey() {
        return getPrefix() + "." + getKeyName();
    }
}
