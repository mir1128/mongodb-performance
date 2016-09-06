package com.loobo.repository;

import com.loobo.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    Book findByTitle(String title);

    void delete(String title);

    List<Book> findByTitleStartingWithOrderByTitleAsc(String regexp, Pageable pageable);

    List<Book> findByAuthorStartingWithOrderByAuthorAsc(String regexp, Pageable pageable);
}

