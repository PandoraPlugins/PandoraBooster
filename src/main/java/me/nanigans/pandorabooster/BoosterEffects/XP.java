package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class XP extends Booster {
    private final long duration;
    private final double amplifier;
    public final static Map<UUID, XP> xpPlayers = new HashMap<>();

    public XP(Player player, Map<String, Object> data, String name){
        super(player, name, data);
        this.amplifier = Double.parseDouble(data.get("amplifier").toString());
        duration = Long.parseLong(data.get("time").toString());
        xpPlayers.put(player.getUniqueId(), this);
    }

    public XP(Player player, Map<String, Object> data, String name, long time){
        super(player, name, data);
        this.amplifier = Double.parseDouble(data.get("amplifier").toString());
        duration = time;
        xpPlayers.put(player.getUniqueId(), this);
    }

    public double getAmplifier() {
        return amplifier;
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
