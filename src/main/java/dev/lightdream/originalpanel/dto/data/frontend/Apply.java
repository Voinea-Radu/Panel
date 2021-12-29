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
    public String discord;
    @DatabaseField(columnName = "english")
    public String english;
    @DatabaseField(columnName = "commands")
    public String commands;
    @DatabaseField(columnName = "argument")
    public String why;
    @DatabaseField(columnName = "status")
    public ApplyData.ApplyStatus status;
    @DatabaseField(columnName = "decision")
    public ApplyData.ApplyDecision decision;

    public Apply(String user, Long timestamp, int age, String section, String discord, String english, String commands, String why, ApplyData.ApplyStatus status, ApplyData.ApplyDecision decision) {
        super(Main.instance, user, timestamp);
        this.age = age;
        this.section = section;
        this.discord = discord;
        this.english = english;
        this.commands = commands;
        this.why = why;
        this.status = status;
        this.decision = decision;
    }

    public Apply(ApplyData.ApplyCreateData data){
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), data.timestamp);
        this.age= data.age;
        this.section = data.section;
        this.discord = data.discordID;
        this.english = data.english;
        this.commands = data.commands;
        this.why = data.why;
        this.status = data.status;
        this.decision = ApplyData.ApplyDecision.UNANSWERED;
    }

    @Override
    public String getBaseUrl() {
        return "apply";
    }

}
