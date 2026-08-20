package app.pharmacy.services;

import app.pharmacy.entities.Product;
import app.pharmacy.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getLastChanceProducts() {
        return productRepository.findTop4ByOrderByStockQuantityAsc();
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public Product saveProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        return productRepository.save(product);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public List<Product> filterInStock(List<Product> products) {
        return products.stream()
                .filter(product -> product.getStockQuantity() > 0)
                .collect(Collectors.toList());
    }

    public List<Product> filterNoPrescription(List<Product> products) {
        return products.stream()
                .filter(product -> !product.isPrescriptionRequired())
                .collect(Collectors.toList());
    }

    public List<Product> sortProducts(List<Product> products, String sortType) {
        return switch (sortType) {
            case "price_asc" -> products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
            case "price_desc" -> products.stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .collect(Collectors.toList());
            case "name" -> products.stream()
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toList());
            case "stock" -> products.stream()
                    .sorted(Comparator.comparing(Product::getStockQuantity).reversed())
                    .collect(Collectors.toList());
            default -> products;
        };
    }

    public Optional<Product> getProductById(UUID id) {
        return productRepository.findById(id);
    }

}