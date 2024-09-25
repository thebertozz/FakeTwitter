import model.Comment;
import model.Post;
import model.User;
import model.responses.BooleanResponse;
import model.responses.PostsListResponse;
import model.responses.UsersListResponse;

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

		System.out.println("--- Benvenuti in Fake Twitter! --- Registrati o effettua il login");
		System.out.println();
		System.out.println("1. Registrati");
		System.out.println("2. Effettua il login");

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

				System.out.println();
				System.out.println("Welcome back, " + userHandle + "!");
				System.out.println();

				showRegisteredUserMenu(userHandle);

			} else {
				System.out.println();
				System.out.println("L'utente " + userHandle + " non esiste, effettua la registrazione");
				System.out.println();
				showRootMainMenu();
			}

		} catch (RemoteException e) {

			System.out.println();
			System.out.println("Errore durante il login: " + e.getMessage());
			System.out.println();
		}
	}

	//UI per registrare l'utente

	public static void showRegistrationMenu() {

		System.out.println("--- Inserisci l'handle @ che vuoi utilizzare ---");

		String userHandle = keyboardInput.nextLine();

		try {

			BooleanResponse response = fakeTwitterInterface.registerUser(userHandle);

			if (response.getData()) {

				System.out.println();
				System.out.println("Utente " + userHandle + " registrato!");
				System.out.println();

				showRegisteredUserMenu(userHandle);

			} else {
				System.out.println();
				System.out.println("L'utente " + userHandle + " esiste già, effettua il login");
				showRootMainMenu();
			}

		} catch (RemoteException e) {
			System.out.println();
			System.out.println("Errore durante la registrazione: " + e.getMessage());
		}
	}

	//UI per l'utente registrato

	public static void showRegisteredUserMenu(String userHandle) {

		System.out.println("--- Cosa vuoi fare oggi, " + "@" + userHandle + "? ---");
		System.out.println();
		System.out.println("1. Nuovo post");
		System.out.println("2. Mostra la lista di tutti i post");
		System.out.println("3. Mostra la lista dei post di chi segui");
		System.out.println("4. Mostra la lista degli utenti del servizio");
		System.out.println("5. Esci");

		int selection = keyboardInput.nextInt();
		keyboardInput.nextLine(); //per il \n

		switch (selection) {
			case 1:
				System.out.println("--- A cosa stai pensando? ---");

				String post = keyboardInput.nextLine();

				try {

					BooleanResponse response = fakeTwitterInterface.newPost(userHandle, post);
					if (response.isSuccess()) {
						System.out.println();
						System.out.println("Post inserito!");
						System.out.println();
					} else {
						System.out.println("Errore nella creazione del post");
					}

				} catch (RemoteException e) {

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
				showUsersList(userHandle);
				break;
			case 5:
				System.out.println("Grazie per aver utilizzato FakeTwitter!");
				System.exit(0);
			default:
				System.out.println("Puoi scegliere tra le opzioni 1,2,3,4 o 5.");
		}
	}

	//UI per mostrare la lista dei post

	public static void showPostsList(String userHandle, boolean onlyFollowedUsers) {

		PostsListResponse posts = null;

		System.out.println();
		System.out.println("--- Lista dei post ---");
		System.out.println();

		try {

			posts = onlyFollowedUsers ? fakeTwitterInterface.getFollowedPosts(userHandle) : fakeTwitterInterface.getLatestPosts();

			for (int i = 0; i < posts.getData().size(); i++) {

				Post currentPost = posts.getData().get(i);

				System.out.println(i + 1 + " - " + "Utente: @" + currentPost.getUserHandle());
				System.out.println("\tMessaggio: " + posts.getData().get(i).getMessage());
				System.out.println("\tLike: " + currentPost.getLikesCount());

				if (!currentPost.getCommentList().isEmpty()) {

					System.out.println("\t\t--- Commenti al post ---");

					for (Comment comment : currentPost.getCommentList()) {
						System.out.println("\t\t@" + comment.getUserHandle() + ": " + comment.getMessage());
					}
				}

                System.out.println();
			}

		} catch (RemoteException e) {
			System.out.println("Eccezione nel recupero dei post: " + e.getMessage());
		}

		if (posts.getData().isEmpty()) {
			if (onlyFollowedUsers) {
				System.out.println("La lista dei post è ancora vuota. Inizia a seguire qualche utente!");
			} else {
				System.out.println("La lista dei post è ancora vuota. Aggiungi tu il primo!");
			}
			System.out.println();

			showRegisteredUserMenu(userHandle);

		} else {

			//Opzioni per i post

			System.out.println("--- Seleziona l'opzione desiderata ---");
			System.out.println();
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

					if (numberForComment <= posts.getData().size()) {

						System.out.println("--- Inserisci il commento ---");
						String message = keyboardInput.nextLine();

						try {

							BooleanResponse response = fakeTwitterInterface.commentPost(userHandle, posts.getData().get(numberForComment - 1).getPostUuid(), message);

							if (response.getData()) {
								System.out.println();
								System.out.println("Commento aggiunto!");
							} else {
								System.out.println();
								System.out.println("Errore nel commentare il post");
							}

						} catch (Exception e) {
							System.out.println();
							System.out.println("Eccezione nel commentare il post: " + e.getMessage());
						}
					} else {
						System.out.println();
						System.out.println("Seleziona un post esistente!");
					}

					showPostsList(userHandle, onlyFollowedUsers);

					break;

				case 2: //Like

					System.out.println("--- Scegli il numero del post al quale vuoi mettere like ---");

					int numberForLike = keyboardInput.nextInt();
					keyboardInput.nextLine(); //per il \n

                    if (numberForLike <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterInterface.likePost(userHandle, posts.getData().get(numberForLike - 1).getPostUuid());

                            if (response.getData()) {
                                System.out.println("Like aggiunto!");
                            } else {
                                System.out.println("Errore nel like del post");
                            }

                        } catch (Exception e) {

                            System.out.println("Eccezione nel like del post: " + e.getMessage());
                        }
                    } else {
                        System.out.println();
                        System.out.println("Seleziona un post esistente!");
                    }

                    showPostsList(userHandle, onlyFollowedUsers);

					break;
				case 3: //Segui un utente

					System.out.println("--- Scegli il numero del post relativo all'utente che vuoi seguire ---");

					int numberForFollow = keyboardInput.nextInt();
					keyboardInput.nextLine(); //per il \n

                    if (numberForFollow <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterInterface.followUser(userHandle, posts.getData().get(numberForFollow - 1).getUserHandle());

                            if (response.getData()) {
                                System.out.println("Follow aggiunto!");
                            } else {
                                System.out.println("Errore nel follow dell'utente. Ricorda che non puoi seguire te stesso!");
                            }

                        } catch (Exception e) {

                            System.out.println("Eccezione nel follow dell'utente: " + e.getMessage());
                        }
                    } else {
                        System.out.println();
                        System.out.println("Seleziona un post esistente!");
                    }

                    showRegisteredUserMenu(userHandle);

					break;

				case 4: //Smettere di seguire un utente

					System.out.println("--- Scegli il numero del post relativo all'utente che vuoi smettere di seguire ---");

					int numberForUnFollow = keyboardInput.nextInt();
					keyboardInput.nextLine(); //per il \n

                    if (numberForUnFollow <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterInterface.unFollowUser(userHandle, posts.getData().get(numberForUnFollow - 1).getUserHandle());

                            if (response.getData()) {
                                System.out.println("Follow rimosso!");
                            } else {
                                System.out.println("Errore nella rimozione del follow dell'utente");
                            }

                        } catch (Exception e) {

                            System.out.println("Eccezione nella rimozione del follow dell'utente: " + e.getMessage());
                        }
                    } else {
                        System.out.println();
                        System.out.println("Seleziona un post esistente!");
                    }

                    showRegisteredUserMenu(userHandle);

					break;
				case 5: //Elimina un post personale
					System.out.println("--- Scegli il numero del post che vuoi eliminare (se pubblicato da te) ---");

					int numberForDeletion = keyboardInput.nextInt();
					keyboardInput.nextLine(); //per il \n

                    if (numberForDeletion <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterInterface.deletePost(userHandle, posts.getData().get(numberForDeletion - 1).getPostUuid());

                            if (response.getData()) {
                                System.out.println("Post rimosso!");
                            } else {
                                System.out.println("Errore nella rimozione del post, puoi eliminare solo i post pubblicati da te stesso.");
                            }

                        } catch (Exception e) {
                            System.out.println("Errore nella rimozione del post: " + e.getMessage());
                        }
                    } else {
                        System.out.println();
                        System.out.println("Seleziona un post esistente!");
                    }

                    showRegisteredUserMenu(userHandle);

					break;
				case 6: //Torna indietro
					showRegisteredUserMenu(userHandle);
					break;
				default:
					System.out.println("Puoi scegliere tra le opzioni 1,2,3,4,5 o 6");
					break;
			}
		}
	}

	//UI per mostrare la lista degli utenti per i messaggi diretti

	public static void showUsersList(String userHandle) {

		UsersListResponse users = null;

		System.out.println("--- Lista degli utenti registrati ---");
		System.out.println();

		try {

			users = fakeTwitterInterface.getUsersList(userHandle);

			if (users.getData().isEmpty()) {

				System.out.println("La lista degli utenti è ancora vuota.");
				System.out.println();

				showRegisteredUserMenu(userHandle);

			} else {

				for (int i = 0; i < users.getData().size(); i++) {

					User currentUser = users.getData().get(i);

					System.out.println(i + 1 + " - " + "@" + currentUser.getUserHandle());
				}

				//Opzioni per i messaggi agli utenti

				System.out.println();
				System.out.println("--- Seleziona l'opzione desiderata ---");
				System.out.println();
				System.out.println("1. Chatta con un utente");
				System.out.println("2. Torna indietro");

				int selection = keyboardInput.nextInt();
				keyboardInput.nextLine(); //per il \n

				switch (selection) {
					case 1:
						//TODO
						break;
					case 2:
						showRegisteredUserMenu(userHandle);
						break;
				}
			}

		} catch (RemoteException e) {

			System.out.println("Eccezione nel recupero degli utenti: " + e.getMessage());
		}
	}
}
