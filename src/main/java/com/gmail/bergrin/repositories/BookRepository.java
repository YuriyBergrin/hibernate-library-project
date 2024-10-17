package com.gmail.bergrin.repositories;

import java.util.List;

import com.gmail.bergrin.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

  List<Book> findByOwnerId(int personId);
  List<Book> findByNameStartingWith(String name);
}
