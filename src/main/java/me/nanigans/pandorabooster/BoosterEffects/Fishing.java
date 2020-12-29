package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fishing extends Booster {

    private static final Map<UUID, Fishing> fishBoosters = new HashMap<>();

    public Fishing(OfflinePlayer player, String name, Map<String, Object> booster, BoostEnder timer) {
        super(player, name, booster, timer);
    }

    @Override
    public void useBooster() {
        fishBoosters.put(player.getUniqueId(), this);
        if(player.isOnline())
        player.getPlayer().sendMessage(ChatColor.GREEN+"Fishing booster added");
    }

    @Override
    public void stop() {
        fishBoosters.remove(player.getUniqueId(), this);
    }

    public static Map<UUID, Fishing> getFishBoosters() {
        return fishBoosters;
    }
}
