package ua.garmash.internetshop.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.dto.UserDto;
import ua.garmash.internetshop.service.OrderService;
import ua.garmash.internetshop.service.UserService;
import ua.garmash.internetshop.validator.UserValidator;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService, UserValidator userValidator) {
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
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/new")
    public String saveUser(UserDto dto, Model model) {

        if (userService.findByName(dto.getUsername()) != null) {
            logger.error(String.format("User with login '%s ' is already exist.", dto.getUsername()));
            throw new RuntimeException("User with login '" + dto.getUsername() + "' is already exist");
        }
        int minAge = 18;
        if (dto.getAge() < minAge) {
            logger.error(String.format("User is so yang, %s", dto.getUsername()));
            throw new RuntimeException("You are too young '" + dto.getUsername() + "'");
        }
        if (userService.save(dto)) {
            logger.debug(String.format("User with id: %s successfully created.", dto.getId()));
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                    return "redirect:/users";
                } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CLIENT"))) {
                    return "redirect:/users/profile";
                }
            }
            return "redirect:/login";
        } else {
            logger.debug(String.format("User with id: %s not created.", dto.getId()));
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
        List<OrderDto> ordersDto = orderService.getOrdersByUserName(userDto.getUsername());
        model.addAttribute("user", userDto);
        model.addAttribute("orders", ordersDto);
        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile")
    public String updateUserProfile(UserDto dto, Model model, Principal principal) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            userService.updateProfile(dto);
            logger.debug(String.format("User profile with id: %s successfully updated.", dto.getId()));
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
        logger.debug(String.format("User with id: %s has been deleted.", id));
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        UserDto userDto = userService.getUserDtoById(id);
        List<OrderDto> ordersDto = orderService.getOrdersByUserName(userDto.getUsername());
        model.addAttribute("user", userDto);
        model.addAttribute("orders", ordersDto);
        return "profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable Long id, UserDto dto) {
        if (!Objects.equals(dto.getId(), id)) {
            throw new RuntimeException("Order Id exception.");
        }
        if (dto.getPassword().isEmpty()) {
            dto.setPassword(userService.getUserDtoById(id).getPassword());
        }
        logger.debug(String.format("User info with id: %s successfully updated.", dto.getId()));
        userService.updateProfile(dto);
        return "/{id}/edit";
    }
}
