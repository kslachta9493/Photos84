package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
/**
 *<h1> Photo class used to encapsulate all data related to photos. <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class Photo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String photoname;
	private String location;
	private String caption;
	private Calendar timedate;
	private HashMap<String, List<String>> map = new HashMap<>();
	/**
	 * Creates a new photo with photoname and location
	 * @param photoname : User defined name of photo
	 * @param location : Location of photo on hard drive.
	 */
	public Photo(String photoname, String location) {
		this.photoname = photoname;
		this.location = location;
	}
	/**
	 * Returns the user defined name of photo
	 * @return String photo : name of photo
	 */
	public String getPhotoName() {
		return this.photoname;
	}
	/**
	 * Sets the name of photo
	 * @param String photoname : new name of photo
	 */
	public void setPhotoName(String photoname) {
		this.photoname = photoname;
	}
	/**
	 * Returns the location of photo on hard drive
	 * @return String location : I.E "C:\Users\Photo\test.jpg".
	 */
	public String getLocation() {
		return this.location;
	}
	/**
	 * Return the caption associated with photo
	 * @return String caption : caption
	 */
	public String getCaption() {
		return this.caption;
	}
	/**
	 * Sets the caption associated with the photo
	 * @param String caption : caption
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/**
	 * Adds a tag to the photo to be searched on.
	 * @param String key
	 * @param String value
	 * @return boolean : Returns true if the tag was added or false if it fails
	 */
	public boolean addTag(String key, String value) {
		if (map.containsKey(key)) {
			List<String> valuelist = map.get(key);
			if (!valuelist.contains(value)) {
				valuelist.add(value);
				map.remove(key);
				map.put(key, valuelist);
				return true;
			}
			return false;
		} else {
			List<String> valuelist = new ArrayList<>();
			valuelist.add(value);
			map.put(key, valuelist);
		}
		return true;
	}
	/**
	 * Removes tag from HashMap
	 * @param String key : Key of Hashmap
	 * @param String value : Value of HashMap
	 */
	public void removeTag(String key, String value) {
		List<String> valuelist = map.get(key);
		valuelist.remove(value);
		map.remove(key);
		map.put(key, valuelist);
	}
	/**
	 * Returns all tags associated with a certain key in HashMap
	 * @param String Key : Key value of HashMap
	 * @return List<String> value : Returns all values from a certain key.
	 */
	public List<String> getTag(String key) {
		return map.get(key);
	}
	/**
	 * Return the HashMap<String, List<String>> containing all tags.
	 * @return HashMap<String, List<String>> map : Hashmap containg all tags.
	 */
	public HashMap<String, List<String>> getAllTags() {
		return map;
	}
	/**
	 * Returns the time the photo was taken.
	 * @return Calendar c :Calendar instance 
	 */
	public Calendar getTime() {
		return this.timedate;
	}
	/**
	 * Sets the time the photo was taken
	 * @param Calendar c : Calendar instance
	 */
	public void setTime(Calendar c) {
		this.timedate = c;
		timedate.set(Calendar.MILLISECOND, 0);
	}
}
