package me.nanigans.pandorabooster.DataEnums;

import me.nanigans.pandorabooster.PandoraBooster;

public enum Items {
    ISBOOSTER("IsBooster"),
    USERPATH(PandoraBooster.getPlugin(PandoraBooster.class).getDataFolder().getAbsolutePath()+"/Users"),
    BOOSTS("boosts");

    String data;
    Items(String data) {
        this.data = data;
    }
    public String getData() {
        return data;
    }
}
