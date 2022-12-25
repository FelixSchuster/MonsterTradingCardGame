package at.fhtw.mtcg.exception;

public class InvalidCardException extends RuntimeException {
    public InvalidCardException(String message) {
        super(message);
    }
    public InvalidCardException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidCardException(Throwable cause) {
        super(cause);
    }
}
