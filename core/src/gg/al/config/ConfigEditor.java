package gg.al.config;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.*;

/**
 * Created by Thomas Neumann on 15.03.2017.<br />
 * Abstract class describing a ConfigEditor, which houses methods for
 * changing the current configuration, and flushing this configuration,
 * as well as listeners for the flush event.
 */
public abstract class ConfigEditor {

    protected final ListMultimap<String, IConfigValueChangedListener> listeners;
    protected final List<IConfigValueChangedListener> allValueListeners;
    protected final ListMultimap<String, IConfigValueChangedListener> listenersAfter;
    protected final List<IConfigValueChangedListener> allValueListenersAfter;
    protected final Properties properties;
    protected final Properties before;

    public ConfigEditor() {
        this.properties = new Properties();
        this.before = new Properties();
        this.listeners = LinkedListMultimap.create();
        this.allValueListeners = new ArrayList<>();
        this.listenersAfter = LinkedListMultimap.create();
        this.allValueListenersAfter = new ArrayList<>();
    }


    public void addConfigValueChangedListener(IConfigValueChangedListener listener, boolean afterFlush) {
        if (afterFlush)
            allValueListenersAfter.add(listener);
        else
            allValueListeners.add(listener);
    }

    public void addConfigValueChangedListener(IConfigKey key, IConfigValueChangedListener listener, boolean afterFlush) {
        if (afterFlush)
            listenersAfter.put(key.getKey(), listener);
        else
            listeners.put(key.getKey(), listener);
    }

    public void removeConfigValueChangedListener(IConfigValueChangedListener listener, boolean afterFlush) {
        if (afterFlush)
            allValueListenersAfter.remove(listener);
        else
            allValueListeners.remove(listener);
    }

    public void removeConfigValueChangedListener(IConfigKey key, IConfigValueChangedListener listener, boolean afterFlush) {
        if (afterFlush)
            listenersAfter.remove(key.getKey(), listener);
        else
            listeners.remove(key.getKey(), listener);
    }

    public void addConfigValueChangedListener(IConfigValueChangedListener listener) {
        addConfigValueChangedListener(listener, false);
    }

    public void addConfigValueChangedListener(IConfigKey key, IConfigValueChangedListener listener) {
        addConfigValueChangedListener(key, listener, false);
    }

    public void removeConfigValueChangedListener(IConfigValueChangedListener listener) {
        removeConfigValueChangedListener(listener, false);
    }

    public void removeConfigValueChangedListener(IConfigKey key, IConfigValueChangedListener listener) {
        removeConfigValueChangedListener(key, listener, false);
    }

    /**
     * Sets the current active set of properties to the given properties
     *
     * @param properties the {@link Properties} to set the active set to.
     */
    public void setEditing(Properties properties) {
        this.properties.clear();
        this.properties.putAll(properties);
        this.before.clear();
        this.before.putAll(properties);
    }

    public void setValue(IConfigKey key, Object value) {
        properties.setProperty(key.getKey(), value.toString());
    }

    public void setValue(IConfigKey key, List<?> value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.size() - 1; i++) {
            builder.append(value.get(i).toString()).append(", ");
        }
        builder.append(value.get(value.size() - 1));
        properties.setProperty(key.getKey(), builder.toString());
    }

    /**
     * Notifies all listeners that a config value has been changed.
     * This is called before updating all config instances.
     * @param key the key of the changed value
     * @param value the changed value
     */
    protected void fireConfigValueChanged(String key, String value) {
        for (IConfigValueChangedListener listener : allValueListeners) {
            listener.valueChanged(key, value);
        }

        for (IConfigValueChangedListener listener : listeners.get(key)) {
            listener.valueChanged(key, value);
        }
    }


    /**
     * Notifies all listeners that a config value has been changed.
     * This is called after updating all config instances.
     * @param key the key of the changed value
     * @param value the changed value
     */
    protected void fireConfigValueChangedAfter(String key, String value) {
        for (IConfigValueChangedListener listener : allValueListenersAfter) {
            listener.valueChanged(key, value);
        }

        for (IConfigValueChangedListener listener : listenersAfter.get(key)) {
            listener.valueChanged(key, value);
        }
    }

    /**
     * Flushes the current active set of {@link Properties} to the config instances.
     */
    public void flush() {
        List<Map.Entry<Object, Object>> toFire = new LinkedList<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (!entry.getValue().equals(before.getProperty(entry.getKey().toString()))) {
                fireConfigValueChanged(entry.getKey().toString(), entry.getValue().toString());
                toFire.add(entry);
            }
        }
        write();
        setEditing((Properties) properties.clone());
        for (Map.Entry<Object, Object> entry : toFire) {
            fireConfigValueChangedAfter(entry.getKey().toString(), entry.getValue().toString());
        }
    }

    /**
     * Abstract method for defining the saving of the properties.
     */
    protected abstract void write();
}
