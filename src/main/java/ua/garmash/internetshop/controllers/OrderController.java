package ua.garmash.internetshop.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.model.OrderStatus;
import ua.garmash.internetshop.model.User;
import ua.garmash.internetshop.service.OrderService;
import ua.garmash.internetshop.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping
    public String aboutOrders(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("orders", null);
        } else {
            List<OrderDto> ordersByUserDto = orderService.getOrdersByUser(principal.getName());
            model.addAttribute("orders", ordersByUserDto);
        }
        return "orders";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getOrderById(id));
        return "order";
    }

    @GetMapping("/thanks-for-order")
    public String showThanks() {
        return "thanks-for-order";
    }

    @GetMapping("/{id}/delivery")
    public String getDeliveryPayment(@PathVariable Long id, Model model, Principal principal) {
        OrderDto orderDto = orderService.getOrderById(id);
        if (principal != null) {
            User user = userService.getUserByName(principal.getName());
            orderDto.setRecipient(user.getFirstName() == null ? "" : user.getFirstName() + " " +
                    user.getLastName() == null ? "" : user.getLastName());
            orderDto.setEmail(user.getEmail());
            orderDto.setPhone(user.getPhone());
            orderDto.setAddress(user.getCity());
        }
        model.addAttribute("order", orderDto);
        return "delivery-pay";
    }

    @PostMapping("/delivery")
    public String setDeliveryPayment(OrderDto dto, Principal principal) {

        dto.setStatus(OrderStatus.APPROVED);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
/*        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            orderService.saveOrderFromDto(dto);
            return "redirect:/users/" + dto.getId() + "/edit";
        }*/

        if (principal != null) {
            dto.setUser(userService.getUserByName(principal.getName()));
        } else {
            dto.setUser(userService.getUserById(0L));
        }

        orderService.saveOrderFromDto(dto);

        if (principal == null) {

            return "redirect:/order/thanks-for-order";
        }

        return "redirect:/order/" + dto.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/delete")
    public String delOrder(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        orderService.delOrderById(id);
        return "redirect:/users/profile";
    }

/*    @PostMapping
    public String commitBucket(Principal principal) {
        if (principal != null) {
            bucketService.commitBucketToOrder(principal.getName());
        }
        return "redirect:/bucket";
    }

    @GetMapping("/delete")
    public String delProduct(@RequestParam("id") Long productId, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        bucketService.delProductById(principal.getName(), productId);
        return "redirect:/bucket";
    }

    @GetMapping("/update")
    public String updateAmount(@RequestParam("id") Long productId,
                               @RequestParam("amount") Long productAmount, Principal principal) {
        if (principal == null) {
            return "redirect:/products";
        }
        bucketService.updateAmount(principal.getName(), productId);
        return "redirect:/bucket";
    }*/
}

