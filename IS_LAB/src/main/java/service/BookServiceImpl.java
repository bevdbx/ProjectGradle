package service;

import model.AudioBook;
import model.Book;
import model.EBook;
import repository.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    @Override
    public int getRunTime(Long id) {
        if (findById(id) instanceof AudioBook) {
            return ((AudioBook) findById(id)).getRunTime();
        }
        return -1; // Return -1 if the book is not an AudioBook
    }

    @Override
    public String getFormat(Long id) {
        if (findById(id) instanceof EBook) {
            return ((EBook) findById(id)).getFormat();
        }
        return null; // Return null if the book is not an EBook
    }
}