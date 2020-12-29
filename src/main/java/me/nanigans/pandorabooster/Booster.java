package me.nanigans.pandorabooster;

import me.nanigans.pandorabooster.BoosterEffects.BoostEnder;
import me.nanigans.pandorabooster.DataEnums.Items;
import me.nanigans.pandorabooster.Utility.Glow;
import me.nanigans.pandorabooster.Utility.ItemUtils;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Booster {
    protected Player player;
    protected Map<String, Object> boosterData;
    protected String name;
    protected long timeOut;
    protected double amp;
    protected short chance;
    protected BoostEnder timer;

    public Booster(Player player, String name, Map<String, Object> booster, BoostEnder timer){
        this.player = player;
        this.boosterData = booster;
        this.name = name;
        this.timeOut = Long.parseLong(booster.get("time").toString());
        this.amp = Double.parseDouble(booster.get("amplifier").toString());
        this.chance = Short.parseShort(booster.get("chance").toString());
        this.timer = timer;
    }

     public abstract void useBooster();

     public abstract void stop();

    /**
     * Creates a new booster itemstack
     *
     * @param amount the amount to make the itemstack
     * @return the new booster
     */
    public static ItemStack createBoosterItem(int amount, String nameType, Map<String, Object> boosterData){

        ItemStack boosterItem;
        final String material = boosterData.get("material").toString();
        final Map<String, Object> itemMeta = (Map<String, Object>) boosterData.get("itemMeta");
        final String name = ChatColor.translateAlternateColorCodes('&', itemMeta.get("displayName").toString());

        if (EnumUtils.isValidEnum(Material.class, material)) {
            boosterItem = ItemUtils.createItem(Material.valueOf(material), name, Items.ISBOOSTER+"~"+nameType);
        }else{
            boosterItem = ItemUtils.createItem(material, name, Items.ISBOOSTER+"~"+nameType);
        }

        boosterItem.setAmount(amount);
        final ItemMeta meta = boosterItem.getItemMeta();
        List<String> lore = (List<String>) ((JSONArray) JsonUtil.getData(nameType + ".itemMeta.lore")).stream()
                .map(i -> ItemUtils.replacePlaceHolders(i.toString(), boosterData)).collect(Collectors.toList());
        meta.setLore(lore);

        if(Boolean.parseBoolean(itemMeta.get("glow").toString())){
            meta.addEnchant(new Glow(71), 1, true);
        }
        boosterItem.setItemMeta(meta);

        return boosterItem;

    }

    public double getAmp() {
        return amp;
    }

    public short getChance() {
        return chance;
    }

    public BoostEnder getTimer() {
        return timer;
    }

    public void setTimer(BoostEnder timer) {
        this.timer = timer;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Object> getBoosterData() {
        return boosterData;
    }
}
