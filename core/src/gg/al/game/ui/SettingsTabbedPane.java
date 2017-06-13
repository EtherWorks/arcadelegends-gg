package gg.al.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Created by Patrick Windegger on 21.03.2017.
 * Class responsible for providing a TabbedPane for the 3 different settings (Video, Audio, Input) {@link gg.al.game.screen.SettingsScreen}
 */
@Slf4j
public class SettingsTabbedPane extends Container {

    protected Table contentTable;
    protected HashMap<TextButton, Table> componentMap;
    private Table baseTable;
    private Table buttonTable;
    private int x;
    private int y;


    public SettingsTabbedPane(Skin skin, int x, int y, HashMap<TextButton, Table> componentMap) {
        this.x = x;
        this.y = y;
        this.componentMap = componentMap;
        baseTable = new Table();
        buttonTable = new Table();
        contentTable = new Table();

        baseTable.add(buttonTable);
        baseTable.row();
        baseTable.add(contentTable);

        this.setPosition(x / 2, y / 1.3f);
        this.setActor(baseTable);
    }

    /**
     * Method responsible for adding a new Tab to the TabbedPane
     *
     * @param btnTab - TextButton you want to add to the TabbedPane
     */
    public void addTab(TextButton btnTab) {
        btnTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setCurrentTab(btnTab);
            }
        });
        buttonTable.add(btnTab).pad(10);
        centerBaseTable();
    }

    /**
     * Method responsible for centering the whole TabbedPane
     */
    public void centerBaseTable() {
        this.setPosition(x / 2, y / 1.3f);
    }

    /**
     * Method responsible for making the Tab visible depending on which TextButton was clicked
     *
     * @param btnTab - TextButton of the tab you want to show
     */
    public void setCurrentTab(TextButton btnTab) {
        Table currentTable = componentMap.get(btnTab);
        contentTable.clear();
        contentTable.add(currentTable);
    }


}
