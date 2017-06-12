package gg.al.logic.entity;

import com.badlogic.gdx.utils.ObjectMap;
import com.google.gson.reflect.TypeToken;
import gg.al.logic.component.data.Template;
import gg.al.util.GsonUtil;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Thomas Neumann on 07.04.2017.<br />
 * Arguments indexed with {@link String} objects for use with Entities.
 */
public class EntityArguments {

    private final ObjectMap<String, Object> arguments;

    public EntityArguments() {
        arguments = new ObjectMap<>();
    }

    public static EntityArguments fromFile(String name) throws IOException {

        EntityArguments arguments = new EntityArguments();
        InputStream is = EntityArguments.class.getResourceAsStream("/" + name);
        InputStreamReader reader = new InputStreamReader(is);
        Type type = new TypeToken<Map<String, Template>>() {
        }.getType();
        Map<String, Template> defs = GsonUtil.getGSON().fromJson(reader, type);
        reader.close();

        for (Map.Entry<String, Template> entries : defs.entrySet()) {
            arguments.put(entries.getKey(), entries.getValue());
        }
        return arguments;
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

    public Properties toProperties() {
        Properties properties = new Properties();
        for (ObjectMap.Entries<String, Object> iter = arguments.entries(); iter.hasNext(); ) {
            ObjectMap.Entry<String, Object> entr = iter.next();
            properties.put(entr.key, entr.value);
        }
        return properties;
    }

    public void fromProperties(Properties properties) {
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            arguments.put(entry.getKey().toString(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        return "EntityArguments{" +
                "arguments=" + arguments +
                '}';
    }
}
