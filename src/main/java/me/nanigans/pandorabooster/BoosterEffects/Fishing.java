package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.BoostTypes;
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
        if(effectBoosters.get(player.getUniqueId()).containsKey(BoostTypes.FISHING)){
            effectBoosters.get(player.getUniqueId()).get(BoostTypes.FISHING).getTimer().cancel();
        }
        effectBoosters.get(player.getUniqueId()).put(BoostTypes.FISHING, this);
        if(player.isOnline())
        player.getPlayer().sendMessage(ChatColor.GREEN+"Fishing booster added");
    }

    @Override
    public void stop() {
        effectBoosters.get(player.getUniqueId()).remove(BoostTypes.FISHING);
        if(effectBoosters.get(player.getUniqueId()).isEmpty())
            effectBoosters.remove(player.getUniqueId());
    }

    public static Map<UUID, Fishing> getFishBoosters() {
        return fishBoosters;
    }
}
