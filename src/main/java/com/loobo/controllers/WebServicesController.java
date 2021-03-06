package com.loobo.controllers;

import com.loobo.model.Book;
import com.loobo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class WebServicesController {
    @Autowired
    BookRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

     @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public Book saveBook(@RequestBody Book book) {
        return repository.save(book);
    }

    @RequestMapping(value = "/book/{title}", method = RequestMethod.GET)
    @Cacheable(value = "book", key = "#title")
    public Book findBook(@PathVariable String title) {
//        Book value = (Book) redisTemplate.opsForHash().get("BOOK", title);
//        if (null != value)
//        {
//            return value;
//        }

        Long begin  = System.currentTimeMillis();
        Book result = repository.findByTitle(title);
//        redisTemplate.opsForHash().put("BOOK", title, result);

        Long end = System.currentTimeMillis();
        System.out.println(" query " + title + " cost: " + (end - begin) + "ms");
        return result;
    }

    @RequestMapping(value = "/books/title/{start}", method = RequestMethod.GET)
    public List<Book> findBooksStartWith(@PathVariable String start) {
        int page = Math.abs(new Random().nextInt() % 10);
        Long begin  = System.currentTimeMillis();
        List<Book> result = repository.findByTitleStartingWithOrderByTitleAsc(start, new PageRequest(page, 100));
        Long end = System.currentTimeMillis();
        System.out.println(" query " + start + " cost: " + (end - begin) + "ms");
        return result;
    }

    @RequestMapping(value = "/books/author/{start}", method = RequestMethod.GET)
    public List<Book> findBooksAuthorStartWith(@PathVariable String start) {
        int page = Math.abs(new Random().nextInt() % 10);
        Long begin  = System.currentTimeMillis();
        List<Book> result = repository.findByAuthorStartingWithOrderByAuthorAsc(start, new PageRequest(page, 100));
        Long end = System.currentTimeMillis();
        System.out.println(" query " + start + " cost: " + (end - begin) + "ms");
        return result;
    }
}
