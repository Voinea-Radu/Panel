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

    @SuppressWarnings("unused")
    public enum EnglishKnowledge {
        NONE, DECENT, FLUENT
    }

    @SuppressWarnings("unused")
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

        public void clean() {
            playTime = playTime.replace("\"", "");
            english = english.replace("\"", "");
            why = why.replace("\"", "");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyDecisionData {
        public String cookie;
        public String decision;
        public int id;

        //All args constructor and no args constructor

    }


}
