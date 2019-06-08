package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import model.*;
/**
 * <h1>UserController for the User sub-system.
 * Controls all UI elements. Disables unusable buttons or hides them. <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 * 
 */
public class UserController {
	@FXML Button logout;
	@FXML AnchorPane ap;
	@FXML TilePane tile;
	@FXML ListView<String> tagview;
	@FXML Button add, delete, rename, recaption, display,  move, copy, search, addtag, deletetag, back, forward;
	@FXML Label datelabel, photodate, cplabel, captionlabel, selectedlabel, selectedalbum, hover;
	@FXML ImageView imagedisplay;
	@FXML Button albumview;
	private Controller control;
	private boolean albummode = true;
	/**
	 * Called when a user requests to logout. Calls showlogin to redisplay the initial login screen.
	 * @param event contains the user form of input.
	 */
	public void logout(ActionEvent event) {
		Stage userStage = (Stage) logout.getScene().getWindow();
		control.save();
		showLogin();
		userStage.hide();
	}
	/**
	 * Called when the form is first created. Creates a new controller for logic processing.
	 */
	@FXML void initialize() {
		control = new Controller();
		redraw();
	}
	/**
	 * Shows the login screen.
	 */
	private void showLogin() {
		try {
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("/view/Login.FXML"));
			Scene scene = new Scene(root,288,155);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Should be called anytime there is an update to the data structure it is supporting.
	 * Will redraw the gallery of images or folders.
	 */
	private void redraw() {
		//control.refresh();
		if (albummode == false) {
			albumview.setVisible(true);
			display.setDisable(false);
			Album current = control.getCurrentAlbum();
			tile.getChildren().clear();
			List<Photo> photos = current.getPhotos();
			albummode = false;
			for (Photo p : photos) {
				//System.out.println("Photo: " + p.getLocation());
				Image image = null;
				try {
					image = new Image(new FileInputStream(p.getLocation()), 64, 0, true, true);
				} catch (FileNotFoundException e) {
					return;
				}
				ImageView iv = new ImageView(image);
				Label l = new Label(p.getCaption());
				l.setGraphic(iv);
				l.setMaxWidth(128);
				l.setContentDisplay(ContentDisplay.TOP);
				if (control.getSelectedPhoto() == null) {
					control.setSelectedPhoto(p);
					selectedlabel = l;
				}
				if (control.getSelectedPhoto().equals(p)) {
					selectedlabel = l;
					l.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
				}
				l.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						if (selectedlabel != null)
							selectedlabel.setBorder(null);
						l.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
						selectedlabel = l;
						control.setSelectedPhoto(p);
						enablephoto();
						display(null);
						//l.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, null)));
						//iv.set
					}
				});
					tile.getChildren().add(l);
			}
		} else {
			albumview.setVisible(false);
			display.setDisable(true);
			tile.getChildren().clear();
			List<Album> albums = control.getCurrentUser().getAlbums();
			for (Album a : albums) {
				Image image = null;
				try {
					image = new Image(new FileInputStream("img/folder.jpg"), 64, 0, true, true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				Label l = new Label(a.getAlbumName() + "(" + a.getPhotos().size() + ")");
				if (a.getPhotos().size() != 0) {
					Tooltip tool = new Tooltip();
					Calendar ce = null;
					Calendar cl = null;
					for (Photo p: a.getPhotos()) {
						if (ce == null)
							ce = p.getTime();
						if (cl == null)
							cl = p.getTime();
						
						if (ce.after(p.getTime()))
							ce = p.getTime();
						else if (cl.before(p.getTime()))
							cl = p.getTime();
					}
					tool.setText("" + (ce.get(Calendar.MONTH) + 1) + "/" + ce.get(Calendar.DATE) + "/" + ce.get(Calendar.YEAR)
							+ "-"+ (cl.get(Calendar.MONTH) + 1) + "/" + cl.get(Calendar.DATE) + "/" + cl.get(Calendar.YEAR));
					l.setTooltip(tool);
				}
				ImageView iv = new ImageView(image);
				l.setGraphic(iv);
				l.setContentDisplay(ContentDisplay.TOP);
				l.setMaxWidth(64);
				if (control.getCurrentAlbum() != null && control.getCurrentAlbum().equals(a)) {
					selectedalbum = l;
					l.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
				}
				l.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						if (event.getClickCount() == 2) {
							delete.setDisable(true);
							rename.setDisable(true);
							albumview.setVisible(true);
							control.setCurrentAlbum(a);

							tile.getChildren().clear();
							List<Photo> photos = a.getPhotos();
							albummode = false;
							for (Photo p : photos) {
								//System.out.println("Photo: " + p.getLocation());
								Image image = null;
								try {
									image = new Image(new FileInputStream(p.getLocation()), 64, 0, true, true);
								} catch (FileNotFoundException e) {
									return;
								}
								ImageView iv = new ImageView(image);
								Label l = new Label(p.getCaption());
								l.setGraphic(iv);
								l.setContentDisplay(ContentDisplay.TOP);
								l.setMaxWidth(128);
								l.setOnMouseClicked(new EventHandler<MouseEvent>() {
									@Override
									public void handle(MouseEvent event) {
										if (selectedlabel != null)
											selectedlabel.setBorder(null);
										l.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
										selectedlabel = l;
										control.setSelectedPhoto(p);
										enablephoto();
										display(null);
									}
								});
								tile.getChildren().add(l);
								if (tile.getChildren().size() == 1) {
									enablephoto();
									selectedlabel = l;
									l.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
									control.setSelectedPhoto(p);
									display(null);
								}
							}
						} else {
							if (selectedalbum != null)
								selectedalbum.setBorder(null);
							l.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
							selectedalbum = l;
							control.setCurrentAlbum(a);
							enablealbum();
						}
					}
					
				});
				tile.getChildren().add(l);
			}
		}
	}
	/**
	 * Enable button functionality related to photos and disables buttons not relevant.
	 */
	private void enablephoto() {
		albummode = false;
		add.setDisable(false);
		delete.setDisable(false);
		rename.setDisable(true);
		recaption.setDisable(false);
		search.setDisable(false);
		display.setDisable(false);
		copy.setDisable(false);
		move.setDisable(false);
		datelabel.setVisible(true);
		photodate.setVisible(true);
		back.setVisible(true);
		forward.setVisible(true);
		tagview.setVisible(true);
		addtag.setVisible(true);
		deletetag.setVisible(true);
		imagedisplay.setVisible(true);
		hover.setVisible(false);
	}
	/**
	 * Enables button functionality needed for albums and disables any not relevant.
	 */
	private void enablealbum() {
		albummode = true;
		//disability
		add.setDisable(false);
		delete.setDisable(false);
		rename.setDisable(false);
		recaption.setDisable(true);
		search.setDisable(false);
		display.setDisable(true);
		copy.setDisable(true);
		move.setDisable(true);
		datelabel.setVisible(false);
		photodate.setVisible(false);
		back.setVisible(false);
		forward.setVisible(false);
		tagview.setVisible(false);
		addtag.setVisible(false);
		deletetag.setVisible(false);
		imagedisplay.setVisible(false);
		hover.setVisible(true);
	}
	/**
	 * Called whenever the add button is pressed.
	 * Used to create new albums or place a new photo in an existing album.
	 * @param event
	 */
	public void add(ActionEvent event) {
		boolean exists = false;
		if (albummode == false) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Add Photo");
			Stage userStage = (Stage) add.getScene().getWindow();
			fileChooser.getExtensionFilters().addAll(
					//new FileChooser.ExtensionFilter("All Images", "*"),
					new FileChooser.ExtensionFilter("JPG", "*.jpg"),
					new FileChooser.ExtensionFilter("PNG", "*.png")
					);
			try {
				File file = fileChooser.showOpenDialog(userStage);
				//System.out.println("PIC" + file.getAbsolutePath());
				String path = file.getAbsolutePath();
				Photo newphoto = new Photo("np", path);
				//Calendar c = Calendar.getInstance();
				SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				sdf.format(file.lastModified());
				sdf.getCalendar();
				newphoto.setTime(sdf.getCalendar());
				control.getCurrentAlbum().addPhoto(newphoto);
			} catch (Exception e) {
				
			}
			control.save();
			redraw();
			display(null);
			
		} else {
			TextInputDialog dialog = new TextInputDialog("Add Album");
			dialog.setTitle("Add Album");
			dialog.setHeaderText("Enter album name");
			dialog.setContentText("Album:");
			Optional<String> result = dialog.showAndWait();
			List<Album> albums = control.getCurrentUser().getAlbums();
			if (result.isPresent()) {
				if (!result.get().equals("")) {
					exists = false;
					for (Album a : albums) {
						if (a.getAlbumName().equals(result.get())) {
							exists = true;
						}
					}
					if (!exists) {
						Album album = new Album(result.get());
						control.getCurrentUser().addAlbum(album);
						control.save();
						redraw();
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Failed To Create Album");
						alert.setHeaderText("Album already exists");
						alert.setContentText("There already exists an album named: " + result.get());

						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Failed To Create Album");
					alert.setHeaderText(null);
					alert.setContentText("You must specify an album name");

					alert.showAndWait();
				}
			}
			//result.ifPresent(arg0);
		}
	}
	/**
	 * Used to delete an entire album or one singular photo.
	 * @param event
	 */
	public void delete(ActionEvent event) {
		if (albummode == false) {

			if (control.getSelectedPhoto() == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No photo selected");
				alert.setHeaderText(null);
				alert.setContentText("You must select a photo first");
				alert.showAndWait();
				return;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Photo?");
			alert.setHeaderText(null);
			alert.setContentText("You are about to delete this photo. Are you sure?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){

				control.getCurrentAlbum().removePhoto(control.getSelectedPhoto());
				control.save();
				control.setSelectedPhoto(null);
				redraw();
			}
		} else {
			if (control.getCurrentAlbum() == null) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("No album Selected");
				alert.setHeaderText(null);
				alert.setContentText("You must select an album first");

				alert.showAndWait();
				return;
			}
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Album?");
			alert.setHeaderText("You are about to delete album " + control.getCurrentAlbum().getAlbumName());
			alert.setContentText("Are you sure?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				
				control.getCurrentUser().removeAlbum(control.getCurrentAlbum());
				control.setCurrentAlbum(null);
				control.save();
				redraw();
			}
		}

	}
	/**
	 * Used to rename an album.
	 * @param event
	 */
	public void rename(ActionEvent event) {
		boolean exists = false;
		Album album = control.getCurrentAlbum();
		TextInputDialog dialog = new TextInputDialog(album.getAlbumName());
		dialog.setTitle("Rename");
		dialog.setHeaderText("Enter new album name");
		dialog.setContentText("Album Name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			List<Album> albums = control.getCurrentUser().getAlbums();
			for (Album a : albums) {
				if (a.getAlbumName().equals(result.get())) {
					exists = true;
				}
			}
			if (!exists) {
				album.setAlbumName(result.get());
				control.save();
				redraw();	
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Failed to rename album");
				alert.setHeaderText("There already exists an album named: " + result.get());
				alert.setContentText("Please use a different name");

				alert.showAndWait();	
			}
		}
	}
	/**
	 * Used to recaption a photo.
	 * @param event
	 */
	public void recaption(ActionEvent event) {
		Photo photo = control.getSelectedPhoto();
		//System.out.println("SELECTED PHOTO IS:" + photo.getPhotoName());
		TextInputDialog dialog = new TextInputDialog(photo.getCaption());
		dialog.setTitle("Recaption");
		dialog.setHeaderText("Enter new caption");
		dialog.setContentText("Caption:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			photo.setCaption(result.get());
			control.save();
			redraw();
			display(null);
		}
	}
	/**
	 * Displays larger version of selected image.
	 * @param event
	 */
	public void display(ActionEvent event) {
		Image image = null;
		Photo p = control.getSelectedPhoto();
		try {
			image = new Image(new FileInputStream(p.getLocation()), 600, 400, true, true);
			imagedisplay.setImage(image);
			cplabel.setVisible(true);
			captionlabel.setVisible(true);
			captionlabel.setText(p.getCaption());
			 try {
				 Calendar cal = p.getTime();
				 String date = "" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
				 photodate.setText(date);
			 } catch (Exception e) {
				 photodate.setText("N/A");
			 }
			tagrefresh();
		} catch (Exception e) {
		}
	}
	private void tagrefresh() {
		Photo p = control.getSelectedPhoto();
		ObservableList<String> tags = FXCollections.observableArrayList();
		for (Map.Entry<String, List<String>> entry: p.getAllTags().entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			for (String s : value) {
				tags.add(key + " : " + s);
			}
		}
		tagview.setItems(tags);
	}
	/**
	 * Creates a new searchinterface. Allows for search based on date,
	 * conjunctive and disjunctive searching.
	 * @param event
	 */
	public void search(ActionEvent event) {
		try {
			Stage childStage = new Stage();
			Stage userStage = (Stage) search.getScene().getWindow();
			Parent root  = FXMLLoader.load(getClass().getResource("/view/SearchInterface.FXML"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			childStage.setScene(scene);
			childStage.initOwner(userStage);
			childStage.show();
			childStage.setOnHidden(new EventHandler<WindowEvent>() {
				public void handle (WindowEvent we) {
					control.refresh();
					redraw();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Moves a photo from source album to dest album.
	 * @param event
	 */
	public void move(ActionEvent event) {
		Photo photo = control.getSelectedPhoto();
		List<Album> albums = control.getCurrentUser().getAlbums();
		Album curalbum = control.getCurrentAlbum();
		ChoiceDialog<Album> dialog = new ChoiceDialog<>(albums.get(0), albums);
		
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Move photo to new album");
		dialog.setContentText("Choose album:");

		// Traditional way to get the response value.
		Optional<Album> result = dialog.showAndWait();
		if (result.isPresent()){
		    //System.out.println("Your choice: " + result.get());
			for (Album a : albums) {
				if (a.equals(result.get())) {
					a.addPhoto(photo);
					curalbum.removePhoto(photo);
					control.save();
					redraw();
					display(null);
				}
			}
		}
		control.save();
	}
	/**
	 * Creates a copy of currently selected photo and places it in selected album.
	 * @param event
	 */
	public void copy(ActionEvent event) {
		Photo photo = control.getSelectedPhoto();
		List<Album> albums = control.getCurrentUser().getAlbums();
		Album curalbum = control.getCurrentAlbum();
		ChoiceDialog<Album> dialog = new ChoiceDialog<>(albums.get(0), albums);
		dialog.setTitle("Copy photo");
		dialog.setHeaderText("Copy photo to another album");
		dialog.setContentText("Choose album:");

		// Traditional way to get the response value.
		Optional<Album> result = dialog.showAndWait();
		if (result.isPresent()){
		    //System.out.println("Your choice: " + result.get());
			for (Album a : albums) {
				if (a.equals(result.get())) {
					a.addPhoto(photo);
					//curalbum.removePhoto(photo);
					control.save();
					redraw();
					//display(null);
				}
			}
		}
		//albums.add(curalbum);
		control.save();
		redraw();
	}
	/**
	 * Allows a user to add tags to a photo to be searched on.
	 * Also allows a user to define new tagfields.
	 * @param event
	 */
	public void addTag(ActionEvent event) {
		Photo photo = control.getSelectedPhoto();
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Add a custom tag to your photo");
		ButtonType addTag = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addTag, ButtonType.CANCEL);
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 10));
		TextField taginput = new TextField();
		ChoiceBox<String> tagchoice = new ChoiceBox<String>();
		ObservableList<String> taglist = FXCollections.observableArrayList();
		TextField newtaginput = new TextField();
		for (String s : control.getTags()) {
			taglist.add(s);
		}
		Button newtag = new Button("Create new Tag");
		newtag.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (newtaginput.getText().isEmpty())
					return;
				else {
					if (!control.getTags().contains(newtaginput.getText())) {
						control.addTag(newtaginput.getText());
						taglist.add(newtaginput.getText());
						tagchoice.setItems(taglist);
						control.save();
					}
				}
			}
		});
		tagchoice.setItems(taglist);
		taginput.setPromptText("#Tag");
		newtaginput.setPromptText("New Tag");
		grid.add(new Label("Tag Field"), 0, 0);
		grid.add(tagchoice, 1, 0);
		grid.add(taginput, 1, 1);
		grid.add(new Label("New Tag Field"), 2, 0);
		grid.add(newtaginput, 3, 0);
		grid.add(newtag, 3, 1);
		grid.add(new Label("Tag:"), 0, 1);


		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(addTag);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		taginput.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == addTag) {
		        return new Pair<>(tagchoice.getSelectionModel().getSelectedItem(), taginput.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
		    //System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
		    photo.addTag(usernamePassword.getKey(), usernamePassword.getValue());
		    control.save();
		    tagrefresh();
		});
		control.save();
	}
	/**
	 * Removes selected tag from photo.
	 * @param event
	 */
	public void deleteTag(ActionEvent event) {
		if (tagview.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Select a tag");
			alert.setHeaderText(null);
			alert.setContentText("You must select a tag to delete first!");

			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Tag");
			alert.setHeaderText("You are about to delete this tag");
			alert.setContentText("Are you ok with this?");
	
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				Photo p = control.getSelectedPhoto();
					String[] holder = tagview.getSelectionModel().getSelectedItem().split(" : ");
					p.removeTag(holder[0], holder[1]);
					tagrefresh();
					control.save();
			}
		}
	}
	/**
	 * Sets the interface back into album mode. Disables functionality not relevant.
	 * @param event
	 */
	public void albumview(ActionEvent event) {
		albummode = true;
		//disability
		add.setDisable(false);
		delete.setDisable(true);
		rename.setDisable(true);
		recaption.setDisable(true);
		search.setDisable(false);
		display.setDisable(true);
		copy.setDisable(true);
		move.setDisable(true);
		cplabel.setVisible(false);
		captionlabel.setVisible(false);
		datelabel.setVisible(false);
		photodate.setVisible(false);
		back.setVisible(false);
		forward.setVisible(false);
		tagview.setVisible(false);
		addtag.setVisible(false);
		deletetag.setVisible(false);
		imagedisplay.setVisible(false);
		hover.setVisible(true);
		redraw();
	}
	/**
	 * Iterates backwards through the list of photos in an album.
	 * @param event
	 */
	public void back(ActionEvent event) {
		int location = tile.getChildren().indexOf(selectedlabel);
		if (location == 0) {
			location = tile.getChildren().size() - 1;
			control.setSelectedPhoto(control.getCurrentAlbum().getPhotos().get(location));
		} else {
			location--;
			control.setSelectedPhoto(control.getCurrentAlbum().getPhotos().get(location));		
		}

		selectedlabel.setBorder(null);
		selectedlabel = (Label) tile.getChildren().get(location);
		selectedlabel.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
		//control.setSelectedPhoto(p);
		display(null);
		control.save();
		
	}
	/**
	 * Iterates forward through the list of photos in an album.
	 * @param event
	 */
	public void forward(ActionEvent event) {
		int location = tile.getChildren().indexOf(selectedlabel);
		if (location == tile.getChildren().size() - 1) {
			location = 0;
			control.setSelectedPhoto(control.getCurrentAlbum().getPhotos().get(location));
		} else {
			location++;
			control.setSelectedPhoto(control.getCurrentAlbum().getPhotos().get(location));
		}

		selectedlabel.setBorder(null);
		selectedlabel = (Label) tile.getChildren().get(location);
		selectedlabel.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
		//control.setSelectedPhoto(p);
		display(null);
		control.save();
	}
}
