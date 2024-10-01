package client;

import model.Client;
import model.Comment;
import model.Post;
import model.responses.BooleanResponse;
import model.responses.IntegerResponse;
import model.responses.PostsListResponse;
import model.responses.ClientsListResponse;
import server.FakeTwitterServerInterface;
import utils.Constants;
import utils.Utils;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class FakeTwitterClientMain {

	public static FakeTwitterServerInterface fakeTwitterServerInterface;

	public static FakeTwitterClient fakeTwitterClient;

	static Scanner keyboardInput = new Scanner(System.in);

	public static void main(String[] args) {

		try {

			//Registry del server

			Registry registry = LocateRegistry.getRegistry(Constants.defaultHost, Constants.baseServicePort);

			fakeTwitterServerInterface = (FakeTwitterServerInterface) registry.lookup(Constants.serviceRegistryName);

			showRootMainMenu();

		} catch (Exception e) {

			System.out.println("Errore nell'inizializzazione del servizio. Verifica che il server sia operativo.");
		}
	}

	//Punto di ingresso per l'interfaccia utente

	public static void showRootMainMenu() {

		//Menu principale

		System.out.println("--- Benvenuti in Fake Twitter! --- Registrati o effettua il login");
		System.out.println();
		System.out.println("1. Registrati");
		System.out.println("2. Effettua il login");

		String stringForSelection = keyboardInput.nextLine();

		while (!Utils.isNumeric(stringForSelection)) {
			System.out.println("Inserisci un numero");
			stringForSelection = keyboardInput.nextLine();
		}

		int selection = Integer.parseInt(stringForSelection);

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

			BooleanResponse response = fakeTwitterServerInterface.login(userHandle);

			if (response.getData()) {

				System.out.println();
				System.out.println("Welcome back, " + userHandle + "!");
				System.out.println();

				//Inizializzazione della messaggistica tra client una volta loggati

				clientMessagingInit(userHandle);

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

			BooleanResponse response = fakeTwitterServerInterface.registerUser(userHandle);

			if (response.getData()) {

				System.out.println();
				System.out.println("Utente " + userHandle + " registrato!");
				System.out.println();

				//Inizializzazione della messaggistica tra client una volta registrati

				clientMessagingInit(userHandle);

				showRegisteredUserMenu(userHandle);

			} else {
				System.out.println();
				System.out.println("L'utente " + userHandle + " esiste già, effettua il login");
				System.out.println();
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

		String stringForSelection = keyboardInput.nextLine();

		while (!Utils.isNumeric(stringForSelection)) {
			System.out.println("Inserisci un numero");
			stringForSelection = keyboardInput.nextLine();
		}

		int selection = Integer.parseInt(stringForSelection);

		switch (selection) {
			case 1:
				System.out.println("--- A cosa stai pensando? ---");

				String post = keyboardInput.nextLine();

				try {

					BooleanResponse response = fakeTwitterServerInterface.newPost(userHandle, post);
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
				showClientsList(userHandle);
				break;
			case 5:
				try {
					fakeTwitterServerInterface.unRegisterClient(userHandle);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Eccezione nella deregistrazione del client");
				}
				System.out.println("Il client è stato rimosso dal pool degli utenti attivi. Grazie per aver utilizzato FakeTwitter!");
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

			posts = onlyFollowedUsers ? fakeTwitterServerInterface.getFollowedPosts(userHandle) : fakeTwitterServerInterface.getLatestPosts();

			for (int i = 0; i < posts.getData().size(); i++) {

				Post currentPost = posts.getData().get(i);

				System.out.println(i + 1 + " - " + "Utente: @" + currentPost.getUserHandle());
				System.out.println("\tMessaggio: " + posts.getData().get(i).getMessage());
				System.out.println("\tLike: " + currentPost.getLikesCount());
				System.out.println("\tData: " + Utils.formatDate(currentPost.getCreatedAt()));

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

			String stringForSelection = keyboardInput.nextLine();

			while (!Utils.isNumeric(stringForSelection)) {
				System.out.println("Inserisci un numero");
				stringForSelection = keyboardInput.nextLine();
			}

			int selection = Integer.parseInt(stringForSelection);

			switch (selection) {
				case 1: //Commento

					System.out.println("--- Scegli il numero del post che vuoi commentare ---");

					String stringForPostSelection = keyboardInput.nextLine();

					while (!Utils.isNumeric(stringForPostSelection)) {
						System.out.println("Inserisci un numero");
						stringForPostSelection = keyboardInput.nextLine();
					}

					int numberForComment = Integer.parseInt(stringForPostSelection);

					if (numberForComment <= posts.getData().size()) {

						System.out.println("--- Inserisci il commento ---");
						String message = keyboardInput.nextLine();

						try {

							BooleanResponse response = fakeTwitterServerInterface.commentPost(userHandle, posts.getData().get(numberForComment - 1).getPostUuid(), message);

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

					String stringForLikeSelection = keyboardInput.nextLine();

					while (!Utils.isNumeric(stringForLikeSelection)) {
						System.out.println("Inserisci un numero");
						stringForLikeSelection = keyboardInput.nextLine();
					}

					int numberForLike = Integer.parseInt(stringForLikeSelection);

                    if (numberForLike <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterServerInterface.likePost(userHandle, posts.getData().get(numberForLike - 1).getPostUuid());

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

					String stringForFollowSelection = keyboardInput.nextLine();

					while (!Utils.isNumeric(stringForFollowSelection)) {
						System.out.println("Inserisci un numero");
						stringForFollowSelection = keyboardInput.nextLine();
					}

					int numberForFollow = Integer.parseInt(stringForFollowSelection);

					if (numberForFollow <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterServerInterface.followUser(userHandle, posts.getData().get(numberForFollow - 1).getUserHandle());

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

					String stringForUnFollowSelection = keyboardInput.nextLine();

					while (!Utils.isNumeric(stringForUnFollowSelection)) {
						System.out.println("Inserisci un numero");
						stringForUnFollowSelection = keyboardInput.nextLine();
					}

					int numberForUnFollow = Integer.parseInt(stringForUnFollowSelection);

                    if (numberForUnFollow <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterServerInterface.unFollowUser(userHandle, posts.getData().get(numberForUnFollow - 1).getUserHandle());

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

					String stringForPostDeletion = keyboardInput.nextLine();

					while (!Utils.isNumeric(stringForPostDeletion)) {
						System.out.println("Inserisci un numero");
						stringForPostDeletion = keyboardInput.nextLine();
					}

					int numberForDeletion = Integer.parseInt(stringForPostDeletion);

                    if (numberForDeletion <= posts.getData().size()) {

                        try {

                            BooleanResponse response = fakeTwitterServerInterface.deletePost(userHandle, posts.getData().get(numberForDeletion - 1).getPostUuid());

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

	//UI per mostrare la lista dei client per i messaggi diretti

	public static void showClientsList(String userHandle) {

		ClientsListResponse clients = null;

		System.out.println("--- Lista degli utenti registrati ---");
		System.out.println();

		try {

			clients = fakeTwitterServerInterface.getClientsList(userHandle);

			if (clients.getData().isEmpty()) {

				System.out.println("La lista degli utenti è ancora vuota.");
				System.out.println();

				showRegisteredUserMenu(userHandle);

			} else {

				for (int i = 0; i < clients.getData().size(); i++) {

					Client currentClient = clients.getData().get(i);

					System.out.println(i + 1 + " - " + "@" + currentClient.getUserHandle());
				}

				//Opzioni per i messaggi agli utenti

				System.out.println();
				System.out.println("--- Seleziona l'opzione desiderata ---");
				System.out.println();
				System.out.println("1. Chatta con un utente");
				System.out.println("2. Torna indietro");

				String stringForSelection = keyboardInput.nextLine();

				while (!Utils.isNumeric(stringForSelection)) {
					System.out.println("Inserisci un numero");
					stringForSelection = keyboardInput.nextLine();
				}

				int selection = Integer.parseInt(stringForSelection);

				switch (selection) {
					case 1:

						//Selezione utente

						System.out.println("--- Scegli il numero dell'utente al quale vuoi inviare il messaggio ---");

						String stringForMessage = keyboardInput.nextLine();

						while (!Utils.isNumeric(stringForMessage)) {
							System.out.println("Inserisci un numero");
							stringForMessage = keyboardInput.nextLine();
						}

						int numberForMessage = Integer.parseInt(stringForMessage);

						if (numberForMessage <= clients.getData().size()) {

							String message = "";

							//Loop per la messaggistica

							while (!message.equals("Q")) {

								try {

									System.out.println("--- Messaggio (Q per uscire) ---");

									message = keyboardInput.nextLine();

									if (!message.equals("Q")) {

										BooleanResponse response = fakeTwitterClient.peerToPeer(userHandle, clients.getData().get(numberForMessage - 1).getPort(), message);

										if (response.getData()) {
											System.out.println();
											System.out.println("Messaggio inviato!");
											System.out.println();

										} else {
											System.out.println();
											System.out.println("Errore nell'invio del messaggio");
										}
									}

								} catch (Exception e) {
									System.out.println();
									System.out.println("Eccezione nell'invio del messaggio: " + e.getMessage());
								}

							} //Chiusura del while

							//showRegisteredUserMenu(userHandle);

						} else {
							System.out.println();
							System.out.println("Seleziona un utente!");
						}

						showRegisteredUserMenu(userHandle);

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

	public static void clientMessagingInit(String userHandle) {

		try {

			//Recupero della propria porta e setup per la messaggistica diretta

			IntegerResponse clientPort = fakeTwitterServerInterface.registerNewClient(Constants.defaultHost, userHandle);

			if (clientPort.getData() != 0) {

				fakeTwitterClient = new FakeTwitterClient();

				Registry clientRegistry = LocateRegistry.createRegistry(clientPort.getData());

				clientRegistry.bind(Constants.clientsRegistryName, fakeTwitterClient);

				System.out.println("Registrazione per la messaggistica completata. Porta: " + clientPort.getData());

				System.out.println();
			}

		} catch (RemoteException | AlreadyBoundException e) {

			e.printStackTrace();
			System.out.println("Eccezione nella registrazione del client per la messaggistica: " + e.getMessage());
		}
	}
}
