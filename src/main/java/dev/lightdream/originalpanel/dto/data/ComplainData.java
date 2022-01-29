package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ComplainData {

    public enum ComplainStatus {

        OPEN_AWAITING_TARGET_RESPONSE("Awaiting target response"), OPEN_AWAITING_STAFF_APPROVAL("Awaiting staff approval"), @SuppressWarnings("unused") CLOSED("Closed");

        public final String message;

        ComplainStatus(String message) {
            this.message = message;
        }

    }

    @SuppressWarnings("unused")
    public enum ComplainDecision {

        UNANSWERED, APPROVED, DENIED

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplainCreateData {

        public String cookie;
        public String target;
        public String section;
        public String dateAndTime;
        public String description;
        public String proof;
        public ComplainStatus status;
        public String targetResponse;
        public Long timestamp;

        public void clean() {
            target = target.replace("\"", "");
            section = section.replace("\"", "");
            dateAndTime = dateAndTime.replace("\"", "");
            proof = proof.replace("\"", "");
            targetResponse = targetResponse.replace("\"", "");
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplainTargetResponseData {

        public String cookie;
        public int id;
        public String targetResponse;

    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplainDecisionData {
        public String cookie;
        public String decision;
        public int id;

    }


}
