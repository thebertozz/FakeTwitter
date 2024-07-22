import model.responses.BooleanResponse;
import model.responses.PostsListResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FakeTwitterInterface extends Remote {
    BooleanResponse registerUser(String handle) throws RemoteException;
    BooleanResponse newPost(String post) throws RemoteException;
    BooleanResponse deletePost(String postUuid) throws RemoteException;
    BooleanResponse directMessage(String handle, String message) throws RemoteException;
    BooleanResponse followUser(String handle) throws RemoteException;
    BooleanResponse unFollowUser(String handle) throws RemoteException;
    PostsListResponse getLatestPosts(long since) throws RemoteException;
    PostsListResponse getFollowedPosts(long since) throws RemoteException;
    BooleanResponse commentPost(String postUuid, String comment);
}
