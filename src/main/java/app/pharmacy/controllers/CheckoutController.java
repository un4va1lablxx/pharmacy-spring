package app.pharmacy.controllers;

import app.pharmacy.dto.DeliveryAddress;
import app.pharmacy.entities.*;
import app.pharmacy.services.CartService;
import app.pharmacy.services.OrderService;
import app.pharmacy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String checkout(Model model) {
        boolean isAuthenticated = checkUserAuthentication();
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", calculateTotal());
        model.addAttribute("itemsCount", cartService.getTotalItemsCount());
        return "checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestParam String paymentMethod,
                             @RequestParam String street,
                             @RequestParam String house,
                             @RequestParam(required = false) String apartment,
                             @RequestParam(required = false) String comment,
                             RedirectAttributes redirectAttributes) {
        try {
            if (!checkUserAuthentication()) {
                redirectAttributes.addFlashAttribute("error", "Требуется авторизация");
                return "redirect:/login";
            }
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Выберите способ оплаты");
                return "redirect:/checkout";
            }
            if (street == null || street.trim().isEmpty() || house == null || house.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Заполните адрес доставки");
                return "redirect:/checkout";
            }
            if (cartService.getCartItems().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Корзина пуста");
                return "redirect:/cart";
            }
            DeliveryAddress deliveryAddress = new DeliveryAddress();
            deliveryAddress.setStreet(street);
            deliveryAddress.setHouse(house);
            deliveryAddress.setApartment(apartment);
            deliveryAddress.setComment(comment);
            Order order = createOrder(paymentMethod, deliveryAddress);
            Order savedOrder = orderService.saveOrder(order);
            return "redirect:/checkout/success?orderId=" + savedOrder.getId().toString();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании заказа: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/success")
    public String orderSuccess(@RequestParam UUID orderId, Model model) {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String deliveryTime = orderService.calculateDeliveryTime(order.getCreatedAt());
        model.addAttribute("order", order);
        model.addAttribute("deliveryTime", deliveryTime);
        return "order-success";
    }


    private Order createOrder(String paymentMethod, DeliveryAddress deliveryAddress) {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCreatedAt(LocalDateTime.now());
        order.setTotal(calculateTotal());
        try {
            order.setPaymentMethod(PaymentMethod.valueOf(paymentMethod));
        } catch (IllegalArgumentException e) {
            order.setPaymentMethod(PaymentMethod.CASH);
        }
        order.setShippingStatus(ShippingStatus.CREATED);
        String address = formatAddress(deliveryAddress);
        order.setShippingAddress(address);
        userService.getCurrentUser().ifPresent(order::setUser);
        cartService.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID());
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            order.getOrderItems().add(orderItem);
        });
        return order;
    }

    private boolean checkUserAuthentication() {
        return userService.isAuthenticated();
    }

    private BigDecimal calculateTotal() {
        return cartService.getCartItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String formatAddress(DeliveryAddress deliveryAddress) {
        StringBuilder address = new StringBuilder();

        if (deliveryAddress.getStreet() != null && !deliveryAddress.getStreet().trim().isEmpty()) {
            address.append(deliveryAddress.getStreet().trim());
        }

        if (deliveryAddress.getHouse() != null && !deliveryAddress.getHouse().trim().isEmpty()) {
            if (!address.isEmpty()) address.append(", ");
            address.append("д. ").append(deliveryAddress.getHouse().trim());
        }

        if (deliveryAddress.getApartment() != null && !deliveryAddress.getApartment().trim().isEmpty()) {
            if (!address.isEmpty()) address.append(", ");
            address.append("кв. ").append(deliveryAddress.getApartment().trim());
        }

        return address.toString();
    }
}