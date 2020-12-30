package me.nanigans.pandorabooster.Events;

import dev.minecraftplugins.pandora.pandoralake.listener.RewardEvent;
import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.DataEnums.Items;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import me.nanigans.pandorabooster.Utility.NBTData;
import me.nanigans.pandoramines.Events.OreGainEvent;
import me.swanis.mobcoins.events.MobCoinsReceiveEvent;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class BoosterEvents implements Listener {


    @EventHandler
    public void playerLeave(PlayerQuitEvent event){

        final Player player = event.getPlayer();
        if(Booster.getEffectBoosters().containsKey(player.getUniqueId())){
            Booster.getEffectBoosters().get(player.getUniqueId()).forEach((i, j) -> {
                j.getTimer().pause();
            });
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        final Player player = event.getPlayer();
        if(Booster.getEffectBoosters().containsKey(player.getUniqueId())) {
            Booster.getEffectBoosters().get(player.getUniqueId()).forEach((i, j) -> {
                j.getTimer().resume();
            });
        }
    }


    @EventHandler
    public void mobCoinGain(MobCoinsReceiveEvent event){

        final Player player = event.getProfile().getPlayer();
        if(containsBooster(player, BoostTypes.MOBCOIN)){
            final MobCoin mobCoin = (MobCoin) Booster.getEffectBoosters().get(player.getUniqueId()).get(BoostTypes.MOBCOIN);
            if(Math.random()*100 < mobCoin.getChance()){
                event.setAmount((int) Math.round(event.getAmount()*mobCoin.getAmp()));
            }
        }

    }

    public static boolean containsBooster(Player player, BoostTypes boost){
        return Booster.getEffectBoosters().containsKey(player.getUniqueId()) && Booster.getEffectBoosters().get(player.getUniqueId()).containsKey(boost);
    }

    @EventHandler
    public void onGainFish(RewardEvent event){

        final Player player = event.getPlayer();
        if(containsBooster(player, BoostTypes.FISHING)){
            final Fishing fishing = (Fishing) Booster.getEffectBoosters().get(player.getUniqueId()).get(BoostTypes.FISHING);
            if(Math.random()*100 < fishing.getChance()){
                event.getItem().setAmount((int) Math.round(event.getItem().getAmount()*fishing.getAmp()));
            }
        }

    }

    @EventHandler
    public void onXPGain(PlayerExpChangeEvent event){

        final Player player = event.getPlayer();
        if (containsBooster(player, BoostTypes.XP)) {
            XP boost = (XP) Booster.getEffectBoosters().get(player.getUniqueId()).get(BoostTypes.XP);
            if(Math.random()*100 < boost.getChance()) {
                final double amplifier = boost.getAmp();
                event.setAmount((int) Math.round(event.getAmount() * amplifier));
            }
        }
    }

    @EventHandler
    public void onMoneyGain(UserBalanceUpdateEvent event){

        final Player player = event.getPlayer();
        if(containsBooster(player, BoostTypes.MONEY)){

            double added = event.getNewBalance().doubleValue() - event.getOldBalance().doubleValue();
            if(added > 0){
                final Money money = (Money) Booster.getEffectBoosters().get(player.getUniqueId()).get(BoostTypes.MONEY);
                if(Math.random() < money.getChance()) {
                    added *= money.getAmp();
                    event.setNewBalance(event.getOldBalance().add(BigDecimal.valueOf(added)));
                }
            }

        }

    }

    @EventHandler
    public void onOreDig(OreGainEvent event){
        final Player player = event.getPlayer();
        if(containsBooster(player, BoostTypes.MINES)){
            final Mines mines = (Mines) Booster.getEffectBoosters().get(player.getUniqueId()).get(BoostTypes.MINES);
            if(Math.random()*100 < mines.getChance()) {
                final double amp = mines.getAmp();
                event.getItem().setAmount((int) Math.round(event.getItem().getAmount() * amp));
            }
        }

    }

    @EventHandler
    public void rightClickBooster(PlayerInteractEvent event) {

        if(event.getAction().toString().toLowerCase().contains("right")){
            if(event.getItem() != null){
                final ItemStack item = event.getItem();
                if(NBTData.containsNBT(item, Items.ISBOOSTER.toString())){
                    final String boosterName = NBTData.getNBT(item, Items.ISBOOSTER.toString());
                    final Player player = event.getPlayer();
                    final Map<String, Object> booster = (Map<String, Object>) JsonUtil.getData(boosterName);
                    final String type = booster.get("type").toString();
                    Booster booster1 = getBoosterfromName(type, player, boosterName, booster);

                    if(booster1 != null) {
                        final BoostEnder boostEnder = new BoostEnder(booster1);
                        booster1.setTimer(boostEnder);
                        if(!Booster.getEffectBoosters().containsKey(player.getUniqueId()))
                            Booster.getEffectBoosters().put(player.getUniqueId(), new HashMap<>());

                        booster1.useBooster();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                JsonUtil.getData("_messages.booster_activate").toString().replaceAll("\\{type}", type)
                        .replaceAll("\\{name}", boosterName)));
                        Timer t = new Timer();
                        t.schedule(boostEnder, booster1.getTimeOut());
                        if (item.getAmount() == 1) {
                            player.setItemInHand(null);
                        }
                        item.setAmount(item.getAmount() - 1);
                    }

                }
            }

        }

    }

    public static Booster getBoosterfromName(String type, OfflinePlayer player, String boosterName, Map<String, Object> booster){

        Booster booster1 = null;
        switch (type) {

            case "XP": booster1 = new XP(player, booster, boosterName, null);
                break;
            case "MONEY": booster1 = new Money(player, boosterName, booster, null);
                break;
            case "MOBCOIN": booster1 = new MobCoin(player, boosterName, booster, null);
                break;
            case "FISHING": booster1 = new Fishing(player, boosterName, booster, null);
                break;
            case "MINE": booster1 = new Mines(player, boosterName, booster, null);
                break;

        }
        return booster1;
    }



}
