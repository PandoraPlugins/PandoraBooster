package me.nanigans.pandorabooster.BoosterEffects;

import me.nanigans.pandorabooster.Booster;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class BoostEnder extends TimerTask {
    private final Booster booster;
    private long endTime;
    private long remainingTime;
    private boolean isPaused;
    public BoostEnder(Booster boost){
        this.booster = boost;
        this.endTime = boost.getTimeOut() + System.currentTimeMillis();
    }

    public void pause() {
        this.isPaused = true;
        this.remainingTime = endTime - System.currentTimeMillis();
        this.cancel();
    }

    public void resume() {

        final BoostEnder timer = new BoostEnder(this.booster);
        timer.setEndTime(System.currentTimeMillis()+remainingTime);
        timer.setRemainingTime(this.remainingTime);
        Timer t = new Timer();
        t.schedule(timer, this.remainingTime);
        this.isPaused = false;
        this.booster.setTimer(timer);
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public boolean isPaused() {
        return isPaused;
    }

    @Override
    public void run() {
        booster.stop();
        System.out.println("task");
    }
}
