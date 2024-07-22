import model.responses.BooleanResponse;
import model.responses.PostsListResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FakeTwitter extends UnicastRemoteObject implements FakeTwitterInterface {

    //TODO: inserire DAO per accesso al DB

    protected FakeTwitter() throws RemoteException {
        super();
    }

    @Override
    public BooleanResponse registerUser(String handle) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse newPost(String post) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse deletePost(String postUuid) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse directMessage(String handle, String message) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse followUser(String handle) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse unFollowUser(String handle) throws RemoteException {
        return null;
    }

    @Override
    public PostsListResponse getLatestPosts(long since) throws RemoteException {
        return null;
    }

    @Override
    public PostsListResponse getFollowedPosts(long since) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse commentPost(String postUuid, String comment) {
        return null;
    }
}
