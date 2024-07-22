package model.responses;

abstract class ServiceResponse<T> {

    private boolean success;
    private int errorCode;
    private String errorMessage;
    private T data;
    private int elapsed;

    public ServiceResponse() {
    }

    //In caso di errore
    public ServiceResponse(int errorCode, String errorMessage, int elapsed) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.elapsed = elapsed;
        this.data = null;
        this.success = false;
    }

    //In caso di successo, data sarà popolato
    public ServiceResponse(T data, int elapsed) {
        this.data = data;
        this.elapsed = elapsed;
        this.errorCode = 0;
        this.success = true;
        this.errorMessage = null;
    }

    //Getter e setter

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getElapsed() {
        return elapsed;
    }

    public void setElapsed(int elapsed) {
        this.elapsed = elapsed;
    }
}