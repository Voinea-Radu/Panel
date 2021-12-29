package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasehandler.annotations.database.DatabaseField;
import dev.lightdream.databasehandler.annotations.database.DatabaseTable;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.data.UnbanData;
import dev.lightdream.originalpanel.utils.Utils;

@DatabaseTable(table = "unbans")
public class UnbanRequest extends FrontEndData {

    @DatabaseField(columnName = "staff")
    public String staff;
    @DatabaseField(columnName = "reason")
    public String reason;
    @DatabaseField(columnName = "date_and_time")
    public String dateAndTime;
    @DatabaseField(columnName = "ban")
    public String ban;
    @DatabaseField(columnName = "argument")
    public String argument;
    @DatabaseField(columnName = "status")
    public UnbanData.UnbanStatus status;
    @DatabaseField(columnName = "decision")
    public UnbanData.UnbanDecision decision;

    public UnbanRequest(UnbanData.UnbanCreateData data) {
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), data.timestamp);
        this.staff = data.staff;
        this.reason = data.reason;
        this.dateAndTime = data.dateAndTime;
        this.ban = data.ban;
        this.argument = data.argument;
        this.status = data.status;
        this.decision = UnbanData.UnbanDecision.UNANSWERED;
    }

    @SuppressWarnings("unused")
    public UnbanRequest() {
        super(Main.instance, "", 0L);
    }

    @Override
    public String getBaseUrl() {
        return "unban";
    }
}
