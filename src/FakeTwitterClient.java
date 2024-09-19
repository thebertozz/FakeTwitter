import model.responses.BooleanResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class FakeTwitterClient {

    public static FakeTwitterInterface fakeTwitterInterface;

    static Scanner keyboardInput = new Scanner(System.in);

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

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine();

        switch (selection) {
            case 1:
                showRegistrationMenu();
                break;
            case 2:
                showRecentPostsOnly();
                break;
            default:
                System.out.println("Puoi scegliere tra le opzioni 1 e 2.");
                showUserInterface();
        }
    }

    //UI per registrare l'utente

    public static void showRegistrationMenu() {

        System.out.println("--- Inserisci l'handle @ che vuoi utilizzare ---");

        String userHandle = keyboardInput.nextLine();

        try {

           BooleanResponse response = fakeTwitterInterface.registerUser(userHandle);

           if (response.getData()) {

               System.out.println("Utente " + userHandle + " registrato!");
               System.out.println();

               showRegisteredUserMenu(userHandle);

           } else {
               System.out.println("L'utente " + userHandle + " esiste già");
           }

        } catch (RemoteException e) {

            System.out.println("Errore durante la registrazione: " + e.getMessage());
        }
    }

    //UI per l'utente registrato

    public static void showRegisteredUserMenu(String userHandle) {

        System.out.println("--- Cosa vuoi fare oggi? ---");
        System.out.println("1. Nuovo post");
        System.out.println("2. Mostra la lista dei post");
        System.out.println("3. Mostra la lista dei post di chi segui");

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine(); //per il \n

        switch (selection) {
            case 1:
                System.out.println("--- A cosa stai pensando? ---");

                String post = keyboardInput.nextLine();

                try {

                    fakeTwitterInterface.newPost(userHandle, post);

                } catch(RemoteException e) {

                    System.out.println("Errore nella creazione del post: " + e.getMessage());
                }

                break;
            case 2:
                showRecentPostsOnly();
                break;
            case 3:
                break;
            default:
                System.out.println("Puoi scegliere tra le opzioni 1 e 2.");
        }
    }

    //UI per mostrare la lista dei post da utente non registrato

    public static void showRecentPostsOnly() {

    }
}
