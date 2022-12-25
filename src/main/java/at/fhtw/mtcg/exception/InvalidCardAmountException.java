package at.fhtw.mtcg.exception;

public class InvalidCardAmountException extends RuntimeException {
    public InvalidCardAmountException(String message) {
        super(message);
    }
    public InvalidCardAmountException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidCardAmountException(Throwable cause) {
        super(cause);
    }
}
