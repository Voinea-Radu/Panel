package dev.lightdream.originalpanel.managers;

import dev.lightdream.originalpanel.Main;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class DiscordManager {


    public void sendNotification(Long discordID, String url, String notification) {

        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.notificationEmbed
                                        .parse("notification", notification)
                                        .parse("url", url)
                                        .build().build())
                                .queue(null,
                                        new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                                e -> {
                                                }))));
    }

}
