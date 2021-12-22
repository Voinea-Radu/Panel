package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.originalpanel.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public abstract class FrontEndData {

    public String url;
    public int id;
    public Long timestamp;
    public String user;

    @SuppressWarnings("unused")
    public String getTimestampDate() {
        return Utils.millisecondsToDate(timestamp);
    }

    @SuppressWarnings("unused")
    public String getURL() {
        return "/" + url + "?id=" + id;
    }
}
