package dev.lightdream.originalpanel.managers;

import dev.lightdream.logger.Logger;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.Cache;
import me.nurio.minecraft.pinger.MinecraftServerPinger;
import me.nurio.minecraft.pinger.beans.MinecraftServerStatus;

public class CacheManager {

    public Main main;
    public Cache onlinePlayers;
    public Cache registeredPlayersCount;
    public Cache donorsCount;
    public Cache staffs;


    public CacheManager(Main main) {
        Logger.good("Started caching");
        this.main = main;

        onlinePlayers = new Cache(cache -> {

            MinecraftServerStatus server = MinecraftServerPinger.ping("original.gg");

            if (server.isOffline()) {
                cache.update(1);
                return;
            }

            cache.update(server.getPlayers().getOnline());
        }, 60 * 1000L); //1 minute

        registeredPlayersCount = new Cache(cache -> cache.update(Main.instance.databaseManager.getRegisteredCount()), 30 * 60 * 60 * 1000L); // 30 minutes

        donorsCount = new Cache(cache -> cache.update(Main.instance.databaseManager.getDonorsCount()), 30 * 60 * 60 * 1000L); //30 minutes

        staffs = new Cache(cache -> cache.update(Main.instance.databaseManager.getStaff()), 12 * 60 * 60 * 60 * 1000L); // 12 hours

        Logger.good("Caching data finished");
    }


}
