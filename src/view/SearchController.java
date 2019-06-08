package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.*;
/**
 *<h1>SearchController for the User sub-system.
 * Allows for date search, conjunctive and disjunctive searching of tags <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class SearchController {
	private Controller control = new Controller();
	private ObservableList<String> tagchoices;
	@FXML Button datesearch, tagsearch, createalbum;
	@FXML TextField tag1, tag2;
	@FXML DatePicker fromdate, todate;
	@FXML CheckBox orcheck;
	@FXML TilePane tile;
	@FXML ChoiceBox<String> tag1drop, tag2drop;
	@FXML void initialize() {
		control.refresh();
		tagchoices = FXCollections.observableArrayList();
		for (String s : control.getTags()) {
			tagchoices.add(s);
		}
		tag1drop.setItems(tagchoices);
		tag2drop.setItems(tagchoices);
	}
	private List<Photo> output;
	/**
	 * Searches based on selected dates. Updates TileView to display
	 * matching photos.
	 * @param event
	 */
	public void dateSearch(ActionEvent event) {
		tile.getChildren().clear();
		LocalDate fromld = fromdate.getValue();
		LocalDate told = todate.getValue();
		if (fromld == null || told == null) {
			//error dialog
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("No Date Selected");
			alert.setHeaderText("You must select a From date and To date");
			alert.setContentText("Please selecte a from and to date");

			alert.showAndWait();
			createalbum.setDisable(true);
			return;
		}
		Date fromd = java.sql.Date.valueOf(fromld);
		Date tod = java.sql.Date.valueOf(told);
		List<Album> albums = control.getCurrentUser().getAlbums();
		output = new ArrayList<>();
		//System.out.println(fromd.toString());
		//System.out.println(tod.toString());
		for (Album a: albums) {
			List<Photo> photos = a.getPhotos();
			for (Photo p: photos) {
				try {
					Calendar cal = p.getTime();
					cal.getTime();
					//System.out.println(cal.getTime().toString());
					if (fromd.before(cal.getTime()) && tod.after(cal.getTime())) {
						output.add(p);
					}
				} catch (Exception e) {
					
				}
			}
		}
		populateTiles();
	}
	private void populateTiles() {
		if (output.isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Search Results");
			alert.setHeaderText("There were no photos matching your search");
			alert.setContentText("Please refine the search results");

			alert.showAndWait();
			createalbum.setDisable(true);
		}
		for (Photo p: output) {
			Image image = null;
			try {
				image = new Image(new FileInputStream(p.getLocation()), 64, 0, true, true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			ImageView iv = new ImageView(image);
			Label l = new Label(p.getCaption());
			l.setGraphic(iv);
			l.setContentDisplay(ContentDisplay.TOP);
			l.setMaxWidth(128);
			tile.getChildren().add(l);
			createalbum.setDisable(false);
		}
	}
	/**
	 * Searches photo based on selected tags. Updates TileView to display
	 * matching photos.
	 * @param event
	 */
	public void tagSearch(ActionEvent event) {
		tile.getChildren().clear();
		output = new ArrayList<>();
		String firsttagdrop = tag1drop.getSelectionModel().getSelectedItem();
		String secondtagdrop = tag2drop.getSelectionModel().getSelectedItem();
		String firsttag = tag1.getText();
		String secondtag = tag2.getText();
		
		if (firsttag.isEmpty() || firsttagdrop.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Tag Search Failed");
			alert.setHeaderText("#Tag1 and tag must be filled in");
			alert.setContentText("Please select a #tag1 and tag");

			alert.showAndWait();
		}
		else {
			if (!secondtag.isEmpty() && !secondtagdrop.isEmpty())
				if (orcheck.isSelected())
					orsearch(firsttagdrop, firsttag, secondtagdrop, secondtag);
				else
					andsearch(firsttagdrop, firsttag, secondtagdrop, secondtag);
			else
				onetagsearch(firsttagdrop, firsttag);
		}
		
	}
	private void orsearch(String firsttagdrop, String firsttag, String secondtagdrop, String secondtag) {
		List<Album> albums = control.getCurrentUser().getAlbums();
		for (Album a : albums) {
			List<Photo> photos = a.getPhotos();
			for (Photo p : photos) {
				for (Map.Entry<String, List<String>> entry: p.getAllTags().entrySet()) {
					String key = entry.getKey();
					if (key.equals(firsttagdrop)) {
						List<String> value = entry.getValue();
						for (String s : value) {
							if (s.equals(firsttag)) {
								//System.out.println("key:" + key + "  string:" + s);
								if (!output.contains(p))
									output.add(p);
							}
						}					
					} else if (key.equals(secondtagdrop)) {
						List<String> value = entry.getValue();
						for (String s : value) {
							if (s.equals(secondtag)) {
								//System.out.println("key:" + key + "  string:" + s);
								if (!output.contains(p))
									output.add(p);
							}
						}	
					}
				}
			}
		}
		populateTiles();
	}
	private void andsearch(String firsttagdrop, String firsttag, String secondtagdrop, String secondtag) {
		List<Album> albums = control.getCurrentUser().getAlbums();
		boolean first, second = false;
		for (Album a : albums) {
			List<Photo> photos = a.getPhotos();
			for (Photo p : photos) {
				first = false;
				second = false;
				for (Map.Entry<String, List<String>> entry: p.getAllTags().entrySet()) {
					String key = entry.getKey();
					if (key.equals(firsttagdrop)) {
						List<String> value = entry.getValue();
						for (String s : value) {
							if (s.equals(firsttag)) {
								first = true;
							}
						}					
					} else if (key.equals(secondtagdrop)) {
						List<String> value = entry.getValue();
						for (String s : value) {
							if (s.equals(secondtag)) {
								second = true;
							}
						}	
					}
				}
				if (first && second) {
					if (!output.contains(p))
						output.add(p);
				}
			}
		}
		populateTiles();
	}
	private void onetagsearch(String tagfield, String tag) {
		List<Album> albums = control.getCurrentUser().getAlbums();
		for (Album a : albums) {
			List<Photo> photos = a.getPhotos();
			for (Photo p : photos) {
				for (Map.Entry<String, List<String>> entry: p.getAllTags().entrySet()) {
					String key = entry.getKey();
					if (key.equals(tagfield)) {

						List<String> value = entry.getValue();
						for (String s : value) {
							if (s.equals(tag)) {
								//System.out.println("key:" + key + "  string:" + s);
								if (!output.contains(p))
									output.add(p);
							}
						}					
					}
				}
			}
		}
		populateTiles();
	}
	/**
	 * Creates a new album of photos from the search results of date or tag search
	 * @param event
	 */
	public void createAlbum(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		Stage childStage = (Stage) createalbum.getScene().getWindow();
		dialog.setTitle("Create Album");
		dialog.setHeaderText("Please name this new album");
		dialog.setContentText("Album Name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println("Im here");
			List<Album> albums = control.getCurrentUser().getAlbums();
			if (result.get().equals(""))
				return;
			for (Album a: albums) {
				if (a.getAlbumName().equals(result.get())) {
					//output album name exists
					return;
				}
			}
			Album newalbum = new Album(result.get());
			for (Photo p: output) {
				System.out.println("adding");
				newalbum.addPhoto(p);
			}
			control.getCurrentUser().addAlbum(newalbum);
			control.save();
			childStage.close();
		}

	}
}
