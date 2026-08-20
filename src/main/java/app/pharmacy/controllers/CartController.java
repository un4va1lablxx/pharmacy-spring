package app.pharmacy.controllers;

import app.pharmacy.entities.CartItem;
import app.pharmacy.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String cart(Model model) {
        List<CartItem> cartItems = cartService.getCartItems();
        int totalItemsCount = cartService.getTotalItemsCount();
        int uniqueItemsCount = cartService.getUniqueItemsCount();
        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice().doubleValue() * item.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("itemsCount", totalItemsCount);
        model.addAttribute("uniqueItemsCount", uniqueItemsCount);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam UUID productId, @RequestParam int quantity) {
        try {
            cartService.addToCart(productId, quantity);
            return "redirect:/cart";
        } catch (Exception e) {
            return "redirect:/?error=add_failed";
        }
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam UUID itemId, @RequestParam int quantity) {
        cartService.updateCartItem(itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeCartItem(@RequestParam UUID itemId) {
        cartService.removeCartItem(itemId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }

    @GetMapping("/count")
    @ResponseBody
    public int getCartItemsCount() {
        return cartService.getTotalItemsCount();
    }
}