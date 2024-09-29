package model.responses;

public class IntegerResponse extends ServiceResponse<Integer> {

    public IntegerResponse() {
        super();
    }

    public IntegerResponse(int errorCode, String errorMessage, long elapsed) {
        super(errorCode, errorMessage, elapsed);
    }

    public IntegerResponse(int data, long elapsed) {
        super(data, elapsed);
    }
}