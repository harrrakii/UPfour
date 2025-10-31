package com.example.library.repository;

import com.example.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    List<Publisher> findByCityContainingIgnoreCase(String city);
    List<Publisher> findByNameContainingIgnoreCase(String name);
}
