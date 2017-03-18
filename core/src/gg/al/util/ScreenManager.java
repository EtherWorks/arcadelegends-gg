package gg.al.util;

import com.badlogic.gdx.Screen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas Neumann on 18.03.2017.
 */
public class ScreenManager {
    private Map<Class<? extends Screen>, Screen> screenMap;

    public ScreenManager() {
        screenMap = new HashMap<>();
    }

    public <T extends Screen> T put(T screen, Class<T> type) {
        screenMap.put(type, screen);
        return screen;
    }

    public <T extends Screen> T get(Class<T> type) {
        return (T) screenMap.get(type);
    }
}
