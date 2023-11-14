package model.builder;

import model.AudioBook;
import model.EBook;

public class EBookBuilder extends BookBuilder<EBookBuilder, EBook> {

    public EBookBuilder setFormat(String format) {
        getBook().setFormat(format);
        return this;
    }

    @Override
    protected AudioBook createInstance() {
        return new AudioBook();
    }

    @Override
    protected EBook createBookInstance() {
        return null;
    }
}