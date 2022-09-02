package dev.lightdream.originalpanel.dto;

import dev.lightdream.originalpanel.dto.data.ApplyData;
import dev.lightdream.originalpanel.dto.data.BugsData;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.dto.data.UnbanData;

public class DriverConfig extends dev.lightdream.databasemanager.dto.DriverConfig {

    public DriverConfig() {
        MYSQL.dataTypes.put(ComplainData.ComplainStatus.class, "TEXT");
        MYSQL.dataTypes.put(ComplainData.ComplainDecision.class, "TEXT");
        MYSQL.dataTypes.put(UnbanData.UnbanStatus.class, "TEXT");
        MYSQL.dataTypes.put(UnbanData.UnbanDecision.class, "TEXT");
        MYSQL.dataTypes.put(BugsData.BugStatus.class, "TEXT");
        MYSQL.dataTypes.put(ApplyData.ApplyDecision.class, "TEXT");
        MYSQL.dataTypes.put(ApplyData.ApplyStatus.class, "TEXT");
    }

}
