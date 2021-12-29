package dev.lightdream.originalpanel.dto.data;

import dev.lightdream.originalpanel.dto.Sanctity;
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
        public Sanctity sanctity;
        public String englishCheck;
        public String importantCommands;
        public String whyArguments;
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