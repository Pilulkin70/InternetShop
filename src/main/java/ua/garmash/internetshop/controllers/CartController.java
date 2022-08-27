package ua.garmash.internetshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.garmash.internetshop.dto.BucketDto;
import ua.garmash.internetshop.service.BucketService;
import ua.garmash.internetshop.service.CartService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/bucket")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String aboutCart(Model model) {
        BucketDto bucketDto = cartService.getCartDto();
        model.addAttribute("bucket", bucketDto);
        model.addAttribute("totalPrice", bucketDto.getSum());
        return "bucket";
    }

    @PostMapping(params = "submit")
    public String commitCart(Principal principal) {
        if (principal != null) {
            return "redirect:/order/" + cartService.commitCartToOrder(principal.getName());
        } else {
            return "redirect:/order/" + cartService.commitCartToOrder(UUID.randomUUID().toString());
        }
    }

    @PostMapping(params = "clear")
    public String clearCart() {
            cartService.clearCart();
        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String delProductFromCart(@RequestParam("id") Long productId) {
        cartService.delProductFromCartById(productId);
        return "redirect:/bucket";
    }

/*    @GetMapping("/update")
    public String updateCartProductAmount(@RequestParam("id") Long productId,
                               @RequestParam("amount") Long productAmount) {
        cartService.updateCartProductAmount( productId, 1);
        return "redirect:/bucket";
    }*/

    @GetMapping("/incamount")
    public String addCartProductAmount(@RequestParam("id") Long productId) {
        cartService.updateCartProductAmount(productId, 1);
        return "redirect:/bucket";
    }

    @GetMapping("/decamount")
    public String subCartProductAmount(@RequestParam("id") Long productId) {
        cartService.updateCartProductAmount(productId, -1);
        return "redirect:/bucket";
    }
}

