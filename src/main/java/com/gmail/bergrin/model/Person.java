package com.gmail.bergrin.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "person")
@Data
public class Person {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  private String name;
  @Column(name = "birth_year")
  private int birthYear;
  @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
  @Cascade(value = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Book> books;

  public void addBook(Book book) {
    if (books != null) {
      books.add(book);
    } else {
      books = new ArrayList<>();
      books.add(book);
    }
  }
}
