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
    @DatabaseField(columnName = "english_check")
    public String english;
    @DatabaseField(columnName = "important_commands")
    public String commands;
    @DatabaseField(columnName = "why_argument")
    public String why;
    @DatabaseField(columnName = "status")
    public ApplyData.ApplyStatus status;
    @DatabaseField(columnName = "decision")
    public ApplyData.ApplyDecision decision;

    public Apply(ApplyData.ApplyCreateData data) {
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), data.timestamp);
        this.age = data.age;
        this.section = data.section;
        this.discord = data.discord;
        this.english = data.englishCheck;
        this.commands = data.importantCommands;
        this.why = data.whyArguments;
        this.status = data.status;
        this.decision = ApplyData.ApplyDecision.UNANSWERED;
    }

    @Override
    public String getBaseUrl() {
        return "apply";
    }

}
