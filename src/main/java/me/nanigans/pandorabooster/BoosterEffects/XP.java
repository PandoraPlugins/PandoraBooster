package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XP extends Booster {
    private final static Map<UUID, XP> xpBoost = new HashMap<>();

    public XP(OfflinePlayer player, Map<String, Object> data, String name, BoostEnder timer){
        super(player, name, data, timer);
    }

    @Override
    public void useBooster() {
        if (xpBoost.containsKey(player.getUniqueId())) {
            final XP xp = xpBoost.get(player.getPlayer().getUniqueId());
            if(xp != null) {
                final BoostEnder timer = xp.getTimer();
                if (timer != null)
                    timer.cancel();
            }
        }
        System.out.println(1);
        xpBoost.put(player.getUniqueId(), this);
        if(player.isOnline())
            player.getPlayer().sendMessage(ChatColor.GREEN+"Activated XP Booster");
    }

    @Override
    public void stop() {
        System.out.println(2);
        xpBoost.remove(player.getUniqueId());
    }

    public static Map<UUID, XP> getXpBoost() {
        return xpBoost;
    }


}
