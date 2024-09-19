package model;

import java.io.Serializable;

public class Comment implements Serializable {
    String commentUuid;
    String userHandle;
    String message;
    long createdAt;

    public Comment(String commentUuid, String userHandle, String message, long createdAt) {
        this.commentUuid = commentUuid;
        this.userHandle = userHandle;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getCommentUuid() {
        return commentUuid;
    }

    public void setCommentUuid(String commentUuid) {
        this.commentUuid = commentUuid;
    }

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
