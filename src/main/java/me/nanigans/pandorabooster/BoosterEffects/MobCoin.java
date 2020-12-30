package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MobCoin extends Booster {

    private static final Map<UUID, MobCoin> mobCoinBoosters = new HashMap<>();

    public MobCoin(OfflinePlayer player, String name, Map<String, Object> booster, BoostEnder timer) {
        super(player, name, booster, timer);
    }

    @Override
    public void useBooster() {
        if(effectBoosters.get(player.getUniqueId()).containsKey(BoostTypes.MOBCOIN)){
            effectBoosters.get(player.getUniqueId()).get(BoostTypes.MOBCOIN).getTimer().cancel();
        }
        effectBoosters.get(player.getUniqueId()).put(BoostTypes.MOBCOIN, this);
        if(player.isOnline())
        player.getPlayer().sendMessage(ChatColor.GREEN+"MobCoin booster added");
    }

    @Override
    public void stop() {
        effectBoosters.get(player.getUniqueId()).remove(BoostTypes.MOBCOIN);
        if(effectBoosters.get(player.getUniqueId()).isEmpty())
            effectBoosters.remove(player.getUniqueId());
    }

    public static Map<UUID, MobCoin> getMobCoinBoosters() {
        return mobCoinBoosters;
    }
}
