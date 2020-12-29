package me.nanigans.pandorabooster;

import me.nanigans.pandorabooster.BoosterEffects.XP;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FunctionalInterface
interface BoostMethods{
    void execute(Player player, Map<String, Object> data, String name);
}
public class Booster {
    protected Player player;
    protected Map<String, Object> boosterData;
    protected String name;

    public Booster(Player player, String name, Map<String, Object> booster){
        this.player = player;
        this.boosterData = booster;
        this.name = name;
    }

    public void useBooster(){

        addEffectToPlayer(player, this.name, boosterData);

    }

    public static void addEffectToPlayer(Player player, String name, Map<String, Object> data){
        System.out.println("data = " + data);
        final EType eType = EType.valueOf(data.get("type").toString());
        eType.execute(player, data, name);
    }



    private static final Map<String, BoostMethods> methods = new HashMap<String, BoostMethods>(){{
        put(String.valueOf(Booster.EType.XP), EType::addXP);
        put(String.valueOf(Booster.EType.MONEY), EType::addMoney);
        put(String.valueOf(Booster.EType.MOBCOIN), EType::addMobCoin);
        put(String.valueOf(Booster.EType.FISHING), EType::addFishing);
        put(String.valueOf(Booster.EType.MINE),  EType::addMine);
    }};

    private enum EType{
        XP,
        MONEY,
        MOBCOIN,
        FISHING,
        MINE;

        public void execute(Player player, Map<String, Object> data, String name){
            methods.get(this.toString()).execute(player, data, name);
        }


        private static void addXP(Player player, Map<String, Object> data, String name){
            final XP xp = new XP(player, data, name);

        }
        private static void addMoney(Player player, Map<String, Object> data, String name){

        }
        private static void addMobCoin(Player player, Map<String, Object> data, String name){

        }
        private static void addFishing(Player player, Map<String, Object> data, String name){

        }
        private static void addMine(Player player, Map<String, Object> data, String name){

        }

    }

    /**
     * Creates a new booster itemstack
     * <li>XP</li>
     * <li>MONEY</li>
     * <li>MOBCOIN</li>
     * <li>FISHING</li>
     * <li>MINE</li>
     * @param amount the amount to make the itemstack
     * @return the new booster
     */
    public ItemStack createBoosterItem(int amount){

        ItemStack boosterItem;
        final String material = boosterData.get("material").toString();
        final Map<String, Object> itemMeta = (Map<String, Object>) boosterData.get("itemMeta");
        final String name = ChatColor.translateAlternateColorCodes('&', itemMeta.get("displayName").toString());

        if (EnumUtils.isValidEnum(Material.class, material)) {
            boosterItem = ItemUtils.createItem(Material.valueOf(material), name, Items.ISBOOSTER+"~"+this.name);
        }else{
            boosterItem = ItemUtils.createItem(material, name, Items.ISBOOSTER+"~"+this.name);
        }

        boosterItem.setAmount(amount);
        final ItemMeta meta = boosterItem.getItemMeta();
        List<String> lore = (List<String>) ((JSONArray) JsonUtil.getData(this.name + ".itemMeta.lore"))
                .stream().map(i -> ItemUtils.replacePlaceHolders(i.toString(), boosterData)).collect(Collectors.toList());
        meta.setLore(lore);

        if(Boolean.parseBoolean(itemMeta.get("glow").toString())){
            meta.addEnchant(new Glow(71), 1, true);
        }
        boosterItem.setItemMeta(meta);

        return boosterItem;

    }

    public Player getPlayer() {
        return player;
    }

    public Map<String, Object> getBoosterData() {
        return boosterData;
    }
}
