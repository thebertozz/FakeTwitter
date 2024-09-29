package server;

import utils.Constants;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FakeTwitterServerMain {
    public static void main(String[] args) {
        try {
            FakeTwitterServer fakeTwitterServer = new FakeTwitterServer();

            Registry registry = LocateRegistry.createRegistry(Constants.baseServicePort);

            registry.bind(Constants.serviceRegistryName, fakeTwitterServer);

            System.out.println("Il server è in esecuzione");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
