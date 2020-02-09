package pl.lenistwo.restexample.exceptions;

public class LimitCannotBeLessThanOneException extends RuntimeException {

    public LimitCannotBeLessThanOneException() {
    }

    public LimitCannotBeLessThanOneException(String message) {
        super(message);
    }
}
