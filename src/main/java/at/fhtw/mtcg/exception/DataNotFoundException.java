package at.fhtw.mtcg.exception;
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataNotFoundException(Throwable cause) {
        super(cause);
    }
}
