package gg.al.config;


/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Listener interface for the configuration value changed event of
 * the {@link ConfigEditor} class.
 */
@FunctionalInterface
public interface IConfigValueChangedListener {
    void valueChanged(String key, String value);
}
