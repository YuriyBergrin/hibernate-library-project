package com.gmail.bergrin.controllers;

import java.util.List;

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

@Controller
@RequestMapping("/people")
public class PeopleController {

  private final PeopleService peopleService;
  private final BookService bookService;

  @Autowired
  public PeopleController(PeopleService peopleService, BookService bookService) {
    this.peopleService = peopleService;
    this.bookService = bookService;
  }

  @GetMapping()
  public String index(Model model) {
    model.addAttribute("people", peopleService.findAll());
    return "/people/index";
  }

  @GetMapping("/{id}")
  public String show(@PathVariable("id") int id, Model model) {
    Person person = peopleService.findById(id);
    List<Book> books = bookService.findByPersonId(id);
    model.addAttribute("person", person);
    model.addAttribute("books", books);
    return "people/show";
  }

  @GetMapping("/new")
  public String newPerson(@ModelAttribute("person") Person person) {
    return "people/new";
  }

  @PostMapping()
  public String addPerson(@ModelAttribute("person") Person person) {
    peopleService.save(person);
    return "redirect:/people";
  }

  @GetMapping("/{id}/edit")
  public String edit(@PathVariable("id") int id, Model model) {
    model.addAttribute("person", peopleService.findById(id));
    return "people/edit";
  }

  @PatchMapping("/{id}")
  public String update(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
    peopleService.update(id, person);
    return "redirect:/people";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    peopleService.delete(id);
    return "redirect:/people";
  }
}
