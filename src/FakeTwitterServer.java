import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FakeTwitterServer {
    public static void main(String[] args) {
        try {
            FakeTwitter test = new FakeTwitter();

            Registry registry = LocateRegistry.createRegistry(Constants.servicePort);

            registry.bind(Constants.serviceRegistryName, test);

            System.out.println("Il server è in esecuzione");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
