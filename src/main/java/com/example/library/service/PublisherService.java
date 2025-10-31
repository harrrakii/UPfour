package com.example.library.service;

import com.example.library.entity.Publisher;
import com.example.library.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    // ✅ Все издательства
    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    // ✅ Найти по ID
    public Publisher findById(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));
    }

    // ✅ Создать издательство
    public Publisher create(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    // ✅ Обновить издательство
    public Publisher update(Long id, Publisher updatedPublisher) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Издательство не найдено"));

        publisher.setName(updatedPublisher.getName());
        publisher.setCity(updatedPublisher.getCity());
        publisher.setEmail(updatedPublisher.getEmail());

        return publisherRepository.save(publisher);
    }

    // ✅ Удалить издательство
    public void delete(Long id) {
        publisherRepository.deleteById(id);
    }
}
