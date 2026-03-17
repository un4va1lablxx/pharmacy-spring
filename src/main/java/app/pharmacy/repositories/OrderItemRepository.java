package app.pharmacy.repositories;

import app.pharmacy.entities.Order;
import app.pharmacy.entities.OrderItem;
import app.pharmacy.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> { }