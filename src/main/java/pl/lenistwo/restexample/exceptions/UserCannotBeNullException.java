package pl.lenistwo.restexample.exceptions;

public class UserCannotBeNullException extends RuntimeException {

    public UserCannotBeNullException() {
    }

    public UserCannotBeNullException(String message) {
        super(message);
    }
}
