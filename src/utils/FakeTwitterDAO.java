package utils;

import model.Post;
import model.User;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class FakeTwitterDAO {

	private static final String usersFile = "users.txt";
	private static final String postsFile = "posts.txt";

	public static List<User> loadStoredUsers() {

		try {

			FileInputStream fileInputStream = new FileInputStream(usersFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			List<User> storedUsers = (ArrayList<User>) objectInputStream.readObject();
			objectInputStream.close();

			return storedUsers;
		}
		catch (Exception e) {
			System.out.println("Eccezione nel caricamento degli utenti, il file potrebbe non essere presente");
			return new ArrayList<User>();
		}
	}

	public static boolean saveUsersToStorage(List<User> users) {

		try {

			FileOutputStream fileInputStream = new FileOutputStream(usersFile, false);
			ObjectOutputStream objectInputStream = new ObjectOutputStream(fileInputStream);
			objectInputStream.writeObject(users);
			objectInputStream.close();

			return true;
		}
		catch (Exception e) {
			System.out.println("Eccezione nel salvataggio degli utenti");
			e.printStackTrace();
			return false;
		}
	}

	public static List<Post> loadStoredPosts() {

		try {

			FileInputStream fileInputStream = new FileInputStream(postsFile);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			List<Post> storedPosts = (ArrayList<Post>) objectInputStream.readObject();
			objectInputStream.close();

			return storedPosts;
		}

		catch (Exception e) {
			System.out.println("Eccezione nel caricamento dei post, il file potrebbe non essere presente");
			return new ArrayList<Post>();
		}
	}

	public static boolean savePostsToStorage(List<Post> posts) {

		try {

			FileOutputStream fileInputStream = new FileOutputStream(postsFile, false);
			ObjectOutputStream objectInputStream = new ObjectOutputStream(fileInputStream);
			objectInputStream.writeObject(posts);
			objectInputStream.close();

			return true;
		}
		catch (Exception e) {
			System.out.println("Eccezione nel salvataggio dei post");
			e.printStackTrace();
			return false;
		}
	}
}
