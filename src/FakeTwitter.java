import model.Post;
import model.User;
import model.responses.BooleanResponse;
import model.responses.PostsListResponse;
import model.responses.UsersListResponse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.UUID;

public class FakeTwitter extends UnicastRemoteObject implements FakeTwitterInterface {

    private final ArrayList<User> users;
    private final ArrayList<Post> posts;

    protected FakeTwitter() throws RemoteException {
        super();
        users = (ArrayList<User>) StorageUtils.loadStoredUsers();
        posts = (ArrayList<Post>) StorageUtils.loadStoredPosts();
    }

    //Registrazione utente
    @Override
    public BooleanResponse registerUser(String userHandle) throws RemoteException {

        long startTime = System.currentTimeMillis();

        if (!userHandle.isBlank()) {

            User newUser = new User.Builder()
                    .withUserHandle(userHandle)
                    .withUserUuid(UUID.randomUUID().toString())
                    .withCreatedAt(System.currentTimeMillis())
                    .withFollowers(new ArrayList<>())
                    .withFollowing(new ArrayList<>())
                    .build();

            if (!users.stream().map(User::getUserHandle).toList().contains(newUser.getUserHandle())) {
                users.add(newUser);
                StorageUtils.saveUsersToStorage(users);
                return new BooleanResponse(true, System.currentTimeMillis() - startTime);
            } else {
                return new BooleanResponse(false, System.currentTimeMillis() - startTime); //L'utente esiste già
            }
        } else {
            return new BooleanResponse(false, System.currentTimeMillis() - startTime); //Ricevuta stringa vuota
        }
    }

    //Login utente

    @Override
    public BooleanResponse login(String userHandle) throws RemoteException {

        long startTime = System.currentTimeMillis();

        if (!users.stream().map(User::getUserHandle).toList().contains(userHandle)) {
            return new BooleanResponse(false, System.currentTimeMillis() - startTime);
        } else {
            return new BooleanResponse(true, System.currentTimeMillis() - startTime); //L'utente esiste e può fare login
        }
    }

    //Nuovo post

    @Override
    public BooleanResponse newPost(String userHandle, String message) throws RemoteException {

        long startTime = System.currentTimeMillis();

        Post newPost = new Post.Builder()
                .withPostUuid(UUID.randomUUID().toString())
                .withCommentList(new ArrayList<>())
                .withLikedBy(new ArrayList<>())
                .withLikesCount(0)
                .withMessage(message)
                .withUserHandle(userHandle)
                .withCreatedAt(System.currentTimeMillis())
                .build();

        posts.add(newPost);

        StorageUtils.savePostsToStorage(posts);

        return new BooleanResponse(true, System.currentTimeMillis() - startTime);
    }

    //Rimozione post

    @Override
    public BooleanResponse deletePost(String userHandle, String postUuid) throws RemoteException {

        long startTime = System.currentTimeMillis();

        Boolean removed = posts.removeIf(element -> element.getUserHandle().equals(userHandle) && element.getPostUuid().equals(postUuid));
        StorageUtils.savePostsToStorage(posts);
        return new BooleanResponse(removed, System.currentTimeMillis() - startTime);
    }

    //Like del post

    @Override
    public BooleanResponse likePost(String userHandle, String postUuid) throws RemoteException {

        long startTime = System.currentTimeMillis();

        boolean updated = false;

        for (int i = 0; i < posts.size(); i++) {

            if (posts.get(i).getPostUuid().equals(postUuid)) {

                Post postToUpdate = posts.get(i);
                postToUpdate.addLike(userHandle);
                posts.set(i, postToUpdate);

                updated = true;
            }
        }

        StorageUtils.savePostsToStorage(posts);

        return new BooleanResponse(updated, System.currentTimeMillis() - startTime);
    }

    //Seguire un utente

    @Override
    public BooleanResponse followUser(String follower, String followed) throws RemoteException {

        long startTime = System.currentTimeMillis();

        boolean updated = false;

        if (follower.equals(followed)) {

            return new BooleanResponse(updated, 0);

        } else {

            for (int i = 0; i < users.size(); i++) {

                if (users.get(i).getUserHandle().equals(follower)) {

                    User userToUpdate = users.get(i);
                    userToUpdate.addFollowing(followed);
                    users.set(i, userToUpdate);

                    updated = true;
                }
            }

            StorageUtils.saveUsersToStorage(users);

            return new BooleanResponse(updated, System.currentTimeMillis() - startTime);
        }
    }

    //Smettere di seguire un utente

    @Override
    public BooleanResponse unFollowUser(String follower, String followed) throws RemoteException {

        long startTime = System.currentTimeMillis();

        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserHandle().equals(follower)) {

                User userToUpdate = users.get(i);
                userToUpdate.removeFollowing(followed);
                users.set(i, userToUpdate);

                updated = true;
            }
        }

        StorageUtils.saveUsersToStorage(users);

        return new BooleanResponse(updated, System.currentTimeMillis() - startTime);
    }

    //Recupera gli ultimi post

    @Override
    public PostsListResponse getLatestPosts() throws RemoteException {

        long startTime = System.currentTimeMillis();

        //TODO: mettere opzione per giorno, settimana, mese, tutto

        return new PostsListResponse(posts, System.currentTimeMillis() - startTime);
    }

    //Recupera solo i post degli utenti seguiti

    @Override
    public PostsListResponse getFollowedPosts(String userHandle) throws RemoteException {

        long startTime = System.currentTimeMillis();

        //TODO: mettere opzione per giorno, settimana, mese, tutto

        ArrayList<Post> followedPosts = new ArrayList<>();

        //Cerco gli utenti seguiti

        ArrayList<String> followedUsers = (ArrayList<String>) users.stream().filter(element -> element.getUserHandle().equals(userHandle)).findFirst().orElse(null).getFollowing();

        //Cerco i post scritti dagli utenti seguiti

        if (followedUsers != null) {

			for (Post post : posts) {

				if (followedUsers.contains(post.getUserHandle())) {
					followedPosts.add(post);
				}
			}
        }

        return new PostsListResponse(followedPosts, System.currentTimeMillis() - startTime);
    }

    //Recupera la lista degli utenti

    @Override
    public UsersListResponse getUsersList(String userHandle) throws RemoteException {

        long startTime = System.currentTimeMillis();

        return new UsersListResponse(users.stream().filter(element -> !element.getUserHandle().equals(userHandle)).toList(), System.currentTimeMillis() - startTime);
    }

    //Commenta un post

    @Override
    public BooleanResponse commentPost(String userHandle, String postUuid, String comment) throws RemoteException {

        long startTime = System.currentTimeMillis();

        boolean updated = false;

        for (int i = 0; i < posts.size(); i++) {

            if (posts.get(i).getPostUuid().equals(postUuid)) {

                Post postToUpdate = posts.get(i);
                postToUpdate.addComment(userHandle, comment);
                posts.set(i, postToUpdate);

                updated = true;
            }
        }

        StorageUtils.savePostsToStorage(posts);

        return new BooleanResponse(updated, System.currentTimeMillis() - startTime);
    }

    //Invia un messaggio diretto ad un utente

    @Override
    public BooleanResponse directMessage(String userHandle, String message) throws RemoteException {
        return null;
    }
}
