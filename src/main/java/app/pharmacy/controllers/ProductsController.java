package app.pharmacy.controllers;

import app.pharmacy.entities.Product;
import app.pharmacy.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductsController {

    private final ProductService productService;

    private final List<String> CATEGORIES = Arrays.asList(
            "Обезболивающие", "Антибиотики", "Витамины", "Спазмолитики",
            "Антигистаминные", "БАД"
    );

    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Boolean noPrescription,
            @RequestParam(required = false) String sort,
            Model model) {

        List<Product> products;

        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search);
            model.addAttribute("searchQuery", search);
        } else if (category != null && !category.trim().isEmpty()) {
            products = productService.getProductsByCategory(category);
            model.addAttribute("currentCategory", category);
        } else {
            products = productService.getAllProducts();
        }
        if (inStock != null && inStock) {
            products = productService.filterInStock(products);
        }
        if (noPrescription != null && noPrescription) {
            products = productService.filterNoPrescription(products);
        }
        if (sort != null) {
            products = productService.sortProducts(products, sort);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("inStock", inStock);
        model.addAttribute("noPrescription", noPrescription);
        model.addAttribute("currentSort", sort);

        return "products";
    }
}