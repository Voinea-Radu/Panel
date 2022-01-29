package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.logger.Debugger;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.data.ApplyData;
import dev.lightdream.originalpanel.utils.Utils;

import java.util.concurrent.atomic.AtomicReference;

@DatabaseTable(table = "applies")
public class Apply extends FrontEndData {

    @DatabaseField(columnName = "age")
    public int age;
    @DatabaseField(columnName = "section")
    public String section;
    @DatabaseField(columnName = "discord")
    public Long discordID;
    @DatabaseField(columnName = "english")
    public String english;
    @DatabaseField(columnName = "commands")
    public String commands;
    @DatabaseField(columnName = "argument")
    public String argument;
    @DatabaseField(columnName = "status")
    public ApplyData.ApplyStatus status;
    @DatabaseField(columnName = "decision")
    public ApplyData.ApplyDecision decision;
    @DatabaseField(columnName = "bans")
    public int bans;
    @DatabaseField(columnName = "kicks")
    public int kicks;
    @DatabaseField(columnName = "warns")
    public int warns;
    @DatabaseField(columnName = "mutes")
    public int mutes;
    @DatabaseField(columnName = "play_time")
    public String playTime;

    public Apply(ApplyData.ApplyCreateData data) {
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), data.timestamp);
        this.age = data.age;
        this.section = data.section;
        this.discordID = data.discordID;
        this.english = data.english;
        this.commands = data.commands;
        this.argument = data.why;
        this.status = data.status;
        this.decision = ApplyData.ApplyDecision.UNANSWERED;
        this.bans = data.bans;
        this.kicks = data.kicks;
        this.warns = data.warns;
        this.mutes = data.mutes;
        this.playTime = data.playTime;
    }

    @SuppressWarnings("unused")
    public Apply() {

    }

    @Override
    public String getBaseUrl() {
        return "apply";
    }

    @SuppressWarnings("unused")
    public String getDiscordTag() {
        AtomicReference<String> tag = new AtomicReference<>(null);
        if (discordID == 0L) {
            tag.set("Not Linked");
        } else {
            Main.instance.bot.retrieveUserById(discordID).queue(user -> {
                if (user == null) {
                    tag.set("Not Linked");
                    return;
                }
                tag.set(user.getAsTag());
            });
        }

        //Awaiting the discord username to be retrieved from discord API
        int cycles = 0;
        while (tag.get() == null) {
            cycles++;
            if (cycles > 400000000) {
                Debugger.info("Break because of timeout");
                tag.set("Not Loaded");
                break;
            }
        }
        return tag.get() == null ? discordID.toString() : tag.get();
    }
}
