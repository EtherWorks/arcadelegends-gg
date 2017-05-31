package gg.al.logic.component;

import com.artemis.PooledComponent;
import gg.al.logic.component.data.Item;


/**
 * Created by Thomas Neumann on 31.05.2017.<br />
 */
public class InventoryComponent extends PooledComponent {

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
