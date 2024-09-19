import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FakeTwitterServer {
    public static void main(String[] args) {
        try {
            FakeTwitter fakeTwitter = new FakeTwitter();

            Registry registry = LocateRegistry.createRegistry(Constants.servicePort);

            registry.bind(Constants.serviceRegistryName, fakeTwitter);

            System.out.println("Il server è in esecuzione");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
