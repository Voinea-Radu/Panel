package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.dto.entry.DatabaseEntry;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.utils.Utils;

public abstract class FrontEndData extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "user")
    public String user;
    @DatabaseField(columnName = "timestamp")
    public Long timestamp;
    @DatabaseField(columnName = "notify")
    public boolean notify;

    public FrontEndData(DatabaseMain main, String user, Long timestamp) {
        super(main);
        this.user = user;
        this.timestamp = timestamp;
        this.notify = false;
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
        return "/" + getBaseUrl() + "?id=" + getID();
    }

    public String getFullURL() {
        return Main.instance.config.baseURL + "/" + getBaseUrl() + "?id=" + getID();
    }

    public abstract String getBaseUrl();

    public void sendNotification() {
        this.notify = true;
        save();
    }

    public void readNotification() {
        this.notify = false;
        save();
    }

    public boolean hasNotification() {
        return notify;
    }

}
