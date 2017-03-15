package gg.al.config;

import java.util.List;
import java.util.Properties;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public abstract class ConfigEditor {

    protected final Properties properties;

    public ConfigEditor() {
        this.properties = new Properties();
    }

    public void setEditing(Properties properties) {
        this.properties.clear();
        this.properties.putAll(properties);
    }

    public void setValue(String key, Object value) {
        properties.setProperty(key, value.toString());
    }

    public void setValue(String key, List<?> value) {
        properties.setProperty(key, value.toString());
    }

    public abstract void flush();
}
