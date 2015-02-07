package br.com.betterplace.core.service;

@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

    private Integer errorCode;

    public ServiceException() {
        super();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}