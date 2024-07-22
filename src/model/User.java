package model;

import java.util.List;

public class User {
    String handle;
    String userUuid;
    List<String> followers;
    List<String> following;

    public User(String handle, String userUuid, List<String> followers, List<String> following) {
        this.handle = handle;
        this.userUuid = userUuid;
        this.followers = followers;
        this.following = following;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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
}
