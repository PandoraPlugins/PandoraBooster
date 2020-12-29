package me.nanigans.pandorabooster.Utility;

public enum BoostTypes {
    XP("XP", me.nanigans.pandorabooster.BoosterEffects.XP.class),
    FISHING("Fishing", me.nanigans.pandorabooster.BoosterEffects.Fishing.class),
    MINES("Mines", me.nanigans.pandorabooster.BoosterEffects.Mines.class),
    MOBCOIN("MobCoin", me.nanigans.pandorabooster.BoosterEffects.MobCoin.class),
    MONEY("Money", me.nanigans.pandorabooster.BoosterEffects.Money.class);

    private String type;
    private Class<?> clazz;

    BoostTypes(String type, Class<?> xpClass) {
        this.type = type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getType(){
        return this.type;
    }

}
