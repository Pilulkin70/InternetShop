package ua.garmash.internetshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.model.Order;
import ua.garmash.internetshop.service.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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

