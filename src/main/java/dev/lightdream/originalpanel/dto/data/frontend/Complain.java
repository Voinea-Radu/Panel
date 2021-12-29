package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasehandler.annotations.database.DatabaseField;
import dev.lightdream.databasehandler.annotations.database.DatabaseTable;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.utils.Utils;

@DatabaseTable(table = "complains")
public class Complain extends FrontEndData {

    @DatabaseField(columnName = "target")
    public String target;
    @DatabaseField(columnName = "section")
    public String section;
    @DatabaseField(columnName = "date_and_time")
    public String dateAndTime;
    @DatabaseField(columnName = "description")
    public String description;
    @DatabaseField(columnName = "proof")
    public String proof;
    @DatabaseField(columnName = "status")
    public ComplainData.ComplainStatus status;
    public String targetResponse;
    @DatabaseField(columnName = "decision")
    public ComplainData.ComplainDecision decision;

    public Complain(ComplainData.ComplainCreateData data) {
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), data.timestamp);
        this.target = data.target;
        this.section = data.section;
        this.dateAndTime = data.dateAndTime;
        this.description = data.description;
        this.proof = data.proof;
        this.status = data.status;
        this.targetResponse = data.targetResponse;
        this.decision = ComplainData.ComplainDecision.UNANSWERED;
    }

    public Complain() {
        super(Main.instance, "", 0L);
    }

    @Override
    public String getBaseUrl() {
        return "complain";
    }
}
