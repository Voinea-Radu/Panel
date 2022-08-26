package dev.lightdream.originalpanel.dto.data.frontend;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    public String textEn;
    public String textRo;
    public int id;
    public String baseURL;

    public static Notification of(Apply apply) {
        return new Notification(
                "Your application status has been changed",
                "Statusul aplicatiei tale a fost modificat",
                apply.id,
                apply.getBaseUrl()
        );
    }

    public static Notification of(Bug bug) {
        return new Notification(
                "Your bug report has been closed",
                "Raportul bugului tau a fost inchis",
                bug.id,
                bug.getBaseUrl()
        );
    }

    public static Notification of(Complain complain, boolean self) {
        if (self) {
            return new Notification(
                    "You have a new complain you need to respond to",
                    "Ai o noua plangere asupra careia trebuie sa raspunzi",
                    complain.id,
                    complain.getBaseUrl()
            );
        }
        return new Notification(
                "Your complain status has been updated",
                "Statusul plangerii tale a fost modificat",
                complain.id,
                complain.getBaseUrl()
        );
    }

    public static Notification of(UnbanRequest unban) {
        return new Notification(
                "Your unban request status has been updated",
                "Statusul cererii tale de unban a fost modificat",
                unban.id,
                unban.getBaseUrl()
        );
    }

    @SuppressWarnings("unused")
    public String getURL() {
        return "/" + baseURL + "?id=" + id;
    }


}
