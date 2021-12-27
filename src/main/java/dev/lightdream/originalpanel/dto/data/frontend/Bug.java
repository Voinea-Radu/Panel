package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasehandler.annotations.database.DatabaseField;
import dev.lightdream.databasehandler.annotations.database.DatabaseTable;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.data.BugsData;
import dev.lightdream.originalpanel.utils.Utils;

@DatabaseTable(table = "bugs")
public class Bug extends FrontEndData {

    @DatabaseField(columnName = "section")
    public String section;
    @DatabaseField(columnName = "description")
    public String description;
    @DatabaseField(columnName = "status")
    public BugsData.BugStatus status;

    public Bug(String user, Long timestamp, String section, String description, BugsData.BugStatus status) {
        super(Main.instance, user, "bug", timestamp);
        this.section = section;
        this.description = description;
        this.status = status;
    }

    public Bug() {
        super();
    }

    public Bug(BugsData.BugCreateData data) {
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), "bug", data.timestamp);
        this.section = data.section;
        this.description = data.description;
        this.status = data.status;
    }
}
