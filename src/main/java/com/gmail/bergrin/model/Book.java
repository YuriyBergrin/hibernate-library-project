package com.gmail.bergrin.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book")
@NoArgsConstructor
@Getter
@Setter
public class Book {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "name")
  private String name;
  @Column(name = "publish_year")
  private int publishYear;
  @Column(name = "receive_date")
  private LocalDate receiveDate;

  @ManyToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person owner;

  @Transient
  private boolean expired;

  public Book(String name, int publishYear) {
    this.name = name;
    this.publishYear = publishYear;
  }
}
