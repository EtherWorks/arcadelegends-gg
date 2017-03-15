package gg.al.desktop.config;

import gg.al.config.Config;
import gg.al.config.ConfigEditor;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Thomas Neumann on 15.03.2017.
 */
public class DesktopConfigEditor extends ConfigEditor {

    public static final String CONFIGFILELOCATION = "config/config.properties";

    public static String getCurrentConfigPATH() {
        return System.getProperty("user.dir") + File.separator + CONFIGFILELOCATION;
    }

    public static ConfigurationProvider buildConfigProvider() {
        ConfigurationSource s = new FilesConfigurationSource(() -> Arrays.asList(Paths.get(getCurrentConfigPATH())));
        ConfigurationProvider p = new ConfigurationProviderBuilder().withConfigurationSource(s).build();
        return p;
    }

    public static Config buildConfig() {
        return new Config(buildConfigProvider(), new DesktopConfigEditor());
    }

    @Override
    public void flush() {
        try {
            properties.store(new FileOutputStream(new File(getCurrentConfigPATH())), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
