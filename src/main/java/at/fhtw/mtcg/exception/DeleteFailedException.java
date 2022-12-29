package at.fhtw.mtcg.exception;

public class DeleteFailedException extends RuntimeException {
    public DeleteFailedException(String message) {
        super(message);
    }
    public DeleteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    public DeleteFailedException(Throwable cause) {
        super(cause);
    }
}
