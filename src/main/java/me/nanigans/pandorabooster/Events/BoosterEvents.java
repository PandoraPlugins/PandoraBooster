package me.nanigans.pandorabooster.Events;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.DataEnums.Items;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import me.nanigans.pandorabooster.Utility.NBTData;
import me.nanigans.pandorabooster.Utility.YamlGenerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class BoosterEvents implements Listener {

    @EventHandler
    public void rightClickBooster(PlayerInteractEvent event){

        if(event.getAction().toString().toLowerCase().contains("right")){
            if(event.getItem() != null){
                final ItemStack item = event.getItem();
                if(NBTData.containsNBT(item, Items.ISBOOSTER.toString())){
                    final String boosterName = NBTData.getNBT(item, Items.ISBOOSTER.toString());
                    final Player player = event.getPlayer();
                    final Map<String, Object> booster = (Map<String, Object>) JsonUtil.getData(boosterName);
                    final YamlGenerator yaml = new YamlGenerator(Items.USERPATH.getData() + "/" + player.getUniqueId() + ".yml");

                    final List<Map<?, ?>> mapList = yaml.getData().getMapList(Items.BOOSTS.getData());
                    if(mapList != null){
                        new Booster(player, boosterName, booster).useBooster();
                    }

                }
            }

        }

    }

}
