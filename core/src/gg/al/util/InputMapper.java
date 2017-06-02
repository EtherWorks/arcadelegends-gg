package gg.al.util;

import com.badlogic.gdx.utils.ObjectMap;
import gg.al.config.IInputConfig;

/**
 * Created by Thomas Neumann on 02.06.2017.
 */
public class InputMapper {
    private final ObjectMap<IInputConfig.InputKeys, InputEvent> inputEvents;
    private final IInputConfig inputConfig;

    public InputMapper(IInputConfig inputConfig) {
        inputEvents = new ObjectMap<>();
        this.inputConfig = inputConfig;
    }

    public void handleInput(int key) {
        for (ObjectMap.Entry<IInputConfig.InputKeys, InputEvent> ent : inputEvents.entries())
            if (IInputConfig.InputKeys.getFromKey(ent.key, inputConfig) == key) {
                ent.value.onInput();
                break;
            }
    }

    public void registerInputHanlder(IInputConfig.InputKeys key, InputEvent event) {
        inputEvents.put(key, event);
    }

    public void clear() {
        inputEvents.clear();
    }

    @FunctionalInterface
    public interface InputEvent {
        void onInput();
    }
}
