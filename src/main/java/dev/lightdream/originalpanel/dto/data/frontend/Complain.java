package dev.lightdream.originalpanel.dto.data.frontend;


import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Complain extends FrontEndData{

    public String target;
    public String section;
    public String dateAndTime;
    public String description;
    public String proof;
    public ComplainData.ComplainStatus status;
    public String targetResponse;
    public ComplainData.ComplainDecision decision;

    public Complain(int id, Long timestamp, String user, String target, String section, String dateAndTime, String description, String proof, ComplainData.ComplainStatus status, String targetResponse, ComplainData.ComplainDecision decision) {
        super("complain", id, timestamp, user);
        this.target = target;
        this.section = section;
        this.dateAndTime = dateAndTime;
        this.description = description;
        this.proof = proof;
        this.status = status;
        this.targetResponse = targetResponse;
        this.decision = decision;
    }
}
