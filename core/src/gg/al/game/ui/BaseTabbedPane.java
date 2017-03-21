package gg.al.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Created by Patrick Windegger on 21.03.2017.
 * Class responsible for providing a TabbedPane for our requirements
 */
@Slf4j
public abstract class BaseTabbedPane extends Container {

    private Table baseTable;
    private Table buttonTable;
    protected Table contentTable;

    protected HashMap<TextButton, Table> componentMap;

    private int x;
    private int y;


    public BaseTabbedPane(Skin skin, int x, int y, HashMap<TextButton, Table> componentMap) {
        this.x = x;
        this.y = y;
        this.componentMap = componentMap;
        baseTable = new Table();
        buttonTable = new Table();
        contentTable = new Table();

        baseTable.add(buttonTable);
        baseTable.row();
        baseTable.add(contentTable);

        this.setPosition(x / 2, x / 1.3f);
        this.setActor(baseTable);
    }

    public void addTab(TextButton btnTab) {
        btnTab.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentTable(btnTab);
            }
        });
        buttonTable.add(btnTab).pad(10);
        centerBaseTable();
    }

    public void centerBaseTable() {
        this.setPosition(x / 2, y / 1.3f);
    }

    protected abstract void currentTable(TextButton btnTab);




}
