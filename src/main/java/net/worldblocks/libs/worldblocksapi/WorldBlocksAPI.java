package net.worldblocks.libs.worldblocksapi;

import net.worldblocks.libs.worldblocksapi.databases.DatabaseModule;
import net.worldblocks.libs.worldblocksapi.utilities.NBTEditor;
import net.worldblocks.libs.worldblocksapi.utilities.SerializationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class WorldBlocksAPI extends JavaPlugin {

    private Map<Modules, Module> modules = new HashMap<>();

    @Override
    public void onEnable() {
        instantiateDefaultModules();
    }

    public Module getModuleGeneric(Modules id) {
        return modules.get(id);
    }

    public void instantiateModule(Modules id, Module module) {
        module.instantiate(this);
        modules.put(id, module);
    }

    public void instantiateDefaultModules() {
        instantiateModule(Modules.DATABASES, new DatabaseModule());
    }

    public static WorldBlocksAPI getAPI() {
        return (WorldBlocksAPI) Bukkit.getServer().getPluginManager().getPlugin("WorldblocksAPI");
    }

    public DatabaseModule getDatabaseModule() {
        return (DatabaseModule) modules.get("database");
    }

}
