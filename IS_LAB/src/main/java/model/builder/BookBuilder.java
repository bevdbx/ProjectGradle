package model.builder;

import model.AudioBook;
import model.Book;

import java.time.LocalDate;

public abstract class BookBuilder<B extends BookBuilder<B, T>, T extends Book> {
    protected T book;

    public BookBuilder() {
            book = createBookInstance();
            if (book == null) {
                System.err.println("WARNING: createBookInstance() returned null. BookBuilder may not work as expected.");
            }
    }

    protected abstract AudioBook createInstance();

    protected abstract T createBookInstance();

    public B setId(Long id) {
        book.setId(id);
        return self();
    }

    public B setAuthor(String author) {
        book.setAuthor(author);
        return self();
    }

    public B setTitle(String title) {
        book.setTitle(title);
        return self();
    }

    public B setPublishedDate(LocalDate publishedDate) {
        book.setPublishedDate(publishedDate);
        return self();
    }

    public T build() {
        validateBook();
        return book;
    }

    private void validateBook() {
        if (book.getId() == null || book.getAuthor() == null || book.getTitle() == null) {
            throw new IllegalStateException("Incomplete book information for building");
        }
    }

    @SuppressWarnings("unchecked")
    private B self() {
        return (B) this;
    }

    protected T getBook() {
        return book;
    }
}