package app.pharmacy.services;

import app.pharmacy.entities.Order;
import app.pharmacy.entities.OrderItem;
import app.pharmacy.entities.Product;
import app.pharmacy.repositories.OrderRepository;
import app.pharmacy.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order saveOrder(Order order) {
        if (order.getId() == null) {
            Order savedOrder = orderRepository.save(order);
            updateProductStock(savedOrder);
            return savedOrder;
        } else {
            return orderRepository.save(order);
        }
    }

    @Transactional
    public void deleteOrder(UUID id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    @Transactional
    protected void updateProductStock(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            int orderedQuantity = orderItem.getQuantity();
            int newStockQuantity = product.getStockQuantity() - orderedQuantity;
            if (newStockQuantity < 0) {
                newStockQuantity = 0;
            }
            product.setStockQuantity(newStockQuantity);
            productRepository.save(product);
        }
    }

    public Optional<Order> findById(UUID id) {
        return orderRepository.findByIdWithDetails(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAllWithDetails();
    }

    public List<Order> findByUserId(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    public String calculateDeliveryTime(LocalDateTime orderTime) {
        LocalDateTime deliveryTime;
        int hour = orderTime.getHour();
        if (hour >= 22 || hour < 8) {
            deliveryTime = orderTime.plusDays(1)
                    .withHour(10)
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0);
        } else {
            deliveryTime = orderTime.plusHours(2);
            if (deliveryTime.getHour() >= 22) {
                deliveryTime = deliveryTime.plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);
            }
        }
        return formatDeliveryTime(deliveryTime);
    }

    private String formatDeliveryTime(LocalDateTime deliveryTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        if (deliveryTime.toLocalDate().equals(LocalDate.now())) {
            return "сегодня, " + deliveryTime.format(formatter);
        }
        else if (deliveryTime.toLocalDate().equals(LocalDate.now().plusDays(1))) {
            return "завтра, " + deliveryTime.format(formatter);
        }
        else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return deliveryTime.format(dateFormatter) + ", " + deliveryTime.format(formatter);
        }
    }
}