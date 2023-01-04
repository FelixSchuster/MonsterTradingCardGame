package at.fhtw.mtcg.exception;

public class InvalidTradingDealException extends RuntimeException {
    public InvalidTradingDealException(String message) {
        super(message);
    }
    public InvalidTradingDealException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidTradingDealException(Throwable cause) {
        super(cause);
    }
}
