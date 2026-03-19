package com.ecommerce.service;

import com.ecommerce.dto.CartItemRequest;
import com.ecommerce.dto.CartResponse;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // ── Get or create cart for the logged-in user ─────────────────────────────
    public CartResponse getCart() {
        Cart cart = getOrCreateCart();
        return CartResponse.from(cart);
    }

    // ── Add item to cart ──────────────────────────────────────────────────────
    // If product already in cart → increase quantity
    // If new product → create new cart item
    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + request.getProductId()));

        if (!product.getActive()) {
            throw new RuntimeException("Product is no longer available");
        }

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
        }

        // Check if product already in cart
        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(
                    existingItem -> {
                        // Product already in cart — just increase quantity
                        int newQty = existingItem.getQuantity() + request.getQuantity();
                        if (newQty > product.getStock()) {
                            throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
                        }
                        existingItem.setQuantity(newQty);
                        existingItem.updateSubtotal();
                        cartItemRepository.save(existingItem);
                    },
                    () -> {
                        // New product — add fresh cart item
                        CartItem newItem = CartItem.builder()
                                .cart(cart)
                                .product(product)
                                .quantity(request.getQuantity())
                                .unitPrice(product.getPrice())
                                .subtotal(product.getPrice().multiply(java.math.BigDecimal.valueOf(request.getQuantity())))
                                .build();
                        cart.getItems().add(newItem);
                    }
                );

        cartRepository.save(cart);
        return CartResponse.from(cart);
    }

    // ── Update quantity of a cart item ────────────────────────────────────────
    @Transactional
    public CartResponse updateItem(Long cartItemId, CartItemRequest request) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        validateCartOwnership(item.getCart());

        if (request.getQuantity() <= 0) {
            // Quantity 0 or less → remove the item
            item.getCart().getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            Product product = item.getProduct();
            if (request.getQuantity() > product.getStock()) {
                throw new RuntimeException("Insufficient stock. Available: " + product.getStock());
            }
            item.setQuantity(request.getQuantity());
            item.updateSubtotal();
            cartItemRepository.save(item);
        }

        Cart cart = cartRepository.findById(item.getCart().getId()).orElseThrow();
        return CartResponse.from(cart);
    }

    // ── Remove a single item from cart ────────────────────────────────────────
    @Transactional
    public CartResponse removeItem(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        validateCartOwnership(item.getCart());

        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.save(cart);

        return CartResponse.from(cart);
    }

    // ── Clear entire cart ─────────────────────────────────────────────────────
    @Transactional
    public void clearCart() {
        Cart cart = getOrCreateCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private Cart getOrCreateCart() {
        User user = getCurrentUser();
        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateCartOwnership(Cart cart) {
        User currentUser = getCurrentUser();
        if (!cart.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied — this cart does not belong to you");
        }
    }
}
