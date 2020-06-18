package io.bitpark.redditclone.exceptions;

public class SubRedditNotFoundException extends RuntimeException {
    public SubRedditNotFoundException(String message) {
        super(message);
    }
}
