package dev.lightdream.originalpanel.managers;

import dev.lightdream.logger.Logger;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.Cache;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.dto.data.frontend.Complain;
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
        }, 60000L); //60 seconds

        registeredPlayersCount = new Cache(cache -> cache.update(Main.instance.databaseManager.getRegisteredCount()), 1800000L); // 30 minutes

        donorsCount = new Cache(cache -> cache.update(Main.instance.databaseManager.getDonorsCount()), 1800000L); //30 minutes

        staffs = new Cache(cache -> cache.update(Main.instance.databaseManager.getStaff()), 43200000L); // 12 hours

        new Cache(cache -> Main.instance.databaseManager.getAll(Complain.class).forEach(complain -> {
            // 7 days
            if (System.currentTimeMillis() > complain.timestamp + 604800000L &&
                    complain.status == ComplainData.ComplainStatus.OPEN_AWAITING_TARGET_RESPONSE) {
                complain.notify = true;
                complain.status = ComplainData.ComplainStatus.OPEN_AWAITING_STAFF_APPROVAL;
                complain.save();
            }
        }), 43200000L); // 12 hours

        Logger.good("Caching data finished");
    }


}
