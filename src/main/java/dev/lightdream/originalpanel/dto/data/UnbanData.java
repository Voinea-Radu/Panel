package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class UnbanData {

    public enum UnbanStatus {

        OPEN("Awaiting staff response"),
        CLOSED("Closed");

        public String message;

        UnbanStatus(String message) {
            this.message = message;
        }

    }

    @SuppressWarnings("unused")
    public enum UnbanDecision {

        UNANSWERED,
        APPROVED,
        DENIED

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnbanCreateData {

        public String cookie;
        public String staff;
        public String reason;
        public String dateAndTime;
        public String ban;
        public String argument;
        public UnbanStatus status;
        public Long timestamp;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnbanDecisionData {

        public String cookie;
        public String decision;
        public int id;

    }


}
