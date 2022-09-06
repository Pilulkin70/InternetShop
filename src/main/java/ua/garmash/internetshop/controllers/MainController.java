package ua.garmash.internetshop.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class MainController {

    public MainController() {
    }

    @RequestMapping({"", "/"})
    public String index(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession,
                        @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        if (httpSession.getAttribute("myID") == null) {
            String uuid = UUID.randomUUID().toString();
            httpSession.setAttribute("myID", uuid);
        }
        if (httpSession.getAttribute("myIP") == null) {
            String uip = httpServletRequest.getRemoteAddr();
            httpSession.setAttribute("myIP", uip);
        }
        model.addAttribute("uuid", httpSession.getAttribute("myID"));
        model.addAttribute("userIP", httpSession.getAttribute("myIP"));
        return "index";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/aboutme")
    public String aboutMe() {
        return "aboutme";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
