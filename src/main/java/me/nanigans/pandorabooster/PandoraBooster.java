package me.nanigans.pandorabooster;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.Commands.CurrentBoosters;
import me.nanigans.pandorabooster.Commands.GiveBooster;
import me.nanigans.pandorabooster.Events.BoosterEvents;
import me.nanigans.pandorabooster.Utility.*;
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
        getCommand("currentboosters").setExecutor(new CurrentBoosters());
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
                            if(!file2.getAbsolutePath().endsWith(".yml")) continue;

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
                                t.schedule(boostEnder, Math.abs(Long.parseLong(effect.get("timer").toString())));
                                booster.setTimer(boostEnder);
                                if(!Booster.getEffectBoosters().containsKey(player.getUniqueId()))
                                    Booster.getEffectBoosters().put(player.getUniqueId(), new HashMap<>());
                                Booster.getEffectBoosters().get(player.getUniqueId()).put(booster.getBoostType(), booster);
                                booster.useBooster();

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


        final Map<UUID, Map<BoostTypes, Booster>> effectBoosters = Booster.getEffectBoosters();
        if (effectBoosters.size() > 0) {
            effectBoosters.forEach((i, j) -> j.forEach((k, l) -> {

                final YamlGenerator yaml = new YamlGenerator(getDataFolder() + "/Effects/" + l.getType() + "/" + i + ".yml");
                final Map<String, Object> data = new HashMap<>();
                data.put("name", l.getName());
                l.getTimer().pause();
                data.put("timer", l.getTimer().getRemainingTime());
                yaml.getData().set("Effect", data);
                yaml.save();

            }));

        }
    }
}
