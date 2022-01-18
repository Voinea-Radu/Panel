package dev.lightdream.originalpanel.managers;

import dev.lightdream.logger.Logger;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.Cache;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.dto.data.frontend.Complain;
import me.nurio.minecraft.pinger.MinecraftServerPinger;
import me.nurio.minecraft.pinger.beans.MinecraftServerStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class CacheManager {

    public Main main;
    public Cache onlinePlayers;
    public Cache registeredPlayersCount;
    public Cache donorsCount;
    public Cache staffs;
    public Cache donationsGoal;
    public Cache topDonator;

    @SuppressWarnings("TextBlockMigration")
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

        donationsGoal = new Cache(cache -> {
            StringBuilder storeScrape = new StringBuilder();

            URLConnection connection;
            try {
                connection = new URL("https://store.original.gg").openConnection();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    for (String line; ((line = reader.readLine()) != null); ) {
                        storeScrape.append("\n").append(line);
                    }
                } finally {
                    if (reader != null) try {
                        reader.close();
                    } catch (IOException ignore) {
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String percentString = (storeScrape.substring(storeScrape.indexOf("<div id=\"js-goal\" class=\"donation-goal\">"), storeScrape.indexOf("<div class=\"progress  progress-striped   active \">")));

            percentString = percentString.replace("<div id=\"js-goal\" class=\"donation-goal\">\n" +
                    "            <p>\n" +
                    "                \n" +
                    "    \t\t        \t\t\t    ", "");
            percentString = percentString.replace("\n" +
                    "    \t\t     \n" +
                    "            </p>", "");
            percentString = percentString.replace("\n", "");
            percentString = percentString.replace("% completed", "");
            percentString = percentString.replace(" ", "");

            int percent = Integer.parseInt(percentString);

            cache.update(percent);

        }, 3600000L); //1 hour

        topDonator = new Cache(cache->{
            StringBuilder storeScrape = new StringBuilder();

            URLConnection connection;
            try {
                connection = new URL("https://store.original.gg").openConnection();
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    for (String line; ((line = reader.readLine()) != null); ) {
                        storeScrape.append("\n").append(line);
                    }
                } finally {
                    if (reader != null) try {
                        reader.close();
                    } catch (IOException ignore) {
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String rawData = (storeScrape.substring(storeScrape.indexOf("div class=\"panel-heading\"><i class=\"fas fa-medal\"></i> Top Customer</div>"), storeScrape.indexOf("</div><div id=\"js-payments\" class=\"panel panel-default module\">")));

            String nameData = rawData.substring(rawData.indexOf("<div class=\"ign\">"), rawData.indexOf(" </div>\n" +
                    "                <div class=\"amount\">"));

            String amountData = rawData.substring(rawData.indexOf("<div class=\"amount\">"), rawData.indexOf("<small>EUR</small>"));

            nameData = nameData.replace("<div class=\"ign\">", "");
            nameData=nameData.replace("\n" +
                    "<div class=\"amount\">", "");
            amountData = amountData.replace(" ", "");
            amountData = amountData.replace("Donated", "");
            amountData = amountData.replace("<divclass=\"amount\">\n", "");

            cache.update(nameData+"|||"+amountData);
        }, 3600000L);

        Logger.good("Caching data finished");
    }


}
