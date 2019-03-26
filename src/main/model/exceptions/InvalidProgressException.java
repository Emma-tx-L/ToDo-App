package model.exceptions;

public class InvalidProgressException extends IllegalArgumentException {
    public InvalidProgressException() {
    }

    public InvalidProgressException(String message) {
        super(message);
    }
}
