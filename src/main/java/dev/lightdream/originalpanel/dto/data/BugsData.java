package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class BugsData {

    @SuppressWarnings("unused")
    public enum BugStatus {

        OPEN("Awaiting staff review"), CLOSED("Closed");

        public final String message;

        BugStatus(String message) {
            this.message = message;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BugCreateData {

        public String cookie;
        public String section;
        public String description;
        public Long timestamp;
        public BugStatus status;

        public void clean() {
            section = section.replace("\"", "");
            description = description.replace("\"", "");
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BugCloseData {

        public String cookie;
        public int id;

    }
}
