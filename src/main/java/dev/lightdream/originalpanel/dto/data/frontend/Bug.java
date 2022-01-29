package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
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

    public Bug() {
        super(Main.instance, "", 0L);
    }

    public Bug(BugsData.BugCreateData data) {
        super(Main.instance, Utils.getUsernameFromCookie(data.cookie), data.timestamp);
        this.section = data.section;
        this.description = data.description;
        this.status = data.status;
    }

    @Override
    public String getBaseUrl() {
        return "bug";
    }
}
