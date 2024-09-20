import model.Comment;
import model.Post;
import model.responses.BooleanResponse;
import model.responses.PostsListResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
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

        System.out.println("--- Welcome, @" + userHandle +"!" + " Cosa vuoi fare oggi? ---");
        System.out.println("1. Nuovo post");
        System.out.println("2. Mostra la lista dei post");
        System.out.println("3. Mostra la lista dei post di chi segui");
        System.out.println("4. Esci");

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine(); //per il \n

        switch (selection) {
            case 1:
                System.out.println("--- A cosa stai pensando? ---");

                String post = keyboardInput.nextLine();

                try {

                    fakeTwitterInterface.newPost(userHandle, post);
                    System.out.println("Post inserito!");

                } catch(RemoteException e) {

                    System.out.println("Errore nella creazione del post: " + e.getMessage());
                }

                showRegisteredUserMenu(userHandle);

                break;
            case 2:
                showGenericPostsList(userHandle);
                break;
            case 3:
                break;
            case 4:
                System.exit(0);
            default:
                System.out.println("Puoi scegliere tra le opzioni 1 e 2.");
        }
    }

    //UI per mostrare la lista dei post

    public static void showGenericPostsList(String userHandle) {

        PostsListResponse posts = null;

        System.out.println("--- Lista dei post ---");

        try {

            posts = fakeTwitterInterface.getLatestPosts();

            for (int i = 0; i < posts.getData().size(); i++) {

                Post currentPost = posts.getData().get(i);

                System.out.println(i + 1 + " - " + "Utente: @" + currentPost.getUserHandle());
                System.out.println(posts.getData().get(i).getMessage());

                if (!currentPost.getCommentList().isEmpty()) {

                    System.out.println("\t--- Commenti al post ---");

                    for (Comment comment: currentPost.getCommentList()) {
                        System.out.println("\tUtente: @" + comment.getUserHandle() + ": " + comment.getMessage());
                    }
                }

                System.out.println("Like: " + currentPost.getLikesCount());
            }

        } catch (RemoteException e) {
            System.out.println("Errore nel recupero dei post: " + e.getMessage());
        }

        //Opzioni

        System.out.println("--- Seleziona l'opzione desiderata ---");
        System.out.println("1. Commenta post");
        System.out.println("2. Metti like ad un post");
        System.out.println("3. Segui un utente");
        System.out.println("4. Torna indietro");

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine(); //per il \n

        switch (selection) {
            case 1:
                System.out.println("--- Scegli il numero del post che vuoi commentare ---");

                int numberForComment = keyboardInput.nextInt();
                keyboardInput.nextLine(); //per il \n

                System.out.println("--- Inserisci il commento ---");
                String message = keyboardInput.nextLine();

                try {

                    //TODO: chiamare la comment post passando il commento

                    fakeTwitterInterface.commentPost(userHandle, posts.getData().get(numberForComment - 1).getPostUuid(), message);

                    System.out.println("Commento aggiunto!");

                } catch(Exception e) {

                    System.out.println("Errore nel commentare il post: " + e.getMessage());
                }

                showGenericPostsList(userHandle);

                break;

            case 2:

                System.out.println("--- Scegli il numero del post al quale vuoi mettere like ---");

                int numberForLike = keyboardInput.nextInt();
                keyboardInput.nextLine(); //per il \n

                try {

                    fakeTwitterInterface.likePost(userHandle, posts.getData().get(numberForLike - 1).getPostUuid());

                    System.out.println("Like aggiunto!");

                    showGenericPostsList(userHandle);

                } catch(Exception e) {

                    System.out.println("Errore nel like del post: " + e.getMessage());
                }

                break;
            case 3:
                break;
            case 4:
                showRegisteredUserMenu(userHandle);
                break;
            default:
                System.out.println("Puoi scegliere tra le opzioni 1,2,3");
        }
    }

    //UI per mostrare la lista dei post da utente non registrato

    public static void showRecentPostsOnly() {

    }
}
