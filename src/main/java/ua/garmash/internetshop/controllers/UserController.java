package ua.garmash.internetshop.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.dto.UserDto;
import ua.garmash.internetshop.service.OrderService;
import ua.garmash.internetshop.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        System.out.println("Called method newUser");
        model.addAttribute("user", new UserDto());
//        return "user";
        return "register";
    }

    @PostMapping("/new")
    public String saveUser(UserDto dto, Model model) {
        if (userService.findByName(dto.getUsername()) != null) {
            throw new RuntimeException("User with login '" + dto.getUsername() + "' is already exist");
        }
        if (userService.save(dto)) {
            return "redirect:/login";
        } else {
            model.addAttribute("user", dto);
            return "user";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }

        UserDto userDto = userService.getUserDtoByName(principal.getName());
        List<OrderDto> ordersDto = orderService.getOrdersByUser(userDto.getUsername());
        model.addAttribute("user", userDto);
        model.addAttribute("orders", ordersDto);
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateProfileUser(UserDto dto, Model model, Principal principal) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            userService.updateProfile(dto);
            return "redirect:/users/" + dto.getId() + "/edit";
        }

        if (principal == null
                || !Objects.equals(principal.getName(), dto.getUsername())) {
            throw new RuntimeException("You are not authorize");
        }
        if (dto.getPassword() != null
                && !dto.getPassword().isEmpty()
                && !Objects.equals(dto.getPassword(), dto.getMatchingPassword())) {
            model.addAttribute("user", dto);
            return "profile";
        }

        userService.updateProfile(dto);
        return "redirect:/users/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/delete")
    public String delUser(@PathVariable Long id) {
        userService.delUserById(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, Model model, Principal principal) {
        UserDto userDto = userService.getUserDtoById(id);
        List<OrderDto> ordersDto = orderService.getOrdersByUser(userDto.getUsername());
        model.addAttribute("user", userDto);
        model.addAttribute("orders", ordersDto);
        return "profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id, UserDto dto, Model model, Principal principal) {
        if (dto.getId() != id) {
            throw new RuntimeException("User Id exception.");
        }
        if (dto.getPassword().isEmpty()) {
            dto.setPassword(userService.getUserDtoById(id).getPassword());
        }
        userService.updateProfile(dto);
        return "/{id}/edit";
    }
}
