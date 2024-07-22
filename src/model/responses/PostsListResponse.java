package model.responses;

import model.Post;

import java.util.List;

public class PostsListResponse extends ServiceResponse<List<Post>> {

    public PostsListResponse() {
        super();
    }

    public PostsListResponse(int errorCode, String errorMessage, int elapsed) {
        super(errorCode, errorMessage, elapsed);
    }

    public PostsListResponse(List<Post> data, int elapsed) {
        super(data, elapsed);
    }
}