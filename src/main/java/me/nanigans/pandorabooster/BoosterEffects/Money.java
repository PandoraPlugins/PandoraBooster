package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Money extends Booster {
    private static final Map<UUID, Money> moneyBoosts = new HashMap<>();

    public Money(Player player, String name, Map<String, Object> booster) {
        super(player, name, booster);
    }

    @Override
    public void useBooster() {
        moneyBoosts.put(player.getUniqueId(), this);
        player.sendMessage(ChatColor.GREEN+"You have used a money booster");
    }

    @Override
    public void stop() {
        moneyBoosts.remove(player.getUniqueId(), this);
    }

    public static Map<UUID, Money> getMoneyBoosts() {
        return moneyBoosts;
    }
}
