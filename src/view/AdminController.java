package view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Optional;

import controller.Controller;
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
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.User;
/**
 * <h1>AdminController for the Admin sub-system.
 * Allows for creation or deletion of users.<h1>
 * @author Kevin Slachta
 * @author Leon J Kim
 *
 */
public class AdminController {
	
	private Controller control;
	private ObservableList<User> users = FXCollections.observableArrayList();
	private String filename = "docs/test.txt";
	@FXML private ListView<User> adminuserlist;
	@FXML private Button createUser;
	@FXML private Button deleteUser;
	
	@FXML void initialize() {
		control = new Controller();
		ListUsers(null);
		//readFile();
	}
	
	private void readFile() {
		FileInputStream file;
		ObjectInputStream in;
		try {
			file = new FileInputStream(filename);
			in = new ObjectInputStream(file);
			User user = null;
			while (user != null) {
				user = (User) in.readObject();
				users.add(user);
			}
			in.close();
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	/**
	 * Lists all users
	 * @param event
	 * @throws Exception
	 */
	public void ListUsers(ActionEvent event){
		
		 ObservableList<User> list = FXCollections.observableArrayList(control.getUsers());
		 adminuserlist.setItems(list);
		
	}
	/**
	 * Creates a new user.
	 * @param event
	 * @throws Exception
	 */
	public void CreateUser(ActionEvent event) {
		
		 TextInputDialog td = new TextInputDialog("Create a new User"); 
		 td.setTitle("Create User");
		 td.setHeaderText("Enter New Username");
		 td.setContentText("Username:");
		 boolean exists = false;
		 Optional<String> result = td.showAndWait();
		 
		 if (result.isPresent()) {
			 for (User s: control.getUsers()) {
				 if (s.getUsername().equals(result.get())) {
					 exists = true;
				 }
			 }
			 if (!exists) {
				 User u = new User(result.get());
				 control.addUser(u);
				 control.save();
			 } else {
				 Alert alert = new Alert(AlertType.ERROR);
				 alert.setTitle("User Already Exists");
				 alert.setHeaderText(null);
				 alert.setContentText("Ooops, there already exists a user by username: " + result.get());

				 alert.showAndWait();
			 }
		 }
		 ObservableList<User> list = FXCollections.observableArrayList(control.getUsers());
		 adminuserlist.setItems(list);
		
	}
	/**
	 * Deletes a user.
	 * @param event
	 * @throws Exception
	 */
	public void DeleteUser(ActionEvent event) {
		
		 adminuserlist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		 User selectedIdx = adminuserlist.getSelectionModel().getSelectedItem();
		 control.removeUser(selectedIdx);
		 control.save();
		 ObservableList<User> list = FXCollections.observableArrayList(control.getUsers());
		 adminuserlist.setItems(list);
		 
	}
	
	@FXML Button logout;
	/**
	 * Allows a user to logout.
	 * @param event
	 */
	public void logout(ActionEvent event) {
		//System.out.println("IM CALLED");
		Stage userStage = (Stage) logout.getScene().getWindow();
		control.save();
		showLogin();
		userStage.hide();
	}
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
}