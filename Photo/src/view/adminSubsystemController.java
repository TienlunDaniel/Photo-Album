package view;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * This is the admin subsystem class.
 */
public class adminSubsystemController {
	/**
	 * present admin subsystem has a stage, observable list, hashmap that map the users.
	 */
	public static Stage stage; 
	public static adminSubsystemController adminInstance;
	public static ObservableList<String> obslist;
	public HashMap<String, Users> map;
	
    @FXML Button createButton;
    @FXML Button deleteButton;
    @FXML TextField usernameText;
    @FXML ListView<String> userList;
    @FXML Button logOut;
    @FXML Button quit;
    
    public void start(){
    	obslist = FXCollections.observableArrayList();
    	userList.setItems(obslist);
    	map = new HashMap<String, Users>(1000, 2.0f);
    }
    
    public void create(){
        String name = usernameText.getText();
        if(name.isEmpty()) return;
        if(!obslist.contains(name)){
        	Users user = new Users(name);
        	map.put(name, user);
        	obslist.add(name);
        	loginPageController.clear(usernameText);
        }else{
        	showItem(0);
        } 
    }

    public void delete(){
    	int index = userList.getSelectionModel().getSelectedIndex();
    	if(index < 0) return;
    	obslist.remove(index);
    }
    
    public void logOut(){
    	adminSubsystemController.stage.close();
    	loginPageController.stage.show();
    }
    
    public void quit(){
    	System.exit(0);
    }
    /**
     * it tells different error messages to the user with different numbers of types.
     * @param type
     */
    public static void showItem(int type) {                
		   Alert alert = new Alert(AlertType.INFORMATION);
		   alert.setTitle("Error Alert!!!!!");
		   alert.setHeaderText("INPUT ERROR");
		   String content = "";
		   
		   if (type == 0) {
			   content = "There already exists an object with the same name";
		   }else if(type == 1){
			   content = "Invalid Username";
		   }else if(type == 2){
			   content = "The tag you want to remove doesn't exist.";
		   }else if (type == 3){
			   content = "album doesn't exist";
		   }else if (type == 4){
			   content = "Invalid input (cp albumName for copy mv albumName for move)"; 
		   }else if (type == 5){
			   content = "invalid input";
		   }
		   
		   alert.setContentText(content);
		   alert.showAndWait();
	   }
}
