package client;

import model.responses.BooleanResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FakeTwitterClientInterface extends Remote {
    BooleanResponse peerToPeerInit(String userHandle, int clientPort, String message) throws RemoteException;
    BooleanResponse sendMessage(String userHandle, String message) throws RemoteException;
}
