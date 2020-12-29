package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;

import java.util.TimerTask;

public class BoostEnder extends TimerTask {

    private final Booster booster;
    public BoostEnder(Booster boost){
        this.booster = boost;
    }

    @Override
    public void run() {
        booster.stop();
        System.out.println("task");
    }
}
