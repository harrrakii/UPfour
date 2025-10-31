package com.example.library.controller;

import com.example.library.dto.OrderDTO;
import com.example.library.entity.Order;
import com.example.library.entity.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    // ✅ Все заказы (для админа)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }

    // ✅ Заказы текущего пользователя
    @GetMapping("/my")
    public List<Order> getMyOrders() {
        Long userId = getCurrentUserId();
        return orderService.findByUser(userId);
    }

    // ✅ Один заказ
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    // ✅ Создать заказ из корзины (возвращает DTO)
    @PostMapping
    public ResponseEntity<?> createOrderFromCart() {
        Long userId = getCurrentUserId();
        try {
            OrderDTO dto = orderService.createFromCart(userId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Изменить статус заказа
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    // ✅ Удалить заказ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 🔒 Получение текущего пользователя
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + username));
        return user.getId();
    }
}
