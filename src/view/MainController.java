package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import model.*;
/**
 *<h1>MainController for Login.
 * Used to check if a user exists and setup any needed information. <h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class MainController {
	private List<User> users = new ArrayList<>();
	private String filename = "docs/test.txt";
	private Controller control;
	@FXML private ListView<User> adminuserlist;
	@FXML private Label status;
	@FXML private TextField username;
	@FXML private Button button;

	@FXML void initialize() {
		control = new Controller();
		if (control.getUsers().isEmpty())
			stockUsers();
		//testOutput();
		status.setVisible(false);
		control.save();
	}
	private void stockUsers() {
		User stock = new User("stock");
		Album stockalb = new Album("stock");
		try {
			Photo ph = new Photo("stock1", "img/stock1.jpg");
			File file = new File("img/stock1.jpg");
			SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sdf.format(file.lastModified());
			ph.setTime(sdf.getCalendar());
			stockalb.addPhoto(ph);
			
			ph = new Photo("stock2", "img/stock2.jpg");
			file = new File("img/stock2.jpg");
			sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sdf.format(file.lastModified());
			ph.setTime(sdf.getCalendar());
			stockalb.addPhoto(ph);
			ph = new Photo("stock3", "img/stock3.jpg");
			file = new File("img/stock3.jpg");
			sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sdf.format(file.lastModified());
			ph.setTime(sdf.getCalendar());
			stockalb.addPhoto(ph);
			ph = new Photo("stock4", "img/stock4.jpg");
			file = new File("img/stock4.jpg");
			sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sdf.format(file.lastModified());
			ph.setTime(sdf.getCalendar());
			stockalb.addPhoto(ph);
			ph = new Photo("stock5", "img/stock5.jpg");
			file = new File("img/stock5.jpg");
			sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sdf.format(file.lastModified());
			ph.setTime(sdf.getCalendar());
			stockalb.addPhoto(ph);
			ph = new Photo("stock6", "img/stock6.jpg");
			file = new File("img/stock6.jpg");
			sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sdf.format(file.lastModified());
			ph.setTime(sdf.getCalendar());
			stockalb.addPhoto(ph);
			stock.addAlbum(stockalb);
		} catch (Exception e) {
			
		}
		control.addUser(stock);
		control.save();
	}
	private void testOutput() {
		users = control.getUsers();
		for (User u : users) {
			List<Album> a = u.getAlbums();
			//System.out.println(u.getUsername());
			for (Album ab : a) {
				//System.out.println(ab.getAlbumName());
				List<Photo> p = ab.getPhotos();
				for (Photo pb : p) {
					//System.out.println(pb.getPhotoName());
				}
			}
		}
	}
	private void testUsers() {
		User random = new User("1");
		Album ab = new Album("Swag");
		Photo ph = new Photo("test", "img/folder.jpg");
		ab.addPhoto(ph);
		ph = new Photo("dingo", "img/flower.jpg");
		ab.addPhoto(ph);
		random.addAlbum(ab);
		ab = new Album("Swag1");
		random.addAlbum(ab);
		ab = new Album("Swag2");
		random.addAlbum(ab);
		ab = new Album("Swag3");
		control.addUser(random);
	}
	/**
	 * Checks if a user is admin or exists.
	 * @param event
	 * @throws Exception
	 */
	public void Login(ActionEvent event) throws Exception {
		boolean login = false;
		if(username.getText().equals("admin")) {
	
			Stage primaryStage = new Stage();
			Stage curStage = (Stage) button.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/view/AdminPage.FXML"));
			Scene scene = new Scene(root,500,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Admin Page");
			primaryStage.setScene(scene);
			primaryStage.show();
			curStage.hide();
		} else {
			users = control.getUsers();
			for (User user : users) {
				if(username.getText().equals(user.getUsername())) { 
					login = true;
					userLogin(user);
				}
			}
			if (login == false) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Invalid Login");
				alert.setHeaderText(null);
				alert.setContentText("There is no user registered as: " + username.getText());
	
				alert.showAndWait();
			}
		}

	}
	private void userLogin(User user) {
		control.setCurrentUser(user);
		control.save();
		Stage primaryStage = (Stage) button.getScene().getWindow();
		Stage childStage = new Stage();
		try {
			Parent child = FXMLLoader.load(getClass().getResource("/view/UserInterface.FXML"));
			Scene scene = new Scene(child, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			childStage.setTitle(user.toString());
			childStage.setScene(scene);
			childStage.show();
			primaryStage.hide();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@FXML
	//private Button listUsers;
	@FXML
	private Button createUser;
	@FXML
	private Button deleteUser;
	
	public void ListUsers(ActionEvent event) throws Exception{
		//adminuserlist = new ListView<>();
		//adminuserlist.setItems(users);
		
	}
}

