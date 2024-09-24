import model.Comment;
import model.Post;
import model.responses.BooleanResponse;
import model.responses.PostsListResponse;

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

            showRootMainMenu();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errore nell'inizializzazione del servizio");
        }
    }

    //Punto di ingresso per l'interfaccia utente

    public static void showRootMainMenu() {

        //Menu principale

        System.out.println("--- Benvenuti in Fake Twitter! --- Registrati per utilizzare tutte le funzionalità");
        System.out.println("1. Registrati");
        System.out.println("2. Effettua il login");
        //System.out.println("2. Mostra la lista di post recenti");

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine();

        switch (selection) {
            case 1:
                showRegistrationMenu();
                break;
            case 2:
                showLoginMenu();
                break;
            default:
                System.out.println("Puoi scegliere tra le opzioni 1 o 2.");
                showRootMainMenu();
        }
    }

    //UI per la login

    public static void showLoginMenu() {

        System.out.println("--- Inserisci il tuo nome utente (handle) ---");

        String userHandle = keyboardInput.nextLine();

        try {

            BooleanResponse response = fakeTwitterInterface.login(userHandle);

            if (response.getData()) {

                System.out.println("Welcome back, " + userHandle + "!");
                System.out.println();

                showRegisteredUserMenu(userHandle);

            } else {
                System.out.println("L'utente " + userHandle + " non esiste, effettua la registrazione");
                showRootMainMenu();
            }

        } catch (RemoteException e) {

            System.out.println("Errore durante il login: " + e.getMessage());
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
               System.out.println("L'utente " + userHandle + " esiste già, effettua il login");
               showRootMainMenu();
           }

        } catch (RemoteException e) {

            System.out.println("Errore durante la registrazione: " + e.getMessage());
        }
    }

    //UI per l'utente registrato

    public static void showRegisteredUserMenu(String userHandle) {

        System.out.println("--- Welcome, @" + userHandle +"!" + " Cosa vuoi fare oggi? ---");
        System.out.println("1. Nuovo post");
        System.out.println("2. Mostra la lista di tutti i post");
        System.out.println("3. Mostra la lista dei post di chi segui");
        //System.out.println("4. Mostra la lista degli utenti");
        System.out.println("4. Esci");

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine(); //per il \n

        switch (selection) {
            case 1:
                System.out.println("--- A cosa stai pensando? ---");

                String post = keyboardInput.nextLine();

                try {

                    BooleanResponse response = fakeTwitterInterface.newPost(userHandle, post);
                    if (response.isSuccess()) {
                        System.out.println("Post inserito!");
                    } else {
                        System.out.println("Errore nella creazione del post");
                    }

                } catch(RemoteException e) {

                    System.out.println("Eccezione nella creazione del post: " + e.getMessage());
                }

                showRegisteredUserMenu(userHandle);

                break;
            case 2:
                showPostsList(userHandle, false);
                break;
            case 3:
                showPostsList(userHandle, true);
                break;
            case 4:
                System.exit(0);
            default:
                System.out.println("Puoi scegliere tra le opzioni 1,2,3 o 4.");
        }
    }

    //UI per mostrare la lista dei post

    public static void showPostsList(String userHandle, boolean onlyFollowedUsers) {

        PostsListResponse posts = null;

        System.out.println("--- Lista dei post ---");

        try {

            posts = onlyFollowedUsers ? fakeTwitterInterface.getFollowedPosts(userHandle) : fakeTwitterInterface.getLatestPosts();

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
            System.out.println("Eccezione nel recupero dei post: " + e.getMessage());
        }

        if (posts.getData().isEmpty()) {
            System.out.println();
            System.out.println("La lista dei post è ancora vuota. Aggiungi tu il primo!");
            System.out.println();

            showRegisteredUserMenu(userHandle);

        } else {

        //Opzioni per i post

        System.out.println("--- Seleziona l'opzione desiderata ---");
        System.out.println("1. Commenta post");
        System.out.println("2. Metti like ad un post");
        System.out.println("3. Segui un utente");
        System.out.println("4. Smetti di seguire un utente");
        System.out.println("5. Elimina un tuo post");
        System.out.println("6. Torna indietro");

        int selection = keyboardInput.nextInt();
        keyboardInput.nextLine(); //per il \n

            switch (selection) {
                case 1: //Commento

                    System.out.println("--- Scegli il numero del post che vuoi commentare ---");

                    int numberForComment = keyboardInput.nextInt();
                    keyboardInput.nextLine(); //per il \n

                    System.out.println("--- Inserisci il commento ---");
                    String message = keyboardInput.nextLine();

                    try {

                        BooleanResponse response = fakeTwitterInterface.commentPost(userHandle, posts.getData().get(numberForComment - 1).getPostUuid(), message);

                        if (response.isSuccess()) {
                            System.out.println("Commento aggiunto!");
                        } else {
                            System.out.println("Errore nel commentare il post");
                        }

                    } catch(Exception e) {

                        System.out.println("Eccezione nel commentare il post: " + e.getMessage());
                    }

                    showPostsList(userHandle, onlyFollowedUsers);

                    break;

                case 2: //Like

                    System.out.println("--- Scegli il numero del post al quale vuoi mettere like ---");

                    int numberForLike = keyboardInput.nextInt();
                    keyboardInput.nextLine(); //per il \n

                    try {

                        BooleanResponse response = fakeTwitterInterface.likePost(userHandle, posts.getData().get(numberForLike - 1).getPostUuid());

                        if (response.isSuccess()) {
                            System.out.println("Like aggiunto!");
                        } else {
                            System.out.println("Errore nel like del post");
                        }

                        showPostsList(userHandle, onlyFollowedUsers);

                    } catch(Exception e) {

                        System.out.println("Eccezione nel like del post: " + e.getMessage());
                    }

                    break;
                case 3: //Segui un utente

                    System.out.println("--- Scegli il numero del post relativo all'utente che vuoi seguire ---");

                    int numberForFollow = keyboardInput.nextInt();
                    keyboardInput.nextLine(); //per il \n

                    try {

                        BooleanResponse response = fakeTwitterInterface.followUser(userHandle, posts.getData().get(numberForFollow - 1).getUserHandle());

                        if (response.isSuccess()) {
                            System.out.println("Follow aggiunto!");
                        } else {
                            System.out.println("Errore nel follow dell'utente");
                        }

                        showPostsList(userHandle, onlyFollowedUsers);

                    } catch(Exception e) {

                        System.out.println("Eccezione nel follow dell'utente: " + e.getMessage());
                    }
                    break;

                case 4: //Smettere di seguire un utente

                    System.out.println("--- Scegli il numero del post relativo all'utente che vuoi smettere di seguire ---");

                    int numberForUnFollow = keyboardInput.nextInt();
                    keyboardInput.nextLine(); //per il \n

                    try {

                        BooleanResponse response = fakeTwitterInterface.unFollowUser(userHandle, posts.getData().get(numberForUnFollow - 1).getUserHandle());

                        if (response.isSuccess()) {
                            System.out.println("Follow rimosso!");
                        } else {
                            System.out.println("Errore nella rimozione del follow dell'utente");
                        }

                        showPostsList(userHandle, onlyFollowedUsers);

                    } catch(Exception e) {

                        System.out.println("Eccezione nella rimozione del follow dell'utente: " + e.getMessage());
                    }

                    break;
                case 5: //Elimina un post personale
                    System.out.println("--- Scegli il numero del post che vuoi eliminare (se pubblicato da te) ---");

                    int numberForDeletion = keyboardInput.nextInt();
                    keyboardInput.nextLine(); //per il \n

                    try {

                        BooleanResponse response = fakeTwitterInterface.deletePost(userHandle, posts.getData().get(numberForDeletion - 1).getPostUuid());

                        if (response.isSuccess()) {
                            System.out.println("Post rimosso!");
                        } else {
                            System.out.println("Errore nella rimozione del post, puoi eliminare solo i post pubblicati da te stesso.");
                        }

                        showPostsList(userHandle, onlyFollowedUsers);

                    } catch(Exception e) {

                        System.out.println("Errore nella rimozione del post: " + e.getMessage());
                    }
                    break;
                case 6: //Torna indietro
                    showRegisteredUserMenu(userHandle);
                    break;
                default:
                    System.out.println("Puoi scegliere tra le opzioni 1,2,3,4,5 o 6");
            }
        }
    }

    //UI per mostrare la lista dei post da utente non registrato

    public static void showRecentPostsOnly() {

    }
}
