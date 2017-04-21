package gg.al.logic.entity;

import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

/**
 * Created by Thomas Neumann on 07.04.2017.<br />
 * Arguments indexed with {@link String} objects for use with Entity.
 */
public class EntityArguments {
    private final ObjectMap<String, Object> arguments;

    public EntityArguments() {
        arguments = new ObjectMap<>();
    }

    /**
     * @param key property name
     * @return true if and only if the property exists
     */
    public boolean containsKey(String key) {
        return arguments.containsKey(key);
    }

    /**
     * @param key property name
     * @return the value for that property if it exists, otherwise, null
     */
    public Object get(String key) {
        return arguments.get(key);
    }

    /**
     * Returns the object for the given key, casting it to clazz.
     *
     * @param key  the key of the object
     * @param type the class of the object
     * @return the object or null if the object is not in the map
     * @throws ClassCastException if the object with the given key is not of type clazz
     */
    public <T> T get(String key, Class<T> type) {
        return (T) get(key);
    }

    /**
     * Returns the object for the given key, casting it to clazz.
     *
     * @param key          the key of the object
     * @param defaultValue the default value
     * @param type         the class of the object
     * @return the object or the defaultValue if the object is not in the map
     * @throws ClassCastException if the object with the given key is not of type clazz
     */
    public <T> T get(String key, T defaultValue, Class<T> type) {
        Object object = get(key);
        return object == null ? defaultValue : (T) object;
    }

    /**
     * @param key   property name
     * @param value value to be inserted or modified (if it already existed)
     */
    public void put(String key, Object value) {
        arguments.put(key, value);
    }

    /**
     * @param properties set of properties to be added
     */
    public void putAll(EntityArguments properties) {
        this.arguments.putAll(properties.arguments);
    }

    /**
     * @param key property name to be removed
     */
    public void remove(String key) {
        arguments.remove(key);
    }

    /**
     * Removes all properties
     */
    public void clear() {
        arguments.clear();
    }

    /**
     * @return iterator for the property names
     */
    public Iterator<String> getKeys() {
        return arguments.keys();
    }

    /**
     * @return iterator to properties' values
     */
    public Iterator<Object> getValues() {
        return arguments.values();
    }
}
