package gg.al.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Created by Patrick Windegger on 21.03.2017.
 */
@Slf4j
public class SettingsTabbedPane extends BaseTabbedPane {

    public SettingsTabbedPane(Skin skin, int x, int y, HashMap<TextButton, Table> componentMap) {
        super(skin, x, y, componentMap);
    }

    @Override
    protected void currentTable(TextButton btnTab) {
        Table currentTable = componentMap.get(btnTab);
        log.debug(currentTable.getRows()+"");
        contentTable.clear();
        contentTable.add(currentTable);
    }
}
