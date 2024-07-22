import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FakeTwitterClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(Constants.registryAddress, Constants.servicePort);

            FakeTwitterInterface testInterface = (FakeTwitterInterface) registry.lookup(Constants.serviceRegistryName);

            //String testResponse = testInterface.saySomething();

            //System.out.println("Server response: " + testResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
