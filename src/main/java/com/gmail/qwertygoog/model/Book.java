package com.gmail.qwertygoog.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Cacheable
public class Book extends PanacheEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private Author author;

    public Book() {
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
