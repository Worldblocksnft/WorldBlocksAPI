package net.worldblocks.libs.worldblocksapi.item;

import lombok.Builder;
import lombok.Getter;
import net.worldblocks.libs.worldblocksapi.configuration.Serializable;
import net.worldblocks.libs.worldblocksapi.configuration.yaml.YamlFile;
import net.worldblocks.libs.worldblocksapi.utilities.ColorUtils;
import net.worldblocks.libs.worldblocksapi.utilities.Pair;
import net.worldblocks.libs.worldblocksapi.utilities.Placeholder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class Item extends Serializable {

    private String material;
    private Byte data;
    private String name;
    private List<String> lore;
    private List<Pair<Enchantment, Integer>> enchantments;
    private List<ItemFlag> itemFlags;

    public ItemStack toItemStack(Placeholder... placeholders) {
        if (material == null) {
            return null;
        }

        this.material = material.toUpperCase();
        if (Material.getMaterial(material) == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(Material.getMaterial(material), 1, data != null ? data : 0);

        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        if (name != null) {
            itemMeta.setDisplayName(ColorUtils.spigotColor(Placeholder.apply(name, placeholders)));
        }

        if (lore != null) {
            List<String> loreLines = new ArrayList<>();
            for (String loreLine : lore) {
                loreLines.add(ColorUtils.spigotColor(Placeholder.apply(loreLine, placeholders)));
            }
            itemMeta.setLore(loreLines);
        }

        if (itemFlags != null) {
            itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
        }

        if (enchantments != null) {
            for (Pair<Enchantment, Integer> enchantment : enchantments) {
                itemStack.addUnsafeEnchantment(enchantment.getKey(), enchantment.getValue());
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", material);
        map.put("data", data.intValue());
        map.put("name", name);
        map.put("lore", lore);
        List<String> itemFlags = new ArrayList<>();
        for (ItemFlag itemFlag : getItemFlags()) {
            itemFlags.add(itemFlag.name());
        }
        map.put("flags", itemFlags);
        if (enchantments != null) {
            for (Pair<Enchantment, Integer> pair : enchantments) {
                map.put("enchantments." + pair.getKey().getKey().getKey(), pair.getValue());
            }
        }

        return map;
    }

    public static Item deserialize(YamlFile yamlFile, String path) {
        YamlConfiguration c = yamlFile.getConfig();
        ItemBuilder build = Item.builder();
        build.material(c.getString(path + ".material"));
        if (c.contains(path + ".data")) {
            build.data((byte) c.getInt(path + ".data"));
        }
        if (c.contains(path + ".name")) {
            build.name(c.getString(path + ".name"));
        }
        if (c.contains(path + ".lore")) {
            build.lore(c.getStringList(path + ".lore"));
        }

        if (c.contains(path + ".flags")) {
            List<ItemFlag> itemFlags = new ArrayList<>();
            for (String flagString : c.getStringList(path + ".flags")) {
                try {
                    ItemFlag itemFlag = ItemFlag.valueOf(flagString.toUpperCase());
                    itemFlags.add(itemFlag);
                } catch (IllegalArgumentException e) {

                }
            }
            build.itemFlags(itemFlags);
        }

        if (c.contains(path + ".enchants")) {
            List<Pair<Enchantment, Integer>> enchantList = new ArrayList<>();
            for (String enchantKey : c.getConfigurationSection(path + ".enchants").getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(enchantKey));
                if (enchantment == null) {
                    continue;
                }

                enchantList.add(new Pair<>(enchantment, c.getInt(path + ".enchants." + enchantKey)));
            }
            build.enchantments(enchantList);
        }

        return build.build();
    }

}
