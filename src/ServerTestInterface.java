import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerTestInterface extends Remote {
    String saySomething() throws RemoteException;
}
