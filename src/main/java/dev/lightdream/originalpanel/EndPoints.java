package dev.lightdream.originalpanel;

import dev.lightdream.logger.Debugger;
import dev.lightdream.originalpanel.dto.data.PlayerProfile;
import dev.lightdream.originalpanel.dto.data.frontend.Bug;
import dev.lightdream.originalpanel.dto.data.frontend.Complain;
import dev.lightdream.originalpanel.dto.data.frontend.UnbanRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {
    @GetMapping("/")
    public String indexWithMessage(Model model, String message) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        model.addAttribute("donors_count", Main.instance.cacheManager.donorsCount.get());
        model.addAttribute("registered_count", Main.instance.cacheManager.registeredPlayersCount.get());
        model.addAttribute("online_players_count", Main.instance.cacheManager.onlinePlayers.get());
        model.addAttribute("message", message);

        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        return "login.html";
    }

    @GetMapping("/staff")
    public String staff(Model model) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        model.addAttribute("staffs", Main.instance.cacheManager.staffs.get());
        return "staff.html";
    }

    @GetMapping("/rules")
    public String rules() {
        return "server-rules.html";
    }

    @GetMapping("/complain")
    public String complain(Model model, Integer id) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        if (id == null) {
            return "complaints.html";
        }

        Complain complain = Main.instance.databaseManager.getComplain(id);

        if (complain == null) {
            return "404.html";
        }

        model.addAttribute("complain", complain);

        return "complaints-details.html";
    }

    @GetMapping("/profile")
    public String profile(Model model, String user) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        PlayerProfile profile = new PlayerProfile(user);
        model.addAttribute("profile", profile);
        Debugger.info(profile);
        return "user.html";
    }

    @GetMapping("/unauthorised")
    public String unauthorised() {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        return "401.html";
    }

    @GetMapping("/401")
    public String unauthorised_401() {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        return "401.html";
    }

    @GetMapping("/notfound")
    public String notFound() {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        return "404.html";
    }

    @GetMapping("/404")
    public String notFound_404() {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        return "404.html";
    }

    @GetMapping("/entries")
    public String entries(Model model) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        model.addAttribute("complaints", Main.instance.databaseManager.getComplains());
        model.addAttribute("unbans", Main.instance.databaseManager.getUnbans());
        model.addAttribute("bugs", Main.instance.databaseManager.getBugs());

        return "entries.html";
    }

    @GetMapping("/unban")
    public String unban(Model model, Integer id) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        if (id == null) {
            return "unban.html";
        }

        UnbanRequest unbanRequest = Main.instance.databaseManager.getUnbanRequest(id);

        if (unbanRequest == null) {
            return "404.html";
        }

        model.addAttribute("unbanRequest", unbanRequest);

        return "unban-details.html";
    }

    @GetMapping("/bug")
    public String bugs(Model model, Integer id) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }

        if (id == null) {
            return "bug.html";
        }

        Bug bug = Main.instance.databaseManager.getBug(id);

        if (bug == null) {
            return "404.html";
        }

        model.addAttribute("bug", bug);

        return "bug-details.html";
    }
}


