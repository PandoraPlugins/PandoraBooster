package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobCoin extends Booster {

    private static final Map<UUID, MobCoin> mobCoinBoosters = new HashMap<>();

    public MobCoin(Player player, String name, Map<String, Object> booster, BoostEnder timer) {
        super(player, name, booster, timer);
    }

    @Override
    public void useBooster() {
        if(mobCoinBoosters.containsKey(player.getUniqueId())){
            mobCoinBoosters.get(player.getUniqueId()).getTimer().cancel();
        }
        mobCoinBoosters.put(player.getUniqueId(), this);
        player.sendMessage(ChatColor.GREEN+"MobCoin booster added");
    }

    @Override
    public void stop() {
        mobCoinBoosters.remove(player.getUniqueId());
    }

    public static Map<UUID, MobCoin> getMobCoinBoosters() {
        return mobCoinBoosters;
    }
}
