package gg.al.config;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public abstract class ConfigEditor {

    protected final ListMultimap<String, IConfigValueChangedListener> listeners;
    protected final List<IConfigValueChangedListener> allValueListeners;
    protected final Properties properties;
    protected final Properties before;

    public ConfigEditor() {
        this.properties = new Properties();
        this.before = new Properties();
        this.listeners = LinkedListMultimap.create();
        allValueListeners = new ArrayList<>();
    }


    public void addConfigValueChangedListener(IConfigValueChangedListener listener) {
        allValueListeners.add(listener);
    }

    public void addConfigValueChangedListener(String key, IConfigValueChangedListener listener) {
        listeners.put(key, listener);
    }

    public void removeConfigValueChangedListener(IConfigValueChangedListener listener) {
        allValueListeners.remove(listener);
    }

    public void removeConfigValueChangedListener(String key, IConfigValueChangedListener listener) {
        listeners.remove(key, listener);
    }

    public void setEditing(Properties properties) {
        this.properties.clear();
        this.properties.putAll(properties);
        this.before.clear();
        this.before.putAll(properties);
    }

    public void setValue(String key, Object value) {
        properties.setProperty(key, value.toString());
    }

    public void setValue(String key, List<?> value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.size() - 1; i++) {
            builder.append(value.get(i).toString()).append(", ");
        }
        builder.append(value.get(value.size() - 1));
        properties.setProperty(key, builder.toString());
    }

    protected void fireConfigValueChanged(String key, String value) {
        for (IConfigValueChangedListener listener : allValueListeners) {
            listener.valueChanged(key, value);
        }

        for (IConfigValueChangedListener listener : listeners.get(key)) {
            listener.valueChanged(key, value);
        }
    }

    public void flush() {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (!entry.getValue().equals(before.getProperty(entry.getKey().toString())))
                fireConfigValueChanged(entry.getKey().toString(), entry.getValue().toString());
        }
        write();
        setEditing((Properties) properties.clone());
    }

    protected abstract void write();
}
