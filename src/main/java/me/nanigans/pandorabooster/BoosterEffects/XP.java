package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.Map;

public class XP extends Booster {

    public XP(OfflinePlayer player, Map<String, Object> data, String name, BoostEnder timer){
        super(player, name, data, timer);
    }

    @Override
    public void useBooster() {
        if(effectBoosters.get(player.getUniqueId()).containsKey(BoostTypes.XP)){
            effectBoosters.get(player.getUniqueId()).get(BoostTypes.XP).getTimer().cancel();
        }
        effectBoosters.get(player.getUniqueId()).put(BoostTypes.XP, this);
        if(player.isOnline())
            player.getPlayer().sendMessage(ChatColor.GREEN+"Activated XP Booster");
    }

    @Override
    public void stop() {
        effectBoosters.get(player.getUniqueId()).remove(BoostTypes.XP);
        if(effectBoosters.get(player.getUniqueId()).isEmpty())
            effectBoosters.remove(player.getUniqueId());
    }


}
