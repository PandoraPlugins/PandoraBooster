package me.nanigans.pandorabooster.Utility;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Date;
import java.util.Map;

public class ItemUtils {

    public static String replacePlaceHolders(String str, Map<String, Object> booster){

        str = str.replaceAll("\\{time}", DateParser.formatDateDiff(new Date().getTime()+
                Long.parseLong(booster.get("time").toString())))
                .replaceAll("\\{amplifier}", String.valueOf(Double.parseDouble(booster.get("amplifier").toString())))
                .replaceAll("\\{boostFaction}", String.valueOf(Boolean.valueOf(booster.get("boostFaction").toString())));

        return ChatColor.translateAlternateColorCodes('&', str);
    }

    /**
     * Creates a new item from a string material id
     * @param material the material of the item "ID/ID"
     * @param name the name of the item
     * @param nbt any nbt values
     * @return a new itemstack
     */
    public static ItemStack createItem(String material, String name, String... nbt){

        ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(material.split("/")[0])),
                1, Byte.parseByte(material.split("/")[1]));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        item = NBTData.setNBT(item, nbt);
        return item;

    }
    /**
     * Creates a new item from a material
     * @param material the material of the item
     * @param name the name of the item
     * @param nbt any nbt values
     * @return a new itemstack
     */
    public static ItemStack createItem(Material material, String name, String... nbt){

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        item = NBTData.setNBT(item, nbt);
        return item;

    }

}
