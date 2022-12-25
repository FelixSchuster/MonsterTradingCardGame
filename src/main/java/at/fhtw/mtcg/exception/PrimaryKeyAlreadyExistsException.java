package at.fhtw.mtcg.exception;
public class PrimaryKeyAlreadyExistsException extends RuntimeException {
    public PrimaryKeyAlreadyExistsException(String message) {
        super(message);
    }
    public PrimaryKeyAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
    public PrimaryKeyAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
