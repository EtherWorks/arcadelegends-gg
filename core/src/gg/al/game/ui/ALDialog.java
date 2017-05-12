package gg.al.game.ui;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import java.awt.*;


/**
 * Created by Patrick Windegger on 12.05.2017.
 */
public class ALDialog extends Dialog {

    private Table table;
    private Skin skin;
    private Stage stage;
    private Dialog dialog;
    private BitmapFont font;


    public ALDialog(String title, WindowStyle windowStyle, Skin skin, Stage stage, BitmapFont font) {
        super(title, windowStyle);
        table = new Table();
        this.skin = skin;
        this.stage = stage;
        this.font = font;
        this.setSize(300,300);
    }

    public void initDefaultDialog(Drawable background)
    {
        this.setBackground(background);
        Label lbKey = new Label("Key:", skin);
        table.add(new com.badlogic.gdx.scenes.scene2d.ui.Label("Input Selection", new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.BLACK))).fill().center();
        table.row();
        table.add(lbKey);

        TextField tfKey = new TextField("", skin);

        table.add(tfKey);

        this.add(table);
        this.show(stage);
    }



}
