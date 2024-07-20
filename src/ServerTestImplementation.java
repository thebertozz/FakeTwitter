import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerTestImplementation extends UnicastRemoteObject implements ServerTestInterface {

    protected ServerTestImplementation() throws RemoteException {
        super();
    }

    @Override
    public String saySomething() throws RemoteException {
        return "Hello, World from a very remote server";
    }
}
