import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientTest {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(Constants.registryAddress, Constants.servicePort);

            ServerTestInterface testInterface = (ServerTestInterface) registry.lookup(Constants.serviceRegistryName);

            String testResponse = testInterface.saySomething();

            System.out.println("Server response: " + testResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
