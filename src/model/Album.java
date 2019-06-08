package model;

import java.util.ArrayList;
import java.util.List;
/**
 * <h1> Album class used to encapsulate all data related to albums. <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class Album implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7379556916322413021L;
	/**
	 * 
	 */

	private String albumname;
	private List<Photo> photos = new ArrayList<>();
	/**
	 * Constructs new album with albumname.
	 * @param String albumname : Name of album
	 */
	public Album (String albumname) {
		this.albumname = albumname;
	}
	/**
	 * Returns the name of the album
	 * @return String : name of the album
	 */
	public String getAlbumName() {
		return this.albumname;
	}
	/**
	 * Sets the album name
	 * @param String albumname : new album name.
	 */
	public void setAlbumName(String albumname) {
		this.albumname = albumname;
	}
	/**
	 * Add a photo to the Album
	 * @param Photo photo : Photo to be added
	 */
	public void addPhoto(Photo photo) {
		photos.add(photo);
	}
	/**
	 * Removes a photo from the Album
	 * @param Photo photo : Photo to be removed
	 */
	public void removePhoto(Photo photo) {
		
		boolean test = photos.remove(photo);
		if (!test)
			System.out.println("not here");
	}
	/**
	 * Returns list of photos saved in the album
	 * @return List<Photo> photos : All photos contained in the album.
	 */
	public List<Photo> getPhotos() {
		return this.photos;
	}
	/**
	 * Returns album name
	 */
	public String toString() {
		return this.albumname;
	}
}
