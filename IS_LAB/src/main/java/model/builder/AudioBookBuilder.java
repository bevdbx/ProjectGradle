package model.builder;

import model.AudioBook;

public class AudioBookBuilder extends BookBuilder<AudioBookBuilder, AudioBook> {

    public AudioBookBuilder setRunTime(int runTime) {
        getBook().setRunTime(runTime);
        return this;
    }

    @Override
    protected AudioBook createInstance() {
        return new AudioBook();
    }

    @Override
    protected AudioBook createBookInstance() {
        return null;
    }

}