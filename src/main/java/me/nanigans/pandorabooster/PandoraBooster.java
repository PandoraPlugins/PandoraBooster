package me.nanigans.pandorabooster;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.nanigans.pandorabooster.Commands.GiveBooster;
import me.nanigans.pandorabooster.Events.BoosterEvents;
import me.nanigans.pandorabooster.Utility.CustomizedObjectTypeAdapter;
import me.nanigans.pandorabooster.Utility.Glow;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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
        if(!configFile.exists()) {

            saveResource(configFile.getName(), false);
            try {
                Gson gson = gsonBuilder.create();

                map = gson.fromJson(new FileReader(configFile), HashMap.class);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        xpListen();

    }

    public void xpListen(){

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(this, PacketType.Play.Server.EXPERIENCE)
                {

                    @Override
                    public void onPacketSending(PacketEvent event)
                    {

                        System.out.println("event.getPacket().toString() = " + event.getPacket().toString());
                        System.out.println(1);
                        
//                        if(event.getPacket().getPlayerInfoAction().read(0) == EnumWrappers.PlayerInfoAction.ADD_PLAYER) {
//
//                            final PlayerInfoData pid = event.getPacket().getPlayerInfoDataLists().read(0).get(0);
//                            final Player playerRecieving = Bukkit.getPlayer(pid.getProfile().getUUID());
//
//
//                        }

                    }

                }
        );


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
        // Plugin shutdown logic
    }
}
