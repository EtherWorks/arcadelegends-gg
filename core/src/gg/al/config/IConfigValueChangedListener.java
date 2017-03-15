package gg.al.config;


/**
 * Created by Thomas Neumann on 15.03.2017.
 */
@FunctionalInterface
public interface IConfigValueChangedListener {
    void valueChanged(String key, Object value);
}
