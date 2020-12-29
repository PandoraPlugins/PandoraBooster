package me.nanigans.pandorabooster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.Commands.GiveBooster;
import me.nanigans.pandorabooster.Events.BoosterEvents;
import me.nanigans.pandorabooster.Utility.CustomizedObjectTypeAdapter;
import me.nanigans.pandorabooster.Utility.Glow;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import me.nanigans.pandorabooster.Utility.YamlGenerator;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

public final class PandoraBooster extends JavaPlugin {
    GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new CustomizedObjectTypeAdapter());
    public HashMap map = new HashMap<>();
    private final File fishing = new File("Effects/Fishing");
    private final File mines = new File("Effects/Mines");

    @Override
    public void onEnable() {
        // Plugin startup logic

        File configFile = new File(getDataFolder(), "boosters.json");
        registerGlow();
        getCommand("givebooster").setExecutor(new GiveBooster());
        getServer().getPluginManager().registerEvents(new BoosterEvents(), this);
        fishing.mkdirs();
        mines.mkdirs();
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
        returnEffects();

    }

    private void returnEffects() {

        File file = new File(getDataFolder()+"/Effects");
        if(file.exists()){

            final File[] files = file.listFiles();
            if(files != null)
            for (File file1 : files) {

                if(file1.isDirectory()){

                    File[] nested = file1.listFiles();
                    if(nested != null){

                        for (File file2 : nested) {

                            final YamlGenerator yaml = new YamlGenerator(file2.getAbsolutePath());
                            final Map<String, Object> effect = YamlGenerator.getConfigSectionValue(yaml.getData().get("Effect"), false);
                            final String name = effect.get("name").toString();
                            final Map<String, Object> data = (Map<String, Object>) JsonUtil.getData(name);
                            if(data != null){
                                final OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(file2.getName().replace(".yml", "")));

                                final String type = data.get("type").toString();
                                final Booster booster = BoosterEvents.getBoosterfromName(type, player, name, data);
                                final BoostEnder boostEnder = new BoostEnder(booster);
                                Timer t = new Timer();
                                t.schedule(boostEnder, Long.parseLong(effect.get("timer").toString()));
                                booster.setTimer(boostEnder);
                                booster.useBooster();
                                if (booster instanceof MobCoin) {
                                    MobCoin.getMobCoinBoosters().put(player.getUniqueId(), ((MobCoin) booster));
                                }else if(booster instanceof XP)
                                    XP.getXpBoost().put(player.getUniqueId(), ((XP) booster));
                                else if(booster instanceof Fishing)
                                    Fishing.getFishBoosters().put(player.getUniqueId(), ((Fishing) booster));
                                else if(booster instanceof Mines)
                                    Mines.getMineBoosts().put(player.getUniqueId(), ((Mines) booster));
                                else if(booster instanceof Money)
                                    Money.getMoneyBoosts().put(player.getUniqueId(), (Money) booster);

                                booster.getTimer().pause();
                            }


                        }

                    }

                }

            }

        }

    }

    private void registerGlow() {
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

    if(boosters.size() > 0) {
        boosters.forEach((i, j) -> {
            final YamlGenerator yaml = new YamlGenerator(getDataFolder() + "/Effects/" + j.getType() + "/" + i + ".yml");
            final Map<String, Object> data = new HashMap<>();
            data.put("name", j.getName());
            j.getTimer().pause();
            data.put("timer", j.getTimer().getRemainingTime());
            yaml.getData().set("Effect", data);
            yaml.save();
        });
    }

    }
}
