package lk.ijse.dep11.edupanel.exception;

public class AppException extends RuntimeException {
    private final int errCode;

    public AppException(int errCode) {
        this.errCode = errCode;
    }

    public AppException(int errCode,String message) {
        super(message);
        this.errCode = errCode;
    }

    public AppException(String message, Throwable cause, int errCode) {
        super(message, cause);
        this.errCode = errCode;
    }

    public AppException(Throwable cause, int errCode) {
        super(cause);
        this.errCode = errCode;
    }
}
