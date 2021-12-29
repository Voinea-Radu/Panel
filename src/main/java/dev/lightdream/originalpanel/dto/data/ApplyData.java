package dev.lightdream.originalpanel.dto.data;

import dev.lightdream.originalpanel.dto.Sanctions;
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
        public int hours;
        public String discord;
        public Sanctions sanctions;
        public String english;
        public String commands;
        public String why;
        public Long timestamp;
        public ApplyStatus status;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplyDecisionData {

        public String cookie;
        public String decision;
        public int id;

    }


}
