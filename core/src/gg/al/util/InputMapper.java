package gg.al.util;

import com.badlogic.gdx.utils.ObjectMap;
import gg.al.config.IInputConfig;

/**
 * Created by Thomas Neumann on 02.06.2017. <br>
 * Utility class for mapping a button press to an action.
 */
public class InputMapper {
    private final ObjectMap<IInputConfig.InputKeys, InputEvent> inputEvents;
    private final IInputConfig inputConfig;

    public InputMapper(IInputConfig inputConfig) {
        inputEvents = new ObjectMap<>();
        this.inputConfig = inputConfig;
    }

    /**
     * Calls the responsible {@link InputEvent} for the key, or none if no {@link InputEvent} for this key exists.
     *
     * @param key the key input to be handled
     */
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

    /**
     * Event for a button press.
     */
    @FunctionalInterface
    public interface InputEvent {
        /**
         * Called whenever a corresponding button press was found.
         */
        void onInput();
    }
}
