package dev.lightdream.originalpanel.dto.data;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Response {

    public String code;
    public String messageEn;
    public String messageRo;
    public String data;

    public Response(String code, String messageEn, String messageRo) {
        this.code = code;
        this.messageEn = messageEn;
        this.messageRo = messageRo;
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static Response BAD_CREDENTIALS_401() {
        return new Response("401", "Bad Credentials", "Credentiale Invalide");
    }

    @SuppressWarnings("SpellCheckingInspection")
    public static Response INVALID_ENTRY_422() {
        return new Response("422", "Invalid entry", "Intrare invalida");
    }

    public static Response OK_200(String data) {
        return new Response("200", "OK", "OK", data);
    }

    public static Response OK_200() {
        return new Response("200", "OK", "OK");
    }
}

