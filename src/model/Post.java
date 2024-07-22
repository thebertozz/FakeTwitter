package model;

import java.util.List;

public class Post {
    String postUuid;
    String message;
    String timestamp;
    String userHandle;
    List<Comment> commentList;
    int likes;
    long createdAt;

    public Post(String postUuid, String message, String timestamp, String userHandle, List<Comment> commentList, int likes, long createdAt) {
        this.postUuid = postUuid;
        this.message = message;
        this.timestamp = timestamp;
        this.userHandle = userHandle;
        this.commentList = commentList;
        this.likes = likes;
        this.createdAt = createdAt;
    }

    public String getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(String postUuid) {
        this.postUuid = postUuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}