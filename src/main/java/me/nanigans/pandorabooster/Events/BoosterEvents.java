package me.nanigans.pandorabooster.Events;

import com.earth2me.essentials.Mob;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Timer;

public class BoosterEvents implements Listener {


    @EventHandler
    public void playerLeave(PlayerQuitEvent event){

        final Player player = event.getPlayer();
        if (XP.getXpBoost().containsKey(player.getUniqueId())) {
            final XP xp = XP.getXpBoost().get(player.getUniqueId());
            xp.getTimer().pause();
        }
        if (Fishing.getFishBoosters().containsKey(player.getUniqueId())) {
            final Fishing xp = Fishing.getFishBoosters().get(player.getUniqueId());
            xp.getTimer().pause();
        }
        if (Mines.getMineBoosts().containsKey(player.getUniqueId())) {
            final Mines xp = Mines.getMineBoosts().get(player.getUniqueId());
            xp.getTimer().pause();
        }
        if (MobCoin.getMobCoinBoosters().containsKey(player.getUniqueId())) {
            final MobCoin xp = MobCoin.getMobCoinBoosters().get(player.getUniqueId());
            xp.getTimer().pause();
        }
        if (Money.getMoneyBoosts().containsKey(player.getUniqueId())) {
            final Money xp = Money.getMoneyBoosts().get(player.getUniqueId());
            xp.getTimer().pause();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        final Player player = event.getPlayer();
        if (XP.getXpBoost().containsKey(player.getUniqueId())) {
            final XP xp = XP.getXpBoost().get(player.getUniqueId());
            xp.getTimer().resume();
        }
        if (Fishing.getFishBoosters().containsKey(player.getUniqueId())) {
            final Fishing xp = Fishing.getFishBoosters().get(player.getUniqueId());
            xp.getTimer().resume();
        }
        if (Mines.getMineBoosts().containsKey(player.getUniqueId())) {
            final Mines xp = Mines.getMineBoosts().get(player.getUniqueId());
            xp.getTimer().resume();
        }
        if (MobCoin.getMobCoinBoosters().containsKey(player.getUniqueId())) {
            final MobCoin xp = MobCoin.getMobCoinBoosters().get(player.getUniqueId());
            xp.getTimer().resume();
        }
        if (Money.getMoneyBoosts().containsKey(player.getUniqueId())) {
            final Money xp = Money.getMoneyBoosts().get(player.getUniqueId());
            xp.getTimer().resume();
        }
    }


    @EventHandler
    public void mobCoinGain(MobCoinsReceiveEvent event){

        final Player player = event.getProfile().getPlayer();
        if(MobCoin.getMobCoinBoosters().containsKey(player.getUniqueId())){

            final MobCoin mobCoin = MobCoin.getMobCoinBoosters().get(player.getUniqueId());
            if(Math.random()*100 < mobCoin.getChance()){
                event.setAmount((int) Math.round(event.getAmount()*mobCoin.getAmp()));
            }
        }

    }

    @EventHandler
    public void onGainFish(RewardEvent event){

        final Player player = event.getPlayer();
        if(Fishing.getFishBoosters().containsKey(player.getUniqueId())){
            final Fishing fishing = Fishing.getFishBoosters().get(player.getUniqueId());
            if(Math.random()*100 < fishing.getChance()){
                event.getItem().setAmount((int) Math.round(event.getItem().getAmount()*fishing.getAmp()));
            }
        }

    }

    @EventHandler
    public void onXPGain(PlayerExpChangeEvent event){

        final Player player = event.getPlayer();
        if (XP.getXpBoost().containsKey(player.getUniqueId())) {
            XP boost = XP.getXpBoost().get(player.getUniqueId());
            if(Math.random()*100 < boost.getChance()) {
                final double amplifier = boost.getAmp();
                event.setAmount((int) Math.round(event.getAmount() * amplifier));
            }
        }
    }

    @EventHandler
    public void onMoneyGain(UserBalanceUpdateEvent event){

        final Player player = event.getPlayer();
        if(Money.getMoneyBoosts().containsKey(player.getUniqueId())){

            double added = event.getNewBalance().doubleValue() - event.getOldBalance().doubleValue();
            if(added > 0){
                final Money money = Money.getMoneyBoosts().get(player.getUniqueId());
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
        if(Mines.getMineBoosts().containsKey(player.getUniqueId())){
            final Mines mines = Mines.getMineBoosts().get(player.getUniqueId());
            if(Math.random()*100 < mines.getChance()) {
                final double amp = mines.getAmp();
                event.getItem().setAmount((int) Math.round(event.getItem().getAmount() * amp));
            }
        }

    }

    @EventHandler
    public void rightClickBooster(PlayerInteractEvent event) throws IllegalAccessException, InstantiationException {

        if(event.getAction().toString().toLowerCase().contains("right")){
            if(event.getItem() != null){
                final ItemStack item = event.getItem();
                if(NBTData.containsNBT(item, Items.ISBOOSTER.toString())){
                    final String boosterName = NBTData.getNBT(item, Items.ISBOOSTER.toString());
                    final Player player = event.getPlayer();
                    final Map<String, Object> booster = (Map<String, Object>) JsonUtil.getData(boosterName);
                    final String type = booster.get("type").toString();
                    Booster booster1 = (Booster) BoostTypes.valueOf(type.toUpperCase()).getClazz().newInstance();

                    final BoostEnder boostEnder = new BoostEnder(booster1);
                    booster1.setTimer(boostEnder);
                    booster1.useBooster();
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
