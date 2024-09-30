package client;

import model.responses.BooleanResponse;
import utils.Constants;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FakeTwitterClient extends UnicastRemoteObject implements FakeTwitterClientInterface {

    protected FakeTwitterClient() throws RemoteException {
        super();
    }

    @Override
    public BooleanResponse peerToPeer(String userHandle, int clientPort, String message) throws RemoteException {

        long startTime = System.currentTimeMillis();

        try {

            FakeTwitterClientInterface fakeTwitterClientInterface;

            Registry registry = LocateRegistry.getRegistry(Constants.defaultHost, clientPort);

            fakeTwitterClientInterface = (FakeTwitterClientInterface) registry.lookup(Constants.clientsRegistryName);

            fakeTwitterClientInterface.sendMessage(userHandle, message);

            return new BooleanResponse(true, System.currentTimeMillis() - startTime);

        } catch (NotBoundException e) {

            e.printStackTrace();

            return new BooleanResponse(false, System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public BooleanResponse sendMessage(String userHandle, String message) throws RemoteException {

        long startTime = System.currentTimeMillis();

        System.out.println();
        System.out.println("Messaggio ricevuto da @" + userHandle + ": " + message);
        //System.out.println();

        return new BooleanResponse(true, System.currentTimeMillis() - startTime);
    }
}
