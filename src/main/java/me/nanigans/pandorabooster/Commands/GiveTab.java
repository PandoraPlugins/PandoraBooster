package me.nanigans.pandorabooster.Commands;

import me.nanigans.pandorabooster.Utility.JsonUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GiveTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if(command.getName().equalsIgnoreCase("givebooster")){

            switch (args.length) {
                case 1: return Collections.singletonList("<player>");
                case 2:
                    final Map<String, Object> data = (Map<String, Object>) JsonUtil.getData(null);
                    if(data != null)
                    return data.keySet().stream().filter(i -> !i.startsWith("_comment")).collect(Collectors.toList());
                case 3: return Collections.singletonList("<optional amount>");
            }

        }

        return null;
    }
}
