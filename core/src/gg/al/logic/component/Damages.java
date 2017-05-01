package gg.al.logic.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;
import gg.al.logic.data.Damage;

/**
 * Created by Thomas Neumann on 25.04.2017.<br />
 */
public class Damages extends PooledComponent {

    public final Array<Damage> damages;

    public Damages() {
        damages = new Array<>();
    }

    @Override
    protected void reset() {
        damages.clear();
    }
}
