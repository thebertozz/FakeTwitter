package client;

import model.responses.BooleanResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FakeTwitterClient extends UnicastRemoteObject implements FakeTwitterClientInterface {

    protected FakeTwitterClient() throws RemoteException {
        super();
    }

    @Override
    public BooleanResponse peerToPeerInit(String userHandle, String clientPort, String message) throws RemoteException {

    return null;
    }

    @Override
    public BooleanResponse sendMessage(String userHandle, String clientPort, String message) throws RemoteException {
        return null;
    }
}
