package at.fhtw.mtcg.exception;

public class InsertFailedException extends RuntimeException {
    public InsertFailedException(String message) {
        super(message);
    }
    public InsertFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    public InsertFailedException(Throwable cause) {
        super(cause);
    }
}
