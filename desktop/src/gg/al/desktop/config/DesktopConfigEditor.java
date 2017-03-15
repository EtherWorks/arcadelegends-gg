package gg.al.desktop.config;

import gg.al.config.ConfigEditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public class DesktopConfigEditor extends ConfigEditor {

    @Override
    protected void write() {
        try {
            properties.store(new FileOutputStream(new File(DesktopConfigUtil.getCurrentConfigPath())), "Only edit if you know what you are doing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
