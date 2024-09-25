import model.responses.BooleanResponse;
import model.responses.PostsListResponse;
import model.responses.UsersListResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FakeTwitterInterface extends Remote {
    BooleanResponse registerUser(String userHandle) throws RemoteException;
    BooleanResponse login(String userHandle) throws RemoteException;
    BooleanResponse newPost(String userHandle, String post) throws RemoteException;
    BooleanResponse deletePost(String userHandle, String postUuid) throws RemoteException;
    BooleanResponse likePost(String userHandle, String postUuid) throws RemoteException;
    BooleanResponse followUser(String follower, String followed) throws RemoteException;
    BooleanResponse unFollowUser(String follower, String followed) throws RemoteException;
    PostsListResponse getLatestPosts() throws RemoteException;
    PostsListResponse getFollowedPosts(String userHandle) throws RemoteException;
    BooleanResponse commentPost(String userHandle, String postUuid, String comment) throws RemoteException;
    UsersListResponse getUsersList(String userHandle) throws RemoteException;
    BooleanResponse directMessage(String userHandle, String message) throws RemoteException;
}
