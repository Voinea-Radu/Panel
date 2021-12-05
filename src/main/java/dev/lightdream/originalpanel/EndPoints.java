package dev.lightdream.originalpanel;

import dev.lightdream.originalpanel.utils.Debugger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EndPoints {

    /*
        <tr th:each="message : ${messages}">
            <td th:text="${message.id}">1</td>
            <td><a href="#" th:text="${message.title}">Title ...</a></td>
            <td th:text="${message.text}">Text ...</td>
        </tr>
     */

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("donors_count", Main.instance.cacheManager.donorsCount.get());
        model.addAttribute("registered_count", Main.instance.cacheManager.registeredPlayersCount.get());
        model.addAttribute("online_players_count", Main.instance.cacheManager.onlinePlayers.get());

        return "index.html";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login.html";
    }

    @GetMapping("/staff")
    public String staff(Model model) {
        model.addAttribute("staffs", Main.instance.cacheManager.staffs.get());
        return "staff.html";
    }

    @GetMapping("/rules")
    public String rules(Model model) {
        return "server-rules.html";
    }

    @GetMapping("/complain")
    public String complain(Model model) {
        return "complaints.html";
    }





}
