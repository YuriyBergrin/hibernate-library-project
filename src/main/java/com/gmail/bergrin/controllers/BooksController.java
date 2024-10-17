package com.gmail.bergrin.controllers;

import com.gmail.bergrin.model.Book;
import com.gmail.bergrin.model.Person;
import com.gmail.bergrin.services.BookService;
import com.gmail.bergrin.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BooksController {

  private final BookService bookService;
  private final PeopleService peopleService;

  @Autowired
  public BooksController(BookService bookService, PeopleService peopleService) {
    this.bookService = bookService;
    this.peopleService = peopleService;
  }

  @GetMapping()
  public String index(Model model,
                      @RequestParam(value = "page", required = false) Integer page,
                      @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                      @RequestParam(value = "sortByPublishYear", required = false) boolean sortByPublishYear) {
    if (page == null || booksPerPage == null) {
      model.addAttribute("books", bookService.findAll(sortByPublishYear));
    } else {
      model.addAttribute("books", bookService.findWithPagination(page, booksPerPage, sortByPublishYear));
    }
    return "books/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
    Book book = bookService.findById(id);
    model.addAttribute("book", book);
    Person bookOwner = book.getOwner();
    if (bookOwner != null) {
      model.addAttribute("owner", bookOwner);
    } else {
      model.addAttribute("people", peopleService.findAll());
    }
    return "books/show";
  }

  @GetMapping("/new")
  public String newBook(@ModelAttribute("book") Book book) {
    return "books/new";
  }

  @PostMapping()
  public String addBook(@ModelAttribute("book") Book book) {
    bookService.save(book);
    return "redirect:/books";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable("id") int id, Model model) {
    model.addAttribute("book", bookService.findById(id));
    return "books/edit";
  }

  @PatchMapping("/{id}")
  public String update(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
    bookService.update(id, book);
    return "redirect:/books";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    bookService.delete(id);
    return "redirect:/books";
  }

  @PatchMapping("/{id}/assign")
  public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person owner) {
    bookService.assign(id, owner);
    return "redirect:/books/" + id;
  }

  @PatchMapping("/{id}/release")
  public String release(@PathVariable("id") int id) {
    bookService.release(id);
    return "redirect:/books/" + id;
  }
}
