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
    public void flush() {
        try {
            properties.store(new FileOutputStream(new File(DesktopConfigUtil.getCurrentConfigPath())), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
