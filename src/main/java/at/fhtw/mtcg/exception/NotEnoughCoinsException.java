package at.fhtw.mtcg.exception;

public class NotEnoughCoinsException extends RuntimeException{
    public NotEnoughCoinsException(String message) {
        super(message);
    }
    public NotEnoughCoinsException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotEnoughCoinsException(Throwable cause) {
        super(cause);
    }
}
