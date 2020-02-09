package pl.lenistwo.restexample.exceptions;

public class OffsetCannotBeLessThanZeroException extends RuntimeException{

    public OffsetCannotBeLessThanZeroException() {
    }

    public OffsetCannotBeLessThanZeroException(String message) {
        super(message);
    }
}
