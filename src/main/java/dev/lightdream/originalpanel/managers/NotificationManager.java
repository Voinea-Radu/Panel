package dev.lightdream.originalpanel.managers;

import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.data.frontend.*;

public class NotificationManager {

    public NotificationManager() {

    }

    public void notifyUser(FrontEndData data, String username) {
        notifyUser(data, username, false);

    }

    public void notifyUser(FrontEndData data, String username, boolean target) {
        String uuid = Main.instance.databaseManager.getPlayerUUID(username);
        Long discordID = Main.instance.databaseManager.getDiscordID(uuid);

        notifyUser(data, discordID, target);
    }

    @SuppressWarnings("unused")
    public void notifyUser(FrontEndData data, Long discordID) {
        notifyUser(data, discordID, false);
    }

    public void notifyUser(FrontEndData data, Long discordID, boolean target) {
        //Send panel notification
        if (data instanceof Complain) {
            if (target) {
                ((Complain) data).sendTargetNotification();
            } else {
                data.sendNotification();
            }
        }

        //Send discord notification
        if (discordID == 0L) {
            return;
        }
        String notification = "";

        if (data instanceof Bug) {
            notification = "Bug";
        } else if (data instanceof UnbanRequest) {
            notification = "Unban";
        } else if (data instanceof Apply) {
            notification = "Apply";
        } else if (data instanceof Complain) {
            notification = "Complain";
        }

        Main.instance.discordManager.sendNotification(discordID, data.getFullURL(), notification);
    }


}
