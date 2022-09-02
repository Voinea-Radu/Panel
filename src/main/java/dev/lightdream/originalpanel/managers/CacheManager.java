package dev.lightdream.originalpanel.managers;

import dev.lightdream.logger.Logger;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.Cache;
import dev.lightdream.originalpanel.dto.Staff;
import dev.lightdream.originalpanel.dto.TopDonator;
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
import java.util.List;

public class CacheManager {

    public Main main;
    public Cache<Integer> onlinePlayers;
    public Cache<Integer> registeredPlayersCount;
    public Cache<Integer> donorsCount;
    public Cache<List<Staff>> staffs;
    public Cache<Integer> donationsGoal;
    public Cache<TopDonator> topDonator;

    @SuppressWarnings("TextBlockMigration")
    public CacheManager(Main main) {
        Logger.good("Started caching");
        this.main = main;

        onlinePlayers = new Cache<>(cache -> {

            MinecraftServerStatus server = MinecraftServerPinger.ping("original.gg");

            if (server.isOffline()) {
                cache.update(1);
                return;
            }

            cache.update(server.getPlayers().getOnline());
        }, 60000L); //60 seconds

        registeredPlayersCount = new Cache<>(cache -> cache.update(Main.instance.databaseManager.getRegisteredCount()), 1800000L); // 30 minutes

        donorsCount = new Cache<>(cache -> cache.update(Main.instance.databaseManager.getDonorsCount()), 1800000L); //30 minutes

        staffs = new Cache<>(cache -> cache.update(Main.instance.databaseManager.getStaff()), 43200000L); // 12 hours

        new Cache<>(cache -> Main.instance.databaseManager.getAll(Complain.class).forEach(complain -> {
            // 7 days
            if (System.currentTimeMillis() > complain.timestamp + 604800000L &&
                    complain.status == ComplainData.ComplainStatus.OPEN_AWAITING_TARGET_RESPONSE) {
                Main.instance.notificationManager.notifyUser(complain, complain.user);
                Main.instance.notificationManager.notifyUser(complain, complain.target, true);
                complain.status = ComplainData.ComplainStatus.OPEN_AWAITING_STAFF_APPROVAL;
                complain.save();
            }
        }), 43200000L); // 12 hours

        donationsGoal = new Cache<>(cache -> {
            StringBuilder storeScrape = new StringBuilder();

            URLConnection connection;
            try {
                connection = new URL("https://store.original.gg").openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
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
                cache.update(0);
                return;
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

        topDonator = new Cache<>(cache -> {
            StringBuilder storeScrape = new StringBuilder();

            URLConnection connection;
            try {
                connection = new URL("https://store.original.gg").openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
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
                cache.update(new TopDonator(
                        "", 0
                ));
                return;
            }

            String rawData = (storeScrape.substring(storeScrape.indexOf("div class=\"panel-heading\"><i class=\"fas fa-medal\"></i> Top Customer</div>"),
                    storeScrape.indexOf("</div><div id=\"js-payments\" class=\"panel panel-default module\">")));

            String nameData = rawData.substring(rawData.indexOf("<div class=\"ign\">"),
                    rawData.indexOf(" </div>\n                <div class=\"amount\">"));

            String amountData = rawData.substring(rawData.indexOf("<div class=\"amount\">"), rawData.indexOf("<small>EUR</small>"));

            nameData = nameData.replace("<div class=\"ign\">", "");
            nameData = nameData.replace("\n" +
                    "<div class=\"amount\">", "");
            amountData = amountData.replace(" ", "");
            amountData = amountData.replace("Donated", "");
            amountData = amountData.replace("<divclass=\"amount\">\n", "");

            TopDonator topDonator = new TopDonator(
                    nameData, Double.parseDouble(amountData)
            );
            cache.update(topDonator);
        }, 3600000L);

        Logger.good("Caching data finished");
    }


}
