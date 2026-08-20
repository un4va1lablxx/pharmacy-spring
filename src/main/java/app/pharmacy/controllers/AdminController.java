package app.pharmacy.controllers;

import app.pharmacy.entities.*;
import app.pharmacy.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String adminRedirect() {
        return "redirect:/admin/users";
    }

    @GetMapping("/users")
    public String usersManagement(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        return "admin-users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete-all")
    public String deleteAllUsers() {
        List<User> users = userService.findAll();
        for (User user : users) {
            if (!isAdminUser(user)) {
                userService.deleteById(user.getId());
            }
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam UUID id,
                             @RequestParam String userName,
                             @RequestParam String email,
                             @RequestParam String firstName,
                             @RequestParam(required = false) String lastName,
                             @RequestParam(required = false) String middleName,
                             @RequestParam(required = false) String phone,
                             @RequestParam(required = false) String password) {

        userService.findById(id).ifPresent(user -> {
            if (!isAdminUser(user)) {
                user.setUserName(userName);
                user.setEmail(email);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setMiddleName(middleName);
                user.setPhone(phone);

                if (password != null && !password.trim().isEmpty()) {
                    user.setPasswordHash(passwordEncoder.encode(password));
                }

                userService.save(user);
            }
        });

        return "redirect:/admin/users";
    }

    private boolean isAdminUser(User user) {
        return "admin@selderey.ru".equals(user.getEmail()) ||
                "admin".equals(user.getUserName());
    }

    @GetMapping("/products")
    public String productsManagement(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("product", new Product());
        return "admin-products";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/products/update")
    public String updateProduct(@RequestParam("id") UUID id,
                                @RequestParam("name") String name,
                                @RequestParam("category") String category,
                                @RequestParam("price") BigDecimal price,
                                @RequestParam("stockQuantity") Integer stockQuantity,
                                @RequestParam(value = "prescriptionRequired", defaultValue = "false") Boolean prescriptionRequired,
                                @RequestParam(value = "description", required = false) String description,
                                @RequestParam(value = "manufacturer", required = false) String manufacturer,
                                @RequestParam(value = "imageUrl", required = false) String imageUrl) {

        productService.getProductById(id).ifPresent(product -> {
            product.setName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setPrescriptionRequired(prescriptionRequired);
            product.setDescription(description);
            product.setManufacturer(manufacturer);
            product.setImageUrl(imageUrl);

            productService.saveProduct(product);
        });

        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String ordersManagement(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "admin-orders";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/delete-all")
    public String deleteAllOrders() {
        orderService.deleteAllOrders();
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/update")
    public String updateOrder(@RequestParam("id") UUID id,
                              @RequestParam("shippingStatus") String shippingStatus,
                              @RequestParam("paymentMethod") String paymentMethod,
                              @RequestParam(value = "shippingAddress", required = false) String shippingAddress) {

        orderService.findById(id).ifPresent(order -> {
            try {
                order.setShippingStatus(ShippingStatus.valueOf(shippingStatus.toUpperCase()));
                order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod.toUpperCase()));

                if (shippingAddress != null && !shippingAddress.trim().isEmpty()) {
                    order.setShippingAddress(shippingAddress);
                }
                orderService.saveOrder(order);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Неверный статус заказа или способ оплаты: " + e.getMessage());
            }
        });

        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/{id}/details")
    public String getOrderDetails(@PathVariable UUID id, Model model) {
        try {
            Order order = orderService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            model.addAttribute("order", order);
            return "admin-order-details :: orderDetails";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка загрузки заказа: " + e.getMessage());
            return "admin-order-details :: error";
        }
    }
}