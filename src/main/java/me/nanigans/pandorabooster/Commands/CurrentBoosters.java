package me.nanigans.pandorabooster.Commands;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.BoosterEffects.*;
import me.nanigans.pandorabooster.Utility.BoostTypes;
import me.nanigans.pandorabooster.Utility.DateParser;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
                final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if(offlinePlayer.hasPlayedBefore()){
                    if(Booster.getEffectBoosters().containsKey(offlinePlayer.getUniqueId())) {
                        sender.sendMessage(ChatColor.GOLD+""+offlinePlayer.getName()+" active boosters\n");
                        sendActiveBoosts(offlinePlayer.getPlayer());
                    }else sender.sendMessage(ChatColor.RED+"This player has no active effects");
                }else{
                    sender.sendMessage(ChatColor.RED+"Could not find this player");
                }
                return true;
            }else if(sender instanceof Player){
                Player player = ((Player) sender);
                if (Booster.getEffectBoosters().containsKey(player.getUniqueId())) {
                    sendActiveBoosts(player);
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

    public void sendActiveBoosts(Player player){
        TextComponent sB = new TextComponent(ChatColor.RED+"---"+ChatColor.GOLD+"Active Boosters"+ChatColor.RED+"---\n");

        final Map<BoostTypes, Booster> boosters = Booster.getEffectBoosters().get(player.getUniqueId());
        boosters.forEach((i, j) ->
                sB.addExtra(textBuilder(ChatColor.GOLD+i.getType() + ": "+ChatColor.RESET +
                                DateParser.formatDateDiff(System.currentTimeMillis()+j.getTimer().getTimeLeft())+"\n",
                        ChatColor.RED+"Amplifier: "+ChatColor.WHITE+"x"+ChatColor.GOLD + j.getAmp())));
        sB.addExtra(ChatColor.RED+"===================");
        player.spigot().sendMessage(sB);

    }

    public TextComponent textBuilder(String string, String hover){

        TextComponent msg = new TextComponent(string);
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        return msg;

    }

}
