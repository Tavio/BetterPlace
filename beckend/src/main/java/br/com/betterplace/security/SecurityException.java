package br.com.betterplace.security;

public class SecurityException extends RuntimeException {

    private static final long serialVersionUID = -3172761390154852429L;

    public SecurityException() {
        super();
    }

    public SecurityException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }
}