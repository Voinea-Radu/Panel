package dev.lightdream.originalpanel.dto.data;


import dev.lightdream.originalpanel.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Complain{

    public int id;
    public String user;
    public String target;
    public String section;
    public String dateAndTime;
    public String description;
    public String proof;
    public ComplainData.ComplainStatus status;
    public String targetResponse;
    public Long timestamp;

    public Complain(int id, String user, String target, String section, String dateAndTime, String description, String proof, ComplainData.ComplainStatus status, String targetResponse, Long timestamp) {
        this.id = id;
        this.user = user;
        this.target = target;
        this.section = section;
        this.dateAndTime = dateAndTime;
        this.description = description;
        this.proof = proof;
        this.status = status;
        this.targetResponse = targetResponse;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Complain{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", target='" + target + '\'' +
                ", section='" + section + '\'' +
                ", dateAndTime='" + dateAndTime + '\'' +
                ", description='" + description + '\'' +
                ", proof='" + proof + '\'' +
                ", status=" + status +
                ", targetResponse='" + targetResponse + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String getTimestampDate() {
        return Utils.millisecondsToDate(timestamp);
    }

    public String getComplainURL(){
        return "/complain?id="+id;
    }

    public String getComplainURLFunction(){
        return "redirect('/complain?id="+id + "')";
    }

}
