package me.nanigans.pandorabooster.Events;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.BoosterEffects.XP;
import me.nanigans.pandorabooster.DataEnums.Items;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import me.nanigans.pandorabooster.Utility.NBTData;
import me.nanigans.pandorabooster.Utility.YamlGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BoosterEvents implements Listener {


    @EventHandler
    public void onXPGain(PlayerExpChangeEvent event){

        final Player player = event.getPlayer();
        System.out.println(player);
        if (XP.xpPlayers.containsKey(player.getUniqueId())) {
            XP boost = XP.xpPlayers.get(player.getUniqueId());
            final double amplifier = boost.getAmplifier();
            event.setAmount((int) Math.round(event.getAmount()*amplifier));
        }

    }

    @EventHandler
    public void rightClickBooster(PlayerInteractEvent event){

        if(event.getAction().toString().toLowerCase().contains("right")){
            if(event.getItem() != null){
                final ItemStack item = event.getItem();
                if(NBTData.containsNBT(item, Items.ISBOOSTER.toString())){
                    final String boosterName = NBTData.getNBT(item, Items.ISBOOSTER.toString());
                    final Player player = event.getPlayer();
                    final Map<String, Object> booster = (Map<String, Object>) JsonUtil.getData(boosterName);

                    new Booster(player, boosterName, booster).useBooster();

                }
            }

        }

    }

}
