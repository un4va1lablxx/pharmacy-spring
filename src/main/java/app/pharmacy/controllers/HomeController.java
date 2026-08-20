package app.pharmacy.controllers;

import app.pharmacy.entities.Product;
import app.pharmacy.services.CartService;
import app.pharmacy.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> lastChanceProducts = productService.getLastChanceProducts();
        int cartItemsCount = cartService.getTotalItemsCount();

        model.addAttribute("lastChanceProducts", lastChanceProducts);
        model.addAttribute("cartItemsCount", cartItemsCount);
        return "index";
    }
}