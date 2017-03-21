package gg.al.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Patrick Windegger on 21.03.2017.
 */
@Slf4j
public class ALTabbedPane extends Container {

    private Table baseTable;
    private Table buttonTable;

    private int x;
    private int y;


    public ALTabbedPane(Skin skin, int x, int y) {
        this.x = x;
        this.y = y;
        baseTable = new Table();
        buttonTable = new Table();


        baseTable.add(buttonTable);
        baseTable.row();

        this.setPosition(x / 2, x / 2);
        this.setActor(baseTable);
    }

    public void addTab(TextButton btnTab) {
        buttonTable.add(btnTab).pad(10);
        centerBaseTable();
    }

    public void centerBaseTable() {
        this.setPosition(x / 2, y / 2);
    }


}
