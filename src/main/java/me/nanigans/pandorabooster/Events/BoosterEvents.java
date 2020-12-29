package me.nanigans.pandorabooster.Events;

import com.earth2me.essentials.Essentials;
import com.massivecraft.factions.cmd.econ.CmdMoney;
import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.DataEnums.Items;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import me.nanigans.pandorabooster.Utility.NBTData;
import me.nanigans.pandorabooster.Utility.YamlGenerator;
import me.nanigans.pandoramines.Events.OreGainEvent;
import net.ess3.api.events.UserBalanceUpdateEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Timer;

public class BoosterEvents implements Listener {


    @EventHandler
    public void onXPGain(PlayerExpChangeEvent event){

        final Player player = event.getPlayer();
        if (XP.getXpBoost().containsKey(player.getUniqueId())) {
            XP boost = XP.getXpBoost().get(player.getUniqueId());
            final double amplifier = boost.getAmplifier();
            event.setAmount((int) Math.round(event.getAmount()*amplifier));
        }

    }

    @EventHandler
    public void onMoneyGain(UserBalanceUpdateEvent event){

        final Player player = event.getPlayer();
        if(Money.getMoneyBoosts().containsKey(player.getUniqueId())){

            double added = event.getNewBalance().doubleValue() - event.getOldBalance().doubleValue();
            if(added > 0){
                final Money money = Money.getMoneyBoosts().get(player.getUniqueId());
                added *= money.getAmp();
                event.setNewBalance(event.getOldBalance().add(BigDecimal.valueOf(added)));
                System.out.println("bal added " + added);
            }

        }

    }

    @EventHandler
    public void onOreDig(OreGainEvent event){
        final Player player = event.getPlayer();
        System.out.println(1);
        if(Mines.getMineBoosts().containsKey(player.getUniqueId())){
            System.out.println(2);
            final Mines mines = Mines.getMineBoosts().get(player.getUniqueId());
            final double amp = mines.getAmp();
            event.getItem().setAmount((int) (event.getItem().getAmount()*amp));
            System.out.println("amt got" + event.getItem().getAmount());

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
                    final String type = booster.get("type").toString();
                    Booster booster1 = null;

                    System.out.println("type = " + type);
                    switch (type) {
                        case "XP": booster1 = new XP(player, booster, boosterName);
                        break;
                        case "MONEY": booster1 = new Money(player, boosterName, booster);
                        break;
                        case "MOBCOIN": booster1 = new MobCoin(player, boosterName, booster);
                        break;
                        case "FISHING": booster1 = new Fishing(player, boosterName, booster);
                        break;
                        case "MINE": booster1 = new Mines(player, boosterName, booster);
                        break;
                    }
                    if(booster1 != null) {

                        booster1.useBooster();
                        final BoostEnder boostEnder = new BoostEnder(booster1);
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

}
