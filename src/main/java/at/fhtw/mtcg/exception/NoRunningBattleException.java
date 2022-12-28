package at.fhtw.mtcg.exception;

public class NoRunningBattleException extends RuntimeException {
    public NoRunningBattleException(String message) {
        super(message);
    }
    public NoRunningBattleException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoRunningBattleException(Throwable cause) {
        super(cause);
    }
}
