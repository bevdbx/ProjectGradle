package org.example;

import database.JDBConnectionWrapper;
import model.AudioBook;
import model.Book;
import model.builder.BookBuilder;
import repository.BookRepository;
import repository.BookRepositoryMySQL;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args){
        //System.out.println("Hello world!");

        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper("test_library");

        BookRepository bookRepository = new BookRepositoryMySQL(connectionWrapper.getConnection());

        Book book = new BookBuilder() {
            @Override
            protected AudioBook createInstance() {
                return null;
            }

            @Override
            protected Book createBookInstance() {
                return null;
            }
        }
                .setAuthor("', '', null); SLEEP(20); --")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(2010, 6, 2))
                .build();

        bookRepository.save(book);
/*
        EBook ebook = new EBookBuilder()
                .setAuthor("AAB")
                .setTitle("Titlu AAB")
                .setPublishedDate(LocalDate.of(2011,7,12))
                .setFormat("Kindle")
                .build();

        bookRepository.save(ebook);

 */

        System.out.println(bookRepository.findAll());


    }
}