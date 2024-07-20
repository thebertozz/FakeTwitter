import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerTest {
    public static void main(String[] args) {
        try {
            ServerTestImplementation test = new ServerTestImplementation();

            Registry registry = LocateRegistry.createRegistry(Constants.servicePort);

            registry.bind(Constants.serviceRegistryName, test);

            System.out.println("Server started!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
