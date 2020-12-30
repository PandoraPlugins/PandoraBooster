package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Money extends Booster {
    private static final Map<UUID, Money> moneyBoosts = new HashMap<>();

    public Money(OfflinePlayer player, String name, Map<String, Object> booster, BoostEnder timer) {
        super(player, name, booster, timer);
    }

    @Override
    public void useBooster() {
        if(effectBoosters.get(player.getUniqueId()).containsKey(BoostTypes.MONEY)){
            effectBoosters.get(player.getUniqueId()).get(BoostTypes.MONEY).getTimer().cancel();
        }
        effectBoosters.get(player.getUniqueId()).put(BoostTypes.MONEY, this);
    }

    @Override
    public void stop() {
        effectBoosters.get(player.getUniqueId()).remove(BoostTypes.MONEY);
        if(effectBoosters.get(player.getUniqueId()).isEmpty())
            effectBoosters.remove(player.getUniqueId());    }

    public static Map<UUID, Money> getMoneyBoosts() {
        return moneyBoosts;
    }
}
