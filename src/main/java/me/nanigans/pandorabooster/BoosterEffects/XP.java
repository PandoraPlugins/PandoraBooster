package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XP extends Booster {
    private final long duration;
    private final double amplifier;
    private final static Map<UUID, XP> xpBoost = new HashMap<>();

    public XP(Player player, Map<String, Object> data, String name){
        super(player, name, data);
        this.amplifier = Double.parseDouble(data.get("amplifier").toString());
        duration = Long.parseLong(data.get("time").toString());
    }

    public XP(Player player, Map<String, Object> data, String name, long time){
        super(player, name, data);
        this.amplifier = Double.parseDouble(data.get("amplifier").toString());
        duration = time;
    }

    public double getAmplifier() {
        return amplifier;
    }

    @Override
    public void useBooster() {
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

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getData() {
        return super.boosterData;
    }

    public long getDuration() {
        return duration;
    }
}
