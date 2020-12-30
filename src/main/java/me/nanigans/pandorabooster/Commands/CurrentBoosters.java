package me.nanigans.pandorabooster.Commands;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import me.nanigans.pandorabooster.Utility.DateParser;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CurrentBoosters implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(command.getName().equalsIgnoreCase("currentboosters")){

            if(sender.hasPermission("Boosters.SeeOthers") && args.length > 0){


            }else if(sender instanceof Player){
                Player player = ((Player) sender);
                TextComponent sB = new TextComponent(ChatColor.RED+"---"+ChatColor.GOLD+"Active Boosters"+ChatColor.RED+"---\n");
                if (Booster.getEffectBoosters().containsKey(player.getUniqueId())) {

                    final Map<BoostTypes, Booster> boosters = Booster.getEffectBoosters().get(player.getUniqueId());
                    boosters.forEach((i, j) -> sB.addExtra(textBuilder(i.getType() + ": " + j.getTimer().getTimeLeft(),
                            "Amplifier: " + j.getAmp())));
                    sB.addExtra("=============");
                    player.spigot().sendMessage(sB);

                }else{
                    player.sendMessage(ChatColor.RED+"You do not have any active boosters");
                }
                return true;
            }else{
                sender.sendMessage(ChatColor.RED+"Please specify a user");
            }

        }

        return false;
    }

    public TextComponent textBuilder(String string, String hover){

        TextComponent msg = new TextComponent(string);
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        return msg;

    }

}
