package model.responses;

public class BooleanResponse extends ServiceResponse<Boolean> {

    public BooleanResponse() {
        super();
    }

    public BooleanResponse(int errorCode, String errorMessage, long elapsed) {
        super(errorCode, errorMessage, elapsed);
    }

    public BooleanResponse(Boolean data, long elapsed) {
        super(data, elapsed);
    }
}