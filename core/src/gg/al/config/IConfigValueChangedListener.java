package gg.al.config;


/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Listener interface for the configuration value changed event of
 * the {@link ConfigEditor} class.
 */
@FunctionalInterface
public interface IConfigValueChangedListener {
    /**
     * Listener method for a {@link IConfigValueChangedListener#valueChanged(String, String)} event.
     *
     * @param key   the key of the changed value
     * @param value the changed value
     */
    void valueChanged(String key, String value);
}
