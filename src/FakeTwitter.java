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

        //TODO: load pre made elements for the home screen

        super();
        users = (ArrayList<User>) StorageUtils.loadStoredUsers();
        posts = (ArrayList<Post>) StorageUtils.loadStoredPosts();
    }

    //Registrazione utente
    @Override
    public BooleanResponse registerUser(String handle) throws RemoteException {

        if (!handle.isBlank()) {

            User newUser = new User.Builder()
                    .withUserHandle(handle)
                    .withUserUuid(UUID.randomUUID().toString())
                    .withCreatedAt(System.currentTimeMillis())
                    .withFollowers(new ArrayList<>())
                    .withFollowing(new ArrayList<>())
                    .build();

            if (!users.stream().map(User::getUserHandle).toList().contains(newUser.getUserHandle())) {
                users.add(newUser);
                StorageUtils.saveUsersToStorage(users);
                return new BooleanResponse(true, 0);
            } else {
                return new BooleanResponse(false, 0); //L'utente esiste già
            }
        } else {
            return new BooleanResponse(false, 0); //Ricevuta stringa vuota
        }
    }

    //Login utente

    @Override
    public BooleanResponse login(String handle) throws RemoteException {
        if (!users.stream().map(User::getUserHandle).toList().contains(handle)) {
            return new BooleanResponse(false, 0);
        } else {
            return new BooleanResponse(true, 0); //L'utente esiste e può fare login
        }
    }

    //Nuovo post

    @Override
    public BooleanResponse newPost(String handle, String message) throws RemoteException {
        Post newPost = new Post.Builder()
                .withPostUuid(UUID.randomUUID().toString())
                .withCommentList(new ArrayList<>())
                .withLikedBy(new ArrayList<>())
                .withLikesCount(0)
                .withMessage(message)
                .withUserHandle(handle)
                .withCreatedAt(System.currentTimeMillis())
                .build();

        posts.add(newPost);

        StorageUtils.savePostsToStorage(posts);

        return new BooleanResponse(true, 0);
    }

    //Rimozione post

    @Override
    public BooleanResponse deletePost(String handle, String postUuid) throws RemoteException {
        Boolean removed = posts.removeIf(element -> element.getUserHandle().equals(handle) && element.getPostUuid().equals(postUuid));
        StorageUtils.savePostsToStorage(posts);
        return new BooleanResponse(removed, 0);
    }

    //Like del post

    @Override
    public BooleanResponse likePost(String handle, String postUuid) throws RemoteException {

        boolean updated = false;

        for (int i = 0; i < posts.size(); i++) {

            if (posts.get(i).getPostUuid().equals(postUuid)) {

                Post postToUpdate = posts.get(i);
                postToUpdate.addLike(handle);
                posts.set(i, postToUpdate);

                updated = true;
            }
        }

        StorageUtils.savePostsToStorage(posts);

        return new BooleanResponse(updated, 0);
    }

    //Seguire un utente

    @Override
    public BooleanResponse followUser(String follower, String followed) throws RemoteException {

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

            return new BooleanResponse(updated, 0);
        }
    }

    //Smettere di seguire un utente

    @Override
    public BooleanResponse unFollowUser(String follower, String followed) throws RemoteException {

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

        return new BooleanResponse(updated, 0);
    }

    //Recupera gli ultimi post

    @Override
    public PostsListResponse getLatestPosts() throws RemoteException {

        //TODO: mettere opzione per giorno, settimana, mese, tutto

        return new PostsListResponse(posts, 0);
    }

    //Recupera solo i post degli utenti seguiti

    @Override
    public PostsListResponse getFollowedPosts(String handle) throws RemoteException {

        //TODO: mettere opzione per giorno, settimana, mese, tutto

        ArrayList<Post> followedPosts = new ArrayList<>();

        //Cerco gli utenti seguiti

        ArrayList<String> followedUsers = (ArrayList<String>) users.stream().filter(element -> element.getUserHandle().equals(handle)).findFirst().orElse(null).getFollowing();

        //Cerco i post scritti dagli utenti seguiti

        if (followedUsers != null) {

			for (Post post : posts) {

				if (followedUsers.contains(post.getUserHandle())) {
					followedPosts.add(post);
				}
			}
        }

        return new PostsListResponse(followedPosts, 0);
    }

    //Recupera la lista degli utenti

    @Override
    public UsersListResponse getUsersList() throws RemoteException {
        return new UsersListResponse(users, 0);
    }

    //Commenta un post

    @Override
    public BooleanResponse commentPost(String handle, String postUuid, String comment) throws RemoteException {

        boolean updated = false;

        for (int i = 0; i < posts.size(); i++) {

            if (posts.get(i).getPostUuid().equals(postUuid)) {

                Post postToUpdate = posts.get(i);
                postToUpdate.addComment(handle, comment);
                posts.set(i, postToUpdate);

                updated = true;
            }
        }

        StorageUtils.savePostsToStorage(posts);

        return new BooleanResponse(updated, 0);
    }

    //Invia un messaggio diretto ad un utente

    @Override
    public BooleanResponse directMessage(String handle, String message) throws RemoteException {
        return null;
    }
}
