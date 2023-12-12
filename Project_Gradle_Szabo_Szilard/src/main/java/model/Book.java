package model;

import java.time.LocalDate;
import java.util.Date;

// Java Bean


public class Book {

    private Long id;

    private String author;

    private String title;

    private LocalDate publishedDate;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return String.format("Book ID: %d | Author: %s | Title: %s | Published Date: %s | Quantity: %d", id, author, title, publishedDate, quantity);
    }
}
