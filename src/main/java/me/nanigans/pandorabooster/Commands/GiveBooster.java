package me.nanigans.pandorabooster.Commands;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GiveBooster implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("givebooster")){

            if(args.length > 0){

                Player player = Bukkit.getPlayerExact(args[0]);
                if(player != null){

                    if(args.length > 1){

                        final Map<String, Object> data = ((Map<String, Object>) JsonUtil.getData(null));
                        if(data != null) {
                            final List<String> boosters = data.keySet().stream().filter(i -> !i.startsWith("_comment")).collect(Collectors.toList());
                            if(boosters.contains(args[1])){

                                Map<String, Object> booster = ((Map<String, Object>) data.get(args[1]));
                                if(booster != null){

                                    int amount = 1;
                                    if(args.length > 2){
                                        if(NumberUtils.isNumber(args[2])){
                                            amount = Integer.parseInt(args[2]);
                                        }
                                    }

                                    ItemStack boosterItem = Booster.createBoosterItem(amount, args[1], booster);
                                    if (!player.getInventory().addItem(boosterItem).isEmpty()) {
                                        player.getWorld().dropItem(player.getLocation(), boosterItem);
                                        player.sendMessage(ChatColor.GOLD+"Your inventory was full so this booster was dropped on the groud");
                                    }
                                    sender.sendMessage(ChatColor.GREEN+"Item given");

                                    return true;
                                }else{
                                    sender.sendMessage(ChatColor.RED+"Couldn't find this booster");
                                }

                            }else{
                                sender.sendMessage(ChatColor.RED+"Please specify a registered booster");
                            }
                        }

                    }else{
                        sender.sendMessage(ChatColor.RED+"Please specify the booster to give");
                    }

                }else{
                    sender.sendMessage(ChatColor.RED+"Please specify a valid player");
                }

            }else{
                sender.sendMessage(ChatColor.RED+"Please specify a player");
                return false;
            }

            return true;
        }

        return false;
    }
}
