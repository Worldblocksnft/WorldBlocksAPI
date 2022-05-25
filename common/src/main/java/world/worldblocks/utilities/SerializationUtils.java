package world.worldblocks.utilities;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class SerializationUtils {

    public static String serializeItem(ItemStack i) {
        String json = "";
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("material", i.getType().toString());
        jsonObj.put("stackSize", i.getAmount());
        jsonObj.put("maxStackSize", i.getMaxStackSize());

        Collection<String> keys = NBTEditor.getKeys(i);

        if (i.hasItemMeta()) {
            Map<String, Integer> enchantments = new HashMap<>();
            List<String> itemFlags = new ArrayList<>();

            ItemMeta meta = i.getItemMeta();

            if (meta.hasDisplayName())
                jsonObj.put("displayName", meta.getDisplayName());

            if (meta.hasEnchants()) {
                for (Enchantment enchantment : meta.getEnchants().keySet()) {
                    int level = meta.getEnchantLevel(enchantment);
                    enchantments.put(enchantment.getName(), level);
                }
                jsonObj.put("enchantments", enchantments);
            }

            if (meta.hasLore()) {
                List<String> lore = meta.getLore();
                jsonObj.put("lore", lore);
            }

            if (meta.getItemFlags().size() != 0) {
                for (ItemFlag itemFlag : meta.getItemFlags()) {
                    itemFlags.add(itemFlag.toString());
                }
                jsonObj.put("itemFlags", itemFlags);
            }

            if (meta.hasCustomModelData()) {
                jsonObj.put("customModelData", meta.getCustomModelData());
            }

            Map<String, Object> nbt = new HashMap<>();

            if (keys != null) {
                for (String s : keys) {
                    if (s.equalsIgnoreCase("Enchantments"))
                        continue;
                    if (s.equalsIgnoreCase("HideFlags"))
                        continue;
                    if (s.equalsIgnoreCase("display"))
                        continue;

                    byte b = NBTEditor.getByte(i, s);
                    boolean bool = NBTEditor.getBoolean(i, s);
                    byte[] arr = NBTEditor.getByteArray(i, s);
                    double dob = NBTEditor.getDouble(i, s);
                    float f = NBTEditor.getFloat(i, s);
                    int in = NBTEditor.getInt(i, s);
                    int[] intarr = NBTEditor.getIntArray(i, s);
                    long l = NBTEditor.getLong(i, s);

                    if (b != 0) {
                        nbt.put(s, b);
                    } else if (arr !=  null) {
                        nbt.put(s, arr);
                    } else if (dob != 0D) {
                        nbt.put(s, dob);
                    } else if (f != 0f) {
                        nbt.put(s, f);
                    } else if (in != 0) {
                        nbt.put(s, in);
                    } else if (intarr != null) {
                        nbt.put(s, intarr);
                    } else if (l != 0L) {
                        nbt.put(s, l);
                    } else {
                        nbt.put(s, bool);
                    }
                }

                jsonObj.put("nbt", nbt);
            }
        }

        json = jsonObj.toJSONString();

        return json;
    }

    public static ItemStack deserializeItem(String json) {
        ItemStack itemStack = null;

        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

            if (jsonObject.containsKey("material")) {
                Material material = Material.valueOf((String) jsonObject.get("material"));
                itemStack = new ItemStack(material);

                if (jsonObject.containsKey("stackSize")) {
                    Long stackSize = (Long) jsonObject.get("stackSize");
                    itemStack.setAmount(stackSize.intValue());
                }

                ItemMeta meta = itemStack.getItemMeta();

                if (jsonObject.containsKey("customModelData")) {
                    Long modelData = (Long) jsonObject.get("customModelData");
                    meta.setCustomModelData(modelData.intValue());
                }

                if (jsonObject.containsKey("displayName")) {
                    String displayName = (String) jsonObject.get("displayName");
                    meta.setDisplayName(displayName);
                }

                if (jsonObject.containsKey("enchantments")) {
                    Map<String, Long> enchantments = (Map<String, Long>) jsonObject.get("enchantments");
                    for (String s : enchantments.keySet()) {
                        meta.addEnchant(Enchantment.getByName(s), enchantments.get(s).intValue(), true);
                    }
                }

                if (jsonObject.containsKey("lore")) {
                    List<String> lore = (List<String>) jsonObject.get("lore");
                    meta.setLore(lore);
                }

                if (jsonObject.containsKey("itemFlags")) {
                    List<String> itemFlags = (List<String>) jsonObject.get("itemFlags");
                    for (String s : itemFlags) {
                        meta.addItemFlags(ItemFlag.valueOf(s));
                    }
                }

                if (jsonObject.containsKey("nbt")) {
                    Map<String, Object> nbt = (Map<String, Object>) jsonObject.get("nbt");
                    for (String s : nbt.keySet()) {
                        itemStack = NBTEditor.set(itemStack, nbt.get(s), s);
                    }
                }

                itemStack.setItemMeta(meta);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return itemStack;
    }

}
