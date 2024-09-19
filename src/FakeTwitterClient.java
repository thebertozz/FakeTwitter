import model.responses.BooleanResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class FakeTwitterClient {

    public static FakeTwitterInterface fakeTwitterInterface;

    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.getRegistry(Constants.registryAddress, Constants.servicePort);

            fakeTwitterInterface = (FakeTwitterInterface) registry.lookup(Constants.serviceRegistryName);

            showUserInterface();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'inizializzazione del servizio");
        }
    }

    //Punto di ingresso per l'interfaccia utente

    public static void showUserInterface() {

        //Menu principale

        System.out.println("--- Benvenuti in Fake Twitter! --- Registrati per utilizzare tutte le funzionalità");
        System.out.println("1. Registrati");
        System.out.println("2. Mostra la lista di post recenti");

        Scanner keyboardInput = new Scanner(System.in);

        int selection = keyboardInput.nextInt();

        switch (selection) {
            case 1:
                showRegistrationMenu();
                break;
            case 2:
                showRecentPostsOnly();
                break;
            default:
                System.out.println("Puoi scegliere tra le opzioni 1 e 2.");
        }
    }

    //UI per registrare l'utente

    public static void showRegistrationMenu() {

        System.out.println("--- Inserisci l'handle @ che vuoi utilizzare ---");

        Scanner keyboardInput = new Scanner(System.in);

        String userHandle = keyboardInput.nextLine();

        try {

           BooleanResponse response = fakeTwitterInterface.registerUser(userHandle);

           if (response.getData()) {
               showRegisteredUserMenu();
           }

        } catch (RemoteException e) {

            System.out.println("Errore durante la registrazione.");
        }
    }

    //UI per l'utente registrato

    public static void showRegisteredUserMenu() {

        System.out.println("--- Fake Twitter ---");
    }

    //UI per mostrare la lista dei post da utente non registrato

    public static void showRecentPostsOnly() {

    }
}
