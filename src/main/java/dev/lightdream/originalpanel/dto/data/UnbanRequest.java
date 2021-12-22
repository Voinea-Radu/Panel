package dev.lightdream.originalpanel.dto.data;

import dev.lightdream.originalpanel.utils.Utils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnbanRequest {

    public int id;
    public String user;
    public String staff;
    public String reason;
    public String dateAndTime;
    public String ban;
    public String argument;
    public UnbanData.UnbanStatus status;
    public Long timestamp;
    public UnbanData.UnbanDecision decision;

    public String getTimestampDate() {
        return Utils.millisecondsToDate(timestamp);
    }

    public String getComplainURL() {
        return "/unban?id=" + id;
    }

    public String getComplainURLFunction() {
        return "redirect('" + getComplainURL() + "')";
    }


}
