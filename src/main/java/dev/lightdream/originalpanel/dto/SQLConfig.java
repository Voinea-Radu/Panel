package dev.lightdream.originalpanel.dto;

import dev.lightdream.originalpanel.dto.data.ApplyData;
import dev.lightdream.originalpanel.dto.data.BugsData;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.dto.data.UnbanData;

import java.util.HashMap;

public class SQLConfig extends dev.lightdream.databasemanager.dto.SQLConfig {

    public SQLConfig() {
        super(new HashMap<>() {{
            put(ComplainData.ComplainStatus.class, "TEXT");
            put(ComplainData.ComplainDecision.class, "TEXT");
            put(UnbanData.UnbanStatus.class, "TEXT");
            put(UnbanData.UnbanDecision.class, "TEXT");
            put(BugsData.BugStatus.class, "TEXT");
            put(ApplyData.ApplyDecision.class, "TEXT");
            put(ApplyData.ApplyStatus.class, "TEXT");
        }});
    }


}
