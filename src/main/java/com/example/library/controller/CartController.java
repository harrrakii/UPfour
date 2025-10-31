package com.example.library.controller;

import com.example.library.dto.CartDTO;
import com.example.library.entity.Cart;
import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    // ✅ Получить корзину текущего пользователя
    @GetMapping
    public List<CartDTO> getMyCart() {
        Long userId = getCurrentUserId();
        List<Cart> cartItems = cartService.getUserCart(userId);

        return cartItems.stream().map(item -> {
            var book = item.getBook();
            return new CartDTO(
                    item.getId(),
                    book != null ? book.getTitle() : "—",
                    (book != null && book.getAuthor() != null) ? book.getAuthor().getName() : "—",
                    (book != null && book.getPublisher() != null) ? book.getPublisher().getName() : "—",
                    item.getQuantity(),
                    book != null ? book.getPrice() : BigDecimal.ZERO,
                    book != null ? book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())) : BigDecimal.ZERO
            );
        }).collect(Collectors.toList());
    }

    // ✅ Добавить книгу в корзину
    @PostMapping("/{bookId}")
    public ResponseEntity<Cart> addBookToCart(@PathVariable Long bookId,
                                              @RequestParam(defaultValue = "1") Integer quantity) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.addToCart(userId, bookId, quantity));
    }

    // ✅ Обновить количество
    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateQuantity(@PathVariable Long cartId,
                                               @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(cartId, quantity));
    }

    // ✅ Удалить элемент
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long cartId) {
        cartService.deleteItem(cartId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Очистить корзину
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart(getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    // 🔒 Получить ID текущего пользователя
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));
        return user.getId();
    }
}
