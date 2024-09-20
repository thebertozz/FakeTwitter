package model.responses;

import model.Post;
import model.User;

import java.util.List;

public class UsersListResponse extends ServiceResponse<List<User>> {

    public UsersListResponse() {
        super();
    }

    public UsersListResponse(int errorCode, String errorMessage, int elapsed) {
        super(errorCode, errorMessage, elapsed);
    }

    public UsersListResponse(List<User> data, int elapsed) {
        super(data, elapsed);
    }
}