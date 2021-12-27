package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasehandler.DatabaseMain;
import dev.lightdream.databasehandler.annotations.database.DatabaseField;
import dev.lightdream.databasehandler.dto.DatabaseEntry;
import dev.lightdream.originalpanel.utils.Utils;

public abstract class FrontEndData extends DatabaseEntry {

    @DatabaseField(columnName = "user")
    public String user;

    public String url;
    public Long timestamp;

    public FrontEndData(DatabaseMain main, String user, String url, Long timestamp) {
        super(main);
        this.user = user;
        this.url = url;
        this.timestamp = timestamp;
    }

    public FrontEndData() {
        super(null);
    }

    @SuppressWarnings("unused")
    public String getTimestampDate() {
        return Utils.millisecondsToDate(timestamp);
    }

    @SuppressWarnings("unused")
    public String getURL() {
        return "/" + url + "?id=" + id;
    }
}
