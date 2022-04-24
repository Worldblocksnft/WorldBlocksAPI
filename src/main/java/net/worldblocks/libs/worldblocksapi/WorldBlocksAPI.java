package net.worldblocks.libs.worldblocksapi;

import net.worldblocks.libs.worldblocksapi.databases.DatabaseModule;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class WorldBlocksAPI extends JavaPlugin {

    private Map<String, Module> modules = new HashMap<>();

    @Override
    public void onEnable() {
        instantiateDefaultModules();
    }

    public Module getModuleGeneric(String id) {
        return modules.get(id);
    }

    public void instantiateModule(String name, Module module) {
        module.instantiate(this);
        modules.put(name, module);
    }

    public void instantiateDefaultModules() {
        instantiateModule("database", new DatabaseModule());
    }

    public static WorldBlocksAPI getAPI() {
        return (WorldBlocksAPI) Bukkit.getServer().getPluginManager().getPlugin("WorldblocksAPI");
    }

    public DatabaseModule getDatabaseModule() {
        return (DatabaseModule) modules.get("database");
    }

}
