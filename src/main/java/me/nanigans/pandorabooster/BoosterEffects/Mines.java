package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
            mineBoosts.get(player.getUniqueId()).getTimer().cancel();
        }
        mineBoosts.put(player.getUniqueId(), this);
        if(player.isOnline())
        player.getPlayer().sendMessage(ChatColor.GREEN+"Mine Boost used");
    }

    @Override
    public void stop() {
        mineBoosts.remove(player.getUniqueId());
    }

    public static Map<UUID, Mines> getMineBoosts() {
        return mineBoosts;
    }
}
