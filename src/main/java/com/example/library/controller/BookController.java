package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // ✅ Получить все книги
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    // ✅ Получить книгу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    // ✅ Создать книгу (с валидацией)
    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book, BindingResult result) {
        if (result.hasErrors()) {
            // собираем все ошибки валидации в одну строку
            String errorMsg = result.getFieldErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .reduce((m1, m2) -> m1 + "; " + m2)
                    .orElse("Ошибка валидации данных");
            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            return ResponseEntity.ok(bookService.create(book));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body("Ошибка сохранения книги: нарушена целостность данных (возможно, не выбраны связанные объекты).");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Не удалось сохранить книгу: " + e.getMessage());
        }
    }

    // ✅ Обновить книгу (с валидацией)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody Book book, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getFieldErrors().stream()
                    .map(err -> err.getDefaultMessage())
                    .reduce((m1, m2) -> m1 + "; " + m2)
                    .orElse("Ошибка валидации данных");
            return ResponseEntity.badRequest().body(errorMsg);
        }

        try {
            return ResponseEntity.ok(bookService.update(id, book));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body("Ошибка обновления книги: нарушена целостность данных.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Не удалось обновить книгу: " + e.getMessage());
        }
    }

    // ✅ Удалить книгу (с обработкой каскадных ошибок)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest()
                    .body("Невозможно удалить книгу, так как она связана с другими записями (например, заказами или корзиной).");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при удалении книги: " + e.getMessage());
        }
    }
}
