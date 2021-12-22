package dev.lightdream.originalpanel.dto.data.frontend;

import dev.lightdream.originalpanel.dto.data.UnbanData;
import dev.lightdream.originalpanel.utils.Utils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnbanRequest extends FrontEndData {

    public String staff;
    public String reason;
    public String dateAndTime;
    public String ban;
    public String argument;
    public UnbanData.UnbanStatus status;
    public UnbanData.UnbanDecision decision;


    public UnbanRequest(int id, Long timestamp, String user, String staff, String reason, String dateAndTime, String ban, String argument, UnbanData.UnbanStatus status, UnbanData.UnbanDecision decision) {
        super("unban",id, timestamp, user);
        this.staff = staff;
        this.reason = reason;
        this.dateAndTime = dateAndTime;
        this.ban = ban;
        this.argument = argument;
        this.status = status;
        this.decision = decision;
    }
}
