package com.example.library.service;

import com.example.library.dto.OrderDTO;
import com.example.library.dto.OrderItemDTO;
import com.example.library.entity.*;
import com.example.library.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    // ✅ Все заказы (для админа)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // ✅ Заказы конкретного пользователя
    public List<Order> findByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // ✅ Один заказ по ID
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
    }

    // ✅ Создание заказа из корзины
    @Transactional
    public OrderDTO createFromCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }

        System.out.println("📦 В корзине товаров: " + cartItems.size());

        Order order = new Order();
        order.setUser(user);
        order.setStatus("CREATED");
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;

        for (Cart item : cartItems) {
            Book book = item.getBook();

            if (book == null) {
                throw new RuntimeException("Ошибка: у товара в корзине нет книги (book == null)");
            }

            if (book.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Недостаточно экземпляров книги: " + book.getTitle());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(book.getPrice());

            total = total.add(orderItem.getSubtotal());
            order.getOrderItems().add(orderItem);

            book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
            bookRepository.save(book);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        // Очистить корзину
        cartRepository.deleteAll(cartItems);

        // ✅ Преобразуем в DTO
        OrderDTO dto = new OrderDTO();
        dto.setId(savedOrder.getId());
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setOrderDate(savedOrder.getOrderDate());
        dto.setTotalAmount(savedOrder.getTotalAmount());
        dto.setStatus(savedOrder.getStatus());

        dto.setOrderItems(savedOrder.getOrderItems().stream().map(oi -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setId(oi.getId());
            itemDTO.setOrderId(savedOrder.getId());
            itemDTO.setProductId(oi.getBook().getId());
            itemDTO.setProductName(oi.getBook().getTitle());
            itemDTO.setQuantity(oi.getQuantity());
            itemDTO.setPrice(oi.getPrice());
            itemDTO.setSubtotal(oi.getSubtotal());
            return itemDTO;
        }).toList());

        return dto;
    }

    // ✅ Изменить статус
    public Order updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ не найден"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // ✅ Удалить заказ
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
