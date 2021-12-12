package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ComplainData {

    public enum ComplainStatus {
        OPEN_AWAITING_TARGET_RESPONSE("Awaiting target response"),
        OPEN_AWAITING_STAFF_APPROVAL("Awaiting staff approval"),
        CLOSED("Closed");

        public String message;

        ComplainStatus(String message){
            this.message=message;
        }
    }

    public enum ComplainDecision {
        UNANSWERED,
        APPROVED,
        DENIED;
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

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplainTargetResponseData{
        public String cookie;
        public int id;
        public String targetResponse;

        @Override
        public String toString() {
            return "ComplainTargetResponseData{" +
                    "cookie='" + cookie + '\'' +
                    ", id=" + id +
                    ", targetResponse='" + targetResponse + '\'' +
                    '}';
        }
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComplainDecisionData {

        public String cookie;
        public String decision;
        public int id;

    }


}
