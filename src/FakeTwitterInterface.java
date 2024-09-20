import model.responses.BooleanResponse;
import model.responses.PostsListResponse;
import model.responses.UsersListResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FakeTwitterInterface extends Remote {
    BooleanResponse registerUser(String handle) throws RemoteException;
    BooleanResponse newPost(String handle, String post) throws RemoteException;
    BooleanResponse deletePost(String postUuid) throws RemoteException;
    BooleanResponse likePost(String handle, String postUuid) throws RemoteException;
    BooleanResponse directMessage(String handle, String message) throws RemoteException;
    UsersListResponse getUsersList() throws RemoteException;
    BooleanResponse followUser(String handle, String userUuid) throws RemoteException;
    BooleanResponse unFollowUser(String handle, String userUuid) throws RemoteException;
    PostsListResponse getLatestPosts() throws RemoteException;
    PostsListResponse getFollowedPosts(String handle) throws RemoteException;
    BooleanResponse commentPost(String handle, String postUuid, String comment) throws RemoteException;
}
