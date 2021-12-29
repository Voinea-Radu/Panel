package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasehandler.annotations.database.DatabaseField;
import dev.lightdream.databasehandler.annotations.database.DatabaseTable;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.data.ApplyData;
import dev.lightdream.originalpanel.utils.Utils;

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

    public Apply() {

    }

    @Override
    public String getBaseUrl() {
        return "apply";
    }

    public String getDiscordTag() {
        return "";
    }

}
