package me.nanigans.pandorabooster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.Commands.GiveBooster;
import me.nanigans.pandorabooster.Events.BoosterEvents;
import me.nanigans.pandorabooster.Utility.CustomizedObjectTypeAdapter;
import me.nanigans.pandorabooster.Utility.Glow;
import me.nanigans.pandorabooster.Utility.YamlGenerator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PandoraBooster extends JavaPlugin {
    GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new CustomizedObjectTypeAdapter());
    public HashMap map = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        File configFile = new File(getDataFolder(), "boosters.json");
        new File(getDataFolder()+"/Users/").mkdirs();
        registerGlow();
        getCommand("givebooster").setExecutor(new GiveBooster());
        getServer().getPluginManager().registerEvents(new BoosterEvents(), this);
        new File("Effects/Fishing").mkdirs();
        new File("Effects/Mines").mkdirs();
        new File("Effects/MobCoin").mkdirs();
        new File("Effects/Money").mkdirs();
        new File("Effects/XP").mkdirs();

        if(!configFile.exists()) {

            saveResource(configFile.getName(), false);
            try {
                Gson gson = gsonBuilder.create();

                map = gson.fromJson(new FileReader(configFile), HashMap.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Glow glow = new Glow(71);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException ignored){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {

        final Map<UUID, XP> xpBoost = XP.getXpBoost();
        final Map<UUID, Fishing> fishBoosters = Fishing.getFishBoosters();
        final Map<UUID, Mines> mineBoosts = Mines.getMineBoosts();
        final Map<UUID, MobCoin> mobCoinBoosters = MobCoin.getMobCoinBoosters();
        final Map<UUID, Money> moneyBoosts = Money.getMoneyBoosts();
        final Map<UUID, Booster> boosters = new HashMap<>(xpBoost);
        boosters.putAll(fishBoosters);
        boosters.putAll(mineBoosts);
        boosters.putAll(mobCoinBoosters);
        boosters.putAll(moneyBoosts);

        boosters.forEach((i, j) -> {
            final YamlGenerator yaml = new YamlGenerator("Effects/"+j.getType()+"/" + i + ".yml");
            final Map<String, Object> data = new HashMap<>();
            data.put("name", j.getName());
            j.getTimer().pause();
            data.put("timer", j.getTimer().getRemainingTime());
            yaml.getData().set("Effect", data);
            yaml.save();
        });

    }
}
