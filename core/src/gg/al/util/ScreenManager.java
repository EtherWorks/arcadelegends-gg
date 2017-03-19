package gg.al.util;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Thomas Neumann on 18.03.2017.
 */
public class ScreenManager implements Disposable {
    private Map<Class<? extends Screen>, Screen> screenMap;

    public ScreenManager() {
        screenMap = new HashMap<>();
    }

    public synchronized <T extends Screen> T register(T screen, Class<T> type) {
        if (screenMap.containsKey(type))
            throw new IllegalArgumentException("Screen already registerd: " + ClassReflection.getSimpleName(type));
        return (T) screenMap.put(type, screen);
    }

    public synchronized <T extends Screen> T regiser(Class<T> type) throws ReflectionException {
        if (screenMap.containsKey(type))
            throw new IllegalArgumentException("Screen already registerd: " + ClassReflection.getSimpleName(type));
        T screen = ClassReflection.newInstance(type);
        return (T) screenMap.put(type, screen);
    }

    public synchronized <T extends Screen> T get(Class<T> type) {
        if (!screenMap.containsKey(type))
            throw new IllegalArgumentException("Screen not registered: " + ClassReflection.getSimpleName(type));
        return (T) screenMap.get(type);
    }

    public synchronized <T extends Screen> T registerGet(T screen, Class<T> type) {
        register(screen, type);
        return screen;
    }

    public synchronized void remove(Class<? extends Screen> type) {
        if (!screenMap.containsKey(type))
            throw new IllegalArgumentException("Screen not registered: " + ClassReflection.getSimpleName(type));
        screenMap.remove(type);
    }

    public synchronized void dispose(Class<? extends Screen> type) {
        Screen screen = get(type);
        screen.dispose();
        screenMap.remove(type);
    }

    @Override
    public synchronized void dispose() {
        for (Iterator<Map.Entry<Class<? extends Screen>, Screen>> iter = screenMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Class<? extends Screen>, Screen> entry = iter.next();
            entry.getValue().dispose();
            iter.remove();
        }
    }

    public synchronized boolean isRegistered(Class<? extends Screen> type) {
        return screenMap.containsKey(type);
    }
}
