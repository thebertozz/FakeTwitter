package model.responses;

import model.Client;
import model.User;

import java.util.List;

public class ClientsListResponse extends ServiceResponse<List<Client>> {

    public ClientsListResponse() {
        super();
    }

    public ClientsListResponse(int errorCode, String errorMessage, long elapsed) {
        super(errorCode, errorMessage, elapsed);
    }

    public ClientsListResponse(List<Client> data, long elapsed) {
        super(data, elapsed);
    }
}