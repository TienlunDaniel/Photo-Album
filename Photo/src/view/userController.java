package view;

import javafx.scene.text.Text;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
/**This is the page after a normal user login
 */
public class userController {
	
	public static Stage stage;
	public static userController userInstance;
	public static Users currentUser;
	ChangeListener<Number> listener;
	
	@FXML Button logOut;
    @FXML Button quit;
    @FXML Text welcome;
    @FXML ListView<String> album;
    @FXML Text albumInfo;
    /**
	 * open the selected album
	 */
	public void open(){
		int index = album.getSelectionModel().getSelectedIndex();
		if(index< 0){return;}
		
		Album openAlbum = currentUser.albumMap.get(currentUser.album.get(index));
		stage.hide();
		AlbumController.albumInstance.start(openAlbum);
	}
	/**
	 * create a album
	 */
	public void create(){
		popupWindow(0);
	}
	/**
	 * delete a album
	 */
	public void delete(){
		int index = album.getSelectionModel().getSelectedIndex();
		if(index < 0) return;
		currentUser.album.remove(index);
	}
	/**
	 * rename a album
	 */
	public void rename(){
		popupWindow(1);
	}
	/**
	 * logout the application
	 */
	 public void logOut(){
		 	this.album.getSelectionModel().selectedIndexProperty().removeListener(this.listener);
	    	userController.stage.close();
	    	loginPageController.stage.show();
	    }
	 /**
	  * quit the application 
	  */
	 public void quit(){
	    System.exit(0);
	 }
	 /**
	  * set up the UI with the title that has the username
	  * @param currentUser
	  */
	 public void start(Users currentUser){
		 int index = album.getSelectionModel().getSelectedIndex();
		 if(index >= 0){
			 display(index);
		 }
		 welcome.setText("Welcome " + currentUser.name);
		 userController.stage.show();
		 userController.currentUser = currentUser;
		 album.setItems(currentUser.album);
		 
		 this.listener = (obs, oldv, newv) ->{
				display(album.getSelectionModel().getSelectedIndex());
			};
			
		album.getSelectionModel().selectedIndexProperty().addListener(this.listener);
	 }
	 
	 public void display(int index){
			if(index < 0) return;
			
			Album album = currentUser.albumMap.get(currentUser.album.get(index));
			String message = "";
			
			message += "Number of Photos: " + album.thumbnailImages.size() + "\n";
			
			albumInfo.setText(message);
		}
	 /**
	  * according to different situation, pop up a window that asks for input
	  * @param type
	  */
	 private void popupWindow(int type){
		 //set popup
		 Popup pop = new Popup();
		 pop.setHeight(500);
		 pop.setWidth(500);
		 //set hbox
		 HBox hbox = new HBox();
		 //set button and textfield
		 Button enter = new Button("enter");
		 Button cancel = new Button("cancel");
		 TextField text = new TextField();
		 
		 //add
		 hbox.getChildren().add(text);
		 hbox.getChildren().add(enter);
		 hbox.getChildren().add(cancel);
		 pop.getContent().add(hbox);
		 //set on action
		 enter.setOnAction(e ->{
			if(type == 0){
				String temp = text.getText();
				if(temp.isEmpty()){
					
				}else if(currentUser.album.contains(temp)){
					adminSubsystemController.showItem(0);
					text.setText("");
				}else{
					currentUser.album.add(temp);
					currentUser.albumMap.put(temp, new Album(temp));
					pop.hide();
				}
			}else if(type == 1){
				String temp = text.getText();
				if(temp.isEmpty()){
					
				}else if(currentUser.album.contains(temp)){
					adminSubsystemController.showItem(0);
				}else{
					int index = album.getSelectionModel().getSelectedIndex();
					currentUser.album.set(index, temp);
					pop.hide();
				}
				
			}
		 });
		 
		 cancel.setOnAction(e->{
			 pop.hide();
		 });
		 
		 pop.show(userController.stage);
	 }
}









