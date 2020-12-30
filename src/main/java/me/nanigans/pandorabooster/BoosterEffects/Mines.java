package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Mines extends Booster {

    private static final Map<UUID, Mines> mineBoosts = new HashMap<>();

    public Mines(OfflinePlayer player, String name, Map<String, Object> booster, BoostEnder timer) {
        super(player, name, booster, timer);
    }

    @Override
    public void useBooster() {
        if(mineBoosts.containsKey(player.getUniqueId())){
            effectBoosters.get(player.getUniqueId()).get(BoostTypes.FISHING).getTimer().cancel();
        }
        effectBoosters.get(player.getUniqueId()).put(BoostTypes.MINES, this);
    }

    @Override
    public void stop() {
        effectBoosters.get(player.getUniqueId()).remove(BoostTypes.MINES);
        if(effectBoosters.get(player.getUniqueId()).isEmpty())
            effectBoosters.remove(player.getUniqueId());    }

    public static Map<UUID, Mines> getMineBoosts() {
        return mineBoosts;
    }
}
