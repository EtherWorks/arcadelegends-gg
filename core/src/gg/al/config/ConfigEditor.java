package gg.al.config;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.List;
import java.util.Properties;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public abstract class ConfigEditor {

    protected final ListMultimap<String, IConfigValueChangedListener> listeners;
    protected final Properties properties;

    public ConfigEditor() {
        this.properties = new Properties();
        this.listeners = LinkedListMultimap.create();
    }

    public void addConfigValueChangedListener(String key, IConfigValueChangedListener listener) {
        listeners.put(key, listener);
    }

    public void removeConfigValueChangedListener(String key, IConfigValueChangedListener listener) {
        listeners.remove(key, listener);
    }

    public void setEditing(Properties properties) {
        this.properties.clear();
        this.properties.putAll(properties);
    }

    public void setValue(String key, Object value) {
        fireConfigValueChanged(key, value);
        properties.setProperty(key, value.toString());
    }

    public void setValue(String key, List<?> value) {
        fireConfigValueChanged(key, value);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.size() - 1; i++) {
            builder.append(value.get(i).toString()).append(", ");
        }
        builder.append(value.get(value.size() - 1));
        properties.setProperty(key, builder.toString());
    }

    protected void fireConfigValueChanged(String key, Object value) {

        for (IConfigValueChangedListener listener : listeners.get(key)) {
            listener.valueChanged(key, value);
        }
    }

    public abstract void flush();
}
