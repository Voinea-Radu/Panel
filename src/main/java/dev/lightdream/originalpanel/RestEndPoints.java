package dev.lightdream.originalpanel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import dev.lightdream.logger.Debugger;
import dev.lightdream.originalpanel.dto.Staff;
import dev.lightdream.originalpanel.dto.data.*;
import dev.lightdream.originalpanel.dto.data.frontend.*;
import dev.lightdream.originalpanel.utils.Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController()
public class RestEndPoints {

    public List<String> bugsStaff = Arrays.asList("h-manager", "owner");

    public List<String> complainStaff = Arrays.asList("admin", "sradmin", "operator", "supervizor", "manager", "h-manager", "owner");

    public List<String> unbanStaff = Arrays.asList("srmod", "admin", "sradmin", "operator", "supervizor", "manager", "h-manager", "owner");

    public List<String> applyStaff = Arrays.asList("manager", "h-manager", "owner");

    public RestEndPoints() {

    }

    @PostMapping("/api/login/v2")
    public @ResponseBody
    Response login(@RequestBody String dataStream) {
        Debugger.info("Login attempt with dataStream: " + dataStream);

        LoginData data;

        try {
            data = new Gson().fromJson(dataStream, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        if (data == null) {
            return Response.BAD_CREDENTIALS_401();
        }

        if (!Main.instance.rateLimiter.attemptLogin(data.username)) {
            return Response.RATE_LIMITED_429();
        }

        if (!checkPassword(data)) {
            return Response.BAD_CREDENTIALS_401();
        }

        return Response.OK_200(Main.instance.databaseManager.getAuthMePassword(data.username));
    }

    @PostMapping("/api/login/validate")
    public @ResponseBody
    Response validateCookie(@RequestBody String dataStream) {
        Debugger.info(dataStream);
        LoginData data;
        try {
            data = new Gson().fromJson(dataStream, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }


        if (data == null || !data.password.equals(Main.instance.databaseManager.getAuthMePassword(data.username))) {
            return Response.BAD_CREDENTIALS_401();
        }

        return Response.OK_200();
    }

    @PostMapping("/api/form/complain")
    public @ResponseBody
    Response complain(@RequestBody ComplainData.ComplainCreateData data) {

        Debugger.info("Received complain");

        if (Main.instance.databaseManager.getRecentComplaints(Utils.getUsernameFromCookie(data.cookie)).size() != 0) {
            return Response.RATE_LIMITED_429();
        }

        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        if (!Main.instance.databaseManager.validateUser(data.target)) {
            return Response.INVALID_ENTRY_422();
        }

        data.status = ComplainData.ComplainStatus.OPEN_AWAITING_TARGET_RESPONSE;
        data.targetResponse = "";
        data.timestamp = System.currentTimeMillis();
        data.clean();

        Complain complain = new Complain(data);
        complain.save();
        Main.instance.notificationManager.notifyUser(Main.instance.databaseManager.getLastComplain(), data.target, true);

        return Response.OK_200();
    }

    @PostMapping("/api/form/complain-target-responde")
    public @ResponseBody
    Response complainTargetRespond(@RequestBody ComplainData.ComplainTargetResponseData data) {
        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        LoginData loginData;
        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        Complain complain = Main.instance.databaseManager.getComplain(data.id);

        if (complain == null) {
            return Response.INVALID_ENTRY_422();
        }

        if (loginData == null || !loginData.username.equalsIgnoreCase(complain.target)) {
            return Response.BAD_CREDENTIALS_401();
        }

        complain.targetResponse = data.targetResponse;
        complain.status = ComplainData.ComplainStatus.OPEN_AWAITING_STAFF_APPROVAL;

        complain.save();

        Main.instance.notificationManager.notifyUser(complain, loginData.username);

        return Response.OK_200();
    }

    public boolean checkPassword(String username, String password) {
        if (username == null || username.equals("") || password == null || password.equals("")) {
            return false;
        }
        try {
            return BCrypt.verifyer().verify(password.getBytes(StandardCharsets.UTF_8), Main.instance.databaseManager.getAuthMePassword(username).getBytes(StandardCharsets.UTF_8)).verified;
        } catch (Throwable t) {
            return false;
        }
    }

    public boolean checkPassword(LoginData data) {
        if (data == null) {
            return false;
        } else if (data.username == null || data.username.equals("") || data.password == null || data.password.equals("")) {
            return false;
        }
        return checkPassword(data.username, data.password);
    }

    @SuppressWarnings("RedundantIfStatement")
    @PostMapping("/api/check/staff")
    public Response checkStaff(String user, String useCase) {

        if (!Main.instance.isEnabled()) {
            return Response.INVALID_ENTRY_422();
        }

        @SuppressWarnings("unchecked") List<Staff> staffs = (List<Staff>) Main.instance.cacheManager.staffs.get();


        if (staffs.stream().anyMatch(staff -> {
            if (staff.username.equalsIgnoreCase(user)) {
                if (useCase.equals("complain")) {
                    return complainStaff.contains(staff.rank);
                }
                if (useCase.equals("unban")) {
                    return unbanStaff.contains(staff.rank);
                }
                if (useCase.equals("bug")) {
                    return bugsStaff.contains(staff.rank);
                }
                if (useCase.equals("apply")) {
                    return applyStaff.contains(staff.rank);
                }
                if (useCase.equals("any")) {
                    return true;
                }

                return false;
            }
            return false;
        })) {
            return Response.OK_200();
        }

        return Response.BAD_CREDENTIALS_401();
    }

    @PostMapping("/api/update/form/complain")
    public Response changeComplainStatus(@RequestBody ComplainData.ComplainDecisionData data) {
        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        LoginData loginData;
        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        Complain complain = Main.instance.databaseManager.getComplain(data.id);

        if (complain == null) {
            return Response.INVALID_ENTRY_422();
        }

        if (loginData == null || !checkStaff(loginData.username, "complain").code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }


        complain.status = ComplainData.ComplainStatus.CLOSED;
        complain.decision = ComplainData.ComplainDecision.valueOf(data.decision);

        Main.instance.notificationManager.notifyUser(complain, complain.user);
        Main.instance.notificationManager.notifyUser(complain, complain.target, true);

        return Response.OK_200();
    }

    @PostMapping("/api/form/unban")
    public @ResponseBody
    Response unban(@RequestBody UnbanData.UnbanCreateData data) {

        if (Main.instance.databaseManager.getRecentUnbanRequests(Utils.getUsernameFromCookie(data.cookie)).size() != 0) {
            return Response.RATE_LIMITED_429();
        }

        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        if (!Main.instance.databaseManager.validateUser(data.staff)) {
            return Response.INVALID_ENTRY_422();
        }

        data.status = UnbanData.UnbanStatus.OPEN;
        data.timestamp = System.currentTimeMillis();
        data.clean();

        new UnbanRequest(data).save();

        return Response.OK_200();
    }

    @PostMapping("/api/update/form/unban")
    public Response changeUnbanStatus(@RequestBody UnbanData.UnbanDecisionData data) {
        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        LoginData loginData;
        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        UnbanRequest unban = Main.instance.databaseManager.getUnbanRequest(data.id);

        if (unban == null) {
            return Response.INVALID_ENTRY_422();
        }

        if (loginData == null || !checkStaff(loginData.username, "unban").code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        unban.status = UnbanData.UnbanStatus.CLOSED;
        unban.decision = UnbanData.UnbanDecision.valueOf(data.decision);
        unban.save();
        Main.instance.notificationManager.notifyUser(unban, unban.user);

        return Response.OK_200();
    }

    @PostMapping("/api/form/bugs")
    public @ResponseBody
    Response bugs(@RequestBody BugsData.BugCreateData data) {

        if (Main.instance.databaseManager.getRecentBugs(Utils.getUsernameFromCookie(data.cookie)).size() != 0) {
            return Response.RATE_LIMITED_429();
        }

        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        data.status = BugsData.BugStatus.OPEN;
        data.timestamp = System.currentTimeMillis();
        data.clean();

        new Bug(data).save();

        return Response.OK_200();
    }

    @PostMapping("/api/update/form/bug")
    public Response closeBug(@RequestBody BugsData.BugCloseData data) {
        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        LoginData loginData;
        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        Bug bug = Main.instance.databaseManager.getBug(data.id);

        if (bug == null) {
            return Response.INVALID_ENTRY_422();
        }

        if (loginData == null || !checkStaff(loginData.username, "bug").code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        bug.status = BugsData.BugStatus.CLOSED;
        Main.instance.notificationManager.notifyUser(bug, loginData.username);
        return Response.OK_200();
    }

    @PostMapping("/api/form/apply")
    public @ResponseBody
    Response apply(@RequestBody ApplyData.ApplyCreateData data) {

        if (Main.instance.databaseManager.getRecentApplications(Utils.getUsernameFromCookie(data.cookie)).size() != 0) {
            return Response.RATE_LIMITED_429();
        }

        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        PlayerProfile profile = new PlayerProfile(Utils.getUsernameFromCookie(data.cookie));

        data.status = ApplyData.ApplyStatus.OPEN;
        data.timestamp = System.currentTimeMillis();
        data.playTime = profile.playTime;
        data.discordID = profile.discordID;
        data.bans = profile.bans;
        data.kicks = profile.kicks;
        data.mutes = profile.mutes;
        data.warns = profile.warns;

        data.clean();

        new Apply(data).save();

        return Response.OK_200();
    }

    @PostMapping("/api/update/form/apply")
    public Response changeApplyStatus(@RequestBody ApplyData.ApplyDecisionData data) {
        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        LoginData loginData;
        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        Apply apply = Main.instance.databaseManager.getApplication(data.id);

        if (apply == null) {
            return Response.INVALID_ENTRY_422();
        }

        if (loginData == null || !checkStaff(loginData.username, "unban").code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        apply.status = ApplyData.ApplyStatus.CLOSED;
        apply.decision = ApplyData.ApplyDecision.valueOf(data.decision);
        Main.instance.notificationManager.notifyUser(apply, loginData.username);
        apply.save();

        return Response.OK_200();
    }

    @PostMapping("/api/getNotifications")
    public List<Notification> getNotifications(String username) {
        return Main.instance.databaseManager.getNotifications(username);
    }

    @PostMapping("/api/read")
    public Response readNotification(String type, @RequestBody ReadData data) {
        return switch (type) {
            case "apply" -> readNotification(Apply.class, data);
            case "bug" -> readNotification(Bug.class, data);
            case "complain-target" -> readNotification(Complain.class, data, true);
            case "complain-user" -> readNotification(Complain.class, data, false);
            case "unban" -> readNotification(UnbanRequest.class, data);
            default -> Response.INVALID_ENTRY_422();
        };

    }

    private <T extends FrontEndData> Response readNotification(Class<T> clazz, ReadData data) {
        return readNotification(clazz, data, false);
    }

    @SuppressWarnings("unchecked")
    private <T extends FrontEndData> Response readNotification(Class<T> clazz, ReadData data, boolean target) {

        T item;
        if (Bug.class.equals(clazz)) {
            item = (T) Main.instance.databaseManager.getBug(data.id);
        } else if (Apply.class.equals(clazz)) {
            item = (T) Main.instance.databaseManager.getApplication(data.id);
        } else if (Complain.class.equals(clazz)) {
            item = (T) Main.instance.databaseManager.getComplain(data.id);
        } else if (UnbanRequest.class.equals(clazz)) {
            item = (T) Main.instance.databaseManager.getUnbanRequest(data.id);
        } else {
            return Response.INVALID_ENTRY_422();
        }

        if (!item.hasNotification()) {
            return Response.NOT_FOUND_404();
        }

        if (!validateCookie(data.cookie).code.equals("200")) {
            return Response.BAD_CREDENTIALS_401();
        }

        LoginData loginData;
        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.class);
        } catch (Throwable t) {
            return Response.BAD_CREDENTIALS_401();
        }

        if (!item.user.equals(loginData.username)) {
            return Response.INVALID_ENTRY_422();
        }

        if (target && Complain.class.equals(clazz)) {
            ((Complain) item).readTargetNotification();
        } else {
            item.readNotification();
        }

        return Response.OK_200();
    }


}
