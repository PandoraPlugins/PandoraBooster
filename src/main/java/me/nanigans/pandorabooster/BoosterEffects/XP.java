package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XP extends Booster {
    private final static Map<UUID, XP> xpBoost = new HashMap<>();

    public XP(Player player, Map<String, Object> data, String name, BoostEnder timer){
        super(player, name, data, timer);
    }

    @Override
    public void useBooster() {
        if (xpBoost.containsKey(player.getUniqueId())) {
            xpBoost.get(player.getUniqueId()).getTimer().cancel();
        }
        xpBoost.put(player.getUniqueId(), this);
        player.sendMessage(ChatColor.GREEN+"Activated XP Booster");
    }

    @Override
    public void stop() {
        xpBoost.remove(player.getUniqueId());
    }

    public static Map<UUID, XP> getXpBoost() {
        return xpBoost;
    }


}
