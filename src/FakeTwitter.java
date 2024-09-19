import model.Post;
import model.User;
import model.responses.BooleanResponse;
import model.responses.PostsListResponse;

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
        users = new ArrayList<>();
        posts = new ArrayList<>();
    }

    //Metodi del DAO

//    BooleanResponse registerUser(String handle) throws RemoteException;
//    BooleanResponse newPost(String post) throws RemoteException;
//    BooleanResponse deletePost(String postUuid) throws RemoteException;
//    BooleanResponse likePost(String handle, String postUuid) throws RemoteException;
//    BooleanResponse directMessage(String handle, String message) throws RemoteException;
//    BooleanResponse followUser(String handle) throws RemoteException;
//    BooleanResponse unFollowUser(String handle) throws RemoteException;
//    PostsListResponse getLatestPosts(long since) throws RemoteException;
//    PostsListResponse getFollowedPosts(long since) throws RemoteException;
//    BooleanResponse commentPost(String postUuid, String comment);

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

            if (!users.stream().map(User::getUserUuid).toList().contains(newUser.getUserUuid())) {
                users.add(newUser);
                return new BooleanResponse(true, 0);
            } else {
                return new BooleanResponse(false, 0); //L'utente esiste già
            }
        } else {
            return new BooleanResponse(false, 0); //Ricevuta stringa vuota
        }
    }

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

        return new BooleanResponse(true, 0);
    }

    @Override
    public BooleanResponse deletePost(String postUuid) throws RemoteException {
        Boolean removed = posts.removeIf(element -> element.getPostUuid().equals(postUuid));
        return new BooleanResponse(removed, 0);
    }

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

        return new BooleanResponse(updated, 0);
    }

    @Override
    public BooleanResponse directMessage(String handle, String message) throws RemoteException {
        return null;
    }

    @Override
    public BooleanResponse followUser(String handle, String userUuid) throws RemoteException {

        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserUuid().equals(userUuid)) {

                User userToUpdate = users.get(i);
                userToUpdate.addFollowing(handle);
                users.set(i, userToUpdate);

                updated = true;
            }
        }

        return new BooleanResponse(updated, 0);
    }

    @Override
    public BooleanResponse unFollowUser(String handle, String userUuid) throws RemoteException {

        boolean updated = false;

        for (int i = 0; i < users.size(); i++) {

            if (users.get(i).getUserUuid().equals(userUuid)) {

                User userToUpdate = users.get(i);
                userToUpdate.removeFollowing(handle);
                users.set(i, userToUpdate);

                updated = true;
            }
        }

        return new BooleanResponse(updated, 0);
    }

    @Override
    public PostsListResponse getLatestPosts(long since) throws RemoteException {

        //TODO: mettere opzione per giorno, settimana, mese, tutto

        return new PostsListResponse(posts, 0);
    }

    @Override
    public PostsListResponse getFollowedPosts(long since, String handle) throws RemoteException {

        //TODO: mettere opzione per giorno, settimana, mese, tutto

        ArrayList<Post> followedPosts = new ArrayList<>();

        //Cerco gli utenti seguiti

        User currentUser = users.stream().filter(element -> element.getUserHandle().equals(handle)).findFirst().orElse(null);

        //Cerco i post scritti dagli utenti seguiti

        if (currentUser != null) {

			for (Post post : posts) {

				if (post.getUserHandle().equals(handle)) {
					followedPosts.add(post);
				}
			}
        }

        return new PostsListResponse(followedPosts, 0);
    }

    @Override
    public BooleanResponse commentPost(String postUuid, String handle, String comment) throws RemoteException {

        boolean updated = false;

        for (int i = 0; i < posts.size(); i++) {

            if (posts.get(i).getPostUuid().equals(postUuid)) {

                Post postToUpdate = posts.get(i);
                postToUpdate.addComment(handle, comment);
                posts.set(i, postToUpdate);

                updated = true;
            }
        }

        return new BooleanResponse(updated, 0);
    }
}
