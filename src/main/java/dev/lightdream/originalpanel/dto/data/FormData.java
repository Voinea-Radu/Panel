package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class FormData {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormDataRequest{

        public String cookie;
        public String target;
        public String section;
        public String dateAndTime;
        public String description;
        public String proof;

        public FormDataResponse respond(String response){
            return new FormDataResponse(cookie, target, section, dateAndTime, description, proof, response);
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormDataResponse{

        public String cookie;
        public String target;
        public String section;
        public String dateAndTime;
        public String description;
        public String proof;
        public String response;

        public static FormDataResponse error(String error){
            FormDataResponse output= new FormDataResponse();
            output.response=error;
            return output;
        }

    }

}
