import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class FakeTwitterDAO {
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> users;
    private MongoCollection<Document> posts;


    public FakeTwitterDAO() {
        client = MongoClients.create("mongodb//:localhost:27107");
        database = client.getDatabase("fakeTwitter");
        users = database.getCollection("fakeTwitterUsers");
        posts = database.getCollection("fakeTwitterPosts");
    }

    //Metodi del DAO

//    registerUser(String handle) throws RemoteException;
//    BooleanResponse newPost(String post) throws RemoteException;
//    BooleanResponse deletePost(String postUuid) throws RemoteException;
//    BooleanResponse directMessage(String handle, String message) throws RemoteException;
//    BooleanResponse followUser(String handle) throws RemoteException;
//    BooleanResponse unFollowUser(String handle) throws RemoteException;
//    PostsListResponse getLatestPosts(long since) throws RemoteException;
//    PostsListResponse getFollowedPosts(long since) throws RemoteException;
//    BooleanResponse commentPost(String postUu

    //Registrazione utente
    public void registerUser(String handle) {
        ObjectId userId = new ObjectId();
        Document newUser = new Document("_id", userId)
                .append("userHandle", handle)
                .append("userUuid", userId)
                .append("followers", new ArrayList<>())
                .append("following", new ArrayList<>())
                .append("createdAt", System.currentTimeMillis());

        users.insertOne(newUser);
    }

    //Nuovo post

    public void newPost(String handle, String post) {
        ObjectId postId = new ObjectId();
        Document newPost = new Document("_id", postId)
                .append("postUuid", postId)
                .append("message", post)
                .append("userHandle", handle)
                .append("commentList", new ArrayList<>())
                .append("likes", 0)
                .append("likedBy", new ArrayList<>())
                .append("createdAt", System.currentTimeMillis());
    }

    //Like del post

    public void likePost(String userHandle, String postUuid) {
        posts.updateOne(Filters.eq("_id", postUuid),
                Updates.combine(
                        Updates.addToSet("likedBy", userHandle),
                        Updates.inc("likes", 1))
        );
    }


    //Aggiunta di un commento al post

    public void newComment(String userHandle, String postUuid, String message) {
        ObjectId commentId = new ObjectId();
        Document newComment = new Document("_id", commentId)
                .append("commentUuid", commentId)
                .append("userHandle", userHandle)
                .append("message", message)
                .append("createdAt", System.currentTimeMillis());

        posts.updateOne(Filters.eq("_id", postUuid), Updates.addToSet("comments", newComment));
    }
}
