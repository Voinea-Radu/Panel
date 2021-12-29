package dev.lightdream.originalpanel.dto;

import java.util.List;
import java.util.Map;

public class Sanctity {

    public enum SanctityType {
        BAN, KICK, MUTE, WARN
    }

    private Map<SanctityType, Integer> SanctityMap;

    public Sanctity(List<SanctityData> dataList) {

        for(SanctityData data : dataList) {
            this.SanctityMap.put(data.getType(), data.getCount());
        }

    }

    @Override
    public String toString() {
        return "Bans: " + SanctityMap.get(SanctityType.BAN) +
                "\nKicks: " + SanctityMap.get(SanctityType.KICK) +
                "\nMutes: " + SanctityMap.get(SanctityType.MUTE) +
                "\nWarns: " + SanctityMap.get(SanctityType.WARN);
    }

}

class SanctityData {

    private final Sanctity.SanctityType type;
    private final int count;

    public SanctityData(Sanctity.SanctityType type, int count) {
        this.type = type;
        this.count = count;
    }

    public Sanctity.SanctityType getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
}
