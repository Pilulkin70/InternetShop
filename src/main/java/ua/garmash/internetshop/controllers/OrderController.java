package ua.garmash.internetshop.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.model.OrderStatus;
import ua.garmash.internetshop.model.User;
import ua.garmash.internetshop.service.EmailService;
import ua.garmash.internetshop.service.OrderService;
import ua.garmash.internetshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final EmailService emailService;

    public OrderController(OrderService orderService, UserService userService, EmailService emailService) {
        this.orderService = orderService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}")
    public String setById(@PathVariable Long id, OrderDto dto) {
        orderService.saveOrderFromDto(dto);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/thanks-for-order")
    public String showThanks() {
        return "thanks-for-order";
    }

    @GetMapping("/{id}/delivery")
    public String getDeliveryAndPayment(@PathVariable Long id, Model model, Principal principal) {
        OrderDto orderDto = orderService.getOrderById(id);
        if (principal != null) {
            User user;
            if (principal.getName().equalsIgnoreCase("ADMIN")) {
                user = orderDto.getUser();
            } else {
                user = userService.getUserByName(principal.getName());
            }
            if (orderDto.getRecipient() == null) {
                orderDto.setRecipient(user.getFirstName() == null ? "" : user.getFirstName() + " " +
                        (user.getLastName() == null ? "" : user.getLastName()));
            }
            if (orderDto.getEmail() == null) {
                orderDto.setEmail(user.getEmail());
            }
            if (orderDto.getPhone() == null) {
                orderDto.setPhone(user.getPhone());
            }
            if (orderDto.getAddress() == null) {
                orderDto.setAddress(user.getCity());
            }
        } else {
            if (orderDto.getUser().getId() != 0L) {
                return "redirect:/";
            }
        }
        model.addAttribute("order", orderDto);
        return "delivery-pay";
    }

    @PostMapping("/delivery")
    public String setDeliveryAndPayment(OrderDto dto, Principal principal) {
        dto.setStatus(OrderStatus.APPROVED);
        User user;
        if (principal == null) {
            user = userService.getUserById(0L);
        } else {
            user = userService.getUserByName(principal.getName());
        }
        dto.setUser(user);
        orderService.saveOrderFromDto(dto);
        if (principal == null) {
            return "redirect:/orders/thanks-for-order";
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && dto.getSendEmail()) {
            String email = dto.getEmail();
            String subject = user.getFirstName() + " " + user.getLastName();
            LocalDateTime orderDate = dto.getChanged();
            String msg = "Thank you for order N" + dto.getId() + " " +
                    orderDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            emailService.sendSimpleMessage(email, subject, msg);
        }
        return "redirect:/orders/" + dto.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/products";
        }
        String referer = request.getHeader("Referer");
        orderService.delOrderById(id);
        return "redirect:" + referer;
    }
}

