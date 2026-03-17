package app.pharmacy.services;

import app.pharmacy.entities.CartItem;
import app.pharmacy.entities.Product;
import app.pharmacy.entities.User;
import app.pharmacy.repositories.CartItemRepository;
import app.pharmacy.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    private List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        if (userService.isAuthenticated()) {
            User currentUser = userService.getCurrentUser().orElse(null);
            if (currentUser != null) {
                return cartItemRepository.findByUser(currentUser);
            }
        }
        return cartItems;
    }

    public void addToCart(UUID productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (userService.isAuthenticated()) {
            User currentUser = userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
            CartItem existingItem = cartItemRepository.findByUserAndProduct(currentUser, product)
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartItemRepository.save(existingItem);
            } else {
                CartItem newItem = new CartItem();
                newItem.setId(UUID.randomUUID());
                newItem.setUser(currentUser);
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                cartItemRepository.save(newItem);
            }
        } else {
            CartItem existingItem = cartItems.stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                CartItem newItem = new CartItem();
                newItem.setId(UUID.randomUUID());
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                cartItems.add(newItem);
            }
        }
    }

    public void updateCartItem(UUID itemId, int quantity) {
        if (userService.isAuthenticated()) {
            CartItem cartItem = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            cartItems.stream()
                    .filter(item -> item.getId().equals(itemId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
        }
    }

    public void removeCartItem(UUID itemId) {
        if (userService.isAuthenticated()) {
            cartItemRepository.deleteById(itemId);
        } else {
            cartItems.removeIf(item -> item.getId().equals(itemId));
        }
    }

    public void clearCart() {
        if (userService.isAuthenticated()) {
            User currentUser = userService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
            cartItemRepository.deleteByUser(currentUser);
        } else {
            cartItems.clear();
        }
    }

    public int getTotalItemsCount() {
        List<CartItem> items = getCartItems();
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public int getUniqueItemsCount() {
        return getCartItems().size();
    }

    public void saveCartToDatabase(User user) {
        if (!cartItems.isEmpty()) {
            for (CartItem tempItem : cartItems) {
                CartItem existingItem = cartItemRepository.findByUserAndProduct(user, tempItem.getProduct())
                        .orElse(null);
                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + tempItem.getQuantity());
                    cartItemRepository.save(existingItem);
                } else {
                    CartItem newItem = new CartItem();
                    newItem.setId(UUID.randomUUID());
                    newItem.setUser(user);
                    newItem.setProduct(tempItem.getProduct());
                    newItem.setQuantity(tempItem.getQuantity());
                    cartItemRepository.save(newItem);
                }
            }
            cartItems.clear();
        }
    }
}