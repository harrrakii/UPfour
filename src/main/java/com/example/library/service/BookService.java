package com.example.library.service;

import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.entity.Publisher;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    // ✅ Все книги
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    // ✅ Найти по ID
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    // ✅ Создать книгу
    public Book create(Book book) {
        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        Publisher publisher = publisherRepository.findById(book.getPublisher().getId())
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));
        Category category = categoryRepository.findById(book.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategory(category);
        return bookRepository.save(book);
    }

    // ✅ Обновить книгу
    public Book update(Long id, Book updatedBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        Author author = authorRepository.findById(updatedBook.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
        Publisher publisher = publisherRepository.findById(updatedBook.getPublisher().getId())
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));
        Category category = categoryRepository.findById(updatedBook.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        book.setTitle(updatedBook.getTitle());
        book.setGenre(updatedBook.getGenre());
        book.setYear(updatedBook.getYear());
        book.setPrice(updatedBook.getPrice());
        book.setStockQuantity(updatedBook.getStockQuantity());
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategory(category);

        return bookRepository.save(book);
    }

    // ✅ Удалить книгу
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
