package model;

import java.util.List;
import java.util.UUID;

public class Post {
    String postUuid;
    String message;
    String userHandle;
    List<Comment> commentList;
    List<String> likedBy;
    int likesCount;
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

    public Post() {}

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

    public void addLike(String userHandle) {
        this.likedBy.add(userHandle);
        this.likesCount += 1;
    }

    public void removeLike(String handle) {
        this.likedBy.removeIf(element -> element.equals(handle));
        this.likesCount -= 1;
    }

    public void addComment(String handle, String comment) {
        this.commentList.add(new Comment(UUID.randomUUID().toString(), handle, comment, System.currentTimeMillis()));
    }

    public static final class Builder {
        private final Post post;

        public Builder() {
            post = new Post();
        }

        public static Builder aPost() {
            return new Builder();
        }

        public Builder withPostUuid(String postUuid) {
            post.setPostUuid(postUuid);
            return this;
        }

        public Builder withMessage(String message) {
            post.setMessage(message);
            return this;
        }

        public Builder withUserHandle(String userHandle) {
            post.setUserHandle(userHandle);
            return this;
        }

        public Builder withCommentList(List<Comment> commentList) {
            post.setCommentList(commentList);
            return this;
        }

        public Builder withLikesCount(int likesCount) {
            post.setLikesCount(likesCount);
            return this;
        }

        public Builder withLikedBy(List<String> likedBy) {
            post.setLikedBy(likedBy);
            return this;
        }

        public Builder withCreatedAt(long createdAt) {
            post.setCreatedAt(createdAt);
            return this;
        }

        public Post build() {
            return post;
        }
    }
}