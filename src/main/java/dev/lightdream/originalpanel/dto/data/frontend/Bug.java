package dev.lightdream.originalpanel.dto.data.frontend;


import dev.lightdream.originalpanel.dto.data.BugsData;
import dev.lightdream.originalpanel.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Bug extends FrontEndData {

    public String section;
    public String description;
    public BugsData.BugStatus status;

    public Bug(int id, Long timestamp, String user, String section, String description, BugsData.BugStatus status) {
        super("bug", id, timestamp, user);
        this.section = section;
        this.description = description;
        this.status = status;
    }
}
