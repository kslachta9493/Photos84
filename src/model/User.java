package model;

import java.util.ArrayList;
import java.util.List;
/**
 * <h1> User class used to encapsulate all data related to Userss. <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 6260242965413601354L;
	private String username;
	private List<Album> albums = new ArrayList<>();
	/**
	 * Constructs a new User
	 * @param String username : Used to identify user
	 */
	public User (String username) {
		this.username = username;
	}
	/**
	 * returns the username
	 * @return String username : username
	 */
	public String getUsername() {
		return this.username;
	}
	/**
	 * Returns all albums related to the user
	 * @return List<Album> albums : All albums saved to user
	 */
	public List<Album> getAlbums() {
		return this.albums;
	}
	/**
	 * Creates a new album for user
	 * @param Album album : Album to be added.
	 */
	public void addAlbum(Album album) {
		albums.add(album);
	}
	/**
	 * Removes an album from user
	 * @param Album album : Album to be removed.
	 */
	public void removeAlbum (Album album) {
		try {
			albums.remove(album);
		} catch (Exception e) {
			
		}
	}
	/**
	 * Changes username
	 * @param String username : New username.
	 */
	public void setUserName(String username) {
		this.username = username;
	}
	public String toString() {
		return this.username;
	}
}
