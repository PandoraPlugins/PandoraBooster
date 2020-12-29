package me.nanigans.pandorabooster.BoosterEffects;

import net.minecraft.server.v1_8_R3.PacketPlayOutExperience;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;

public class XP implements Listener {
    private final Player player;
    private final String name;
    private final Map<String, Object> data;
    private final long duration;

    public XP(Player player, Map<String, Object> data, String name){
        this.player = player;
        this.data = data;
        this.name = name;
        duration = Long.parseLong(data.get("time").toString());
        gainXP();
    }
    public XP(Player player, Map<String, Object> data, String name, long time){
        this.player = player;
        this.data = data;
        this.name = name;
        duration = time;
    }

    public void gainXP(){
        System.out.println(1);
        final PacketPlayOutExperience xppacket = new PacketPlayOutExperience(10F, 100, 100);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(xppacket);
    }


}
