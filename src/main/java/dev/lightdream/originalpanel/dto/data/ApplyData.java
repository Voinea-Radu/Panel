package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ApplyData {

    public enum ApplyStatus {
        OPEN("Awaiting staff response"),
        CLOSED("Closed");

        public final String message;

        ApplyStatus(String message) {
            this.message = message;
        }
    }

    public enum EnglishKnowledge {
        NONE, DECENT, FLUENT
    }

    public enum ApplyDecision {
        UNANSWERED, APPROVED, DENIED
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyCreateData {

        public String cookie;
        public int age;
        public String section;
        public String playTime;
        public Long discordID;
        public String sanctions;
        public String english;
        public String commands;
        public String why;
        public Long timestamp;
        public ApplyStatus status;
        public int warns;
        public int bans;
        public int mutes;
        public int kicks;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyDecisionData {

        public String cookie;
        public String decision;
        public int id;

    }


}
