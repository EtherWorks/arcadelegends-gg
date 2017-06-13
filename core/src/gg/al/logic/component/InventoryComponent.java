package gg.al.logic.component;

import com.artemis.PooledComponent;
import gg.al.logic.component.data.Item;


/**
 * Created by Thomas Neumann on 31.05.2017.<br>
 * {@link com.artemis.Component} containing {@link Item} slots.
 */
public class InventoryComponent extends PooledComponent {

    /**
     * {@link Item} held by this entity
     */
    public final Item[] items;

    public InventoryComponent() {
        items = new Item[6];
    }

    @Override
    protected void reset() {
        for (int i = 0; i < items.length; i++)
            items[i] = null;
    }
}
