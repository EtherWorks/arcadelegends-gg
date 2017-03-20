package gg.al.game;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

/**
 * Created by Patrick Windegger on 20.03.2017.
 */
public class ALTab extends Tab {

    private Table content = new Table();

    public ALTab()
    {
        super(false, false);
        content.add(new VisLabel("TestLabel"));
    }

    @Override
    public String getTabTitle() {
        return "TestTab";
    }

    @Override
    public Table getContentTable() {
        return content;
    }
}
