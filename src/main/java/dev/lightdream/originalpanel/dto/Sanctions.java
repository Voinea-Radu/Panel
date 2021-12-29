package dev.lightdream.originalpanel.dto;

public class Sanctions {

    public int bans;
    public int kicks;
    public int mutes;
    public int warns;

    public Sanctions(int bans, int kicks, int mutes, int warns) {
        this.bans = bans;
        this.kicks = kicks;
        this.mutes = mutes;
        this.warns = warns;
    }
}


