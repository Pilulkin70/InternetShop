package ua.garmash.internetshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.garmash.internetshop.dto.CartDto;
import ua.garmash.internetshop.service.CartService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String aboutCart(Model model) {
        CartDto cartDto = cartService.getCartDto();
        model.addAttribute("cart", cartDto);
        model.addAttribute("totalPrice", cartDto.getSum());
        return "cart";
    }

    @PostMapping(params = "submit")
    public String commitCartToOrder(Principal principal) {
        if (principal != null) {
            return "redirect:/order/" + cartService.commitCartToOrder(principal.getName()) + "/delivery";
        } else {
            return "redirect:/order/" + cartService.commitCartToOrder(UUID.randomUUID().toString()) + "/delivery";
        }
    }

    @PostMapping(params = "clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/products";
    }

    @PostMapping(params = "save")
    public String saveCart() {
        cartService.saveCart();
        return "redirect:/cart";
    }

    @PostMapping(params = "load")
    public String loadCart() {
        cartService.loadCart();
        return "redirect:/cart";
    }

    @GetMapping("/delete")
    public String delProductFromCart(@RequestParam("id") Long productId) {
        cartService.delProductFromCartById(productId);
        return "redirect:/cart";
    }

    @GetMapping("/incamount")
    public String addCartProductAmount(@RequestParam("id") Long productId) {
        cartService.updateCartProductAmount(productId, 1);
        return "redirect:/cart";
    }

    @GetMapping("/decamount")
    public String subCartProductAmount(@RequestParam("id") Long productId) {
        cartService.updateCartProductAmount(productId, -1);
        return "redirect:/cart";
    }
}

