package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;
import me.nanigans.pandorabooster.Utility.JsonUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Timer;
import java.util.TimerTask;

public class BoostEnder extends TimerTask {
    private final Booster booster;
    private long endTime;
    private long remainingTime;
    public BoostEnder(Booster boost){
        this.booster = boost;
        this.endTime = boost.getTimeOut() + System.currentTimeMillis();
    }

    public void pause() {
        this.remainingTime = endTime - System.currentTimeMillis();
        this.cancel();
    }

    public void resume() {

        final BoostEnder timer = new BoostEnder(this.booster);
        timer.setEndTime(System.currentTimeMillis()+remainingTime);
        timer.setRemainingTime(this.remainingTime);
        Timer t = new Timer();
        t.schedule(timer, this.remainingTime);
        this.booster.setTimer(timer);
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getTimeLeft(){
        return this.endTime - System.currentTimeMillis();
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    @Override
    public void run() {
        booster.stop();
        Bukkit.getPlayer(booster.getPlayer().getUniqueId()).sendMessage(
                ChatColor.translateAlternateColorCodes('&', JsonUtil.getData("_messages.booster_end")
                        .toString().replaceAll("\\{type}", booster.getType())
                        .replaceAll("\\{name}", booster.getName())));
    }
}
