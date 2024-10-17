package com.gmail.bergrin.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.gmail.bergrin.model.Book;
import com.gmail.bergrin.model.Person;
import com.gmail.bergrin.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BookService {

  private final BookRepository bookRepository;

  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public List<Book> findAll(boolean sortByPublishYear) {
    if (sortByPublishYear) {
      return bookRepository.findAll(Sort.by("publishYear"));
    } else {
      return bookRepository.findAll();
    }
  }

  public Book findById(int id) {
    return bookRepository.findById(id).orElse(null);
  }

  @Transactional
  public void save(Book book) {
    bookRepository.save(book);
  }

  @Transactional
  public void update(int id, Book updatedBook) {
    Book bookFromDb = findById(id);
    updatedBook.setId(id);
    updatedBook.setOwner(bookFromDb.getOwner());
    bookRepository.save(updatedBook);
  }

  @Transactional
  public void delete(int id) {
    bookRepository.deleteById(id);
  }

  @Transactional
  public void assign(int id, Person owner) {
    Book updatedBook = findById(id);
    updatedBook.setOwner(owner);
    updatedBook.setReceiveDate(LocalDate.now());
    owner.addBook(updatedBook);
  }

  @Transactional
  public void release(int id) {
    Book updatedBook = findById(id);
    updatedBook.getOwner().getBooks().removeIf(book -> book.getId() == id);
    updatedBook.setOwner(null);
    updatedBook.setReceiveDate(null);
  }

  public List<Book> findByPersonId(int personId) {
    List<Book> books = bookRepository.findByOwnerId(personId);
    books.forEach(book -> {
      LocalDate bookReceiveDate = book.getReceiveDate();
      LocalDate date = LocalDate.now().minusDays(1);
      if (bookReceiveDate.isBefore(date)) {
        book.setExpired(true);
      }
    });
    return books;
  }

  public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByPublishYear) {
    if (sortByPublishYear) {
      return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("publishYear"))).getContent();
    } else {
      return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }
  }

  public List<Book> searchByTitle(String query) {
    return bookRepository.findByNameStartingWith(query);
  }
}
