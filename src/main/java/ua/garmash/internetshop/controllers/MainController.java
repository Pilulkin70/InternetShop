package ua.garmash.internetshop.controllers;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.service.CategoryService;
import ua.garmash.internetshop.service.ProductService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.UUID;

@Controller
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public MainController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

/*    @GetMapping({"", "/"})
    public ModelAndView getIndex(ModelAndView modelAndView) {
        if (modelAndView.getModel().get("myID") == null) {
            String uuid = UUID.randomUUID().toString();
            modelAndView.getModel().put("myID", uuid);
            System.out.println("Generated UUID -> " + uuid);
        }
        modelAndView.getModel().put("uuid", modelAndView.getModel().get("myID"));
        modelAndView.setViewName("index");
        return modelAndView;
    }*/

    @RequestMapping({"", "/"})
    public String index(HttpServletRequest httpServletRequest, Model model, HttpSession httpSession,
                        @PageableDefault(sort={"id"}, direction = Sort.Direction.ASC) Pageable pageable) {

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
/*        model.addAttribute("products", productService.getAll(pageable));
        model.addAttribute("categories", categoryService.findAll());*/
        return "index";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/aboutme")
    public String aboutme() {
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

    @GetMapping("/set-cookie")
    public void setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("data", "Come_to_the_dark_side");//создаем объект Cookie,
        //в конструкторе указываем значения для name и value
        cookie.setPath("/");//устанавливаем путь
        cookie.setMaxAge(86400);//здесь устанавливается время жизни куки
        response.addCookie(cookie);//добавляем Cookie в запрос
        response.setContentType("text/plain");//устанавливаем контекст
    }
}
