package dev.lightdream.originalpanel;

import dev.lightdream.logger.Debugger;
import dev.lightdream.originalpanel.dto.data.PlayerProfile;
import dev.lightdream.originalpanel.dto.data.frontend.Apply;
import dev.lightdream.originalpanel.dto.data.frontend.Bug;
import dev.lightdream.originalpanel.dto.data.frontend.Complain;
import dev.lightdream.originalpanel.dto.data.frontend.UnbanRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@SuppressWarnings("SpringMVCViewInspection")
@Controller
public class EndPoints {
    @GetMapping("/")
    public String index(Model model, String message, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        model.addAttribute("donors_count", Main.instance.cacheManager.donorsCount.get());
        model.addAttribute("registered_count", Main.instance.cacheManager.registeredPlayersCount.get());
        model.addAttribute("online_players_count", Main.instance.cacheManager.onlinePlayers.get());
        model.addAttribute("donations_goal", Main.instance.cacheManager.donationsGoal.get());
        model.addAttribute("top_donator_name", Main.instance.cacheManager.topDonator.get().toString().split("\\|\\|\\|")[0]);
        model.addAttribute("top_donator_amount", Main.instance.cacheManager.topDonator.get().toString().split("\\|\\|\\|")[1]);
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
    public String staff(Model model, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        model.addAttribute("staffs", Main.instance.cacheManager.staffs.get());
        return "staff.html";
    }

    @GetMapping("/rules")
    public String rules(HttpServletRequest request) {
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }
        return "server-rules.html";
    }

    @GetMapping("/complain")
    public String complain(Model model, Integer id, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
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
    public String profile(Model model, String user, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        PlayerProfile profile = new PlayerProfile(user);

        model.addAttribute("profile", profile);
        Debugger.info(profile);
        return "user.html";
    }

    @GetMapping("/unauthorised")
    public String unauthorised(HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        return "401.html";
    }

    @GetMapping("/401")
    public String unauthorised_401(HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        return "401.html";
    }

    @GetMapping("/notfound")
    public String notFound(HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        return "404.html";
    }

    @GetMapping("/404")
    public String notFound_404(HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        return "404.html";
    }

    @GetMapping("/entries")
    public String entries(Model model, HttpServletRequest request, String type) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        if (type == null) {
            model.addAttribute("display", false);
            model.addAttribute("entries", new ArrayList<>());
            model.addAttribute("name", "None");
        } else {
            switch (type) {
                case "complains" -> {
                    model.addAttribute("display", true);
                    model.addAttribute("entries", Main.instance.databaseManager.getComplains());
                    model.addAttribute("name", "Complains");
                }
                case "unbans" -> {
                    model.addAttribute("display", true);
                    model.addAttribute("entries", Main.instance.databaseManager.getUnbans());
                    model.addAttribute("name", "Unbans");
                }
                case "bugs" -> {
                    model.addAttribute("display", true);
                    model.addAttribute("entries", Main.instance.databaseManager.getBugs());
                    model.addAttribute("name", "Bugs");
                }
                case "applies" -> {
                    model.addAttribute("display", true);
                    model.addAttribute("entries", Main.instance.databaseManager.getApplications());
                    model.addAttribute("name", "Applies");
                }
                default -> {
                    model.addAttribute("display", false);
                    model.addAttribute("entries", new ArrayList<>());
                    model.addAttribute("name", "None");
                }
            }
        }
        return "entries.html";
    }

    @GetMapping("/unban")
    public String unban(Model model, Integer id, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
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
    public String bugs(Model model, Integer id, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
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

    @GetMapping("/apply")
    public String apply(Model model, Integer id, HttpServletRequest request) {
        if (!Main.instance.isEnabled()) {
            return "starting.html";
        }
        if (Main.instance.maintenance(request.getHeader("X-FORWARDED-FOR"))) {
            return "maintenance.html";
        }

        if (id == null) {
            return "apply.html";
        }

        Apply apply = Main.instance.databaseManager.getApplication(id);

        if (apply == null) {
            return "404.html";
        }

        model.addAttribute("apply", apply);

        return "apply-details.html";
    }

}


