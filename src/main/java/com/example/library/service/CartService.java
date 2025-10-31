package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Cart;
import com.example.library.entity.User;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CartRepository;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // ✅ Получить все элементы корзины текущего пользователя
    public List<Cart> getUserCart(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // ✅ Добавить книгу в корзину
    @Transactional
    public Cart addToCart(Long userId, Long bookId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        // Проверяем, есть ли уже такая книга в корзине
        Cart cartItem = cartRepository.findByUserIdAndBookId(userId, bookId).orElse(null);

        // 📘 Если книги ещё нет в корзине
        if (cartItem == null) {
            if (quantity > book.getStockQuantity()) {
                throw new RuntimeException("Недостаточно экземпляров книги. Доступно: " + book.getStockQuantity());
            }

            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setBook(book);
            newCart.setQuantity(quantity);
            return cartRepository.save(newCart);
        }

        // 📘 Если книга уже есть — увеличиваем количество
        int updatedQuantity = cartItem.getQuantity() + quantity;

        if (updatedQuantity > book.getStockQuantity()) {
            throw new RuntimeException("Нельзя добавить больше. Максимум: " + book.getStockQuantity());
        }

        cartItem.setQuantity(updatedQuantity);
        return cartRepository.save(cartItem);
    }

    // ✅ Обновить количество книги в корзине
    @Transactional
    public Cart updateQuantity(Long cartId, Integer newQuantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Элемент корзины не найден"));

        if (newQuantity > cart.getBook().getStockQuantity()) {
            throw new RuntimeException("Недостаточно экземпляров книги. Доступно: " + cart.getBook().getStockQuantity());
        }

        cart.setQuantity(newQuantity);
        return cartRepository.save(cart);
    }

    // ✅ Удалить элемент корзины
    @Transactional
    public void deleteItem(Long id) {
        if (!cartRepository.existsById(id)) {
            throw new RuntimeException("Элемент корзины не найден");
        }
        cartRepository.deleteById(id);
    }

    // ✅ Очистить корзину пользователя
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
