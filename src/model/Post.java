package model;

import java.util.List;

public class Post {
    String postUuid;
    String message;
    String userHandle;
    List<Comment> commentList;
    int likesCount;

    List<String> likedBy;
    long createdAt;

    public Post(String postUuid, String message, long createdAt, String userHandle, List<Comment> commentList, int likesCount, List<String> likedBy) {
        this.postUuid = postUuid;
        this.message = message;
        this.createdAt = createdAt;
        this.userHandle = userHandle;
        this.commentList = commentList;
        this.likesCount = likesCount;
        this.likedBy = likedBy;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        this.likedBy = likedBy;
    }
}