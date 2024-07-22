package model.responses;

public class BooleanResponse extends ServiceResponse<Boolean> {

    public BooleanResponse() {
        super();
    }

    public BooleanResponse(int errorCode, String errorMessage, int elapsed) {
        super(errorCode, errorMessage, elapsed);
    }

    public BooleanResponse(Boolean data, int elapsed) {
        super(data, elapsed);
    }
}