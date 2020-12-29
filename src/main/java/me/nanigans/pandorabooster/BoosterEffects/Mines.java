package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Mines extends Booster {

    private static final Map<UUID, Mines> mineBoosts = new HashMap<>();

    public Mines(Player player, String name, Map<String, Object> booster) {
        super(player, name, booster);
    }

    @Override
    public void useBooster() {

    }

    @Override
    public void stop() {

    }

    public static Map<UUID, Mines> getMineBoosts() {
        return mineBoosts;
    }
}
