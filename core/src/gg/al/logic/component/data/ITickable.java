package gg.al.logic.component.data;

import gg.al.logic.component.StatComponent;

/**
 * Created by Thomas Neumann on 31.05.2017.<br />
 * Describes a object which can be ticked, meaning stepped with the stimulation.
 */
public interface ITickable {
    /**
     * @return the {@link TickHandler} for this object.
     */
    TickHandler getTickHandler();

    @FunctionalInterface
    interface TickHandler {
        /**
         * Handles a tick event. Receives a {@link StatComponent} for continence purpose.
         *
         * @param delta         the time since last frame
         * @param statComponent {@link StatComponent} of the entity containing this object
         */
        void onTick(float delta, StatComponent statComponent);
    }
}
