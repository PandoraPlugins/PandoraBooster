package me.nanigans.pandorabooster.Commands;

import me.nanigans.pandorabooster.BoosterEffects.Fishing;
import me.nanigans.pandorabooster.BoosterEffects.Mines;
import me.nanigans.pandorabooster.BoosterEffects.XP;
import me.nanigans.pandorabooster.Utility.DateParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class CurrentBoosters implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(command.getName().equalsIgnoreCase("currentboosters")){

            if(sender.hasPermission("Boosters.SeeOthers") && args.length > 0){


            }else if(sender instanceof Player){
                Player player = ((Player) sender);
                final UUID uuid = player.getUniqueId();
                StringBuilder sB = new StringBuilder("Active Boosters\n-----------");
                if(XP.getXpBoost().containsKey(uuid)){
                    sB.append("XP: " + DateParser.formatDateDiff(
                            new Date().getTime() + XP.getXpBoost().get(uuid).getTimer().getRemainingTime()));
                    sB.append("\n");
                }
                if(Fishing.getFishBoosters().containsKey(uuid)){
                    sB.append("Fishing: " + Fishing.getFishBoosters().get(uuid).getTimer().getRemainingTime()+"\n");
                }
                if(Mines.getMineBoosts().containsKey(uuid))
                    //sB.append("Mining: " + )
            }else{
                sender.sendMessage(ChatColor.RED+"Please specify a user");
            }

        }

        return false;
    }
}
