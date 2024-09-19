package model;

import java.util.List;

public class User {
    String userHandle;
    String userUuid;
    List<String> followers;
    List<String> following;
    long createdAt;

    public User(String userHandle, String userUuid, List<String> followers, List<String> following, long createdAt) {
        this.userHandle = userHandle;
        this.userUuid = userUuid;
        this.followers = followers;
        this.following = following;
        this.createdAt = createdAt;
    }

    public User() {}

    public String getUserHandle() {
        return userHandle;
    }

    public void setUserHandle(String userHandle) {
        this.userHandle = userHandle;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void addFollowing(String handle) {
        following.add(handle);
    }

    public void removeFollowing(String handle) {
        following.removeIf(element -> element.equals(handle));
    }

    public static final class Builder {
        private final User user;

        public Builder() {
            user = new User();
        }

        public static Builder anUser() {
            return new Builder();
        }

        public Builder withUserHandle(String userHandle) {
            user.setUserHandle(userHandle);
            return this;
        }

        public Builder withUserUuid(String userUuid) {
            user.setUserUuid(userUuid);
            return this;
        }

        public Builder withFollowers(List<String> followers) {
            user.setFollowers(followers);
            return this;
        }

        public Builder withFollowing(List<String> following) {
            user.setFollowing(following);
            return this;
        }

        public Builder withCreatedAt(long createdAt) {
            user.setCreatedAt(createdAt);
            return this;
        }

        public User build() {
            return user;
        }
    }
}
