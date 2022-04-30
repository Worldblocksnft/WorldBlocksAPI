package net.worldblocks.libs.worldblocksapi.configuration.yaml;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class YamlFile {

    private final String name;
    private final String path;
    private final String folder;
    private final Plugin plugin;

    private final File cFile;

    private final YamlConfiguration config;


    public void saveConfig() {
        try {
            this.config.save(this.cFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlFile(String name, String path, String folder, Plugin plugin) {
        this.plugin = plugin;
        this.name = name;
        this.path = path;
        this.folder = folder;
        if (this.folder == null) {
            this.cFile = new File(this.path, this.name);
        } else {
            new File(this.path + File.separator + folder).mkdir();
            this.cFile = new File(this.path + File.separator + folder, this.name);
        }
        if (!this.cFile.exists()) {
            String filePath;
            filePath = path;
            if (folder != null) {
                filePath = filePath + File.separator + folder;
            }
            try {
                this.saveResource();
            } catch (IllegalArgumentException e) {
                try {
                    this.cFile.createNewFile();
                } catch (IOException ex) {
                }
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.cFile);
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }

    private void saveResource() {
        if (this.folder != null) {
            plugin.saveResource(this.folder + File.separator + this.name, false);
        } else {
            plugin.saveResource(this.name, false);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getFolder() {
        return this.folder;
    }

    public File getCFile() {
        return this.cFile;
    }

    public YamlConfiguration getConfig() {
        return this.config;
    }
}
