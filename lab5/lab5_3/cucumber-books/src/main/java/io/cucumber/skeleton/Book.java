package io.cucumber.skeleton;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private LocalDate published;

    public Book() {
    }

    public Book(String title, String author, LocalDate published) {
        this.title = title;
        this.author = author;
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublished() {
        return published;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublished(LocalDate published) {
        this.published = published;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", published=" + published +
                '}';
    }
}