package controller;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
/**
 * <h1> Controller class for the User>Album>Photo data structure.
 * Responsible for maintaining the structure and providing an interface
 * to retrieve or store new data <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class Controller {
	private User currentUser;
	private String filename = "docs/test";
	private List<User> users = new ArrayList<>();
	private Album currentalbum;
	private Photo selectedphoto;
	private List<String> taglist = new ArrayList<>();
	public Controller() {
		populateController();
		defaultTags();
		//users.clear();
		//save();
	}
	private void defaultTags() {
		if (!taglist.contains("Person")) {
			taglist.add("Person");
		}
		if (!taglist.contains("Location")) {
			taglist.add("Location");
		}
	}
	private void writeList() {
		for (User u : users) {
			System.out.println("USER: " + u.toString());
		}
	}
	/**
	 * Adds a tag to list of tags associated with a photo.
	 * @param String
	 */
	public void addTag(String tag) {
		this.taglist.add(tag);
	}
	/**
	 * getTags will return all currently defined tag fields.
	 * @return List<String> all defined tags
	 */
	public List<String> getTags() {
		return this.taglist;
	}
	/**
	 * Used to get all currently created users.
	 * @return List<User> contains all created users.
	 */
	public List<User> getUsers() {
		return users;
	}
	/**
	 * Saves currently selected photo
	 * @param Photo p : the currently selected photo
	 */
	public void setSelectedPhoto(Photo p) {
		this.selectedphoto = p;
	}
	/**
	 * Returns the currently selected photo
	 * @return Photo p : the currently selected photo
	 */
	public Photo getSelectedPhoto() {
		return this.selectedphoto;
	}
	/**
	 * Sets the current album to the currently selected album.
	 * @param Album a : the currently selected album.
	 */
	public void setCurrentAlbum(Album a) {
		this.currentalbum = a;
	}
	/**
	 * Gets the currently selected album
	 * @return Album a : the currently selected album
	 */
	public Album getCurrentAlbum() {
		return this.currentalbum;
	}
	/**
	 * Adds a user to the user list.
	 * @param User user : User to add.
	 */
	public void addUser(User user) {
		users.add(user);
	}
	/**
	 * Removes a user from the user list.
	 * @param User user to be removed.
	 */
	public void removeUser (User user) {
		users.remove(user);
	}
	/**
	 * Returns the current active user
	 * @return User user : the current active user.
	 */
	public User getCurrentUser() {
		return this.currentUser;
	}
	/**
	 * Set the current user
	 * @param User user: The current user.
	 */
	public void setCurrentUser(User user) {
		this.currentUser = user;
	}
	/**
	 * Repopulates the data in the controller.
	 * Used to ensure data is up to date.
	 */
	public void refresh() {
		populateController();
	}
	@SuppressWarnings("unchecked")
	private void populateController() {
		FileInputStream file;
		ObjectInputStream in;
		try {
			file = new FileInputStream(filename);
			in = new ObjectInputStream(file);
			users = (List<User>) in.readObject();
			currentUser = (User) in.readObject();
			taglist = (List<String>) in.readObject();
			in.close();
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if (users.isEmpty())
			//System.out.println("WHAT");
	}
	/**
	 * Saves the current controller state.
	 */
	public void save() {
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(users);
			out.writeObject(currentUser);
			out.writeObject(taglist);
			out.close();
			file.close();
		} catch (Exception e) {
			
		}
	}
}
