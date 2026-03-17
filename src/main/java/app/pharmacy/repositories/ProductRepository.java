package app.pharmacy.repositories;

import app.pharmacy.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.stockQuantity ASC LIMIT 4")
    List<Product> findTop4ByOrderByStockQuantityAsc();
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategoryIgnoreCase(String category);
}